package model;

/**
 * Created by zigin on 08.11.2016.
 */

public class FavoriteExpressions {
    private int idFavoriteExp;
    private String textExp;
    private int idExp;
    private int idParentTopic;

    public FavoriteExpressions(int idExp, String textExp, int idParentTopic) {
        this.idExp = idExp;
        this.textExp = textExp;
        this.idParentTopic = idParentTopic;
    }

    public FavoriteExpressions() {
    }

    public int getIdFavoriteExp() {
        return idFavoriteExp;
    }

    public void setIdFavoriteExp(int idFavoriteExp) {
        this.idFavoriteExp = idFavoriteExp;
    }

    public String getTextExp() {
        return textExp;
    }

    public void setTextExp(String textExp) {
        this.textExp = textExp;
    }

    public int getIdExp() {
        return idExp;
    }

    public void setIdExp(int idExp) {
        this.idExp = idExp;
    }

    public int getIdParentTopic() {
        return idParentTopic;
    }

    public void setIdParentTopic(int idParentTopic) {
        this.idParentTopic = idParentTopic;
    }
}
