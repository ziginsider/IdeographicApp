package io.github.ziginsider.ideographicapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import data.AsyncProvider;
import data.Constants;
import data.DatabaseHandler;
import data.InitalDatabaseHandler;
import data.RecentTopicAdapter;
import data.RecyclerItemClickListener;
import model.RecentTopics;

@EActivity(R.layout.activity_recent_topic)
public class RecentTopicActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    InitalDatabaseHandler dba;
    DatabaseHandler dba_data;
    ArrayList<RecentTopics> recentTopicsList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AfterItemClickTask afterItemClickTask;

    @ViewById(R.id.recycler_view_recent_topic)
    RecyclerView mRecyclerView;

    @AfterViews
    void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        AppBarLayout.LayoutParams params =
                (AppBarLayout.LayoutParams) toolbar.getLayoutParams();

        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);

        setSupportActionBar(toolbar);

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
        recentTopicsList = dba.getRecentTopicsList();

        // если мы уверены, что изменения в контенте не изменят размер layout-а RecyclerView
        // передаем параметр true - это увеличивает производительность
        mRecyclerView.setHasFixedSize(true);

        // используем linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // создаем адаптер
        mAdapter = new RecentTopicAdapter(recentTopicsList);
        mRecyclerView.setAdapter(mAdapter);


        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this,
                        mRecyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                ArrayList<Integer> idTopicsPageList = new ArrayList<Integer>();
                                idTopicsPageList.clear();

                                int currentId = recentTopicsList.get(position).getTopicId();
                                //add to recent topics
                                afterItemClickTask = new AfterItemClickTask(RecentTopicActivity.this);
                                afterItemClickTask.execute(currentId);

                                idTopicsPageList.add(currentId);
                                do {
                                    currentId = dba_data.getTopicById(currentId).getTopicParentId();
                                    idTopicsPageList.add(currentId);

                                } while (currentId != 0);



                                Intent i = new Intent(RecentTopicActivity.this,
                                        WorkActivityRecycler_.class);
                                i.putExtra(Constants.EXTRA_TOPICS_OPEN_TABS, idTopicsPageList);
                                startActivity(i);
                                RecentTopicActivity.this.finish();
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                // ...
                            }
                        }
                )
        );

        dba.close();
        dba_data.close();

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
        getMenuInflater().inflate(R.menu.recent_topic, menu);
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

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class AfterItemClickTask extends AsyncTask<Integer, Void, Void> {

        private Context mContext;

        public AfterItemClickTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Integer... params) {

            AsyncProvider asyncProvider = new AsyncProvider();
            asyncProvider.setRecentTopic(mContext, params[0]);

            return null;
        }
    }
}
