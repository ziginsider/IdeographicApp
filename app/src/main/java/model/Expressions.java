package model;

import java.io.Serializable;

/**
 * Created by zigin on 20.09.2016.
 */
public class Expressions implements Serializable {

    private static final long SerialVersionUID = 10L;
    private String expText;
    private int expParentId;
    private int expId;

    public Expressions(String expText, int expParentId, int expId) {
        this.expText = expText;
        this.expParentId = expParentId;
        this.expId = expId;
    }

    public Expressions() {

    }

    public static long getSerialVersionUID() {
        return SerialVersionUID;
    }

    public String getExpText() {
        return expText;
    }

    public void setExpText(String expText) {
        this.expText = expText;
    }

    public int getExpParentId() {
        return expParentId;
    }

    public void setExpParentId(int expParentId) {
        this.expParentId = expParentId;
    }

    public int getExpId() {
        return expId;
    }

    public void setExpId(int expId) {
        this.expId = expId;
    }
}
