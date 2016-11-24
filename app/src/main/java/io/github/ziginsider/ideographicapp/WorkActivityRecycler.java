package io.github.ziginsider.ideographicapp;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import java.util.ArrayList;

import data.Constants;

@WindowFeature({Window.FEATURE_ACTION_BAR_OVERLAY})
@EActivity(R.layout.activity_work_recycler)
public class WorkActivityRecycler extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

        //setup view
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        fab.setTag("show");

        navigationView.setNavigationItemSelectedListener(this);

        //first fragment: root topic
        ArrayList<Integer> idTopicsPageList = getIntent().
                getIntegerArrayListExtra(Constants.EXTRA_TOPICS_OPEN_TABS);

        for (int i = (idTopicsPageList.size() - 1); i >= 0; i--) { //set tabs
            fragmentSlidingTabsRecycler.addPage(idTopicsPageList.get(i));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_recycler);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            //Intent i = new Intent(this, MainActivity_.class);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //this.startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.work, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

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
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_recent_show) {

        } else if (id == R.id.nav_statistic) {

        } else if (id == R.id.nav_options) {

        } else if (id == R.id.nav_help) {

            //  Launch app intro
            Intent i = new Intent(WorkActivityRecycler.this, Intro.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_recycler);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

