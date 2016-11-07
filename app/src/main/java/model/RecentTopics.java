package model;

/**
 * Created by zigin on 06.11.2016.
 */

public class RecentTopics {
    private String topicText;
    private int topicId;
    private int topicWeight;
    private int recentTopicId;

    public RecentTopics(String topicText, int topicId, int topicWeight) {
        this.topicText = topicText;
        this.topicId = topicId;
        this.topicWeight = topicWeight;
    }

    public RecentTopics() {
    }

    public String getTopicText() {
        return topicText;
    }

    public void setTopicText(String topicText) {
        this.topicText = topicText;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int getTopicWeight() {
        return topicWeight;
    }

    public void setTopicWeight(int topicWeight) {
        this.topicWeight = topicWeight;
    }

    public int getRecentTopicId() {
        return recentTopicId;
    }

    public void setRecentTopicId(int recentTopicId) {
        this.recentTopicId = recentTopicId;
    }

    public void downTopicWeight() {
        this.topicWeight--;
    }
}
