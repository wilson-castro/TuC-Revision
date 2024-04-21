package com.backend.backend.models;

import java.lang.reflect.Field;


public class DTOGenerator {
    public static <T, Y> T from(Y object, T dto) {
        Field[] declaredFields = object.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                for (Field dtoField : dto.getClass().getDeclaredFields()) {
                    if (dtoField.getName().equals(field.getName())) {
                        dtoField.setAccessible(true);
                        dtoField.set(dto, field.get(object));
                    }
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return dto;
    }
}
