package io.github.ziginsider.ideographicapp;

/**
 * Created by zigin on 26.10.2016.
 */

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import data.Constants;
import data.DatabaseHandler;
import data.InitalDatabaseHandler;
import data.PersistantStorage;
import data.RecyclerExpAdapter;
import data.RecyclerItemClickListener;
import data.RecyclerTopicAdapter;
import model.Expressions;
import model.RecentTopics;
import model.Topics;

public class FragmentWorkRecycler extends Fragment {

    private int mParentTopicId;

    RecyclerView listTopicContentRecycler;

    AppBarLayout tabbar;
    ViewPager viewPager;
    com.melnykov.fab.FloatingActionButton fab;

    private DatabaseHandler dba;

    public ArrayList<Topics> topicsFromDB;
    private ArrayList<Topics> mFoundTopics;
    //public int topicsCount;
    public ArrayList<Expressions> expFromDB;
    private ArrayList<Expressions> mFoundExp;

    private FragmentActivity workContext;
    private PersistantStorage storage;

    //private RecyclerView.Adapter mAdapterTopic;
    private RecyclerTopicAdapter mAdapterTopic;
    private RecyclerExpAdapter mAdapterExp;
    private RecyclerView.LayoutManager mLayoutManager;

    private AfterItemClickTask afterItemClickTask;

    //private int mSelectItemPosition;

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
        //topicsCount = topicsFromDB.size();

        //get child-expressions
        expFromDB = dba.getExpByIdParent(mParentTopicId);

        //clone fromDB -> foundItems
        cloneItems();

//        listTopicContentRecycler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Log.d("Zig", "listTopicContentRecycler.setOnItemClickListener begin");
//
//                if (!topicsFromDB.isEmpty()) {
//
//                    //save select item position
//                    storage.init(getContext());
//                    if (mParentTopicId == 0) {
//
//                        storage.addProperty(Constants.TOPICS_ROOT_NAME,
//                                mFoundTopics.get(position).getTopicText());
//
//                        Log.d("Zig", "\n+++++++ Press Add topic witn name = " +
//                                Constants.TOPICS_ROOT_NAME +
//                                "\n select topic = " +
//                                mFoundTopics.get(position).getTopicText());
//                    } else {
//                        storage.addProperty(dba.getTopicById(mParentTopicId).getTopicText(),
//                                mFoundTopics.get(position).getTopicText());
//
//                        Log.d("Zig", "\n+++++++ Press Add topic witn name = " +
//                                dba.getTopicById(mParentTopicId).getTopicText() +
//                                "\nselect topic = " +
//                                mFoundTopics.get(position).getTopicText());
//                    }
//
//
//                    FragmentSlidingTabs fragmentSlidingTabs = (FragmentSlidingTabs)
//                            workContext.getSupportFragmentManager().findFragmentById(R.id.fragment_sliding_tabs);
//
//
//                    //if do not have child topics
//                    if (fragmentSlidingTabs.getCountTabs() == (fragmentSlidingTabs.getSelectedTabPosition() + 1) ) {
//                        //get child topic
//                        Topics topic = mFoundTopics.get(position);
//                        //Log.d("Zig", "press topic text = " + topic.getTopicText());
//                        fragmentSlidingTabs.addPage(topic.getTopicId());
//
//                    } else {
//
//                        //remove child topics
//                        while (fragmentSlidingTabs.getCountTabs() != (fragmentSlidingTabs.getSelectedTabPosition() + 1)) {
//
//                            fragmentSlidingTabs.removePage(fragmentSlidingTabs.getSelectedTabPosition() + 1);
//                        }
//                        //get child topic
//                        Topics topic = mFoundTopics.get(position);
//                        //Log.d("Zig", "press topic text = " + topic.getTopicText());
//                        fragmentSlidingTabs.addPage(topic.getTopicId());
//                    }
//                } else {
//
//                    Expressions exp = mFoundExp.get(position);
//                    //Toast.makeText(getActivity(), "PRESS: " + exp.getExpText(), Toast.LENGTH_SHORT).show();
//
//                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
//                    ClipData clip = ClipData.newPlainText(exp.getExpText(), exp.getExpText());
//                    clipboard.setPrimaryClip(clip);
//
//                }
//            }
//
//
//        });

//        listTopicContentRecycler.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Toast.makeText(getActivity(), "Long click", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });

