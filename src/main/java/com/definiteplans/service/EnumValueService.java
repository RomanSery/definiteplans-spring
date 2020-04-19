package com.definiteplans.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.definiteplans.dao.EnumValueRepository;
import com.definiteplans.dom.EnumValue;
import com.definiteplans.dom.enumerations.EnumValueType;


@Service
public class EnumValueService {
    private final EnumValueRepository enumValueRepository;

    private final List<EnumValue> enumCache;

    public EnumValueService(EnumValueRepository enumValueRepository) {
        this.enumValueRepository = enumValueRepository;
        enumCache = enumValueRepository.findAll();
    }

    public List<EnumValue> findByType(EnumValueType type) {
        if(type == null) {
            return Collections.emptyList();
        }
        return enumCache.stream().filter(o -> o.getType() == type.getId()).collect(Collectors.toList());
    }

    public EnumValue findById(int id) {
        return enumCache.stream().filter(o -> o.getId() == id).findFirst().orElse(null);
    }

    public EnumValue getByTypeAndName(EnumValueType type, String name) {
        List<EnumValue> list = findByType(type);
        return list.stream().filter(v -> v.getEnumValue().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
