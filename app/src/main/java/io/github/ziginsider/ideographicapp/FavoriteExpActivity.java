package io.github.ziginsider.ideographicapp;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import data.Constants;
import data.DatabaseHandler;
import data.FavoriteAdapter;
import data.InitalDatabaseHandler;
import model.FavoriteExpressions;

@EActivity(R.layout.activity_favorite_exp)
public class FavoriteExpActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    InitalDatabaseHandler dba;
    DatabaseHandler dba_data;
    ArrayList<FavoriteExpressions> favoriteExpList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //private RecentTopicActivity.AfterItemClickTask afterItemClickTask;

    @ViewById(R.id.recycler_view_favorite_exp)
    RecyclerView mRecyclerView;

    static Button notifCount;
    static int mNotifCount = 0;


    @AfterViews
    void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        AppBarLayout.LayoutParams params =
                (AppBarLayout.LayoutParams) toolbar.getLayoutParams();

        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);

        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setIcon(R.drawable.shape_counter_card_stack);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dba = new InitalDatabaseHandler(this);
        dba_data = new DatabaseHandler(this);
        //get all expressions
        favoriteExpList = dba.getFavoriteExpList();

        // если мы уверены, что изменения в контенте не изменят размер layout-а RecyclerView
        // передаем параметр true - это увеличивает производительность
        //mRecyclerView.setHasFixedSize(true);

        // используем linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // создаем адаптер
        mAdapter = new FavoriteAdapter(favoriteExpList);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.favorite_exp, menu);
//        return true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_exp, menu);

//        View count = menu.findItem(R.id.action_settings).getActionView();
//        notifCount = (Button) count.findViewById(R.id.notif_count);
//        notifCount.setText(String.valueOf(4));

        MenuItem item = menu.findItem(R.id.action_settings);
        MenuItemCompat.setActionView(item, R.layout.feed_update_count);
        notifCount = (Button) MenuItemCompat.getActionView(item);
        notifCount.setText(String.valueOf(mNotifCount));
        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "URrrrrra!", Toast.LENGTH_SHORT).show();
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            setNotifCount(mNotifCount + 1);
            return true;
        }
        if(id == R.id.icon1) {
            setNotifCount(mNotifCount + 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setNotifCount(int count){
        mNotifCount = count;
        invalidateOptionsMenu();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_start) {

            Intent i = new Intent(this, WorkActivityRecycler_.class);
            ArrayList<Integer> startTopicsList = new ArrayList<>();
            startTopicsList.add(0); //set topics root = "Topics"
            i.putExtra(Constants.EXTRA_TOPICS_OPEN_TABS, startTopicsList);
            this.startActivity(i);
            this.finish();

        } else if (id == R.id.nav_recent_open) {

            ArrayList<Integer> idTopicsPageList = new ArrayList<Integer>();
            idTopicsPageList.clear();

            int currentId = dba.getIdTopicTopRecentTopics();

            idTopicsPageList.add(currentId);

            if (currentId != 0) {
                do {
                    currentId = dba_data.getTopicById(currentId).getTopicParentId();
                    idTopicsPageList.add(currentId);

                } while (currentId != 0);
            }
            Intent i = new Intent(this,
                    WorkActivityRecycler_.class);
            i.putExtra(Constants.EXTRA_TOPICS_OPEN_TABS, idTopicsPageList);
            this.startActivity(i);
            this.finish();

        } else if (id == R.id.nav_recent_show) {

            Intent i = new Intent(this, RecentTopicActivity_.class);
            this.startActivity(i);
            this.finish();

        } else if (id == R.id.nav_statistic) {

            Intent i = new Intent(this, StatisticTopicActivity_.class);
            this.startActivity(i);
            this.finish();

        } else if (id == R.id.nav_favorite) {


        } else if (id == R.id.nav_all_exp) {

            Intent i = new Intent(this, ResultTopicSearchActivity_.class);
            this.startActivity(i);
            this.finish();

        } else if (id == R.id.nav_help) {

            //  Launch app intro
            Intent i = new Intent(this, Intro.class);
            startActivity(i);

        } else if (id == R.id.nav_options) {

            Intent i = new Intent(this, OptionsActivity.class);
            this.startActivity(i);

        } else if (id == R.id.nav_rate) {

            Toast.makeText(this, "TODO: rate it", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_exit) {

            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        dba.close();
        dba_data.close();
        super.onDestroy();
    }
}
