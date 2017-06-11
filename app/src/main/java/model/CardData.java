package model;

import java.util.ArrayList;

/**
 * Created by zigin on 09.06.2017.
 */

public class CardData {
    private int cardTopicId;
    private String nameTopics;
    private ArrayList<String> childNames;
    private ArrayList<Integer> childTypes;


    public CardData(int cardTopicId, String nameTopics, ArrayList<String> childNames, ArrayList<Integer> childTypes) {
        this.cardTopicId = cardTopicId;
        this.nameTopics = nameTopics;
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

    public String getNameTopics() {
        return nameTopics;
    }

    public void setNameTopics(String nameTopics) {
        this.nameTopics = nameTopics;
    }

    public ArrayList<Integer> getChildTypes() {
        return childTypes;
    }

    public void setChildTypes(ArrayList<Integer> childTypes) {
        this.childTypes = childTypes;
    }
}
