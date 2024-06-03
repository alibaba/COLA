package com.huawei.charging.domain.charge;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money,Long> {
    @Override
    public Long convertToDatabaseColumn(Money entityData) {
        return Long.valueOf(entityData.getAmount());
    }

    @Override
    public Money convertToEntityAttribute(Long dbData) {
        return Money.of(dbData.intValue());
    }
}
