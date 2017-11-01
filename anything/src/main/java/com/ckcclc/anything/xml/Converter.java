/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-1
 */

package com.ckcclc.anything.xml;

import com.thoughtworks.xstream.XStream;

public class Converter {

    public static void main(String[] args) {
        Person person = new Person("foo", 12);
        XStream xStream = getXstreamObject();
        String xml = xStream.toXML(person);
        System.out.println(xml);
    }

    private static XStream getXstreamObject() {
        XStream xstream = new XStream(); // DomDriver and StaxDriver instances also can be used with constructor
        xstream.alias("person", Person.class); // this will remove the Country class package name
//        xstream.alias("state", State.class); // this will remove the State class package name
//        xstream.useAttributeFor(Country.class, "isoCode"); // make the isoCode to attribute from element
//        xstream.aliasField("code", Country.class, "isoCode"); // change 'isoCode' to code
//        xstream.addImplicitCollection(Country.class, "states"); // don't want all states inside .
        return xstream;
    }
}
