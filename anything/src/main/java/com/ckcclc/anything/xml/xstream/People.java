/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-7
 */

package com.ckcclc.anything.xml.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

@XStreamAlias("people")
public class People {

    private String peopleId;
    private List<Face> faceList;

    public String getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(String peopleId) {
        this.peopleId = peopleId;
    }

    public List<Face> getFaceList() {
        return faceList;
    }

    public void setFaceList(List<Face> faceList) {
        this.faceList = faceList;
    }
}
