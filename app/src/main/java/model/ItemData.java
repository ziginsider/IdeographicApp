package model;

/**
 * Created by zigin on 10.06.2017.
 */

public class ItemData {
    private String nameItem;
    private int imageItemType;

    public ItemData(String nameItem, int imageItemType) {
        this.nameItem = nameItem;
        this.imageItemType = imageItemType;
    }

    public String getNameItem() {
        return nameItem;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public int getImageItemType() {
        return imageItemType;
    }

    public void setImageItemType(int imageItemType) {
        this.imageItemType = imageItemType;
    }
}
