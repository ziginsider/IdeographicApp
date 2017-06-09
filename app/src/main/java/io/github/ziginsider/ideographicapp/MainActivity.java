package io.github.ziginsider.ideographicapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;


import data.Constants;
import data.DatabaseHandler;
import data.InitalDatabaseHandler;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//    }

    //InitalDatabaseHandler dba;
    DatabaseHandler dbHandler;

    //boolean doubleBackToExitPressedOnce = false;

//    @ViewById(R.id.btn_open_recent)
//    Button btnAllTopics;
//
//    @Click(R.id.btn_open_recent)
//    void clickBtnAllTopics() {
//
//        ArrayList<Integer> idTopicsPageList = new ArrayList<Integer>();
//        idTopicsPageList.clear();
//
//        int currentId = dba.getIdTopicTopRecentTopics();
//
//        idTopicsPageList.add(currentId);
//
//        if (currentId != 0) {
//            do {
//                currentId = dbHandler.getTopicById(currentId).getTopicParentId();
//                idTopicsPageList.add(currentId);
//
//            } while (currentId != 0);
//        }
//        Intent i = new Intent(MainActivity.this,
//                WorkActivityRecycler_.class);
//        i.putExtra(Constants.EXTRA_TOPICS_OPEN_TABS, idTopicsPageList);
//        startActivity(i);
//    }
//
//    @Click(R.id.btn_all_exp)
//    void clickBtnAllExp() {
//
//        Intent i = new Intent(this, ResultTopicSearchActivity_.class);
//
//        this.startActivity(i);
//    }
//
//    @Click(R.id.btn_topics_recycler)
//    void clickBtnTopicRecycler() {
//
//        Intent i = new Intent(this, WorkActivityRecycler_.class);
//        ArrayList<Integer> startTopicsList = new ArrayList<>();
//        startTopicsList.add(0); //set topics root = "Topics"
//        i.putExtra(Constants.EXTRA_TOPICS_OPEN_TABS, startTopicsList);
//        this.startActivity(i);
//    }
//
//    @Click(R.id.btn_recent_topics)
//    void clickBtnRecentTopics() {
//
//        Intent i = new Intent(this, RecentTopicActivity_.class);
//        //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        this.startActivity(i);
//    }
//
//    @Click(R.id.btn_favorite_exp)
//    void clickBtnFavoriteExp() {
//
//        Intent i = new Intent(this, FavoriteExpActivity_.class);
//        //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        this.startActivity(i);
//    }
//
//    @Click(R.id.btn_statistic_topic)
//    void clickBtnStatisticTopics() {
//
//        Intent i = new Intent(this, StatisticTopicActivity_.class);
//        //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        this.startActivity(i);
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        setTheme(R.style.AppTheme_NoActionBar);
//        super.onCreate(savedInstanceState);
//    }

    @AfterViews
    void init() {

        //dba = new InitalDatabaseHandler(this); //setup inital db

        //Setup DB
        dbHandler = new DatabaseHandler(this);
        try {
            dbHandler.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            dbHandler.openDataBase();
        } catch (Exception e) {
            //throw sqle;
            Log.d("Main", "Error open db", e);
        }

        dbHandler.close();

        Intent i = new Intent(this, WorkActivityRecycler_.class);


        ArrayList<Integer> startTopicsList = new ArrayList<>();
        startTopicsList.add(0); //set topics root = "Topics"
        i.putExtra(Constants.EXTRA_TOPICS_OPEN_TABS, startTopicsList);


        this.startActivity(i);
        this.finish();
    }



//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            //super.onBackPressed();
//
//            if (doubleBackToExitPressedOnce) {
//                super.onBackPressed();
//                return;
//            }
//
//            this.doubleBackToExitPressedOnce = true;
//            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//            new Handler().postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    doubleBackToExitPressedOnce=false;
//                }
//            }, 2000);
//        }
//    }


    @Override
    protected void onDestroy() {
        //dba.close();
        dbHandler.close();
        super.onDestroy();
    }
}
