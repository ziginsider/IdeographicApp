package model;

import java.io.Serializable;

/**
 * Created by zigin on 20.09.2016.
 */
public class Topics implements Serializable {

    private static final long SerialVersionUID = 10L;
    private String topicText;
    private int topicParentId;
    private String topicLabels;
    private int topicId;

    public Topics(String topicText, int topicParentId, String topicLabels, int topicId) {
        this.topicText = topicText;
        this.topicParentId = topicParentId;
        this.topicLabels = topicLabels;
        this.topicId = topicId;
    }

    public Topics() {

    }

    public static long getSerialVersionUID() {
        return SerialVersionUID;
    }

    public String getTopicText() {
        return topicText;
    }

    public void setTopicText(String topicText) {
        this.topicText = topicText;
    }

    public int getTopicParentId() {
        return topicParentId;
    }

    public void setTopicParentId(int topicParentId) {
        this.topicParentId = topicParentId;
    }

    public String getTopicLabels() {
        return topicLabels;
    }

    public void setTopicLabels(String topicLabels) {
        this.topicLabels = topicLabels;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }
}
