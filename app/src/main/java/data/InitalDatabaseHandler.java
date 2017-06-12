package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import model.CardTopic;
import model.FavoriteExpressions;
import model.RecentTopics;
import model.StatisticTopic;

/**
 * Created by zigin on 06.11.2016.
 */

public class InitalDatabaseHandler extends SQLiteOpenHelper {

    private final ArrayList<RecentTopics> recentTopicsList = new ArrayList<>();
    private final ArrayList<FavoriteExpressions> favoriteExpList = new ArrayList<>();
    private final ArrayList<StatisticTopic> statisticTopicsList = new ArrayList<>();
    private final ArrayList<CardTopic> cardTopicList = new ArrayList<>();


    public InitalDatabaseHandler(Context context) {
        super(context, Constants.INITAL_DATABASE_NAME, null, Constants.INITAL_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table
        String CREATE_RECENT_TABLE = "CREATE TABLE " + Constants.RECENT_TABLE_NAME +
                "(" + Constants.KEY_ID + " INTEGER PRIMARY KEY," +
                Constants.RECENT_TOPIC_TEXT + " TEXT, " +
                Constants.RECENT_TOPIC_ID + " INT, " +
                Constants.RECENT_TOPIC_WEIGHT + " INT" +
                ")";

        String CREATE_FAVORITE_TABLE = "CREATE TABLE " + Constants.FAVORITE_TABLE_NAME +
                "(" + Constants.KEY_ID + " INTEGER PRIMARY KEY," +
                Constants.FAVORITE_EXP_TEXT + " TEXT, " +
                Constants.FAVORITE_EXP_ID + " INT, " +
                Constants.FAVORITE_PARENT_TOPIC_ID + " INT" +
                ")";

        String CREATE_STATISTIC_TABLE = "CREATE TABLE " + Constants.STATISTIC_TABLE_NAME +
                "(" + Constants.KEY_ID + " INTEGER PRIMARY KEY," +
                Constants.STATISTIC_TOPIC_TEXT + " TEXT, " +
                Constants.STATISTIC_TOPIC_ID + " INT, " +
                Constants.STATISTIC_TOPIC_COUNTER + " INT" +
                ")";

        String CREATE_CARD_TABLE = "CREATE TABLE " + Constants.CARD_TABLE_NAME +
                "(" + Constants.KEY_ID + " INTEGER PRIMARY KEY," +
                Constants.CARD_TOPIC_TEXT + " TEXT, " +
                Constants.CARD_TOPIC_ID + " INT" +
                ")";

        db.execSQL(CREATE_RECENT_TABLE);
        db.execSQL(CREATE_FAVORITE_TABLE);
        db.execSQL(CREATE_STATISTIC_TABLE);
        db.execSQL(CREATE_CARD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.RECENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.FAVORITE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.STATISTIC_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.CARD_TABLE_NAME);

        //create a new one
        onCreate(db);
    }

    //add content to table
    public  void addRecentTopic(RecentTopics recentTopics){
        SQLiteDatabase dba = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.RECENT_TOPIC_TEXT, recentTopics.getTopicText());
        values.put(Constants.RECENT_TOPIC_ID, recentTopics.getTopicId());
        values.put(Constants.RECENT_TOPIC_WEIGHT, recentTopics.getTopicWeight());

        dba.insert(Constants.RECENT_TABLE_NAME, null, values);
        dba.close();
    }

    //Get expressions count by id topic-parent
    public int getRecentCountByIdTopic(int idTopic) {

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.RECENT_TABLE_NAME,
                new String[]{
                        Constants.KEY_ID,
                        Constants.RECENT_TOPIC_TEXT,
                        Constants.RECENT_TOPIC_ID,
                        Constants.RECENT_TOPIC_WEIGHT},
                Constants.RECENT_TOPIC_ID + "=?",
                new String[] {String.valueOf(idTopic)},
                null,
                null,
                null,
                null);

        int count = cursor.getCount();

        cursor.close();
        dba.close();

