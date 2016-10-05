package io.github.ziginsider.ideographicapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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

    private CustomListViewTopicAdapter topicAdapter;
    private CustomListViewExpAdapter ExpAdapter;

    private DatabaseHandler dba;

    public ArrayList<Topics> topicsFromDB;
    private ArrayList<Topics> mFoundTopics;
    public int topicsCount;
    public ArrayList<Expressions> expFromDB;
    public int expCount;

    private FragmentActivity workContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_topic_content, container, false);

        Bundle bundle = getArguments();
        parentTopicId = bundle.getInt(Constants.BUNDLE_ID_TOPIC);

        listTopicContent = (ListView) v.findViewById(R.id.list_topic_content);
        textFooterTopicContent = (TextView) v.findViewById(R.id.text_footer_topic_content);



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
        refreshData();
    }

    private void refreshData() {

        if (parentTopicId == 0) {

            textFooterTopicContent.setText(Constants.TOPICS_ROOT_NAME);

        } else {

            textFooterTopicContent.setText(dba.getTopicById(parentTopicId).getTopicText());
        }

        //TODO refactor minimize topicFromDB and mFoundTopic
        //get child-topics
        topicsFromDB = dba.getTopicByIdParent(parentTopicId);
        topicsCount = topicsFromDB.size();
        mFoundTopics = (ArrayList<Topics>) topicsFromDB.clone();
        //get child-expressions
        expFromDB = dba.getExpByIdParent(parentTopicId);
        expCount = expFromDB.size();


        listTopicContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (topicsCount > 0) {

                    Topics topic = mFoundTopics.get(position);
                    //String text = topic.getTopicText();


                    FragmentSlidingTabs fragmentSlidingTabs = (FragmentSlidingTabs)
                            workContext.getSupportFragmentManager().findFragmentById(R.id.fragment_sliding_tabs);

                    fragmentSlidingTabs.addPage(topic.getTopicId());

                } else {

                    Expressions exp = expFromDB.get(position);
                    Toast.makeText(getActivity(), "PRESS: " + exp.getExpText(), Toast.LENGTH_SHORT).show();

                }


            }
        });

        //set fab
        com.melnykov.fab.FloatingActionButton fab = (com.melnykov.fab.FloatingActionButton)
                workContext.findViewById(R.id.fab);

        fab.attachToListView(listTopicContent);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fabLauncher();
            }
        });

        //set search
        MaterialSearchView searchView = (MaterialSearchView) getActivity().findViewById(R.id.search_view);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {

            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                showListView();
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                showSearchResult(newText);
                return  true;
            }
        });

        //searchView.setVisibility(View.INVISIBLE);
        //searchView.act


        //set back (go to previous topic)
        textFooterTopicContent.setOnClickListener( new View.OnClickListener() {
            
            @Override
            public void onClick(View view) {

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

    }

    @Override
    public void onDetach() {
        super.onDetach();
        dba.close();
    }

    public void showListView() {

        if (topicsCount > 0) {

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


    }

    public void showSearchResult(String searchText) {

        if (searchText != null && !searchText.isEmpty()) {

            //List<String> lstFound = new ArrayList<String>();

            if (topicsCount > 0) {

                //show topics search result
                mFoundTopics.clear();

                //FragmentWork fragmentWork = (FragmentWork) getActivity().getSupportFragmentManager().

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


            } else {

                //show expressions search result

            }




        } else {

//                    ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, lstSource);
//                    lstView.setAdapter(adapter);
            showListView();
        }

    }

    public void fabLauncher() {

        if (topicsCount > 0) {

            Toast.makeText(getActivity(), "I'm a black cat! You have " +
                    mFoundTopics.size() + " topics.", Toast.LENGTH_SHORT).show();

        } else {


            Toast.makeText(getActivity(), "I'm a black cat! You have " +
                    expFromDB.size() + " expessions.", Toast.LENGTH_SHORT).show();

        }
    }



}