package com.project.doctorhub.schedule.model;

public enum DayOfWeek {
    WED("چهارشنبه", 4, java.time.DayOfWeek.WEDNESDAY), THU("پنجشنبه", 5, java.time.DayOfWeek.THURSDAY), FRI("جمعه", 6, java.time.DayOfWeek.FRIDAY), SAT("شنبه", 0, java.time.DayOfWeek.SATURDAY), SUN("یکشنبه", 1, java.time.DayOfWeek.SUNDAY), MON("دوشنبه", 2, java.time.DayOfWeek.MONDAY), TUE("سه‌شنبه", 3, java.time.DayOfWeek.TUESDAY);

    private final String solarValue;
    private final Integer order;

    private final java.time.DayOfWeek dayOfWeekValue;

    DayOfWeek(String solarValue, Integer order, java.time.DayOfWeek dayOfWeekValue) {
        this.solarValue = solarValue;
        this.order = order;
        this.dayOfWeekValue = dayOfWeekValue;
    }

    public String getSolarValue() {
        return solarValue;
    }

    public Integer getOrder() {
        return order;
    }

    public java.time.DayOfWeek getDayOfWeekValue() {
        return dayOfWeekValue;
    }
}