        fab = (com.melnykov.fab.FloatingActionButton) getActivity().findViewById(R.id.fab_recycler);

        showListView();
        showHideView();

//        //show and hide toolbar from scroll
//        mLastFirstVisibleItem = listTopicContentRecycler.getFirstVisiblePosition();
//        //mLastFirstVisibleItem = 0;
//
//        tabbar = (AppBarLayout) getActivity().findViewById(R.id.appbar_layout_recycler);
//        viewPager = (ViewPager) getActivity().findViewById(R.id.work_view_pager_recycler);
//
//        listTopicContentRecycler.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//                if (view.getId() == listTopicContentRecycler.getId()) {
//                    final int currentFirstVisibleItem = listTopicContentRecycler.getFirstVisiblePosition();
//
//                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
//
//                        tabbar.animate().translationY(-tabbar.getBottom()).
//                                setInterpolator(new AccelerateInterpolator()).start();
//
//                        viewPager.animate().translationY(-(tabbar.getBottom())).
//                                setInterpolator(new AccelerateInterpolator()).start();
//
//                        fab.animate().translationY(fab.getBottom()).
//                                setInterpolator(new AccelerateInterpolator(2)).start();
//
//                        fab.setTag("hide");
//
//                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
//
//                        tabbar.animate().translationY(0).
//                                setInterpolator(new DecelerateInterpolator()).start();
//
//                        viewPager.animate().translationY(0).
//                                setInterpolator(new DecelerateInterpolator()).start();
//
//                        fab.animate().translationY(0).
//                                setInterpolator(new DecelerateInterpolator()).start();
////                        ResizeAnimation resizeAnimation = new ResizeAnimation(tabbar,
////                                tabbar.getWidth(),
////                                tabbarHeight);
////                        resizeAnimation.setInterpolator(new AccelerateInterpolator());
////                        tabbar.startAnimation(resizeAnimation);
//                        fab.setTag("show");
//                    }
//
//                    mLastFirstVisibleItem = currentFirstVisibleItem;
//                }
//            }
//        });


