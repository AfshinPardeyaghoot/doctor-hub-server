package com.project.doctorhub.consultation.model;

public enum ConsultationStatusType {
    IN_PROCESS("در جریان"), FINISHED("پایان یافته");

    private final String persian;

    ConsultationStatusType(String persian) {
        this.persian = persian;
    }

    public String getPersian() {
        return persian;
    }
}
