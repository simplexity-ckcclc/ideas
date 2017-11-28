/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-7
 */

package com.ckcclc.anything.xml.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("face")
public class Face {

    String faceId;

    String fileId;
    @XStreamAlias("face-state")
    String state;

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
