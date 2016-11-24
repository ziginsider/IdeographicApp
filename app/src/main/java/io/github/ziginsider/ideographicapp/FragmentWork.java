package io.github.ziginsider.ideographicapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import data.Constants;
import data.CustomListViewExpAdapter;
import data.CustomListViewTopicAdapter;
import data.DatabaseHandler;
import data.PersistantStorage;
import model.Expressions;
import model.Topics;

/**
 * Created by zigin on 29.09.2016.
 */

public class FragmentWork extends Fragment {

    private int mParentTopicId;
    //TextView textFooterTopicContent;
    ListView listTopicContent;
    //TextView itemCount;
    //TextView topicLabels;
    //TextView textItemCount;
    //TextView textTopicNameBottomSheet;
    //LinearLayout layoutLabels;
    //RelativeLayout footerTopicContent;
    //RelativeLayout infoTopic;
    //Toolbar toolbar;
    //AppBarLayout appbar;
    AppBarLayout tabbar;
    ViewPager viewPager;
    com.melnykov.fab.FloatingActionButton fab;

    private CustomListViewTopicAdapter topicAdapter;
    private CustomListViewExpAdapter ExpAdapter;

    private DatabaseHandler dba;

    public ArrayList<Topics> topicsFromDB;
    private ArrayList<Topics> mFoundTopics;
    //public int topicsCount;
    public ArrayList<Expressions> expFromDB;
    private ArrayList<Expressions> mFoundExp;
    //public int expCount;

    //ArrayList<String> listTopicLabels;

    //private String mQuerySearch;
    //private boolean mStateSearch;

    private FragmentActivity workContext;
    //private int mSelectItem;
    //private boolean mFlagSelect;
    private int mLastFirstVisibleItem;
    private PersistantStorage storage;

    //private int mSelectItemPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_topic_content, container, false);

        Bundle bundle = getArguments();
        mParentTopicId = bundle.getInt(Constants.BUNDLE_ID_TOPIC);

        listTopicContent = (ListView) v.findViewById(R.id.list_topic_content);
        //textFooterTopicContent = (TextView) v.findViewById(R.id.text_footer_topic_content);
        //itemCount = (TextView) v.findViewById(R.id.item_count);
        //topicLabels = (TextView) v.findViewById(R.id.topic_labels);
        //textItemCount = (TextView) v.findViewById(R.id.text_item_count);
        //layoutLabels = (LinearLayout) v.findViewById(R.id.layout_labels);
        //footerTopicContent = (RelativeLayout) v.findViewById(R.id.footer_topic_content);
        //infoTopic = (RelativeLayout) v.findViewById(R.id.info_topic);
        //textTopicNameBottomSheet = (TextView) v.findViewById(R.id.txt_bs_topic_name);
        //toolbar = (Toolbar) v.findViewById(R.id.toolbar_work);

        //listTopicLabels = new ArrayList<String>();
        //mSelectItem = -ic_intro_1;
        //mFlagSelect = false;

        storage = new PersistantStorage();

        refreshData();

        return v;
    }

    @Override
    public void onAttach(Context context) {
        workContext = (FragmentActivity) context;
        super.onAttach(context);
        dba = new DatabaseHandler(context);
        //workContext.getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public int getmParentTopicId() {
        return mParentTopicId;
    }

