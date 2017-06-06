package model;

/**
 * Created by zigin on 12.12.2016.
 */

public class AllExpChild {
    public String textTranscription;
    public String textDefinition;
    public String textParentTopic;

    public AllExpChild(String option1, String option2, String option3) {
        this.textTranscription = option1;
        this.textDefinition = option2;
        this.textParentTopic = option3;
    }

    public String getTextTranscription() {
        return textTranscription;
    }

    public void setTextTranscription(String textTranscription) {
        this.textTranscription = textTranscription;
    }

    public String getTextDefinition() {
        return textDefinition;
    }

    public void setTextDefinition(String textDefinition) {
        this.textDefinition = textDefinition;
    }

    public String getTextParentTopic() {
        return textParentTopic;
    }

    public void setTextParentTopic(String textParentTopic) {
        this.textParentTopic = textParentTopic;
    }
}
