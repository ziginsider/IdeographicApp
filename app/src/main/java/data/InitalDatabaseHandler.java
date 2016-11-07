package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import model.RecentTopics;

/**
 * Created by zigin on 06.11.2016.
 */

public class InitalDatabaseHandler extends SQLiteOpenHelper {

    private final ArrayList<RecentTopics> recentTopicsList = new ArrayList<>();

    String DB_PATH = null;

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
                ")"; //';' ??

        db.execSQL(CREATE_RECENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.RECENT_TABLE_NAME);

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



}