//    public int getSelectItemPosition() {
//        return mSelectItemPosition;
//    }

    private void refreshData() {

        Log.d("Zig", "begin function refreshData()");

        topicsFromDB = new ArrayList<Topics>();
        expFromDB = new ArrayList<Expressions>();
        //get child-topics
        topicsFromDB = dba.getTopicByIdParentAlphabet(mParentTopicId);
        //topicsCount = topicsFromDB.size();

        //get child-expressions
        expFromDB = dba.getExpByIdParent(mParentTopicId);
        //expCount = expFromDB.size();

        //clone fromDB -> foundItems
        cloneItems();

//        if (mParentTopicId == 0) {
//
//            textFooterTopicContent.setText(Constants.TOPICS_ROOT_NAME);
//
//        } else {
//
//            textFooterTopicContent.setText(dba_inital.getTopicById(mParentTopicId).getTopicText());
//        }

        listTopicContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("Zig", "listTopicContentRecycler.setOnItemClickListener begin");

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


                    FragmentSlidingTabs fragmentSlidingTabs = (FragmentSlidingTabs)
                            workContext.getSupportFragmentManager().findFragmentById(R.id.fragment_sliding_tabs);

//                    Toast.makeText(workContext, "Numbers of tabs = " +
//                            String.valueOf(fragmentSlidingTabs.getCountTabs()) +
//                            "\nPosition current tabs = " +
//                            String.valueOf(fragmentSlidingTabs.getSelectedTabPosition() + ic_intro_1), Toast.LENGTH_LONG).show();

                    //if do not have child topics
                    if (fragmentSlidingTabs.getCountTabs() == (fragmentSlidingTabs.getSelectedTabPosition() + 1) ) {
                        //get child topic
                        Topics topic = mFoundTopics.get(position);
                        //Log.d("Zig", "press topic text = " + topic.getTopicText());
                        fragmentSlidingTabs.addPage(topic.getTopicId());

                    } else {

                        //remove child topics
                        while (fragmentSlidingTabs.getCountTabs() != (fragmentSlidingTabs.getSelectedTabPosition() + 1)) {

                            fragmentSlidingTabs.removePage(fragmentSlidingTabs.getSelectedTabPosition() + 1);
                        }
                        //get child topic
                        Topics topic = mFoundTopics.get(position);
                        //Log.d("Zig", "press topic text = " + topic.getTopicText());
                        fragmentSlidingTabs.addPage(topic.getTopicId());
                    }
                } else {

                    Expressions exp = mFoundExp.get(position);
                    //Toast.makeText(getActivity(), "PRESS: " + exp.getExpText(), Toast.LENGTH_SHORT).show();

                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(exp.getExpText(), exp.getExpText());
                    clipboard.setPrimaryClip(clip);

                }

//                Log.d("Zig", "listTopicContentRecycler.setOnItemClickListener end");

            }


        });

        listTopicContent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getActivity(), "Long click", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        fab = (com.melnykov.fab.FloatingActionButton) getActivity().findViewById(R.id.fab);

//        //set search
//        final MaterialSearchView searchView = (MaterialSearchView) getActivity().findViewById(R.id.search_view);
//
//        //searchView.setQuery(getQuerySearch(), false);
//
//        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
//
//            @Override
//            public void onSearchViewShown() {
//                //setStateSearch(true);
//            }
//
//            @Override
//            public void onSearchViewClosed() {
//
//                Log.d("Zig","onSearchViewClosed in FragmentWork"
//                        + ", for topic parent = "
//                        + textFooterTopicContent
//                        + ", mQuerySearch = "
//                        + getQuerySearch());
//
//                showListView();
//                cloneItems();
//                setQuerySearch("");
//
//                Log.d("Zig","onSearchViewClosed in FragmentWork after showListView(), cloneItems(), setQerySearch(\"\")"
//                        + ", for topic parent = "
//                        + textFooterTopicContent
//                        + ", mQuerySearch = "
//                        + getQuerySearch());
//            }
//        });
//
//        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                //showSearchResult(query);
//                //setQuerySearch(newText);9
//                return  false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                showSearchResult(newText);
//                setQuerySearch(newText);
//
//                Log.d("Zig","onQueryTextChange in FragmentWork, newText = "
//                + newText
//                + ", for topic parent = "
//                + textFooterTopicContent
//                + ", mQuerySearch = "
//                + getQuerySearch());
//
//                return  true;
//            }
//        });


        //set back (go to previous topic)
