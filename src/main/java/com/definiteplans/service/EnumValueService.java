package com.definiteplans.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.definiteplans.dao.EnumValueRepository;
import com.definiteplans.dom.EnumValue;
import com.definiteplans.dom.enumerations.EnumValueType;


@Service
public class EnumValueService {
    @Autowired private EnumValueRepository enumValueRepository;

    public List<EnumValue> getByType(EnumValueType type) {
        if(type == null) {
            return Collections.emptyList();
        }
        List<EnumValue> list = enumValueRepository.findByType(type.getId());
        return list == null ? Collections.emptyList() : list;
    }

    public EnumValue getByTypeAndName(EnumValueType type, String name) {
        List<EnumValue> list = getByType(type);
        return list.stream().filter(v -> v.getEnumValue().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
