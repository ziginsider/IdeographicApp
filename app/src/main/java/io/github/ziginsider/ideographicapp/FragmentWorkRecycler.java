package io.github.ziginsider.ideographicapp;

/**
 * Created by zigin on 26.10.2016.
 */


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import java.util.ArrayList;

import data.AsyncProvider;
import data.Constants;
import data.DatabaseHandler;
import data.PersistantStorage;
import data.RecyclerExpAdapter;
import data.RecyclerItemClickListener;
import data.RecyclerTopicAdapter;
import model.Expressions;
import model.Topics;

public class FragmentWorkRecycler extends Fragment {

    private int mParentTopicId;

    RecyclerView listTopicContentRecycler;

    com.melnykov.fab.FloatingActionButton fab;

    private DatabaseHandler dba;

    public ArrayList<Topics> topicsFromDB;
    private ArrayList<Topics> mFoundTopics;
    //public int topicsCount;
    public ArrayList<Expressions> expFromDB;
    private ArrayList<Expressions> mFoundExp;

    private FragmentActivity workContext;
    private PersistantStorage storage;

    private RecyclerTopicAdapter mAdapterTopic;
    private RecyclerExpAdapter mAdapterExp;
    android.support.v7.widget.LinearLayoutManager mLayoutManager;

    private AfterItemClickTask afterItemClickTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_topic_content_recycler, container, false);

        Bundle bundle = getArguments();
        mParentTopicId = bundle.getInt(Constants.BUNDLE_ID_TOPIC);

        listTopicContentRecycler = (RecyclerView) v.findViewById(R.id.list_topic_content_recycler);

        storage = new PersistantStorage();

        mLayoutManager = new LinearLayoutManager(getContext());
        listTopicContentRecycler.setLayoutManager(mLayoutManager);

        refreshData();

        return v;
    }

    @Override
    public void onAttach(Context context) {
        workContext = (FragmentActivity) context;
        super.onAttach(context);
        dba = new DatabaseHandler(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public int getmParentTopicId() {
        return mParentTopicId;
    }

    private void refreshData() {

        Log.d("Zig", "begin function refreshData()");

        topicsFromDB = new ArrayList<Topics>();
        expFromDB = new ArrayList<Expressions>();
        //get child-topics
        topicsFromDB = dba.getTopicByIdParentAlphabet(mParentTopicId);

        //get child-expressions
        expFromDB = dba.getExpByIdParent(mParentTopicId);

        //clone fromDB -> foundItems
        cloneItems();

        fab = (com.melnykov.fab.FloatingActionButton) getActivity().findViewById(R.id.fab_recycler);

        showListView();
        showHideView();


//        tabbar = (AppBarLayout) getActivity().findViewById(R.id.appbar_layout_recycler);
//        appbar = (AppBarLayout) getActivity().findViewById(R.id.appbar_layout_work_recycler);
//        viewPager = (ViewPager) getActivity().findViewById(R.id.work_view_pager_recycler);
//
//
        listTopicContentRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int mLastFirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final int currentFirstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (currentFirstVisibleItem > this.mLastFirstVisibleItem) {

                    if (fab.getTag() == "show") {
                        fab.animate().translationY(fab.getBottom()).
                                setInterpolator(new AccelerateInterpolator()).
                                setDuration(getResources().
                                        getInteger(android.R.integer.config_mediumAnimTime)).
                                start();
                        fab.setTag("hide");
                    }
                } else if (currentFirstVisibleItem < this.mLastFirstVisibleItem) {

                    fab.animate().translationY(0).
                            setInterpolator(new DecelerateInterpolator()).start();
                    fab.setTag("show");
                }

                this.mLastFirstVisibleItem = currentFirstVisibleItem;
            }
        });
        Log.d("Zig", "End function RefreshData()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dba.close();
    }

    public void showHideView() {

        if(fab != null)
        {
            if (fab.getTag() == "hide") {
                fab.animate().translationY(0).
                        setInterpolator(new DecelerateInterpolator()).start();
                fab.setTag("show");
            }
        }
    }

    public void showListView() {

        // Log.d("Zig", "showListView() begin, mQuerySearch = " + getQuerySearch());

        if (topicsFromDB != null) {
            if (!topicsFromDB.isEmpty()) {


                // создаем адаптер
                //
                mAdapterTopic = new RecyclerTopicAdapter(topicsFromDB);
                listTopicContentRecycler.setAdapter(mAdapterTopic);

                //recyclerview item click
                listTopicContentRecycler.addOnItemTouchListener(
                        new RecyclerItemClickListener(getActivity(),
                                listTopicContentRecycler,
                                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        if (!topicsFromDB.isEmpty()) {

                            //save select item position
                            storage.init(getContext());
                            if (mParentTopicId == 0) {

                                storage.addProperty(Constants.TOPICS_ROOT_NAME,
                                        mFoundTopics.get(position).getTopicText());
                            } else {
                                storage.addProperty(dba.getTopicById(mParentTopicId).getTopicText(),
                                        mFoundTopics.get(position).getTopicText());
                            }

                            FragmentSlidingTabsRecycler fragmentSlidingTabsRecycler =
                                    (FragmentSlidingTabsRecycler) workContext.
                                            getSupportFragmentManager().
                                            findFragmentById(R.id.fragment_sliding_tabs_recycler);

                            //if do not have child topics
                            if (fragmentSlidingTabsRecycler.getCountTabs() ==
                                    (fragmentSlidingTabsRecycler.getSelectedTabPosition() + 1)) {
                                //get child topic
                                Topics topic = mFoundTopics.get(position);
                                fragmentSlidingTabsRecycler.addPage(topic.getTopicId());

                            } else {
                                //remove child topics
                                while (fragmentSlidingTabsRecycler.getCountTabs() !=
                                        (fragmentSlidingTabsRecycler.getSelectedTabPosition() + 1)) {
                                    fragmentSlidingTabsRecycler.
                                            removePage(fragmentSlidingTabsRecycler.
                                                    getSelectedTabPosition() + 1);
                                }
                                //get child topic
                                Topics topic = mFoundTopics.get(position);
                                //Log.d("Zig", "press topic text = " + topic.get TopicText());
                                fragmentSlidingTabsRecycler.addPage(topic.getTopicId());
                            }
                        }
//                     else {
//
//                        Expressions exp = mFoundExp.get(position);
//
//                        ClipboardManager clipboard = (ClipboardManager) getActivity().
//                                getSystemService(Context.CLIPBOARD_SERVICE);
//                        ClipData clip = ClipData.newPlainText(exp.getExpText(), exp.getExpText());
//                        clipboard.setPrimaryClip(clip);
//
//                    }
                        //set recent topic
                        afterItemClickTask = new AfterItemClickTask(getContext());
                        afterItemClickTask.execute(mFoundTopics.get(position).getTopicId());
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        // ...
                    }
                }));
            } else {

                // создаем адаптер
                mAdapterExp = new RecyclerExpAdapter(expFromDB);
                listTopicContentRecycler.setAdapter(mAdapterExp);
                mAdapterExp.notifyDataSetChanged();
            }
        }
    }

    public void showSearchResult(String searchText) {


        if (searchText != null && !searchText.isEmpty() && topicsFromDB != null) {

            if (!topicsFromDB.isEmpty()) {

//                Log.d("Zig", "In showSearchResult()"
//                        + " topicsFromDB isn't empty ");

//                if (mFoundTopics != null) {
//
//                    mFoundTopics.clear();
//                }
//
//
//                for(Topics item:topicsFromDB) {
//
//                    //not case sensitive
//                    if (item.getTopicText().toLowerCase().contains(searchText.toLowerCase())) {
//
//                        mFoundTopics.add(item);
//                    }
//                }
//
//                topicAdapterRecycler = new CustomListViewTopicAdapter(getActivity(),
//                        R.layout.adapter_topic_item,
//                        mFoundTopics); //send id topics current tabs
//                listTopicContentRecycler.setAdapter(topicAdapterRecycler);
//                topicAdapterRecycler.notifyDataSetChanged();
//
//                //textItemCount.setText("Number of subtopics");
//                //itemCount.setText(String.valueOf(mFoundTopics.size()));
            } else { // work with exp
//                Log.d("Zig", "In showSearchResult()"
//                        + " topicsFromDB is empty ");

                //show expressions search result
                if (mFoundTopics != null) {

                    mFoundExp.clear();
                }

                for (Expressions item : expFromDB) {
                    //not case sensitive
                    if (item.getExpText().toLowerCase().contains(searchText.toLowerCase())) {

                        mFoundExp.add(item);
                    }
                }

//                ExpAdapterRecycler = new CustomListViewExpAdapter(getActivity(),
//                        R.layout.adapter_exp_item,
//                        mFoundExp);
//                listTopicContentRecycler.setAdapter(ExpAdapterRecycler);
//                ExpAdapterRecycler.notifyDataSetChanged();

//                mLayoutManager = new LinearLayoutManager(getContext());
//                listTopicContentRecycler.setLayoutManager(mLayoutManager);
                // создаем адаптер
                mAdapterExp = new RecyclerExpAdapter(mFoundExp);
                listTopicContentRecycler.setAdapter(mAdapterExp);
                mAdapterExp.notifyDataSetChanged();
//                textItemCount.setText("Number of expressions:");
//                itemCount.setText(String.valueOf(mFoundExp.size()));
            }

        } else {
            showListView();
        }
    }

    public void showSearchResultTopic(String searchText) {
        if (searchText != null && !searchText.isEmpty()) {

            if (!topicsFromDB.isEmpty()) {

                Toast.makeText(getActivity(), "TODO result of subtopic search: " + searchText,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

//    public void fabLauncher() {
//
//
//        if (!topicsFromDB.isEmpty()) {
//
//            Toast.makeText(getActivity(), "I'm a black cat! You have " +
//                    mFoundTopics.size() + " topics.", Toast.LENGTH_SHORT).show();
//
//        } else {
//
//            Toast.makeText(getActivity(), "I'm a black cat! You have " +
//                    mFoundExp.size() + " expessions.", Toast.LENGTH_SHORT).show();
//
//        }
//
//        //toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
//        //appBarLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
//    }

    public void cloneItems() {

        if (topicsFromDB != null) {
            mFoundTopics = (ArrayList<Topics>) topicsFromDB.clone();
        }
        if (expFromDB != null) {
            mFoundExp = (ArrayList<Expressions>) expFromDB.clone();
        }
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
            asyncProvider.setStatisticTopic(mContext, params[0]);

            return null;
        }
    }
}
