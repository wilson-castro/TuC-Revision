package io.github.wilsoncastro.tucrevision;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class DbTestsContainer implements BeforeAllCallback {

    private static final class CustomPostgreSQLContainer extends PostgreSQLContainer<CustomPostgreSQLContainer> {
        public CustomPostgreSQLContainer(String dockerImageName) {
            super(dockerImageName);
        }
    }

    private static final AtomicBoolean started = new AtomicBoolean(false);
    private static final CustomPostgreSQLContainer database = new CustomPostgreSQLContainer("postgres:16")
            .withEnv("PGDATA", "/var/lib/postgresql/data")
            .withTmpFs(Map.of("/var/lib/postgresql/data", "rw")).withReuse(true);

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if(!started.get()) {
            database.start();

            System.setProperty("spring.datasource.url", database.getJdbcUrl());
            System.setProperty("spring.datasource.username", database.getUsername());
            System.setProperty("spring.datasource.password", database.getPassword());
            System.setProperty("spring.test.database.replace", "none");

            started.set(true);
        }
    }

}
