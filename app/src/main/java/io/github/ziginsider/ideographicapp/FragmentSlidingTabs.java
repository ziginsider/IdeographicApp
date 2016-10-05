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
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

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

                //set search
                MaterialSearchView searchView = (MaterialSearchView) getActivity().findViewById(R.id.search_view);
                searchView.closeSearch();

                viewPager.setCurrentItem(tab.getPosition());
                selectedTabPosition = viewPager.getCurrentItem();
                Log.d("Selected", "Selected " + tab.getPosition());

                final FragmentWork fragmentWork = (FragmentWork) adapter.getItem(selectedTabPosition);
                //set Search
                searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {

                    @Override
                    public void onSearchViewShown() {

                    }

                    @Override
                    public void onSearchViewClosed() {
                        fragmentWork.showListView();
                    }
                });

                searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        fragmentWork.showSearchResult(newText);
                        return  true;
                    }
                });

                //set fab
                com.melnykov.fab.FloatingActionButton fab = (com.melnykov.fab.FloatingActionButton)
                        getActivity().findViewById(R.id.fab);

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        fragmentWork.fabLauncher();
                    }
                });


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                Log.d("Unselected", "Unselected " + tab.getPosition());
            }
        });
    }

    public void addPage(int idTopic) {

        //show topic content
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_ID_TOPIC, idTopic);
        FragmentWork fragmentWork = new FragmentWork();
        fragmentWork.setArguments(bundle);

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


