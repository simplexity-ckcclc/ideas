/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-10
 */

package com.ckcclc.anything.json;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Main {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        PeopleRemark remark = new PeopleRemark();
        try {
            remark.setBornTime(DATE_FORMATTER.parse("2000-10-01"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        remark.setCountry("CN");
        remark.setCredentialType(null);
        remark.setDescription("test");

        String jsonString = remark.toJSonString();

        PeopleRemark rebuild = PeopleRemark.fromJSonString(jsonString);

        System.out.println(DATE_FORMATTER.format(rebuild.getBornTime()));
        System.out.println(rebuild.getCredentialType());
        System.out.println(rebuild.getCountry());
    }
}
