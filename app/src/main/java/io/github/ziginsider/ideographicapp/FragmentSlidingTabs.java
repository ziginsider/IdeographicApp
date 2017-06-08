package io.github.ziginsider.ideographicapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import data.Constants;
import data.DatabaseHandler;
import data.PersistantStorage;
import data.ViewPagerAdapter;
import model.Topics;


/**
 * Created by zigin on 29.09.2016.
 */

public class FragmentSlidingTabs extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    com.melnykov.fab.FloatingActionButton fab;
    TextView textTopicNameBottomSheet;
    TextView textParentTopicNameBottomSheet;
    TextView textNumberOfSubtopics;
    TextView textNumberOfExp;
    TextView textLabels;

    ArrayList<String> listTopicLabels;
    //ArrayList<Integer> listSelectTopicItem;

    private DatabaseHandler dba;

    private PersistantStorage storage;

//    private ArrayList<String> mQuerySearch;
//    private ArrayList<Boolean> mStateSearch;

    private int selectedTabPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sliding_tabs, container, false);
        getIDs(view);

//        if (listSelectTopicItem == null)
//            listSelectTopicItem = new ArrayList<Integer>();

        storage = new PersistantStorage();

        setEvents();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dba = new DatabaseHandler(context);
    }

//    public int getSelectTopicItemPosition (int positionTab) {
//        return listSelectTopicItem.get(positionTab);
//    }

    private void getIDs(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.work_view_pager);
        tabLayout = (TabLayout) view.findViewById(R.id.work_tab_layout);
        adapter = new ViewPagerAdapter(getFragmentManager(), getActivity(), viewPager, tabLayout);
        viewPager.setAdapter(adapter);
//        mQuerySearch = new ArrayList<String>();
//        mStateSearch = new ArrayList<Boolean>();
        Log.d("Zig", "\nonCreateView in FragmentSlidingTabs");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            tabLayout.setElevation(0);
//        }
    }

    public int getSelectedTabPosition() {
        return selectedTabPosition;
    }

    public int getCountTabs() {
        return adapter.getCount();
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

                //////////////////////

                //listSelectTopicItem.add(selectedTabPosition, selectedTabPosition);

                //////////////////////


                final FragmentWork fragmentWork = (FragmentWork) adapter.getItem(selectedTabPosition);

                fragmentWork.showHideView();

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

                        fragmentWork.showSearchResultTopic(query);
                        return  true;
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
//                com.melnykov.fab.FloatingActionButton fab = (com.melnykov.fab.FloatingActionButton)
//                        getActivity().findViewById(R.id.fab);
//
//                fab.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Log.d("Zig", "fab.setOnClickListener in FragmentSlidingTabs");
//
//                        fragmentWork.fabLauncher();
//
////                        ListView listView = (ListView) getActivity().findViewById(R.id.list_topic_content);
////
////                        Toast.makeText(getActivity(), String.valueOf(listView.getCount()), Toast.LENGTH_SHORT).show();
//                    }
//                });

                //fab.show();


                //bottomsheet set
                RelativeLayout rlBottomSheet = (RelativeLayout) getActivity().
                        findViewById(R.id.bottom_sheet);

                final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(rlBottomSheet);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                fab = (com.melnykov.fab.FloatingActionButton) getActivity().findViewById(R.id.fab);

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {

                            InflateBottomSheet();
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }

                    }
                });

                bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                            //Toast.makeText(getActivity(), "Drag Bottomsheet!", Toast.LENGTH_SHORT).show();
                            fragmentWork.showHideView();

                            InflateBottomSheet();

                        } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                            //Toast.makeText(getActivity(), "Expanded Bottomsheet!", Toast.LENGTH_SHORT).show();
                            final OvershootInterpolator interpolator = new OvershootInterpolator();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                fab.animate().rotation(180f).withLayer().setDuration(300).setInterpolator(interpolator).start();
                            }
                        } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                            //Toast.makeText(getActivity(), "Collapsed Bottomsheet!", Toast.LENGTH_SHORT).show();
                            final OvershootInterpolator interpolator = new OvershootInterpolator();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                fab.animate().rotation(0.0f).withLayer().setDuration(300).setInterpolator(interpolator).start();
                            }
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        // React to dragging events