        return count;
    }

    //Get recent topic by id
    public RecentTopics getRecentTopicByTopicId(int idTopic) {

        RecentTopics recentTopic = new RecentTopics();
        //topic.setTopicParentId(0);

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.RECENT_TABLE_NAME,
                new String[] {
                        Constants.KEY_ID,
                        Constants.RECENT_TOPIC_TEXT,
                        Constants.RECENT_TOPIC_ID,
                        Constants.RECENT_TOPIC_WEIGHT },
                Constants.RECENT_TOPIC_ID + "=?",
                new String[] {String.valueOf(idTopic)},
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {

            recentTopic.setTopicText(cursor.getString(cursor.getColumnIndex(Constants.RECENT_TOPIC_TEXT)));
            recentTopic.setTopicId(cursor.getInt(cursor.getColumnIndex(Constants.RECENT_TOPIC_ID)));
            recentTopic.setTopicWeight(cursor.getInt(cursor.getColumnIndex(Constants.RECENT_TOPIC_WEIGHT)));
            recentTopic.setRecentTopicId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

        } else {

            //Toast.makeText(getApplicationContext(), " No matching data", Toast.LENGTH_SHORT).show();
            Log.d(Constants.LOG_TAG, ">>> Get Recent Topic by id: No matching data");
        }


        cursor.close();
        dba.close();

        return recentTopic;
    }

    public ArrayList<RecentTopics> getRecentTopicsList() {

        recentTopicsList.clear();

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.RECENT_TABLE_NAME,
                new String[]{Constants.KEY_ID,
                        Constants.RECENT_TOPIC_TEXT,
                        Constants.RECENT_TOPIC_ID,
                        Constants.RECENT_TOPIC_WEIGHT},
                null,
                null,
                null,
                null,
                Constants.RECENT_TOPIC_WEIGHT + " DESC");

        //loop through...
        if (cursor.moveToFirst()) {
            do {

                RecentTopics recentTopic = new RecentTopics();

                recentTopic.setTopicText(cursor.getString(cursor.getColumnIndex(Constants.RECENT_TOPIC_TEXT)));
                recentTopic.setTopicId(cursor.getInt(cursor.getColumnIndex(Constants.RECENT_TOPIC_ID)));
                recentTopic.setTopicWeight(cursor.getInt(cursor.getColumnIndex(Constants.RECENT_TOPIC_WEIGHT)));
                recentTopic.setRecentTopicId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

                recentTopicsList.add(recentTopic);

            } while (cursor.moveToNext());

            Log.d(Constants.LOG_TAG, ">>> Get all RecentTopics: success");
        } else {

            Log.d(Constants.LOG_TAG, ">>> Get all RecentTopics: No matching data");
        }

        cursor.close();
        dba.close();

        return recentTopicsList;
    }

    public int getIdTopicTopRecentTopics() {

        int idTopicTop = 0;

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.RECENT_TABLE_NAME,
                new String[]{Constants.KEY_ID,
                        Constants.RECENT_TOPIC_TEXT,
                        Constants.RECENT_TOPIC_ID,
                        Constants.RECENT_TOPIC_WEIGHT},
                null,
                null,
                null,
                null,
                Constants.RECENT_TOPIC_WEIGHT + " DESC");

        //loop through...
        if (cursor.moveToFirst()) {

            idTopicTop = cursor.getInt(cursor.getColumnIndex(Constants.RECENT_TOPIC_ID));

            Log.d(Constants.LOG_TAG, ">>> Get id topic top recent topics: success");
        } else {

            Log.d(Constants.LOG_TAG, ">>> Get id topic top recent topics: No matching data");
        }

        cursor.close();
        dba.close();

        return idTopicTop;
    }

    public int getRecentMaxWeight() {

        int maxWeight = 0;

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.RECENT_TABLE_NAME,
                new String[]{Constants.KEY_ID,
                        Constants.RECENT_TOPIC_TEXT,
                        Constants.RECENT_TOPIC_ID,
                        Constants.RECENT_TOPIC_WEIGHT},
                null,
                null,
                null,
                null,
                Constants.RECENT_TOPIC_WEIGHT + " DESC");

        //loop through...
        if (cursor.moveToFirst()) {

            maxWeight = cursor.getInt(cursor.getColumnIndex(Constants.RECENT_TOPIC_WEIGHT));

            Log.d(Constants.LOG_TAG, ">>> Get maxWeight RecentTopics: success");
        } else {

            Log.d(Constants.LOG_TAG, ">>> Get maxWeight RecentTopics: No matching data");
        }

        cursor.close();
        dba.close();

        return maxWeight;
    }

    //Update recent topic weight by id
    public int updateTopicWeightByIdTopic(int idTopic, int newWeight) {

        SQLiteDatabase dba = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.RECENT_TOPIC_WEIGHT, newWeight);

        int totalUpdated = 0;

        totalUpdated = dba.update(Constants.RECENT_TABLE_NAME,
                values,
                Constants.RECENT_TOPIC_ID + "=?",
                new String[] {String.valueOf(idTopic)});

        dba.close();

        return totalUpdated;
    }

    //Get total Recent Topics
    public int getTotalRecentTopics() {

        int totalRecentTopics = 0;

        String query = "SELECT * FROM " + Constants.RECENT_TABLE_NAME;

        SQLiteDatabase dba = this.getReadableDatabase();
        Cursor cursor = dba.rawQuery(query, null);

        totalRecentTopics = cursor.getCount();

        Log.d(Constants.LOG_TAG, ">>> Count Recent Topics = " + String.valueOf(totalRecentTopics));

        cursor.close();
        dba.close();

        return totalRecentTopics;
    }

    //delete a recent topic
    public void deleteRecentTopic(int topicId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Constants.RECENT_TABLE_NAME, Constants.RECENT_TOPIC_ID + " = ? ",
                new String[] {String.valueOf(topicId)});
        db.close();
    }

    //delete a recent topic
    public void deleteLastRecentTopic() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Constants.RECENT_TABLE_NAME, Constants.RECENT_TOPIC_WEIGHT + " = ? ",
                new String[] {String.valueOf(0)});
        db.close();
    }
