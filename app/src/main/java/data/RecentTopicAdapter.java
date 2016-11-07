package data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.ziginsider.ideographicapp.R;
import model.RecentTopics;

/**
 * Created by zigin on 07.11.2016.
 */

public class RecentTopicAdapter extends RecyclerView.Adapter<RecentTopicAdapter.ViewHolder> {

    private ArrayList<RecentTopics> recentTopicsList;
    private int clickedPosition;
    private DatabaseHandler dba;
    private int countItems;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textTopic;
        public TextView numberTopic;
        public int idTopic;
        public ImageView imageNextItem;
        public RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);
            this.textTopic = (TextView) view.findViewById(R.id.text_item_recent_topic);
            this.numberTopic = (TextView) view.findViewById(R.id.number_item_recent_topic);
            this.imageNextItem = (ImageView) view.findViewById(R.id.image_item_recent_topic);
            this.relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_recent_topic);
            this.idTopic = 0;
        }
    }

    public RecentTopicAdapter(ArrayList<RecentTopics> recentItems) {
        this.recentTopicsList = recentItems;
        this.countItems = recentItems.size();
    }

    @Override
    public RecentTopicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_recent_topic, parent, false);

        //there is programmatically change layout: size, paddings, margin, etc...
        dba = new DatabaseHandler(parent.getContext());

        RecentTopicAdapter.ViewHolder vh = new RecentTopicAdapter.ViewHolder(v);

        return vh;
    }

    //refresh recycler item

    @Override
    public void onBindViewHolder(RecentTopicAdapter.ViewHolder holder, final int position) {

        holder.textTopic.setText(recentTopicsList.get(position).getTopicText());
        holder.numberTopic.setText(String.valueOf(position + 1) + ".");
        holder.idTopic = recentTopicsList.get(position).getTopicId();

        if (position == clickedPosition){
            holder.relativeLayout.setBackgroundResource(R.drawable.bg_current_topic);
        } else {
            holder.relativeLayout.setBackgroundResource(R.drawable.ripple_bg_exp);
        }

        if (dba.getTopicCountByIdParent(holder.idTopic) > 0) {
            holder.imageNextItem.setImageResource(R.drawable.ic_chevron_color_right);
        } else {
            holder.imageNextItem.setImageResource(R.drawable.ic_three_circle_green);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //set the position
                clickedPosition = position;

                //bluetoothPreferences.setBluetoothName(device.getName());
                //bluetoothPreferences.setBluetoothAddress(device.getAddress());
                //notify the data has changed
                notifyDataSetChanged();
                //notifyItemChanged(position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                clickedPosition = position;
                notifyDataSetChanged();
                return  true;
            }
        });

        if (position == (countItems - 1)) {
            dba.close();
        }

    }


    @Override
    public int getItemCount() {
        return  recentTopicsList.size();
    }
}
