package io.github.ziginsider.ideographicapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import data.Constants;
import data.DatabaseHandler;
import data.ViewPagerAdapter;
import model.Topics;

/**
 * Created by zigin on 29.09.2016.
 */

public class FragmentSlidingTabs extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private DatabaseHandler dba;

//    private ArrayList<String> mQuerySearch;
//    private ArrayList<Boolean> mStateSearch;

    private int selectedTabPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sliding_tabs, container, false);
        getIDs(view);
        setEvents();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dba = new DatabaseHandler(context);
    }

    private void getIDs(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.work_view_pager);
        tabLayout = (TabLayout) view.findViewById(R.id.work_tab_layout);
        adapter = new ViewPagerAdapter(getFragmentManager(), getActivity(), viewPager, tabLayout);
        viewPager.setAdapter(adapter);
//        mQuerySearch = new ArrayList<String>();
//        mStateSearch = new ArrayList<Boolean>();
        Log.d("Zig", "\nonCreateView in FragmentSlidingTabs");
    }

    public int getSelectedTabPosition() {
        return selectedTabPosition;
    }

    private void setEvents() {

        //setTabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);

                viewPager.setCurrentItem(tab.getPosition());
                selectedTabPosition = viewPager.getCurrentItem();
                Log.d("Zig", "Selected tab, position = " + tab.getPosition());

                final FragmentWork fragmentWork = (FragmentWork) adapter.getItem(selectedTabPosition);

//                Log.d("Zig", "Selected tab, fragmentWork.getQuerySearch = " + fragmentWork.getQuerySearch());

                //set Search
                final MaterialSearchView searchView = (MaterialSearchView) getActivity().findViewById(R.id.search_view);

                //if (mStateSearch.get(selectedTabPosition)) {

                //    searchView.setQuery(mQuerySearch.get(selectedTabPosition), false);

                //} else {

                    searchView.closeSearch();
                //}
                fragmentWork.cloneItems();

                searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {

                    @Override
                    public void onSearchViewShown() {
                        //mStateSearch.set(selectedTabPosition, true);
                    }

                    @Override
                    public void onSearchViewClosed() {

//                        Log.d("Zig","\nonSearchViewClosed in FragmentSlidingTabs before"
//                                + ", for topic parent = "
//                                + fragmentWork.textFooterTopicContent.getText()
//                                + ", for pageTitle = "
//                                + adapter.getPageTitle(selectedTabPosition)
//                                + ", mQuerySearch = "
//                                + mQuerySearch.get(selectedTabPosition));

                        fragmentWork.showListView();
                        fragmentWork.cloneItems();
//                        mQuerySearch.set(selectedTabPosition, "");
//                        mStateSearch.set(selectedTabPosition, false);

//                        Log.d("Zig","onSearchViewClosed in FragmentSlidingTabs after showListView(), cloneItems(), setQerySearch(\"\")"
//                                + ", for topic parent = "
//                                + fragmentWork.textFooterTopicContent.getText()
//                                + ", for pageTitle = "
//                                + adapter.getPageTitle(selectedTabPosition)
//                                + ", mQuerySearch = "
//                                +mQuerySearch.get(selectedTabPosition));
                    }
                });

                //searchView.get
                searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        return  false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        fragmentWork.showSearchResult(newText);
//                        mQuerySearch.set(selectedTabPosition, newText);
//                        mStateSearch.set(selectedTabPosition, true);

//                        Log.d("Zig","\nonQueryTextChange in FragmentSlidingTabs, newText = "
//                                + newText
//                                + ", for topic parent = "
//                                + fragmentWork.textFooterTopicContent
//                                + ", for pageTitle = "
//                                + adapter.getPageTitle(selectedTabPosition)
//                                + ", mQuerySearch = "
//                                + mQuerySearch.get(selectedTabPosition));

                        return  true;
                    }
                });

                //set fab
                com.melnykov.fab.FloatingActionButton fab = (com.melnykov.fab.FloatingActionButton)
                        getActivity().findViewById(R.id.fab);

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.d("Zig", "fab.setOnClickListener in FragmentSlidingTabs");

                        fragmentWork.fabLauncher();

//                        ListView listView = (ListView) getActivity().findViewById(R.id.list_topic_content);
//
//                        Toast.makeText(getActivity(), String.valueOf(listView.getCount()), Toast.LENGTH_SHORT).show();
                    }
                });

                fab.show();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                //final MaterialSearchView searchView = (MaterialSearchView) getActivity().findViewById(R.id.search_view);
                //final FragmentWork fragmentWork = (FragmentWork) adapter.getItem(selectedTabPosition);
                //Log.d("Zig", "Unselected tab, position = " + tab.getPosition());
                //Log.d("Zig", "Unselected tabs, fragmentWork.getQuerySearch = " + fragmentWork.getQuerySearch());

            }
        });
    }

    public void addPage(int idTopic) {

        //show topic content
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_ID_TOPIC, idTopic);
        FragmentWork fragmentWork = new FragmentWork();
        fragmentWork.setArguments(bundle);

//        mQuerySearch.add("");
//        mStateSearch.add(false);
//        Log.d("Zig", "\nAdd page, mQuerySearch = " + mQuerySearch.get(viewPager.getCurrentItem()));

        if (idTopic == 0) {

            adapter.addFragment(fragmentWork, Constants.TOPICS_ROOT_NAME);

        } else {

            adapter.addFragment(fragmentWork, dba.getTopicById(idTopic).getTopicText());
        }
        adapter.notifyDataSetChanged();
        if (adapter.getCount() > 0) tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(adapter.getCount() - 1);
        setupTabLayout();
    }

    public void removePage(int position) {

//        mQuerySearch.remove(position);
//        mStateSearch.remove(position);
        //final FragmentWork fragmentWork = (FragmentWork) adapter.getItem(position);
        //Log.d("Zig", "Add page, fragmentWork.getQuerySearch = " + fragmentWork.getQuerySearch());

        adapter.removeFragment(position);
        setupTabLayout();
    }

    public void setupTabLayout() {
        selectedTabPosition = viewPager.getCurrentItem();
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setCustomView(adapter.getTabView(i));
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        dba.close();
    }
}


