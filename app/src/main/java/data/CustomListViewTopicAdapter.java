package data;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


import io.github.ziginsider.ideographicapp.FragmentSlidingTabs;
import io.github.ziginsider.ideographicapp.FragmentWork;
import io.github.ziginsider.ideographicapp.R;

import model.Topics;

/**
 * Created by zigin on 24.09.2016.
 */
public class CustomListViewTopicAdapter extends ArrayAdapter<Topics> {

    private int layoutResourse;
    private Activity activity;
    private ArrayList<Topics> TopicsList = new ArrayList<>();
    private int mSelectItem;


    public CustomListViewTopicAdapter(Activity act, int resource, ArrayList<Topics> data) {
        super(act, resource, data);
        layoutResourse = resource;
        activity = act;
        TopicsList = data;

        //mSelectItem = -1;

//        TextView textView = (TextView) act.findViewById(R.id.item_count);
//        textView.setText(String.valueOf(TopicsList.size()));


        notifyDataSetChanged();
    }

    public int getmSelectItem() {
        return mSelectItem;
    }

    public void setmSelectItem(int mSelectItem) {
        this.mSelectItem = mSelectItem;
    }

    @Override
    public int getCount() {
        return TopicsList.size();
    }

    @Override
    public Topics getItem(int position) {
        return TopicsList.get(position);
    }

    @Override
    public int getPosition(Topics item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;

        if (row == null || (row.getTag() == null)) {

            LayoutInflater inflater = LayoutInflater.from(activity);
            row = inflater.inflate(layoutResourse, parent, false);

            holder = new ViewHolder();

            holder.topicText = (TextView) row.findViewById(R.id.text_item_topic_content);
//            holder.topicId = (TextView) row.findViewById(R.id.topicId);
//            holder.topicIdParent = (TextView) row.findViewById(R.id.topicIdParent);
//            holder.topicLabels = (TextView) row.findViewById(R.id.topicLabels);



            row.setTag(holder);
        } else {

            holder = (ViewHolder) row.getTag();
        }

        holder.topic = getItem(position);

        holder.topicText.setText(holder.topic.getTopicText());
//        holder.topicIdParent.setText("Parent id = " + String.valueOf(holder.topic.getTopicParentId()));
//        holder.topicId.setText("Topic id = " + String.valueOf(holder.topic.getTopicId()));
//        holder.topicLabels.setText("Labels = " + holder.topic.getTopicLabels());


//        final ViewHolder finalHolder = holder;
//        row.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                int idTopic = finalHolder.topic.getTopicId();
//
//                DatabaseHandler dbHandler = new DatabaseHandler(v.getContext());
//
//                ArrayList<Topics> childTopics = dbHandler.getTopicByIdParent(idTopic);
//
//                if (childTopics.size() > 0) {
//                //if (false) {
//
//                    ArrayList<Topics> topicsTabs = new ArrayList<Topics>();
//
//                    FragmentSlidingTabs fragTabs = (FragmentSlidingTabs)
//
//                } else {
//
////                    ExpCursor expCursor = new ExpCursor();
////                    expCursor.setIdParentExp(finalHolder.topic.getTopicId());
////
////                    Intent in = new Intent (activity, ExpActivity_.class);
////
////                    Bundle mBundle = new Bundle();
////                    mBundle.putSerializable("expCursor", expCursor);
////
////                    in.putExtras(mBundle);
////
////                    activity.startActivity(in);
//
//                }
//
//
//
//
//                dbHandler.close();
//            }
//        });


//        if (position == 3) {
//
//            holder.topicText.setText("ffffffffffffff");
//        }







        return row;
    }

    public class ViewHolder {
        Topics topic;
        TextView topicText;
//        TextView topicIdParent;
//        TextView topicId;
//        TextView topicLabels;
    }
}
