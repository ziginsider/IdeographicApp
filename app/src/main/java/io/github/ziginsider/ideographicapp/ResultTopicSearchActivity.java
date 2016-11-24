package io.github.ziginsider.ideographicapp;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import data.DatabaseHandler;
import data.RecyclerAdapter;
import model.DoubleItem;

@EActivity(R.layout.activity_result_topic_search)
public class ResultTopicSearchActivity extends AppCompatActivity {

    //private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseHandler dba;
    private ArrayList<DoubleItem> mDoubleItems;

    @ViewById(R.id.recycler_view_result_topic_search)
    RecyclerView mRecyclerView;

    @AfterViews
    void init() {

        dba = new DatabaseHandler(this);
        //get all expressions
        mDoubleItems = dba.getDoubleItems();

//        for (Expressions exp : mDoubleItems) {
//
//            Topics topic;
//            topic = dba_inital.getTopicById(exp.getExpParentId());
//            //mListTopicName.add((dba_inital.getTopicById(exp.getExpParentId())).getTopicText());
//            mListTopicName.add("Ziginsider");
//            //mListTopicName.add(topic.getTopicText());
//        }

        // если мы уверены, что изменения в контенте не изменят размер layout-а RecyclerView
        // передаем параметр true - это увеличивает производительность
        mRecyclerView.setHasFixedSize(true);

        // используем linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // создаем адаптер
        mAdapter = new RecyclerAdapter(mDoubleItems);
        mRecyclerView.setAdapter(mAdapter);

        dba.close();
    }


}