        Log.d("Zig", "End function RefreshData()");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        dba.close();
    }

    public void showHideView() {

//        if(fab != null)
//        {
//            if (fab.getTag() == "hide") {
//
//                tabbar = (AppBarLayout) getActivity().findViewById(R.id.appbar_layout_recycler);
//                viewPager = (ViewPager) getActivity().findViewById(R.id.work_view_pager_recycler);
//
//                tabbar.animate().translationY(0).
//                        setInterpolator(new DecelerateInterpolator()).start();
//                viewPager.animate().translationY(0).
//                        setInterpolator(new DecelerateInterpolator()).start();
//                fab.animate().translationY(0).
//                        setInterpolator(new DecelerateInterpolator()).start();
//
//                fab.setTag("show");
//                //Toast.makeText(getActivity(), "I'am working", Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    public void showListView() {

        // Log.d("Zig", "showListView() begin, mQuerySearch = " + getQuerySearch());

        if (topicsFromDB != null) {
            if (!topicsFromDB.isEmpty()) {


//                //setup adapter topics
//                topicAdapterRecycler = new CustomListViewTopicAdapter(getActivity(),
//                        R.layout.adapter_topic_item,
//                        topicsFromDB); //send id topics current tabs
//                listTopicContentRecycler.setAdapter(topicAdapterRecycler);
//                topicAdapterRecycler.notifyDataSetChanged();


//                mLayoutManager = new LinearLayoutManager(getContext());
//                listTopicContentRecycler.setLayoutManager(mLayoutManager);


                // создаем адаптер
                //
                mAdapterTopic = new RecyclerTopicAdapter(topicsFromDB);
                listTopicContentRecycler.setAdapter(mAdapterTopic);

//                FragmentSlidingTabsRecycler fragmentSlidingTabsRecycler =
//                        (FragmentSlidingTabsRecycler) workContext.
//                                getSupportFragmentManager().
//                                findFragmentById(R.id.fragment_sliding_tabs_recycler);
//
//                if ((fragmentSlidingTabsRecycler.getSelectedTabPosition() + 1) !=
//                        fragmentSlidingTabsRecycler.getCountTabs()) {
//
//                    for (int i = 0; i < topicsFromDB.size(); i++){
//
//                        if (topicsFromDB.get(i).getTopicText().
//                                equals(fragmentSlidingTabsRecycler.getNextTabName())) {
//
//                            mAdapterTopic.setClickedPosition(i);
//                            mAdapterTopic.notifyDataSetChanged();
//
//                            Log.d("Zig", "\n-----------> Yeeeeeeeeees!");
//                        }
//                        Log.d("Zig", "\n-----------> Topic name = " +
//                                topicsFromDB.get(i).getTopicText() +
//                                "\n---------> TabNext name = " +
//                                fragmentSlidingTabsRecycler.getNextTabName() +
//                                "\n");
//
//                    }
//                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    if (Objects.equals(fragmentSlidingTabsRecycler.getSelectTabName(), "Logic")) {
//                    mAdapterTopic.setClickedPosition(2);
//                    mAdapterTopic.notifyDataSetChanged();}
//                }

                //mAdapterTopic.notifyDataSetChanged();

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
                                //Log.d("Zig", "press topic text = " + topic.getTopicText());
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
                                //Log.d("Zig", "press topic text = " + topic.getTopicText());
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
//
                    //
                        //set recent topic
                        afterItemClickTask = new AfterItemClickTask(getContext());
                        afterItemClickTask.execute(mFoundTopics.get(position).getTopicId());
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        // ...
                    }
                }));

//                //set ItemClick:
//                mAdapterTopic.setOnItemClickListener(new RecyclerTopicAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View v, int position) {
//
//                        //select item:
//                        mAdapterTopic.setClickedPosition(position);
//                        mAdapterTopic.notifyDataSetChanged();
//
//                        if (!topicsFromDB.isEmpty()) {
//
//                            FragmentSlidingTabsRecycler fragmentSlidingTabsRecycler =
//                                    (FragmentSlidingTabsRecycler) workContext.
//                                            getSupportFragmentManager().
//                                            findFragmentById(R.id.fragment_sliding_tabs_recycler);
//
//                            //if do not have child topics
//                            if (fragmentSlidingTabsRecycler.getCountTabs() ==
//                                    (fragmentSlidingTabsRecycler.getSelectedTabPosition() + 1)) {
//                                //get child topic
//                                Topics topic = mFoundTopics.get(position);
//                                //Log.d("Zig", "press topic text = " + topic.getTopicText());
//                                fragmentSlidingTabsRecycler.addPage(topic.getTopicId());
//
//                            } else {
//
//                                //remove child topics
//                                while (fragmentSlidingTabsRecycler.getCountTabs() !=
//                                        (fragmentSlidingTabsRecycler.getSelectedTabPosition() + 1)) {
//
//                                    fragmentSlidingTabsRecycler.
//                                            removePage(fragmentSlidingTabsRecycler.
//                                                    getSelectedTabPosition() + 1);
//                                }
//                                //get child topic
//                                Topics topic = mFoundTopics.get(position);
//                                //Log.d("Zig", "press topic text = " + topic.getTopicText());
//                                fragmentSlidingTabsRecycler.addPage(topic.getTopicId());
//                            }
//                        }
////                     else {
////
////                        Expressions exp = mFoundExp.get(position);
////
////                        ClipboardManager clipboard = (ClipboardManager) getActivity().
////                                getSystemService(Context.CLIPBOARD_SERVICE);
////                        ClipData clip = ClipData.newPlainText(exp.getExpText(), exp.getExpText());
////                        clipboard.setPrimaryClip(clip);
////
////                    }
//                    }
//
//
//                });

            } else {

                //setup adapter expressions
//                ExpAdapterRecycler = new CustomListViewExpAdapter(getActivity(),
//                        R.layout.adapter_exp_item,
//                        expFromDB);
//                listTopicContentRecycler.setAdapter(ExpAdapterRecycler);
//                ExpAdapterRecycler.notifyDataSetChanged();
//                mLayoutManager = new LinearLayoutManager(getContext());
//                listTopicContentRecycler.setLayoutManager(mLayoutManager);
                // создаем адаптер
                mAdapterExp = new RecyclerExpAdapter(expFromDB);
                listTopicContentRecycler.setAdapter(mAdapterExp);
                mAdapterExp.notifyDataSetChanged();
            }
            // Log.d("Zig", "showListView() end, mQuerySearch = " + getQuerySearch());
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

