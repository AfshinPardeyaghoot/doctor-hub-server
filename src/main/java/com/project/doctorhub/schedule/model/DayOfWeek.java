package com.project.doctorhub.schedule.model;

public enum DayOfWeek {
    WED("چهارشنبه"), THU("پنجشنبه"), FRI("جمعه"), SAT("شنبه"), SUN("یکشنبه"), MON("دوشنبه"), TUE("سه‌شنبه");

    private final String  solarValue;

    DayOfWeek(String solarValue) {
        this.solarValue = solarValue;
    }

    public String getSolarValue(){
        return solarValue;
    }
}
