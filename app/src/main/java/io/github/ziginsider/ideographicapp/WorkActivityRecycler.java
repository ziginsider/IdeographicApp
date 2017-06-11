package io.github.ziginsider.ideographicapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import java.util.ArrayList;

import data.Constants;
import data.DatabaseHandler;
import data.InitalDatabaseHandler;
import model.CardData;
import model.RecentTopics;
import model.Topics;

@WindowFeature({Window.FEATURE_ACTION_BAR_OVERLAY})
@EActivity(R.layout.activity_work_recycler)
public class WorkActivityRecycler extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    InitalDatabaseHandler dba;
    DatabaseHandler dbHandler;

    boolean doubleBackToExitPressedOnce = false;
    static Button buttonBadgeCardStack;
    static int mCardStackCount = 0;

    @ViewById(R.id.toolbar_work_recycler)
    Toolbar toolbar;

    @ViewById(R.id.fab_recycler)
    com.melnykov.fab.FloatingActionButton fab;

    @ViewById(R.id.drawer_layout_recycler)
    DrawerLayout drawer;

    @ViewById(R.id.nav_view_recycler)
    NavigationView navigationView;

    @ViewById(R.id.search_view_recycler)
    MaterialSearchView searchView;

    @FragmentById(R.id.fragment_sliding_tabs_recycler)
    FragmentSlidingTabsRecycler fragmentSlidingTabsRecycler;

    @AfterViews
    void init() {

        dba = new InitalDatabaseHandler(this); //setup inital db

        //Setup DB
        dbHandler = new DatabaseHandler(this);

        //setup view
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        fab.setTag("show");

        navigationView.setNavigationItemSelectedListener(this);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(WorkActivityRecycler.this, Intro.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();


//        try {
//            dbHandler.createDataBase();
//        } catch (IOException ioe) {
//            throw new Error("Unable to create database");
//        }
//        try {
//            dbHandler.openDataBase();
//        } catch (Exception e) {
//            //throw sqle;
//            Log.d("Main", "Error open db", e);
//        }

        ArrayList<Integer> idTopicsPageList = new ArrayList<>();

        //first fragment: root topic
        if (getIntent().getIntegerArrayListExtra(Constants.EXTRA_TOPICS_OPEN_TABS) != null) {

            idTopicsPageList = getIntent().
                    getIntegerArrayListExtra(Constants.EXTRA_TOPICS_OPEN_TABS);
        } else {

            idTopicsPageList.add(0);
        }

        for (int i = (idTopicsPageList.size() - 1); i >= 0; i--) { //set tabs
            fragmentSlidingTabsRecycler.addPage(idTopicsPageList.get(i));
        }

        Toast.makeText(this, "Я в главном!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_recycler);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                exitApp();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.work, menu);

        MenuItem itemSearch = menu.findItem(R.id.action_search);
        searchView.setMenuItem(itemSearch);

        MenuItem item = menu.findItem(R.id.action_badge);
        MenuItemCompat.setActionView(item, R.layout.feed_card_stack_count);
        buttonBadgeCardStack = (Button) MenuItemCompat.getActionView(item);
        buttonBadgeCardStack.setText(String.valueOf(mCardStackCount));
        buttonBadgeCardStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(v.getContext(), CardStackActivity.class));
            }
        });
        buttonBadgeCardStack.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast = Toast.makeText(WorkActivityRecycler.this, "Number of desktops", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER, 150, 100);
                toast.show();
                return true;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    private void setCardStackCount(int count) {
        mCardStackCount = count;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

//        if (id == R.id.action_badge) {
//
//            return true;
//        }

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
            Intent i = new Intent(WorkActivityRecycler.this,
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
            Intent i = new Intent(WorkActivityRecycler.this, Intro.class);
            startActivity(i);

        } else if (id == R.id.nav_options) {

            Intent i = new Intent(this, OptionsActivity.class);
            this.startActivity(i);

        } else if (id == R.id.nav_rate) {

            //Toast.makeText(this, "TODO: rate it", Toast.LENGTH_SHORT).show();
            //Intent i = new Intent(this, WorkTwoActivity_.class);

        } else if (id == R.id.nav_exit) {

            exitApp();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_recycler);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void exitApp() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    @Override
    protected void onDestroy() {
        dba.close();
        dbHandler.close();
        super.onDestroy();
    }
}

