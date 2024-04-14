package com.projeto.pi.projeto_pi.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.projeto.pi.projeto_pi.configuration.Encoder;

/**
 * Altera o valor dos atributos de um objeto existente com os valores de outro
 * objeto, nunca alterando a PK.
 * 
 * @param <T> Tipo do objeto
 * @author Luis Ricardo Alves Santos
 * @since 1.0
 */
public class ReplaceObjectAttributes<T> {

    @Autowired
    private PasswordEncoder passwordEncoder = Encoder.getEncoder();

    T existingItem = null;

    public ReplaceObjectAttributes(T existingItem) {
        this.existingItem = existingItem;
    }

    /**
     * Altera os valores dos atributos de um objeto existente com os valores de
     * outro objeto
     * 
     * @param item Objeto que contém os novos valores a serem alterados
     * @return Objeto existente com os valores alterados
     */
    public T replaceWith(T item) {

        Field[] declaredFields = item.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                String name = field.getName();

                Object value = field.get(item);

                /*
                 * Validação para evitar a troca da PK,
                 * verificado qual campo possui a annotation
                 */

                if (isPk(field)) {
                    continue;
                }
                if (isEncode(field)) {
                    value = passwordEncoder.encode(value.toString());
                }

                if (value == null) {
                    continue;
                }

                Field existingField = existingItem.getClass().getDeclaredField(name);
                existingField.setAccessible(true);
                existingField.set(existingItem, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return existingItem;

    }

    // Verifica se o campo possui a annotation @Id para evitar a troca da PK
    private boolean isPk(Field field) {
        Annotation[] annotations = field.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getSimpleName().equals("Id")) {
                return true;
            }
        }
        return false;
    }

    // Verifica se o campo possui a annotation @Encode para encriptar o valor
    private boolean isEncode(Field field) {
        Annotation[] annotations = field.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getSimpleName().equals("Encode")) {
                return true;
            }
        }
        return false;
    }
}