//        Log.d("Zig", "showSearchResult() end,"
//                + " mQuerySearch = "
//                + getQuerySearch());
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

            int topicId = params[0];
            int currentWeight = 0;
            int setWeight = 0;
            RecentTopics currentRecentTopic;
            int maxTotalRecent = 20;

            InitalDatabaseHandler dba = new InitalDatabaseHandler(mContext);
            DatabaseHandler dba_data = new DatabaseHandler(mContext);

            ArrayList<RecentTopics> recentList = dba.getRecentTopicsList();

            //if the topic is on the recent
            if (dba.getRecentCountByIdTopic(topicId) > 0) {

                currentRecentTopic = dba.getRecentTopicByTopicId(topicId);
                currentWeight = currentRecentTopic.getTopicWeight();
                setWeight = dba.getRecentMaxWeight();

                for(int i = 0; i < recentList.size(); i++) {

                    if (recentList.get(i).getTopicWeight() > currentWeight) {

                        //recentList.get(i).downTopicWeight();
                        dba.updateTopicWeightByIdTopic(recentList.get(i).getTopicId(),
                                (recentList.get(i).getTopicWeight() - 1));

                    } else if(recentList.get(i).getTopicWeight() == currentWeight) {

                        //recentList.get(i).setTopicWeight(setWeight);
                        dba.updateTopicWeightByIdTopic(recentList.get(i).getTopicId(), setWeight);
                    }
                }
            } else { //if the topic isn't on the recent


                int totalRecent = dba.getTotalRecentTopics();
                //if it is first recent
                if (totalRecent == 0) {

                    currentRecentTopic = new RecentTopics(
                            dba_data.getTopicById(topicId).getTopicText(),
                            topicId,
                            1);
                    dba.addRecentTopic(currentRecentTopic);

                } else if (totalRecent < maxTotalRecent) {

                    setWeight = dba.getRecentMaxWeight() + 1;

                    currentRecentTopic = new RecentTopics(
                            dba_data.getTopicById(topicId).getTopicText(),
                            topicId,
                            setWeight);
                    dba.addRecentTopic(currentRecentTopic);
                } else {

                    for(int i = 0; i < recentList.size(); i++) {

                        dba.updateTopicWeightByIdTopic(recentList.get(i).getTopicId(),
                                (recentList.get(i).getTopicWeight() - 1));
                    }

                    dba.deleteLastRecentTopic();

                    setWeight = maxTotalRecent;

                    currentRecentTopic = new RecentTopics(
                            dba_data.getTopicById(topicId).getTopicText(),
                            topicId,
                            setWeight);
                    dba.addRecentTopic(currentRecentTopic);
                }
            }


            dba.close();
            dba_data.close();

            return null;
        }
    }
}
