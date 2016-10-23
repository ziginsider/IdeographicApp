package model;

/**
 * Created by zigin on 20.10.2016.
 */

public class DoubleItem {

    private Expressions exp;
    private Topics topic;

    public DoubleItem(Expressions exp, Topics topic) {
        this.exp = exp;
        this.topic = topic;
    }

    public DoubleItem() {

    }

    public Expressions getExp() {
        return exp;
    }

    public void setExp(Expressions exp) {
        this.exp = exp;
    }

    public Topics getTopic() {
        return topic;
    }

    public void setTopic(Topics topic) {
        this.topic = topic;
    }
}
