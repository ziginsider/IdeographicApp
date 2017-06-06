package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import model.AllExpChild;
import model.AllExpParent;
import model.DoubleItem;
import model.Expressions;
import model.Topics;

/**
 * Created by zigin on 20.09.2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private final ArrayList<Expressions> expList = new ArrayList<>();
    private final ArrayList<Topics> topicList = new ArrayList<>();
    private final ArrayList<DoubleItem> doubleItemsList = new ArrayList<>();
    private final ArrayList<ParentObject> parentObjectsList = new ArrayList<>();

    String DB_PATH = null;

    private final Context dbContext;
    private SQLiteDatabase database;

    public DatabaseHandler(Context context) {

        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.dbContext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";

        Log.d(Constants.LOG_TAG, ">>> object DatabaseHandler was created\n"
                + "\nDB name = " + Constants.DATABASE_NAME
                + "\nDB path = " + DB_PATH);
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {

        } else {

            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");
            }
        }
    }

    private  boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {

            String pathDB = DB_PATH + Constants.DATABASE_NAME;

            checkDB = SQLiteDatabase.openDatabase(pathDB,
                    null,
                    SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

        }

        if (checkDB != null) {

            checkDB.close();
        }

        return  checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {

        InputStream input = dbContext.getAssets().open(Constants.DATABASE_NAME);
        String outFileName = DB_PATH + Constants.DATABASE_NAME;
        OutputStream output = new FileOutputStream(outFileName);
        byte[] buffer = new byte[10];
        int lenght;

        while ((lenght = input.read(buffer)) > 0) {

            output.write(buffer, 0, lenght);
        }

        output.flush();
        output.close();
        input.close();
    }

    public void openDataBase() throws SQLiteException {

        String path = DB_PATH + Constants.DATABASE_NAME;
        database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {

        if (database != null) {
            database.close();
        }

        super.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//        String UPGRAGE_EXP_TABLE = "DROP TABLE IF EXISTS " + Constants.TABLE_EXP_NAME;
//        String UPGRAGE_TOPIC_TABLE = "DROP TABLE IF EXISTS " + Constants.TABLE_TOPIC_NAME;
//
//        db.execSQL(UPGRAGE_EXP_TABLE);
//        db.execSQL(UPGRAGE_TOPIC_TABLE);
//
//        onCreate(db);

        if (newVersion > oldVersion) {

            try {

                copyDataBase();

            } catch (IOException e) {

                Log.d(Constants.LOG_TAG, "Coping database error", e);
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table
//        String CREATE_EXP_TABLE = "CREATE TABLE " + Constants.TABLE_EXP_NAME + "("
//                + Constants.KEY_ID + " INTEGER PRIMARY KEY, "
//                + Constants.EXP_TEXT + " TEXT, "
//                + Constants.EXP_PARENT_ID + " INT)";
//                //+ Constants.DATE_NAME + " LONG)";
//
//        String CREATE_TOPIC_TABLE = "CREATE TABLE " + Constants.TABLE_TOPIC_NAME + "("
//                + Constants.KEY_ID + " INTEGER PRIMARY KEY, "
//                + Constants.TOPIC_TEXT + " TEXT, "
//                + Constants.TOPIC_PARENT_ID + " INT "
//                + Constants.TOPIC_LABELS + " TEXT)";
//
//        Log.d(Constants.LOG_TAG, ">>> Tables by name = "
//                + Constants.TABLE_EXP_NAME
//                + " and name = "
//                + Constants.TABLE_TOPIC_NAME
//                + " was created.");
//
//        db.execSQL(CREATE_EXP_TABLE);
//        db.execSQL(CREATE_TOPIC_TABLE);

    }

    //Get total Exp Saved
    public int getTotalExp() {

        int totalExp = 0;

        String query = "SELECT * FROM " + Constants.TABLE_EXP_NAME;

        SQLiteDatabase dba = this.getReadableDatabase();
        Cursor cursor = dba.rawQuery(query, null);

        totalExp = cursor.getCount();

        Log.d(Constants.LOG_TAG, ">>> Count Expressions = " + String.valueOf(totalExp));

        cursor.close();

        return totalExp;
    }

    //Get total Topics Saved
    public int getTotalTopics() {

        int totalTopics = 0;

        String query = "SELECT * FROM " + Constants.TABLE_TOPIC_NAME;

        SQLiteDatabase dba = this.getReadableDatabase();
        Cursor cursor = dba.rawQuery(query, null);

        totalTopics = cursor.getCount();

        Log.d(Constants.LOG_TAG, ">>> Count Topics = " + String.valueOf(totalTopics));

        cursor.close();

        return totalTopics;
    }

    //delete exp item
    public void deleteExp(int id) {

        SQLiteDatabase dba = this.getWritableDatabase();

        dba.delete(Constants.TABLE_EXP_NAME, Constants.KEY_ID + " = ?",
                new String[]{String.valueOf(id)});

        Log.d(Constants.LOG_TAG, ">>> Exp by id = "
                + String.valueOf(id) + " was deleted.");

        dba.close();
    }

    //delete topic item
    public void deleteTopic(int id) {

        SQLiteDatabase dba = this.getWritableDatabase();

        dba.delete(Constants.TABLE_TOPIC_NAME, Constants.KEY_ID + " = ?",
                new String[]{String.valueOf(id)});

        Log.d(Constants.LOG_TAG, ">>> Topic by id = "
                + String.valueOf(id) + " was deleted.");

        dba.close();
    }

    //add content to db - add exp;
    public void addExp(Expressions exp) {

        SQLiteDatabase dba = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.EXP_TEXT, exp.getExpText());
        values.put(Constants.EXP_PARENT_ID, exp.getExpParentId());

        dba.insert(Constants.TABLE_EXP_NAME, null, values);

        Log.d(Constants.LOG_TAG, ">>> Insert expression = " + exp.getExpText());

        dba.close();
    }

    //add content to db - add topic;
    public void addTopic(Topics topic) {

        SQLiteDatabase dba = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.TOPIC_TEXT, topic.getTopicText());
        values.put(Constants.TOPIC_PARENT_ID, topic.getTopicParentId());
        values.put(Constants.TOPIC_LABELS, topic.getTopicLabels());

        dba.insert(Constants.TABLE_TOPIC_NAME, null, values);

        Log.d(Constants.LOG_TAG, ">>> Insert topic = " + topic.getTopicText());

        dba.close();
    }

    //Get all expressions
    public ArrayList<Expressions> getExpressions() {

        expList.clear();

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.TABLE_EXP_NAME,
                new String[]{Constants.KEY_ID, Constants.EXP_TEXT, Constants.EXP_PARENT_ID},
                null, null, null, null, Constants.EXP_PARENT_ID);

        //loop through...
        if (cursor.moveToFirst()) {
            do {

                Expressions exp = new Expressions();

                exp.setExpText(cursor.getString(cursor.getColumnIndex(Constants.EXP_TEXT)));
                exp.setExpParentId(cursor.getInt(cursor.getColumnIndex(Constants.EXP_PARENT_ID)));
                exp.setExpId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

                expList.add(exp);

            } while (cursor.moveToNext());

            Log.d(Constants.LOG_TAG, ">>> Get all Expressions: success");

        } else {

            Log.d(Constants.LOG_TAG, ">>> Get all Expressions: No matching data");
        }

        cursor.close();
        dba.close();

        return expList;
    }

    //Get all topics
    public ArrayList<Topics> getTopics() {

        topicList.clear();

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.TABLE_TOPIC_NAME,
                new String[]{Constants.KEY_ID, Constants.TOPIC_TEXT, Constants.TOPIC_PARENT_ID,
                Constants.TOPIC_LABELS}, null, null, null, null, Constants.TOPIC_TEXT);

        //loop through...
        if (cursor.moveToFirst()) {
            do {

                Topics topic = new Topics();

                topic.setTopicText(cursor.getString(cursor.getColumnIndex(Constants.TOPIC_TEXT)));
                topic.setTopicParentId(cursor.getInt(cursor.getColumnIndex(Constants.TOPIC_PARENT_ID)));
                topic.setTopicLabels(cursor.getString(cursor.getColumnIndex(Constants.TOPIC_LABELS)));
                topic.setTopicId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

                topicList.add(topic);

            } while (cursor.moveToNext());

            Log.d(Constants.LOG_TAG, ">>> Get all Topics: success");
        } else {

            Log.d(Constants.LOG_TAG, ">>> Get all Topics: No matching data");
        }

        cursor.close();
        dba.close();

        return topicList;
    }

    //Get topic by id
    public Topics getTopicById(int idTopic) {

        Topics topic = new Topics();
        //topic.setTopicParentId(0);

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.TABLE_TOPIC_NAME,
                        new String[] {
                                Constants.KEY_ID,
                                Constants.TOPIC_TEXT,
                                Constants.TOPIC_PARENT_ID,
                                Constants.TOPIC_LABELS },
                        Constants.KEY_ID + "=?",
                        new String[] {String.valueOf(idTopic)},
                        null,
                        null,
                        null,
                        null);

        if (cursor.moveToFirst()) {

            topic.setTopicText(cursor.getString(cursor.getColumnIndex(Constants.TOPIC_TEXT)));
            topic.setTopicParentId(cursor.getInt(cursor.getColumnIndex(Constants.TOPIC_PARENT_ID)));
            topic.setTopicLabels(cursor.getString(cursor.getColumnIndex(Constants.TOPIC_LABELS)));
            topic.setTopicId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

        } else {

            //Toast.makeText(getApplicationContext(), " No matching data", Toast.LENGTH_SHORT).show();
            Log.d(Constants.LOG_TAG, ">>> Get Topic by id: No matching data");
        }


        cursor.close();
        dba.close();

        return topic;
    }



    //Get exp by id
    public Expressions getExpById(int idExp) {
        Expressions exp = new Expressions();
        //topic.setExpParentId(0);

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.TABLE_EXP_NAME,
                new String[] {
                        Constants.KEY_ID,
                        Constants.EXP_TEXT,
                        Constants.EXP_PARENT_ID},
                Constants.KEY_ID + "=?",
                new String[] {String.valueOf(idExp)},
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {

            exp.setExpText(cursor.getString(cursor.getColumnIndex(Constants.EXP_TEXT)));
            exp.setExpParentId(cursor.getInt(cursor.getColumnIndex(Constants.EXP_PARENT_ID)));
            exp.setExpId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

        } else {

            //Toast.makeText(getApplicationContext(), " No matching data", Toast.LENGTH_SHORT).show();
            Log.d(Constants.LOG_TAG, ">>> Get Exp by id: No matching data");
        }


        cursor.close();
        dba.close();

        return exp;
    }

//    //Get Topic parent id with id
//    public int getTopicParentIdWithId(int idTopic) {
//
//    }
//
//    //Get Exp parent id with id
//    public int getExpParentIdWithId(int idTopic) {
//
//    }
//
//    //Get Topic Labels with id
//    public String getTopicLabelsWithId(int idTopic) {
//
//    }

    //Get topics by id topic-parent
    public ArrayList<Topics> getTopicByIdParent(int idParent) {

        topicList.clear();

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.TABLE_TOPIC_NAME,
                new String[]{
                        Constants.KEY_ID,
                        Constants.TOPIC_TEXT,
                        Constants.TOPIC_PARENT_ID,
                        Constants.TOPIC_LABELS},
                Constants.TOPIC_PARENT_ID + "=?",
                new String[] {String.valueOf(idParent)},
                null,
                null,
                null,
                null);

        //loop through...
        if (cursor.moveToFirst()) {
            do {

                Topics topic = new Topics();

                topic.setTopicText(cursor.getString(cursor.getColumnIndex(Constants.TOPIC_TEXT)));
                topic.setTopicParentId(cursor.getInt(cursor.getColumnIndex(Constants.TOPIC_PARENT_ID)));
                topic.setTopicLabels(cursor.getString(cursor.getColumnIndex(Constants.TOPIC_LABELS)));
                topic.setTopicId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

                topicList.add(topic);

            } while (cursor.moveToNext());

            Log.d(Constants.LOG_TAG, ">>> Get Topic by id topics parent: Success");

        } else {

            Log.d(Constants.LOG_TAG, ">>> Get Topic by id topics parent: No matching data. Id Parent = "
                    + String.valueOf(idParent));

        }

        cursor.close();
        dba.close();

        return topicList;
    }

    //Get topics count by id topic-parent
    public int getTopicCountByIdParent(int idParent) {

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.TABLE_TOPIC_NAME,
                new String[]{
                        Constants.KEY_ID,
                        Constants.TOPIC_TEXT,
                        Constants.TOPIC_PARENT_ID,
                        Constants.TOPIC_LABELS},
                Constants.TOPIC_PARENT_ID + "=?",
                new String[] {String.valueOf(idParent)},
                null,
                null,
                null,
                null);

        int count = cursor.getCount();

        cursor.close();
        dba.close();

        return count;
    }


    //Get topics by id topic-parent
    public ArrayList<Topics> getTopicByIdParentAlphabet(int idParent) {

        topicList.clear();

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.TABLE_TOPIC_NAME,
                new String[]{
                        Constants.KEY_ID,
                        Constants.TOPIC_TEXT,
                        Constants.TOPIC_PARENT_ID,
                        Constants.TOPIC_LABELS},
                Constants.TOPIC_PARENT_ID + "=?",
                new String[] {String.valueOf(idParent)},
                null,
                null,
                Constants.TOPIC_TEXT,
                null);

        //loop through...
        if (cursor.moveToFirst()) {
            do {

                Topics topic = new Topics();

                topic.setTopicText(cursor.getString(cursor.getColumnIndex(Constants.TOPIC_TEXT)));
                topic.setTopicParentId(cursor.getInt(cursor.getColumnIndex(Constants.TOPIC_PARENT_ID)));
                topic.setTopicLabels(cursor.getString(cursor.getColumnIndex(Constants.TOPIC_LABELS)));
                topic.setTopicId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

                topicList.add(topic);

            } while (cursor.moveToNext());

            Log.d(Constants.LOG_TAG, ">>> Get Topic by id topics parent: Success");

        } else {

            Log.d(Constants.LOG_TAG, ">>> Get Topic by id topics parent: No matching data. Id Parent = "
                    + String.valueOf(idParent));

        }

        cursor.close();
        dba.close();

        return topicList;
    }


    //Get expressions by id topic-parent
    public ArrayList<Expressions> getExpByIdParent(int idParent) {

        expList.clear();

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.TABLE_EXP_NAME,
                new String[]{
                        Constants.KEY_ID,
                        Constants.EXP_TEXT,
                        Constants.EXP_PARENT_ID},
                Constants.EXP_PARENT_ID + "=?",
                new String[] {String.valueOf(idParent)},
                null,
                null,
                Constants.KEY_ID + " DESC", // sort by _id inverse
                null);

        //loop through...
        if (cursor.moveToFirst()) {
            do {

                Expressions exp = new Expressions();

                exp.setExpText(cursor.getString(cursor.getColumnIndex(Constants.EXP_TEXT)));
                exp.setExpParentId(cursor.getInt(cursor.getColumnIndex(Constants.EXP_PARENT_ID)));
                exp.setExpId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

                expList.add(exp);

            } while (cursor.moveToNext());

            Log.d(Constants.LOG_TAG, ">>> Get Expressions by id topics parent: Success");

        } else {

            Log.d(Constants.LOG_TAG, ">>> Get Expressions by id topics parent: No matching data");
        }

        cursor.close();
        dba.close();

        return expList;
    }

    //Get expressions count by id topic-parent
    public int getExpCountByIdParent(int idParent) {

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.TABLE_EXP_NAME,
                new String[]{
                        Constants.KEY_ID,
                        Constants.EXP_TEXT,
                        Constants.EXP_PARENT_ID},
                Constants.EXP_PARENT_ID + "=?",
                new String[] {String.valueOf(idParent)},
                null,
                null,
                null,
                null);

        int count = cursor.getCount();

        cursor.close();
        dba.close();

        return count;
    }
    //Get expressions by id topic-parent, order by topic text
    public ArrayList<Expressions> getExpByIdParentAlphabet(int idParent) {

        expList.clear();

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.TABLE_EXP_NAME,
                new String[]{
                        Constants.KEY_ID,
                        Constants.EXP_TEXT,
                        Constants.EXP_PARENT_ID},
                Constants.EXP_PARENT_ID + "=?",
                new String[] {String.valueOf(idParent)},
                null,
                null,
                Constants.EXP_TEXT,
                null);

        //loop through...
        if (cursor.moveToFirst()) {
            do {

                Expressions exp = new Expressions();

                exp.setExpText(cursor.getString(cursor.getColumnIndex(Constants.EXP_TEXT)));
                exp.setExpParentId(cursor.getInt(cursor.getColumnIndex(Constants.EXP_PARENT_ID)));
                exp.setExpId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

                expList.add(exp);

            } while (cursor.moveToNext());

            Log.d(Constants.LOG_TAG, ">>> Get Expressions by id topics parent: Success");

        } else {

            Log.d(Constants.LOG_TAG, ">>> Get Expressions by id topics parent: No matching data");
        }

        cursor.close();
        dba.close();

        return expList;
    }

    //Update exp text by id
    public int updateExpTextById(int idExp, String newText) {

        SQLiteDatabase dba = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.EXP_TEXT, newText);

        int totalUpdated = 0;

        totalUpdated = dba.update(Constants.TABLE_EXP_NAME,
                values,
                Constants.KEY_ID + "=?",
                new String[] {String.valueOf(idExp)});

        Log.d(Constants.LOG_TAG, ">>> Update Expression by id = "
                + String.valueOf(idExp)
                + ".\n New text = "
                + newText);

        dba.close();

        return totalUpdated;
    }

    //Update topic text by id
    public int updateTopicTextById(int idTopic, String newText) {

        SQLiteDatabase dba = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.TOPIC_TEXT, newText);

        int totalUpdated = 0;

        totalUpdated = dba.update(Constants.TABLE_TOPIC_NAME,
                values,
                Constants.KEY_ID + "=?",
                new String[] {String.valueOf(idTopic)});

        Log.d(Constants.LOG_TAG, ">>> Update Topic by id = "
                + String.valueOf(idTopic)
                + ".\n New text = "
                + newText);

        dba.close();

        return totalUpdated;
    }

    //Update exp parent id by id
    public int updateExpParentIdById(int idExp, int newParentId) {

        SQLiteDatabase dba = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.EXP_PARENT_ID, newParentId);

        int totalUpdated = 0;

        totalUpdated = dba.update(Constants.TABLE_EXP_NAME,
                values,
                Constants.KEY_ID + "=?",
                new String[] {String.valueOf(idExp)});

        Log.d(Constants.LOG_TAG, ">>> Update Expressions parent id by id = "
                + String.valueOf(idExp)
                + ".\n New parent id = "
                + String.valueOf(newParentId));

        dba.close();

        return totalUpdated;
    }

    //Update topic parent id by id
    public int updateTopicParentIdById(int idTopic, int newParentId) {

        SQLiteDatabase dba = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.TOPIC_PARENT_ID, newParentId);

        int totalUpdated = 0;

        totalUpdated = dba.update(Constants.TABLE_TOPIC_NAME,
                values,
                Constants.KEY_ID + "=?",
                new String[] {String.valueOf(idTopic)});

        Log.d(Constants.LOG_TAG, ">>> Update Topics parent id by id = "
                + String.valueOf(idTopic)
                + ".\n New parent id = "
                + String.valueOf(newParentId));

        dba.close();

        return totalUpdated;
    }

    //Update topic labels by id
    public int updateTopicLabelsById(int idTopic, String newLabels) {

        SQLiteDatabase dba = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.TOPIC_LABELS, newLabels);

        int totalUpdated = 0;

        totalUpdated = dba.update(Constants.TABLE_TOPIC_NAME,
                values,
                Constants.KEY_ID + "=?",
                new String[] {String.valueOf(idTopic)});

        Log.d(Constants.LOG_TAG, ">>> Update Topics labels by id = "
                + String.valueOf(idTopic)
                + ".\n New labels = "
                + newLabels);

        dba.close();

        return totalUpdated;
    }


    //Get topic by id
    public ArrayList<String> getTopicLabels(int idTopic) {

        String topicLabelsIdJoin = "";
        ArrayList<String> listLabels = new ArrayList<String>();
        String[] labelsIDSplit;

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.TABLE_TOPIC_NAME,
                new String[] {
                        Constants.KEY_ID,
                        Constants.TOPIC_TEXT,
                        Constants.TOPIC_PARENT_ID,
                        Constants.TOPIC_LABELS },
                Constants.KEY_ID + "=?",
                new String[] {String.valueOf(idTopic)},
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {

            topicLabelsIdJoin = cursor.getString(cursor.getColumnIndex(Constants.TOPIC_LABELS));

        } else {

            //Toast.makeText(getApplicationContext(), " No matching data", Toast.LENGTH_SHORT).show();
            Log.d(Constants.LOG_TAG, ">>> Get Topic Labels: No matching data");
        }

        if (!topicLabelsIdJoin.isEmpty()) {

            labelsIDSplit = topicLabelsIdJoin.split(",");

            if (labelsIDSplit.length > 0)
            {
                for (int i = 0; i < labelsIDSplit.length; i++) {

                    if (!labelsIDSplit[i].equals(""))
                        listLabels.add(getTopicById(Integer.valueOf(labelsIDSplit[i])).
                                getTopicText());
                }
            }
        }

        cursor.close();
        dba.close();

        return listLabels;
    }


    //Get all expressions and them parent topics
    public ArrayList<DoubleItem> getDoubleItems() {

        doubleItemsList.clear();

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.TABLE_EXP_NAME + " as EX inner join " +
                Constants.TABLE_TOPIC_NAME + " as TP on EX." +
                Constants.EXP_PARENT_ID +
                " = TP." +
                Constants.KEY_ID,
                new String[] {
                        Constants.EXP_TEXT,
                        Constants.EXP_PARENT_ID,
                        Constants.TOPIC_TEXT},
                null, null, null, null,
                "EX." + Constants.KEY_ID + " DESC"); // sort by _id inverse

        //loop through...
        if (cursor.moveToFirst()) {
            do {

                Expressions exp = new Expressions();
                Topics topic = new Topics();

                exp.setExpText(cursor.getString(cursor.getColumnIndex(Constants.EXP_TEXT)));
                exp.setExpParentId(cursor.getInt(cursor.getColumnIndex(Constants.EXP_PARENT_ID)));
                topic.setTopicText(cursor.getString(cursor.getColumnIndex(Constants.TOPIC_TEXT)));

                DoubleItem doubleItem = new DoubleItem(exp, topic);

                doubleItemsList.add(doubleItem);

            } while (cursor.moveToNext());

         } else {

            Log.d(Constants.LOG_TAG, ">>> Get all Double items: No matching data");
        }

        cursor.close();
        dba.close();

        return doubleItemsList;
    }

    //Get all expressions (parent)and them transcription, definition and parent topics (child)
    public ArrayList<ParentObject> getAllExpParent() {

        parentObjectsList.clear();

        SQLiteDatabase dba = this.getReadableDatabase();

        Cursor cursor = dba.query(Constants.TABLE_EXP_NAME + " as EX inner join " +
                        Constants.TABLE_TOPIC_NAME + " as TP on EX." +
                        Constants.EXP_PARENT_ID +
                        " = TP." +
                        Constants.KEY_ID,
                new String[] {
                        Constants.EXP_TEXT,
                        Constants.EXP_PARENT_ID,
                        Constants.TOPIC_TEXT},
                null, null, null, null,
                "EX." + Constants.KEY_ID + " DESC"); // sort by _id inverse

        //loop through...
        if (cursor.moveToFirst()) {
            do {

//                Expressions exp = new Expressions();
//                Topics topic = new Topics();
//
//                exp.setExpText(cursor.getString(cursor.getColumnIndex(Constants.EXP_TEXT)));
//                exp.setExpParentId(cursor.getInt(cursor.getColumnIndex(Constants.EXP_PARENT_ID)));
//                topic.setTopicText(cursor.getString(cursor.getColumnIndex(Constants.TOPIC_TEXT)));

                AllExpParent titleParent =
                        new AllExpParent(cursor.getString(cursor.getColumnIndex(Constants.EXP_TEXT)),
                                cursor.getInt(cursor.getColumnIndex(Constants.EXP_PARENT_ID)));

                List<Object> childList = new ArrayList<>();
                String topicText = cursor.getString(cursor.getColumnIndex(Constants.TOPIC_TEXT));
                childList.add(new AllExpChild("[ˈkɒntɛmˌpleɪtɪv]",
                        "=denoting, concerned with, or inclined to contemplation",
                        topicText));
                titleParent.setChildObjectList(childList);
                parentObjectsList.add(titleParent);

            } while (cursor.moveToNext());

        } else {

            Log.d(Constants.LOG_TAG, ">>> Get all exp parent-child items: No matching data");
        }

        cursor.close();
        dba.close();

        return parentObjectsList;
    }

}
