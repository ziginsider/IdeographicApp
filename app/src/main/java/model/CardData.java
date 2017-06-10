package model;

import java.util.ArrayList;

/**
 * Created by zigin on 09.06.2017.
 */

public class CardData {
    private int cardTopicId;
    private String parentTopics;
    private ArrayList<String> childNames;
    private ArrayList<Integer> childTypes;


    public CardData(int cardTopicId, String parentTopics, ArrayList<String> childNames, ArrayList<Integer> childTypes) {
        this.cardTopicId = cardTopicId;
        this.parentTopics = parentTopics;
        this.childNames = childNames;
        this.childTypes = childTypes;
    }

    public ArrayList<String> getChildNames() {
        return childNames;
    }

    public void setChildNames(ArrayList<String> childNames) {
        this.childNames = childNames;
    }

    public int getCardTopicId() {
        return cardTopicId;
    }

    public void setCardTopicId(int cardTopicId) {
        this.cardTopicId = cardTopicId;
    }

    public String getParentTopics() {
        return parentTopics;
    }

    public void setParentTopics(String parentTopics) {
        this.parentTopics = parentTopics;
    }

    public ArrayList<Integer> getChildTypes() {
        return childTypes;
    }

    public void setChildTypes(ArrayList<Integer> childTypes) {
        this.childTypes = childTypes;
    }
}
