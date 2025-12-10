package com.pismo.challenge.domain.enums;

public enum OperationType {
    NORMAL_PURCHASE(1),
    INSTALLMENT_PURCHASE(2),
    WITHDRAWAL(3),
    CREDIT_VOUCHER(4);

    private final int id;

    OperationType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static OperationType fromId(int id) {
        for (OperationType type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid OperationType id: " + id);
    }
}
