/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-9
 */

package com.ckcclc.anything.xml.xstream;

import com.thoughtworks.xstream.XStream;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        People people = new People();
        people.setPeopleId("foo");
        Face face1 = new Face();
        face1.setFaceId("bar");
        face1.setState("ok");

        Face face2 = new Face();
        face2.setFaceId("foobar");
        face2.setState("ok");

        List<Face> faceList = new ArrayList<>();
        faceList.add(face1);
        faceList.add(face2);
        people.setFaceList(faceList);

        XStream xStream = new XStream();
        String xml = xStream.toXML(people);
        System.out.println(xml);
    }
}
