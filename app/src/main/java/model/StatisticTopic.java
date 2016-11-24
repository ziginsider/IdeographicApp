package model;

/**
 * Created by zigin on 15.11.2016.
 */

public class StatisticTopic {
    private int idStatisticTopic;
    private String textTopic;
    private int idTopic;
    private int counterTopic;

    public StatisticTopic(String textTopic, int idTopic, int counterTopic) {
        this.textTopic = textTopic;
        this.idTopic = idTopic;
        this.counterTopic = counterTopic;
    }

    public StatisticTopic() {
        this.counterTopic = 0;
    }

    public int getIdStatisticTopic() {
        return idStatisticTopic;
    }

    public void setIdStatisticTopic(int idStatisticTopic) {
        this.idStatisticTopic = idStatisticTopic;
    }

    public String getTextTopic() {
        return textTopic;
    }

    public void setTextTopic(String textTopic) {
        this.textTopic = textTopic;
    }

    public int getIdTopic() {
        return idTopic;
    }

    public void setIdTopic(int idTopic) {
        this.idTopic = idTopic;
    }

    public int getCounterTopic() {
        return counterTopic;
    }

    public void setCounterTopic(int counterTopic) {
        this.counterTopic = counterTopic;
    }
}
