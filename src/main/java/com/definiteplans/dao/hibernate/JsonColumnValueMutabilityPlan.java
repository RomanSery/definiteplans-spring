package com.definiteplans.dao.hibernate;

import org.hibernate.type.descriptor.java.MutableMutabilityPlan;

import com.definiteplans.util.EntityUtil;

public final class JsonColumnValueMutabilityPlan extends MutableMutabilityPlan<Object> {
    private static final long serialVersionUID = 1L;
    static final JsonColumnValueMutabilityPlan INSTANCE = new JsonColumnValueMutabilityPlan();

    private JsonColumnValueMutabilityPlan() {
    }

    @Override
    public Object deepCopyNotNull(Object value) {
        return EntityUtil.clone(value);
    }
}
