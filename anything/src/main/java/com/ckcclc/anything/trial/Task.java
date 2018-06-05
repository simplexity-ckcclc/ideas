package com.ckcclc.anything.trial;


/**
 * Created by ckcclc on 13/01/2018.
 */
public class Task {

    private String serial;

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

//    @Override
//    public String toString() {
//        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
//    }


    public static void main(String[] args) {
        Task task = new Task();
        task.setSerial("a");
        System.out.println(task);
    }
}
