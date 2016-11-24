package data;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
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
    private PersistantStorage storage;
    private String mNameSelectTopic;
    private DatabaseHandler dba;
    //private int mSelectItem;
    //private boolean mFlagSelect;
    //FragmentSlidingTabs fragmentSlidingTabs;
    //private ViewPager viewPager;

    public CustomListViewTopicAdapter(Activity act, int resource, ArrayList<Topics> data) {
        super(act, resource, data);
        layoutResourse = resource;
        activity = act;
        TopicsList = data;
        storage = new PersistantStorage();
        dba = new DatabaseHandler(getContext());

        storage.init(getContext());
         if (data.get(0).getTopicParentId() == 0) {

            mNameSelectTopic = storage.getProperty(Constants.TOPICS_ROOT_NAME);

        } else {
            mNameSelectTopic = storage.getProperty(dba.getTopicById(data.get(0).getTopicParentId()).
                    getTopicText());
        }

        notifyDataSetChanged();
    }
//


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
            holder.topicLayout = (RelativeLayout) row.findViewById(R.id.relative_topic_content);



//            int selectItemPosition = fragmentSlidingTabs.
//                    getSelectTopicItemPosition(fragmentSlidingTabs.getSelectedTabPosition());


//            if (position == ic_intro_1) {
//                holder.topicLayout.setBackgroundResource(R.drawable.bg_current_topic);
//            } else {
//                holder.topicLayout.setBackgroundResource(R.drawable.ripple_topic_new);
//            }


//            holder.topicId = (TextView) row.findViewById(R.id.topicId);
//            holder.topicIdParent = (TextView) row.findViewById(R.id.topicIdParent);
//            holder.topicLabels = (TextView) row.findViewById(R.id.topicLabels);

            if (getItem(position).getTopicText().equals(mNameSelectTopic)) {

                holder.topicLayout.setBackgroundResource(R.drawable.bg_current_topic);
            } else {
                holder.topicLayout.setBackgroundResource(R.drawable.ripple_topic_new);
            }

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
//                finalHolder.topicLayout.setBackgroundResource(R.drawable.bg_current_topic);
//            }
//        });


//        if (position == ic_intro_3) {
//
//            holder.topicText.setText("ffffffffffffff");
//        }







        return row;
    }

    public class ViewHolder {
        Topics topic;
        TextView topicText;

        RelativeLayout topicLayout;
//        TextView topicIdParent;
//        TextView topicId;
//        TextView topicLabels;
    }
}
