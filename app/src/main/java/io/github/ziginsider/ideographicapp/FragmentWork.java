package io.github.ziginsider.ideographicapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import data.Constants;
import data.CustomListViewExpAdapter;
import data.CustomListViewTopicAdapter;
import data.DatabaseHandler;
import model.Expressions;
import model.Topics;

/**
 * Created by zigin on 29.09.2016.
 */

public class FragmentWork extends Fragment {

    int parentTopicId;
    TextView textFooterTopicContent;
    ListView listTopicContent;
    TextView itemCount;
    TextView topicLabels;
    TextView textItemCount;
    LinearLayout layoutLabels;

    private CustomListViewTopicAdapter topicAdapter;
    private CustomListViewExpAdapter ExpAdapter;

    private DatabaseHandler dba;

    public ArrayList<Topics> topicsFromDB;
    private ArrayList<Topics> mFoundTopics;
    public int topicsCount;
    public ArrayList<Expressions> expFromDB;
    private ArrayList<Expressions> mFoundExp;
    public int expCount;

    ArrayList<String> listTopicLabels;

    //private String mQuerySearch;
    //private boolean mStateSearch;

    private FragmentActivity workContext;
    private int mSelectItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_topic_content, container, false);

        Bundle bundle = getArguments();
        parentTopicId = bundle.getInt(Constants.BUNDLE_ID_TOPIC);

        listTopicContent = (ListView) v.findViewById(R.id.list_topic_content);
        textFooterTopicContent = (TextView) v.findViewById(R.id.text_footer_topic_content);
        itemCount = (TextView) v.findViewById(R.id.item_count);
        topicLabels = (TextView) v.findViewById(R.id.topic_labels);
        textItemCount = (TextView) v.findViewById(R.id.text_item_count);
        layoutLabels = (LinearLayout) v.findViewById(R.id.layout_labels);

        listTopicLabels = new ArrayList<String>();

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
//
//    public String getQuerySearch() {
//        return mQuerySearch;
//    }
//
//    public void setQuerySearch(String mQuerySearch) {
//        this.mQuerySearch = mQuerySearch;
//    }
//
//    public boolean isStateSearch() {
//        return mStateSearch;
//    }
//
//    public void setStateSearch(boolean mStateSearch) {
//        this.mStateSearch = mStateSearch;
//    }


    public int getmSelectItem() {
        return mSelectItem;
    }

    public void setmSelectItem(int mSelectItem) {
        this.mSelectItem = mSelectItem;
    }

    private void refreshData() {

        Log.d("Zig", "begin function refreshData()");

        //get child-topics
        topicsFromDB = dba.getTopicByIdParentAlphabet(parentTopicId);
        topicsCount = topicsFromDB.size();

        //get child-expressions
        expFromDB = dba.getExpByIdParent(parentTopicId);
        expCount = expFromDB.size();

        //clone fromDB -> foundItems
        cloneItems();

        if (parentTopicId == 0) {

            textFooterTopicContent.setText(Constants.TOPICS_ROOT_NAME);

            itemCount.setText(String.valueOf(mFoundTopics.size()));

            layoutLabels.setVisibility(View.GONE);

        } else {

            textFooterTopicContent.setText(dba.getTopicById(parentTopicId).getTopicText());

            if (!topicsFromDB.isEmpty()) {

                layoutLabels.setVisibility(View.VISIBLE);
                textItemCount.setText("Number of subtopics:");
                //itemCount.setText(String.valueOf(mFoundTopics.size()));

                listTopicLabels = dba.getTopicLabels(parentTopicId);

                StringBuilder sb = new StringBuilder();
                for (String s : listTopicLabels)
                {
                    sb.append(s);
                    sb.append(" ");
                    sb.append("\t");

                }

                topicLabels.setText(sb);

            } else {

                layoutLabels.setVisibility(View.GONE);
                textItemCount.setText("Number of expressions:");
                itemCount.setText(String.valueOf(mFoundExp.size()));
            }

        }

        listTopicContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("Zig", "listTopicContent.setOnItemClickListener begin");


                if (!topicsFromDB.isEmpty()) {

                    Topics topic = mFoundTopics.get(position);
                    //String text = topic.getTopicText();

                    mSelectItem = position;
                    topicAdapter.setmSelectItem(mSelectItem);
                    topicAdapter.notifyDataSetChanged();
                    

                    Log.d("Zig", "press topic text = " + topic.getTopicText());


                    FragmentSlidingTabs fragmentSlidingTabs = (FragmentSlidingTabs)
                            workContext.getSupportFragmentManager().findFragmentById(R.id.fragment_sliding_tabs);

                    fragmentSlidingTabs.addPage(topic.getTopicId());

                } else {

                    Expressions exp = mFoundExp.get(position);
                    //Toast.makeText(getActivity(), "PRESS: " + exp.getExpText(), Toast.LENGTH_SHORT).show();

                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(exp.getExpText(), exp.getExpText());
                    clipboard.setPrimaryClip(clip);

                }

//                Log.d("Zig", "listTopicContent.setOnItemClickListener end");

            }
        });

        //set fab
        com.melnykov.fab.FloatingActionButton fab = (com.melnykov.fab.FloatingActionButton)
                workContext.findViewById(R.id.fab);

        fab.attachToListView(listTopicContent);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Log.d("Zig", "fab.setOnClickListener in FragmentWork");

                fabLauncher();
            }
        });

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

        //searchView.setVisibility(View.INVISIBLE);
        //searchView.act


        //set back (go to previous topic)
        textFooterTopicContent.setOnClickListener( new View.OnClickListener() {
            
            @Override
            public void onClick(View view) {

//                Log.d("Zig", "textFooterTopicContent.setOnClickListener ");

                if (parentTopicId == 0) {

                    Toast.makeText(getActivity(), "<-- Back", Toast.LENGTH_SHORT).show();

                } else {

                    FragmentSlidingTabs fragmentSlidingTabs = (FragmentSlidingTabs)
                            workContext.getSupportFragmentManager().findFragmentById(R.id.fragment_sliding_tabs);

                    fragmentSlidingTabs.removePage(fragmentSlidingTabs.getSelectedTabPosition());

                }

            }
        });


        showListView();

        Log.d("Zig", "End function RefreshData()");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        dba.close();
    }

    public void showListView() {

       // Log.d("Zig", "showListView() begin, mQuerySearch = " + getQuerySearch());

        if (!topicsFromDB.isEmpty()) {

            //setup adapter topics
            topicAdapter = new CustomListViewTopicAdapter(getActivity(),
                    R.layout.adapter_topic_item,
                    topicsFromDB); //send id topics current tabs
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

    public void showSearchResult(String searchText) {

//        Log.d("Zig", "showSearchResult() begin,"
//                + " searchText = "
//                + searchText
//                + " mQuerySearch = "
//                + getQuerySearch());

        if (searchText != null && !searchText.isEmpty()) {

            //setQuerySearch(searchText);
            //setStateSearch(true);

            if (!topicsFromDB.isEmpty()) {

//                Log.d("Zig", "In showSearchResult()"
//                        + " topicsFromDB isn't empty ");

                if (mFoundTopics != null) {

                    mFoundTopics.clear();
                }


                for(Topics item:topicsFromDB) {

                    if (item.getTopicText().contains(searchText)) {

                        mFoundTopics.add(item);
                    }
                }

                topicAdapter = new CustomListViewTopicAdapter(getActivity(),
                        R.layout.adapter_topic_item,
                        mFoundTopics); //send id topics current tabs
                listTopicContent.setAdapter(topicAdapter);
                topicAdapter.notifyDataSetChanged();

                //textItemCount.setText("Number of subtopics");
                //itemCount.setText(String.valueOf(mFoundTopics.size()));



            } else {
//                Log.d("Zig", "In showSearchResult()"
//                        + " topicsFromDB is empty ");

                //show expressions search result
                if (mFoundTopics != null) {

                    mFoundExp.clear();
                }


                for(Expressions item:expFromDB) {

                    if (item.getExpText().contains(searchText)) {

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

    public void fabLauncher() {

        if (!topicsFromDB.isEmpty()) {

            Toast.makeText(getActivity(), "I'm a black cat! You have " +
                    mFoundTopics.size() + " topics.", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(getActivity(), "I'm a black cat! You have " +
                    mFoundExp.size() + " expessions.", Toast.LENGTH_SHORT).show();

        }
    }

    public void cloneItems() {

        if (topicsFromDB != null)  {
            mFoundTopics = (ArrayList<Topics>) topicsFromDB.clone();
        }
        if (expFromDB != null) {
            mFoundExp = (ArrayList<Expressions>) expFromDB.clone();
        }
    }



}