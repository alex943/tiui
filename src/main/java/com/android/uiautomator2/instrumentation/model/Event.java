package com.android.uiautomator2.instrumentation.model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event implements Serializable, Comparable<Event> {
    //{"type":1,
    // "index":0,
    // "threadId":0,
    // "hashCode":2608234, "id":-1, "className":"Landroid/widget/TextView;", "methodName":"onMeasure"}
    private int type;
    private int threadid;
    private int hashCode;
    private int id;
    private String className;
    private String methodName;
    private int index;


    public static final int ERROR = 0;
    public static final int MARK = 1;
    public static final int VIEW = 2;
    public static final int PAGE = 3;
    private int level;
    private int eventType;
    private Date time;
    private String timeString;
    private static SimpleDateFormat formatter =
            new SimpleDateFormat("MM-dd HH:mm:ss.SSS");

    @Override
    public boolean equals(Object o) {
        Event obj = (Event) o;
        return this.hashCode == obj.hashCode && this.id == obj.id;
    }

    @Override
    public int compareTo(@NotNull Event obj) {
        return this.id == obj.id && this.className.equals(obj.className) ? 0 : -1;
    }

    public void update(Event event) {
        this.className = event.className;
    }

    @Override
    public String toString() {
        return "Event2{" +
                "type=" + type +
                ", threadid=" + threadid +
                ", hashCode=" + hashCode +
                ", id=" + id +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", time='" + timeString + '\'' +
                ", level='" + level + '\'' +
                '}';
    }


    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public void setThreadid(int threadid) {
        this.threadid = threadid;
    }
    public int getThreadid() {
        return threadid;
    }

    public void setHashCode(int hashcode) {
        this.hashCode = hashcode;
    }
    public int getHashCode() {
        return hashCode;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setStrId(String id) {
        this.id = Integer.parseInt(id);
    }

    public String getStrId() {
        return id == -1 ? "" : String.valueOf(id);
    }
    public String getHexId() {
        return id == -1 ? "" : String.valueOf(id);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(String time) {
        try {
            this.timeString = time;
            this.time = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public long subtraction(Event ed) {
        long a = this.getTime().getTime();
        long b = ed.getTime().getTime();
        return a - b; // ms
    }
}
