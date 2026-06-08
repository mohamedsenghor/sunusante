package dev.sunusante.dmp.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Converter
@Component
public class MedicalDataConverter implements AttributeConverter<String, String> {

    private final EncryptionUtils encryptionUtils;

    public MedicalDataConverter(@Lazy EncryptionUtils encryptionUtils) {
        this.encryptionUtils = encryptionUtils;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return encryptionUtils.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return encryptionUtils.decrypt(dbData);
    }
}
