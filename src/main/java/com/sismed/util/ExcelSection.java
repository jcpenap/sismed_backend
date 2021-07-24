package com.sismed.util;

import lombok.Getter;

@Getter
public enum ExcelSection {

    IMPORTED_FILE("ImportedFile");

    final String typeValue;

    ExcelSection(final String typeValue) {
        this.typeValue = typeValue;
    }

    @Override
    public String toString() {
        return name();
    }

}
