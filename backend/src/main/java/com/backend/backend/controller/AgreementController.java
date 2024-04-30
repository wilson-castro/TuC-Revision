package com.backend.backend.controller;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.backend.backend.models.agreement.Agreement;
import com.backend.backend.models.agreement.AgreementRepo;
import com.backend.backend.models.agreement.AgreementRequestDTO;
import com.backend.backend.models.agreement.AgreementResponseDTO;
import com.backend.backend.models.importer.Import;
import com.backend.backend.models.importer.ImporterRepo;
import com.backend.backend.models.importer.Import.Status;
import com.backend.backend.utils.ReplaceObjectAttributes;
import com.backend.backend.utils.ReponseBuilder;

import jakarta.validation.Valid;
import java.util.concurrent.Future;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/agreements")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AgreementController {

    @Autowired
    private AgreementRepo repository;

    @Autowired
    private ImporterRepo importerRepo;

    @Autowired
    Importer imp;

    ReponseBuilder er = new ReponseBuilder();

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Agreement> existingItemOptional = repository.findById(id);

        if (existingItemOptional.isPresent()) {
            Agreement existingItem = existingItemOptional.get();

            return new ResponseEntity<AgreementResponseDTO>(existingItem.toDTO(), HttpStatus.OK);
        } else {

            return er.error("Concordancia não encontrada", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public Page<AgreementResponseDTO> getAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest of = PageRequest.of(page, size);

        Page<AgreementResponseDTO> findAll = repository.findAll(of).map(Agreement::toDTO);
        return findAll;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid AgreementRequestDTO item) {
        try {
            Agreement itemToBeSaved = item.toEntity();
            Agreement savedItem = repository.save(itemToBeSaved).get();
            return new ResponseEntity<>(savedItem.toDTO(), HttpStatus.CREATED);
        } catch (Exception e) {

            return er.error("Erro ao criar concordancia:" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/import")
    public ResponseEntity<?> importation(@RequestParam("file") MultipartFile file) throws Exception {

        // Read file content and convert to csv to json

        File tempFile = File.createTempFile(file.getName() + "_COPY", null);
        file.transferTo(tempFile);
        try {

            Import importer = new Import();

            importer.setStatus(Status.STARTED);
            importer.setMessage("Importação iniciada");

            Import save = importerRepo.save(importer);
            // Save all agreements in new thread
            imp.importCSV(tempFile, save);
            return new ResponseEntity<>(save, HttpStatus.OK);
        } catch (Exception e) {

            return er.error("Erro ao criar concordancia:" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/import/{id}")
    public ResponseEntity<?> getImportStatus(@PathVariable Long id) {
        Optional<Import> importStatus = importerRepo.findById(id);

        if (importStatus.isEmpty()) {
            return er.error("Importação não existe", HttpStatus.NOT_FOUND);
        }

        Import importer = importStatus.get();

        return new ResponseEntity<>(importer, HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Agreement> existingItemOptional = repository.findById(id);

        if (existingItemOptional.isPresent()) {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return er.error("Concordancia não encontrada", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody AgreementRequestDTO item)
            throws InterruptedException, ExecutionException {
        Optional<Agreement> existingItemOptional = repository.findById(id);

        if (existingItemOptional.isPresent()) {
            Agreement existingItem = existingItemOptional.get();

            ReplaceObjectAttributes<Agreement> rep = new ReplaceObjectAttributes<>(existingItem);
            rep.replaceWith(item.toEntity());

            AgreementResponseDTO savedItem = repository.save(existingItem).get().toDTO();

            return new ResponseEntity<>(savedItem, HttpStatus.OK);
        } else {
            return er.error("Usuario não encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<?> handleAccessException(Exception ex, WebRequest request) {
        return er.error("Acesso negado. Nivel de permissão insuficiente", HttpStatus.FORBIDDEN);
    }

}

@Service
class Importer {
    @Autowired
    private AgreementRepo repository;

    @Autowired
    ImporterRepo importerRepo;

    @Async
    public void importCSV(File file, Import importer) {
        try {
            byte[] allBytes = Files.readAllBytes(file.toPath());
            Agreement[] agreements;
            try {
                agreements = AgreementRequestDTO.fromCSV(allBytes);
            } catch (Exception e) {
                throw new RuntimeException("Arquivo inválido:" + e);
            }

            long line = 1;
            importer.setStatus(Status.IDDLE);
            importer.setMessage("Aguardando...");
            importerRepo.save(importer);
            List<Future<Agreement>> promisses = new ArrayList<>();
            for (Agreement agreement : agreements) {
                promisses.add(repository.save(agreement));
                line++;
            }

            importer.setStatus(Status.RUNNING);

            line = 1;
            for (Future<Agreement> future : promisses) {
                future.get();
                if (line % 1000 == 0) {
                    importer.setMessage("Concordancias importadas:" + line);
                    importerRepo.save(importer);
                }
                line++;
            }
            boolean delete = file.delete();
            importer.setStatus(Status.FINISHED);
            importer.setMessage("Importação finalizada. TMP file deleted? " + (delete ? "yes" : "no"));
            importerRepo.save(importer);

        } catch (Exception e) {
            importer.setStatus(Status.ERROR);
            importer.setMessage("Erro ao importar:" + e);
            importerRepo.save(importer);
            throw new RuntimeException(e);
        }
    }
}
