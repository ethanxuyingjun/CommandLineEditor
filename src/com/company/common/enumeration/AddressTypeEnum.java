package com.company.common.enumeration;

import static java.lang.Integer.parseInt;

public enum AddressTypeEnum {
    CURRENT_LINE("."), LAST_LINE("$"), LINE_NUMBER("n"), BACKWARD_LINE("+"), FORWARD_LINE("-"), FROM_TO("m,n"), ALL_LINES(","), TOLAST(";"),
    FORWARD_SEARCH("?"), BACKWARD_SEARCH("/"), FLAG_LINE("'");

    public String getLabel() {
        return label;
    }

    private String label;

    private AddressTypeEnum(String lbl) {
        this.label = lbl;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
