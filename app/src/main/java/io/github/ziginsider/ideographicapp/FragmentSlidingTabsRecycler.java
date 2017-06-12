package io.github.ziginsider.ideographicapp;

/**
 * Created by zigin on 26.10.2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import data.AsyncProvider;
import data.Constants;
import data.DatabaseHandler;
import data.PersistantStorage;
import data.ViewPagerAdapter;
import model.Topics;

public class FragmentSlidingTabsRecycler extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    com.melnykov.fab.FloatingActionButton fab;
    com.melnykov.fab.FloatingActionButton fabAdd;

    TextView textTopicNameBottomSheet;
    TextView textParentTopicNameBottomSheet;
    TextView textNumberOfSubtopics;
    TextView textNumberOfExp;
    TextView textLabels;

    ArrayList<String> listTopicLabels;

    private DatabaseHandler dba;

    private PersistantStorage storage;

    private int selectedTabPosition;

    private AfterNewCardClickTask afterNewCardClickTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sliding_tabs_recycler, container, false);
        getIDs(view);

        storage = new PersistantStorage();

        setEvents();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dba = new DatabaseHandler(context);
    }

    private void getIDs(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.work_view_pager_recycler);
        tabLayout = (TabLayout) view.findViewById(R.id.work_tab_layout_recycler);
        adapter = new ViewPagerAdapter(getFragmentManager(), getActivity(), viewPager, tabLayout);
        viewPager.setAdapter(adapter);
    }

    public int getSelectedTabPosition() {
        return selectedTabPosition;
    }

    public int getCountTabs() {
        return adapter.getCount();
    }

    public String getSelectTabName() {

        return adapter.getTitle(selectedTabPosition);
    }

    public String getNextTabName() {

        return adapter.getTitle(selectedTabPosition + 1);
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

                final FragmentWorkRecycler fragmentWorkRecycler = (FragmentWorkRecycler) adapter.
                        getItem(selectedTabPosition);

                fragmentWorkRecycler.showHideView();
                //fragmentWorkRecycler.showListView();

                //set Search
                final MaterialSearchView searchView = (MaterialSearchView) getActivity().
                        findViewById(R.id.search_view_recycler);

                searchView.closeSearch();

                fragmentWorkRecycler.cloneItems();

                searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {

                    @Override
                    public void onSearchViewShown() {
                    }

                    @Override
                    public void onSearchViewClosed() {

                        //fragmentWorkRecycler.showListView();
                        //fragmentWorkRecycler.cloneItems();
                    }
                });

                //searchView.get
                searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        fragmentWorkRecycler.showSearchResultTopic(query);
                        return  true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        fragmentWorkRecycler.showSearchResult(newText);
                        return  true;
                    }
                });

                //bottomsheet set
                RelativeLayout rlBottomSheet = (RelativeLayout) getActivity().
                        findViewById(R.id.bottom_sheet);

                final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(rlBottomSheet);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                fab = (com.melnykov.fab.FloatingActionButton) getActivity().findViewById(R.id.fab_recycler);

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

                fabAdd = (com.melnykov.fab.FloatingActionButton) getActivity().findViewById(R.id.fab_add_desk);

                fabAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //set new card
                        afterNewCardClickTask = new FragmentSlidingTabsRecycler.AfterNewCardClickTask(view.getContext());
                        afterNewCardClickTask.execute();

                        Intent i = new Intent(view.getContext(), WorkActivityRecycler_.class);
                        ArrayList<Integer> startTopicsList = new ArrayList<>();
                        startTopicsList.add(0); //set topics root = "Topics"
                        i.putExtra(Constants.EXTRA_TOPICS_OPEN_TABS, startTopicsList);
                        Activity activity = (Activity)view.getContext();
                        activity.startActivity(i);
                        activity.finish();
                    }
                });

                bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (newState == BottomSheetBehavior.STATE_DRAGGING) {

                            fragmentWorkRecycler.showHideView();

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
                    }
                });


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
            }
        });
    }

    public void InflateBottomSheet() {

        FragmentWorkRecycler fragmentWorkRecycler = (FragmentWorkRecycler) adapter.
                getItem(selectedTabPosition);

        int idTopic = fragmentWorkRecycler.getmParentTopicId();

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
        FragmentWorkRecycler fragmentWorkRecycler = new FragmentWorkRecycler();
        fragmentWorkRecycler.setArguments(bundle);

        if (idTopic == 0) {

            storage.init(getContext());
            storage.addProperty(Constants.TOPICS_ROOT_NAME, "nichts");

            adapter.addFragment(fragmentWorkRecycler, Constants.TOPICS_ROOT_NAME, 0);
        } else {

            storage.init(getContext());
            Topics topic = dba.getTopicById(idTopic);
            String nameParentTopic;

            if (topic.getTopicParentId() == 0) {
                nameParentTopic = Constants.TOPICS_ROOT_NAME;
            } else {

                nameParentTopic = dba.getTopicById(topic.getTopicParentId()).getTopicText();
            }

            if (!(nameParentTopic.equals(topic.getTopicText()))) {
                storage.addProperty(topic.getTopicText(), "nichts");
            }
            adapter.addFragment(fragmentWorkRecycler, topic.getTopicText(), idTopic);
        }
        adapter.notifyDataSetChanged();
        if (adapter.getCount() > 0) tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(adapter.getCount() - 1);
        setupTabLayout();

        fragmentWorkRecycler.showHideView();
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

    class AfterNewCardClickTask extends AsyncTask<Integer, Void, Void> {

        private Context mContext;

        public AfterNewCardClickTask(Context context) {
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
            asyncProvider.setNewCard(mContext);
            return null;
        }
    }
}

