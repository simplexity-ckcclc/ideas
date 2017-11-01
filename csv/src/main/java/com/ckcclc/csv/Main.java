/**
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  huangyucong <huangyucong@tp-link.com.cn>
 * Created: 2017/3/9
 */

package com.ckcclc.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import org.apache.commons.beanutils.PropertyUtils;

import java.io.*;
import java.util.List;

public class Main {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        CSVToBean();
    }


    public static void BeanToCSV() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
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

    public static void CSVToBean() throws Exception {
        FileReader reader = new FileReader("/home/sensetime/IdeaProjects/github/ideas/csv/src/main/java/resources/test.csv");
        List<Person> persons = mapToCSV(reader, Person.class);
        System.out.println(persons.size());
        for (Person person : persons) {
            System.out.println(person.getName() + "." + person.getAge());
        }
    }

    public static  <T> List<T> mapToCSV(Reader reader, Class<T> mapToClass) {
        CsvToBean<T> csvToBean = new CsvToBean<T>();

//        Map<String, String> columnMapping = new HashMap<>();
//        Arrays.stream(mapToClass.getDeclaredFields()).forEach(field -> {
//            columnMapping.put(field.getName(), field.getName());
//        });

        HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<T>();
        strategy.setType(mapToClass);

        CSVReader csvReader = new CSVReader(reader);
        return csvToBean.parse(strategy, csvReader);
    }

}
