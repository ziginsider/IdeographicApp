package model;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;
import java.util.UUID;

/**
 * Created by zigin on 12.12.2016.
 */

public class AllExpParent implements ParentObject {

    private List<Object> mChildrenList;
    private UUID _id;
    private String textExp;
    private int idParentTopic;

    public AllExpParent() {
        super();
    }

    public AllExpParent(String textExp, int idParentTopic) {
        this.textExp = textExp;
        this.idParentTopic = idParentTopic;
        _id = UUID.randomUUID();
    }

    public UUID get_id() {
        return _id;
    }

    public void set_id(UUID _id) {
        this._id = _id;
    }

    public String getTextExp() {
        return textExp;
    }

    public void setTextExp(String textExp) {
        this.textExp = textExp;
    }

    public int getIdParentTopic() {
        return idParentTopic;
    }

    public void setIdParentTopic(int idParentTopic) {
        this.idParentTopic = idParentTopic;
    }

    @Override
    public List<Object> getChildObjectList() {
        return mChildrenList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        mChildrenList = list;
    }
}