///////////////////////////////////////////////////////////////////
// FAVORITE LIST
// Ziginsider 09/11/2016
//////////////////////////////////////////////////////////////////

    //add exp to favorite list
    public  void addFavoriteExp(FavoriteExpressions favoriteExp){
        SQLiteDatabase dba = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.FAVORITE_EXP_TEXT, favoriteExp.getTextExp());
        values.put(Constants.FAVORITE_EXP_ID, favoriteExp.getIdExp());
        values.put(Constants.FAVORITE_PARENT_TOPIC_ID, favoriteExp.getIdParentTopic());

        dba.insert(Constants.FAVORITE_TABLE_NAME, null, values);
        dba.close();
    }

    //delete exp from favorite list
    public void deleteFavoriteExp(int idExp) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Constants.FAVORITE_TABLE_NAME, Constants.FAVORITE_EXP_ID + " = ? ",
                new String[] {String.valueOf(idExp)});
        db.close();
    }

    //get all items favorite list
    public ArrayList<FavoriteExpressions> getFavoriteExpList() {

        favoriteExpList.clear();

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.FAVORITE_TABLE_NAME,
                new String[]{Constants.KEY_ID,
                        Constants.FAVORITE_EXP_TEXT,
                        Constants.FAVORITE_EXP_ID,
                        Constants.FAVORITE_PARENT_TOPIC_ID},
                null,
                null,
                null,
                null,
                null);

        //loop through...
        if (cursor.moveToFirst()) {
            do {
                FavoriteExpressions favoriteExp = new FavoriteExpressions();

                favoriteExp.setTextExp(cursor.getString(cursor.
                        getColumnIndex(Constants.FAVORITE_EXP_TEXT)));
                favoriteExp.setIdExp(cursor.getInt(cursor.
                        getColumnIndex(Constants.FAVORITE_EXP_ID)));
                favoriteExp.setIdParentTopic(cursor.getInt(cursor.
                        getColumnIndex(Constants.FAVORITE_PARENT_TOPIC_ID)));
                favoriteExp.setIdFavoriteExp(cursor.getInt(cursor.
                        getColumnIndex(Constants.KEY_ID)));

                favoriteExpList.add(favoriteExp);

            } while (cursor.moveToNext());

            Log.d(Constants.LOG_TAG, ">>> Get all Favorite Exp: success");
        } else {

            Log.d(Constants.LOG_TAG, ">>> Get all Favorite Exp: No matching data");
        }

        cursor.close();
        dba.close();

        return favoriteExpList;
    }

    public boolean isExpInFavoriteList(int idExp) {

        boolean flag = false;

        String query = "SELECT * FROM " + Constants.FAVORITE_TABLE_NAME +
                " WHERE " + Constants.FAVORITE_EXP_ID + " = ? ";

        SQLiteDatabase dba = this.getReadableDatabase();
        Cursor cursor = dba.rawQuery(query, new String[] {String.valueOf(idExp)});

        if (cursor != null) {

            if (cursor.getCount() > 0) {
                flag = true;
            }
        }

        cursor.close();
        dba.close();

        return flag;
    }