//        footerTopicContent.setOnClickListener( new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
////                Log.d("Zig", "textFooterTopicContent.setOnClickListener ");
//
//                if (mParentTopicId == 0) {
//
//                    Toast.makeText(getActivity(), "<-- Back", Toast.LENGTH_SHORT).show();
//
//                } else {
//
//                    FragmentSlidingTabs fragmentSlidingTabs = (FragmentSlidingTabs)
//                            workContext.getSupportFragmentManager().findFragmentById(R.id.fragment_sliding_tabs);
//
//                    fragmentSlidingTabs.removePage(fragmentSlidingTabs.getSelectedTabPosition());
//
//                }
//
//            }
//        });


        showListView();
        showHideView();

        //show and hide toolbar from scroll
        mLastFirstVisibleItem = listTopicContent.getFirstVisiblePosition();
        //mLastFirstVisibleItem = 0;

        tabbar = (AppBarLayout) getActivity().findViewById(R.id.appbar_layout);
        viewPager = (ViewPager) getActivity().findViewById(R.id.work_view_pager);

        listTopicContent.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (view.getId() == listTopicContent.getId()) {
                    final int currentFirstVisibleItem = listTopicContent.getFirstVisiblePosition();

                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {

                        tabbar.animate().translationY(-tabbar.getBottom()).
                                setInterpolator(new AccelerateInterpolator()).start();

                        viewPager.animate().translationY(-(tabbar.getBottom())).
                                setInterpolator(new AccelerateInterpolator()).start();

                        fab.animate().translationY(fab.getBottom()).
                                setInterpolator(new AccelerateInterpolator(2)).start();

                        fab.setTag("hide");

                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {

                        tabbar.animate().translationY(0).
                                setInterpolator(new DecelerateInterpolator()).start();

                        viewPager.animate().translationY(0).
                                setInterpolator(new DecelerateInterpolator()).start();

                        fab.animate().translationY(0).
                                setInterpolator(new DecelerateInterpolator()).start();
//                        ResizeAnimation resizeAnimation = new ResizeAnimation(tabbar,
//                                tabbar.getWidth(),
//                                tabbarHeight);
//                        resizeAnimation.setInterpolator(new AccelerateInterpolator());
//                        tabbar.startAnimation(resizeAnimation);
                        fab.setTag("show");
                    }

                    mLastFirstVisibleItem = currentFirstVisibleItem;
                }
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

                tabbar = (AppBarLayout) getActivity().findViewById(R.id.appbar_layout);
                viewPager = (ViewPager) getActivity().findViewById(R.id.work_view_pager);

                tabbar.animate().translationY(0).
                        setInterpolator(new DecelerateInterpolator()).start();
                viewPager.animate().translationY(0).
                        setInterpolator(new DecelerateInterpolator()).start();
                fab.animate().translationY(0).
                        setInterpolator(new DecelerateInterpolator()).start();

                fab.setTag("show");

                //Toast.makeText(getActivity(), "I'am working", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showListView() {

       // Log.d("Zig", "showListView() begin, mQuerySearch = " + getQuerySearch());

        if (topicsFromDB != null) {
            if (!topicsFromDB.isEmpty()) {




                //setup adapter topics
                topicAdapter = new CustomListViewTopicAdapter(getActivity(),
                        R.layout.adapter_topic_item,
                        topicsFromDB); //send id topics current tabs
//                if (mFlagSelect) {
//                    topicAdapter.setmSelectItem(mSelectItem);
//                    topicAdapter.setmFlagSelect(true);
//                    mFlagSelect = false;
//                }

                listTopicContent.setAdapter(topicAdapter);
                topicAdapter.notifyDataSetChanged();

            } else {

                //setup adapter expressions
                ExpAdapter = new CustomListViewExpAdapter(getActivity(),
                        R.layout.adapter_exp_item,
                        expFromDB);
                listTopicContent.setAdapter(ExpAdapter);
                ExpAdapter.notifyDataSetChanged();
            }

           // Log.d("Zig", "showListView() end, mQuerySearch = " + getQuerySearch());
        }
    }

    public void showSearchResult(String searchText) {

//        Log.d("Zig", "showSearchResult() begin,"
//                + " searchText = "
//                + searchText
//                + " mQuerySearch = "
//                + getQuerySearch());

        if (searchText != null && !searchText.isEmpty() && topicsFromDB != null) {

            //setQuerySearch(searchText);
            //setStateSearch(true);

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
//                topicAdapter = new CustomListViewTopicAdapter(getActivity(),
//                        R.layout.adapter_topic_item,
//                        mFoundTopics); //send id topics current tabs
//                listTopicContentRecycler.setAdapter(topicAdapter);
//                topicAdapter.notifyDataSetChanged();
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


                for(Expressions item:expFromDB) {
                    //not case sensitive
                    if (item.getExpText().toLowerCase().contains(searchText.toLowerCase())) {

                        mFoundExp.add(item);
                    }
                }

                ExpAdapter = new CustomListViewExpAdapter(getActivity(),
                        R.layout.adapter_exp_item,
                        mFoundExp);
                listTopicContent.setAdapter(ExpAdapter);
                ExpAdapter.notifyDataSetChanged();

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

    public void showSearchResultTopic(String searchText)
    {
        if (searchText != null && !searchText.isEmpty()) {

            if (!topicsFromDB.isEmpty()) {

                Toast.makeText(getActivity(), "TODO result of subtopic search: " + searchText, Toast.LENGTH_SHORT).show();

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

        if (topicsFromDB != null)  {
            mFoundTopics = (ArrayList<Topics>) topicsFromDB.clone();
        }
        if (expFromDB != null) {
            mFoundExp = (ArrayList<Expressions>) expFromDB.clone();
        }
    }



}