package com.project.doctorhub.schedule.model;

public enum DayOfWeek {
    WED("چهارشنبه", 4), THU("پنجشنبه", 5), FRI("جمعه", 6), SAT("شنبه", 0), SUN("یکشنبه", 1), MON("دوشنبه", 2), TUE("سه‌شنبه", 3);

    private final String solarValue;
    private final Integer order;

    DayOfWeek(String solarValue, Integer order) {
        this.solarValue = solarValue;
        this.order = order;
    }

    public String getSolarValue() {
        return solarValue;
    }

    public Integer getOrder() {
        return order;
    }
}