///////////////////////////////////////////////////////////////////
// STATISTIC TOPICS
// Ziginsider 15/11/2016
//////////////////////////////////////////////////////////////////
//add exp to statistic list
    public void addStatisticTopic(StatisticTopic statisticTopic){
        SQLiteDatabase dba = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.STATISTIC_TOPIC_TEXT, statisticTopic.getTextTopic());
        values.put(Constants.STATISTIC_TOPIC_ID, statisticTopic.getIdTopic());
        values.put(Constants.STATISTIC_TOPIC_COUNTER, statisticTopic.getCounterTopic());

        dba.insert(Constants.STATISTIC_TABLE_NAME, null, values);
        dba.close();
    }

    //get all items favorite list
    //
    public ArrayList<StatisticTopic> getStatisticTopicList() {

        statisticTopicsList.clear();

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.STATISTIC_TABLE_NAME,
                new String[]{Constants.KEY_ID,
                        Constants.STATISTIC_TOPIC_TEXT,
                        Constants.STATISTIC_TOPIC_ID,
                        Constants.STATISTIC_TOPIC_COUNTER},
                null,
                null,
                null,
                null,
                Constants.STATISTIC_TOPIC_COUNTER  + " DESC");

        //loop through...
        if (cursor.moveToFirst()) {
            do {
                StatisticTopic statisticTopic = new StatisticTopic();

                statisticTopic.setTextTopic(cursor.getString(cursor.
                        getColumnIndex(Constants.STATISTIC_TOPIC_TEXT)));
                statisticTopic.setIdTopic(cursor.getInt(cursor.
                        getColumnIndex(Constants.STATISTIC_TOPIC_ID)));
                statisticTopic.setCounterTopic(cursor.getInt(cursor.
                        getColumnIndex(Constants.STATISTIC_TOPIC_COUNTER)));
                statisticTopic.setIdStatisticTopic(cursor.getInt(cursor.
                        getColumnIndex(Constants.KEY_ID)));

                statisticTopicsList.add(statisticTopic);

            } while (cursor.moveToNext());

            Log.d(Constants.LOG_TAG, ">>> Get all Statistic topics: success");
        } else {

            Log.d(Constants.LOG_TAG, ">>> Get all Statistic topics: No matching data");
        }

        cursor.close();
        dba.close();

        return statisticTopicsList;
    }

    public boolean isTopicInStatisticList(int idStatistic) {

        boolean flag = false;

        String query = "SELECT * FROM " + Constants.STATISTIC_TABLE_NAME +
                " WHERE " + Constants.STATISTIC_TOPIC_ID + " = ? ";

        SQLiteDatabase dba = this.getReadableDatabase();
        Cursor cursor = dba.rawQuery(query, new String[] {String.valueOf(idStatistic)});

        if (cursor != null) {

            if (cursor.getCount() > 0) {
                flag = true;
            }
        }
        cursor.close();
        dba.close();

        return flag;
    }

    public int upTopicCounterByIdTopic(int idTopic) {

        int newCounter = getCounterTopicByTopicId(idTopic) + 1;

        SQLiteDatabase dba = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.STATISTIC_TOPIC_COUNTER, newCounter);

        int totalUpdated = 0;

        totalUpdated = dba.update(Constants.STATISTIC_TABLE_NAME,
                values,
                Constants.STATISTIC_TOPIC_ID + "=?",
                new String[] {String.valueOf(idTopic)});

        dba.close();

        return totalUpdated;
    }

    //Get recent topic by id
    public int getCounterTopicByTopicId(int idTopic) {

        int Counter = 0;

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.STATISTIC_TABLE_NAME,
                new String[] {
                        Constants.STATISTIC_TOPIC_COUNTER },
                Constants.STATISTIC_TOPIC_ID + "=?",
                new String[] {String.valueOf(idTopic)},
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {

            Counter = cursor.getInt(cursor.getColumnIndex(Constants.STATISTIC_TOPIC_COUNTER));

        } else {

            Log.d(Constants.LOG_TAG, ">>> Get Counter Topic by id: No matching data");
        }

        cursor.close();
        dba.close();

        return Counter;
    }


    ///////////////////////////
    //Card topic
    /////////////////////////

    //add content to table card
    public int addCardTopic(CardTopic cardTopic){
        SQLiteDatabase dba = this.getWritableDatabase();
        int id;

        ContentValues values = new ContentValues();
        values.put(Constants.CARD_TOPIC_TEXT, cardTopic.getTopicText());
        values.put(Constants.CARD_TOPIC_ID, cardTopic.getTopicId());

        id = (int) dba.insert(Constants.CARD_TABLE_NAME, null, values);
        dba.close();

        return id;
    }

    //Get card count by id topic
    public int getCardCountByIdTopic(int idTopic) {

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.CARD_TABLE_NAME,
                new String[]{
                        Constants.KEY_ID,
                        Constants.CARD_TOPIC_TEXT,
                        Constants.CARD_TOPIC_ID},
                Constants.CARD_TOPIC_ID + "=?",
                new String[] {String.valueOf(idTopic)},
                null,
                null,
                null,
                null);

        int count = cursor.getCount();

        cursor.close();
        dba.close();

        return count;
    }

    //Get card topic by id
    public CardTopic getCardTopicByTopicId(int idTopic) {

        CardTopic cardTopic = new CardTopic();
        //topic.setTopicParentId(0);

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.CARD_TABLE_NAME,
                new String[] {
                        Constants.KEY_ID,
                        Constants.CARD_TOPIC_TEXT,
                        Constants.CARD_TOPIC_ID},
                Constants.CARD_TOPIC_ID + "=?",
                new String[] {String.valueOf(idTopic)},
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {

            cardTopic.setTopicText(cursor.getString(cursor.getColumnIndex(Constants.CARD_TOPIC_TEXT)));
            cardTopic.setTopicId(cursor.getInt(cursor.getColumnIndex(Constants.CARD_TOPIC_ID)));
            cardTopic.setCardTopicId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

        } else {

            //Toast.makeText(getApplicationContext(), " No matching data", Toast.LENGTH_SHORT).show();
            Log.d(Constants.LOG_TAG, ">>> Get Card Topic by id: No matching data");
        }


        cursor.close();
        dba.close();

        return cardTopic;
    }

    //get list cards topic
    public ArrayList<CardTopic> getCardTopicList() {

        cardTopicList.clear();

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.CARD_TABLE_NAME,
                new String[]{Constants.KEY_ID,
                        Constants.CARD_TOPIC_TEXT,
                        Constants.CARD_TOPIC_ID},
                null,
                null,
                null,
                null,
                null);

        //loop through...
        if (cursor.moveToFirst()) {
            do {

                CardTopic cardTopic = new CardTopic();

                cardTopic.setTopicText(cursor.getString(cursor.getColumnIndex(Constants.CARD_TOPIC_TEXT)));
                cardTopic.setTopicId(cursor.getInt(cursor.getColumnIndex(Constants.CARD_TOPIC_ID)));
                cardTopic.setCardTopicId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

                cardTopicList.add(cardTopic);

            } while (cursor.moveToNext());

            Log.d(Constants.LOG_TAG, ">>> Get all Cards Topic: success");
        } else {

            Log.d(Constants.LOG_TAG, ">>> Get all Cards Topic: No matching data");
        }

        cursor.close();
        dba.close();

        return cardTopicList;
    }

    //Constants.RECENT_TOPIC_WEIGHT + " DESC"
    //get last id Card
    public int getCardLastId() {

        int lastId = 0;

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.CARD_TABLE_NAME,
                new String[]{Constants.KEY_ID},
                null,
                null,
                null,
                null,
                Constants.KEY_ID + " DESC");

        //loop through...
        if (cursor.moveToFirst()) {

            lastId = cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID));

            Log.d(Constants.LOG_TAG, ">>> Get Card Last Id: success");
        } else {

            Log.d(Constants.LOG_TAG, ">>> Get Card Last Id: No matching data");
        }

        cursor.close();
        dba.close();

        return lastId;
    }

    //Get total Cards Topic
    public int getTotalCardTopic() {

        int totalCardTopic = 0;

        String query = "SELECT * FROM " + Constants.CARD_TABLE_NAME;

        SQLiteDatabase dba = this.getReadableDatabase();
        Cursor cursor = dba.rawQuery(query, null);

        totalCardTopic = cursor.getCount();

        Log.d(Constants.LOG_TAG, ">>> Count Card Topic = " + String.valueOf(totalCardTopic));

        cursor.close();
        dba.close();

        return totalCardTopic;
    }

//    //delete a card by id topic
//    public void deleteCardTopicByIdTopic(int topicId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        db.delete(Constants.CARD_TABLE_NAME, Constants.CARD_TOPIC_ID + " = ? ",
//                new String[] {String.valueOf(topicId)});
//        db.close();
//    }

    //delete a card topic
    public void deleteCardTopicByIdCardTopic(int cardId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Constants.CARD_TABLE_NAME, Constants.KEY_ID + " = ? ",
                new String[] {String.valueOf(cardId)});
        db.close();
    }

    //Update card topic by id
    public int updateCardTopicByIdCardTopic(int idCard, int newIdTopic, String newNameTopic) {

        SQLiteDatabase dba = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.CARD_TOPIC_ID, newIdTopic);
        values.put(Constants.CARD_TOPIC_TEXT, newNameTopic);

        int totalUpdated = 0;

        totalUpdated = dba.update(Constants.CARD_TABLE_NAME,
                values,
                Constants.KEY_ID + "=?",
                new String[] {String.valueOf(idCard)});

        dba.close();

        return totalUpdated;
    }



}
