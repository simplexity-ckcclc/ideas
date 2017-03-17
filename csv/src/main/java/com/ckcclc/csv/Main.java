/**
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  huangyucong <huangyucong@tp-link.com.cn>
 * Created: 2017/3/9
 */

package com.ckcclc.csv;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.apache.commons.beanutils.PropertyUtils;

import java.io.OutputStreamWriter;

public class Main {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//
//        CsvWriter writer = new CsvWriter(out, ',', Charset.forName("utf-8"));
//        String[] contents = {"a", "b", "c"};
//        writer.writeRecord(contents);
//        writer.close();
//
//        System.out.println(out.toString());


//        ColumnPositionMappingStrategy<Person> strategy = new ColumnPositionMappingStrategy<>();
//        strategy.setType(Person.class);
//        String[] columns = new String[]{"NAME", "age"};
//        strategy.setColumnMapping(columns);

        OutputStreamWriter writer = new OutputStreamWriter(System.out);

        StatefulBeanToCsv<Person> converter = new StatefulBeanToCsvBuilder(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();

        converter.write(new Person("foo", 12));
        converter.write(new Person("bar", 12));

        writer.flush();

        Person person = new Person("foo", 12);
        PropertyUtils.setProperty(person, "name", "bar");
        Object value = PropertyUtils.getProperty(person, "name");
        System.out.println(value);

        System.out.println(PropertyUtils.isReadable(person, "name"));
        System.out.println(PropertyUtils.isReadable(person, "non"));
    }
}