//                        bottomSheet.setOnTouchListener(new View.OnTouchListener() {
//                            @Override
//                            public boolean onTouch(View v, MotionEvent event) {
//                                //Toast.makeText(getActivity(), "Touch Bottomsheet!", Toast.LENGTH_SHORT).show();
//                                int action = MotionEventCompat.getActionMasked(event);
//                                switch (action) {
//                                    case MotionEvent.ACTION_UP:
//                                        //Toast.makeText(getActivity(), "Up!", Toast.LENGTH_SHORT).show();
//                                        return false;
//                                    default:
//                                        return true;
//                                }
//                            }
//                        });
                    }
                });


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                //final MaterialSearchView searchView = (MaterialSearchView) getActivity().findViewById(R.id.search_view);
//                final FragmentWork fragmentWork = (FragmentWork) adapter.getItem(selectedTabPosition);
//                fragmentWork.showHideView();
                //Log.d("Zig", "Unselected tab, position = " + tab.getPosition());
                //Log.d("Zig", "Unselected tabs, fragmentWork.getQuerySearch = " + fragmentWork.getQuerySearch());

            }
        });
    }

    public void InflateBottomSheet() {

        FragmentWork fragmentWork = (FragmentWork) adapter.getItem(selectedTabPosition);

        int idTopic = fragmentWork.getmParentTopicId();

        if (idTopic == 0) {

            textTopicNameBottomSheet = (TextView) getActivity().
                    findViewById(R.id.txt_bs_topic_name);
            textParentTopicNameBottomSheet = (TextView) getActivity().
                    findViewById(R.id.txt_bs_parent_topic_name);
            textNumberOfSubtopics = (TextView) getActivity().
                    findViewById(R.id.txt_bs_number_of_subtopics);
            textNumberOfExp = (TextView) getActivity().
                    findViewById(R.id.txt_bs_number_of_exp);
            textLabels = (TextView) getActivity().
                    findViewById(R.id.txt_bs_labels);


            textTopicNameBottomSheet.setText(Constants.TOPICS_ROOT_NAME);
            textParentTopicNameBottomSheet.setVisibility(View.INVISIBLE);
            textLabels.setText("");


            textNumberOfSubtopics.setText(String.valueOf(dba.getTopicCountByIdParent(idTopic)));
            textNumberOfExp.setText(String.valueOf(dba.getExpCountByIdParent(idTopic)));

        } else {

            textTopicNameBottomSheet = (TextView) getActivity().
                    findViewById(R.id.txt_bs_topic_name);
            textParentTopicNameBottomSheet = (TextView) getActivity().
                    findViewById(R.id.txt_bs_parent_topic_name);
            textNumberOfSubtopics = (TextView) getActivity().
                    findViewById(R.id.txt_bs_number_of_subtopics);
            textNumberOfExp = (TextView) getActivity().
                    findViewById(R.id.txt_bs_number_of_exp);
            textLabels = (TextView) getActivity().
                    findViewById(R.id.txt_bs_labels);

            textParentTopicNameBottomSheet.setVisibility(View.VISIBLE);

            //labels
            listTopicLabels = dba.getTopicLabels(idTopic);

            StringBuilder sb = new StringBuilder();
            for (String s : listTopicLabels) {

                sb.append(s);
                sb.append(" ");
                sb.append("\t");
            }
            textLabels.setText(sb);

            //parent topic
            if (dba.getTopicById(idTopic).getTopicParentId() != 0) {

                textParentTopicNameBottomSheet.setText(dba.getTopicById
                    (dba.getTopicById(idTopic).getTopicParentId())
                    .getTopicText());

            } else {

                textParentTopicNameBottomSheet.setText(Constants.TOPICS_ROOT_NAME);
            }

            textTopicNameBottomSheet.setText(dba.getTopicById(idTopic).getTopicText());
            textNumberOfSubtopics.setText(String.valueOf(dba.getTopicCountByIdParent(idTopic)));
            textNumberOfExp.setText(String.valueOf(dba.getExpCountByIdParent(idTopic)));
        }
    }

    public void addPage(int idTopic) {

        //show topic content
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_ID_TOPIC, idTopic);
        FragmentWork fragmentWork = new FragmentWork();
        fragmentWork.setArguments(bundle);

        if (idTopic == 0) {

            storage.init(getContext());
            storage.addProperty(Constants.TOPICS_ROOT_NAME, "nichts");

            adapter.addFragment(fragmentWork, Constants.TOPICS_ROOT_NAME, 0);
        } else {

            storage.init(getContext());
            storage.addProperty(dba.getTopicById(idTopic).getTopicText(), "nichts");

            adapter.addFragment(fragmentWork, dba.getTopicById(idTopic).getTopicText(), idTopic);
        }
        adapter.notifyDataSetChanged();
        if (adapter.getCount() > 0) tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(adapter.getCount() - 1);
        setupTabLayout();

        fragmentWork.showHideView();
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


