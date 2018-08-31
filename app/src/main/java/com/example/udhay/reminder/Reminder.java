package com.example.udhay.reminder;

public class Reminder {

    public static final int IMPORTANCE_LOW = 0;
    public static final int IMPORTANCE_INTERMEDIATE = 1;
    public static final int IMPORTANCE_HIGH = 2;

    public static final String SORT_BY_TIME = "time";
    public static final String SORT_BY_IMPORTANCE = "importance";

    private int importanceLevel;
    private String message;


    public Reminder(int importance , String message){
        importanceLevel = importance;
        this.message = message;
    }

    public int getImportanceLevel() {
        return importanceLevel;
    }


    public String getMessage() {
        return message;
    }


}
