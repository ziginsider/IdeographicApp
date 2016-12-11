package io.github.ziginsider.ideographicapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import data.Constants;
import data.DatabaseHandler;
import data.InitalDatabaseHandler;

@EActivity(R.layout.activity_work_two)
public class WorkTwoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    InitalDatabaseHandler dba;
    DatabaseHandler dbHandler;

    @ViewById(R.id.toolbar_work)
    Toolbar toolbar;

    @ViewById(R.id.fab)
    FloatingActionButton fab;

    @ViewById(R.id.drawer_layout)
    DrawerLayout drawer;

    @ViewById(R.id.nav_view)
    NavigationView navigationView;

    @FragmentById(R.id.fragment_sliding_tabs_recycler_one)
    FragmentSlidingTabs fragmentSlidingTabsOne;

    @FragmentById(R.id.fragment_sliding_tabs_recycler_two)
    FragmentSlidingTabs fragmentSlidingTabsTwo;

    @AfterViews
    void init() {

        dba = new InitalDatabaseHandler(this); //setup inital db

        //Setup DB
        dbHandler = new DatabaseHandler(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentSlidingTabsOne.addPage(0);
        fragmentSlidingTabsTwo.addPage(0);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.work, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                    currentId = dbHandler.getTopicById(currentId).getTopicParentId();
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

        } else if (id == R.id.nav_statistic) {

            Intent i = new Intent(this, StatisticTopicActivity_.class);
            this.startActivity(i);

        } else if (id == R.id.nav_favorite) {

            Intent i = new Intent(this, FavoriteExpActivity_.class);
            this.startActivity(i);

        } else if (id == R.id.nav_all_exp) {

            Intent i = new Intent(this, ResultTopicSearchActivity_.class);
            this.startActivity(i);

        } else if (id == R.id.nav_help) {

            //  Launch app intro
            Intent i = new Intent(this, Intro.class);
            startActivity(i);

        } else if (id == R.id.nav_options) {

            Intent i = new Intent(this, OptionsActivity.class);
            this.startActivity(i);

        } else if (id == R.id.nav_rate) {

            Toast.makeText(this, "TODO: rate it", Toast.LENGTH_SHORT).show();
//            Intent i = new Intent(this, WorkTwoActivity_.class);
//            this.startActivity(i);

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
        dbHandler.close();
        super.onDestroy();
    }
}
