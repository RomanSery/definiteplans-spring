package com.definiteplans.dom.enumerations;

import java.io.Serializable;

public interface GenericEnum extends Serializable {
	default boolean equals(GenericEnum obj) {
        return obj != null && getId() == obj.getId();
    }		

	String getDescription();
	void setDescription(String description);
	int getId();
}
