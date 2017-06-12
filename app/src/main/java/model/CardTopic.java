package model;

/**
 * Created by zigin on 11.06.2017.
 */

public class CardTopic {
    private String topicText;
    private int topicId;
    private int cardTopicId;

    public CardTopic(String topicText, int topicId) {
        this.topicText = topicText;
        this.topicId = topicId;
    }

    public CardTopic() {
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

    public int getCardTopicId() {
        return cardTopicId;
    }

    public void setCardTopicId(int cardTopicId) {
        this.cardTopicId = cardTopicId;
    }
}
