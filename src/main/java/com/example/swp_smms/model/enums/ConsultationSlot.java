package com.example.swp_smms.model.enums;

public enum ConsultationSlot {
    MORNING_SLOT_1("07:00", "09:00"),
    MORNING_SLOT_2("09:30", "11:30"),
    AFTERNOON_SLOT_1("12:00", "14:00"),
    AFTERNOON_SLOT_2("14:30", "16:00");

    private final String startTime;
    private final String endTime;

    ConsultationSlot(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
} 