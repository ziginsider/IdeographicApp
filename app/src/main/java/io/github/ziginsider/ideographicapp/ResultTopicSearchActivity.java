package io.github.ziginsider.ideographicapp;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import data.AllExpAdapter;
import data.AllExpDataCreator;
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
    FastScrollRecyclerView mRecyclerView;





    @AfterViews
    void init() {

        AllExpDataCreator creator = AllExpDataCreator.get(this);
        List<ParentObject> parentObjectList = creator.getAll();


        // если мы уверены, что изменения в контенте не изменят размер layout-а RecyclerView
        // передаем параметр true - это увеличивает производительность
        //mRecyclerView.setHasFixedSize(true);

//        // используем linear layout manager
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        // создаем адаптер
//        mAdapter = new RecyclerAdapter(mDoubleItems);
//        mRecyclerView.setAdapter(mAdapter);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        AllExpAdapter adapter = new AllExpAdapter(this, parentObjectList);
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);

        mRecyclerView.setAdapter(adapter);

    }


}
