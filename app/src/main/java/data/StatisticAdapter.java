package data;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.github.ziginsider.ideographicapp.R;
import model.FavoriteExpressions;
import model.StatisticTopic;

/**
 * Created by zigin on 20.11.2016.
 */

public class StatisticAdapter extends RecyclerView.Adapter<StatisticAdapter.ViewHolder> {

    private ArrayList<StatisticTopic> statisticTopicList;
    //private int clickedPosition;
    private DatabaseHandler dba;
    //private InitalDatabaseHandler dbInital;
    private int countItems;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textTopic;
        public int idTopic;
        public TextView textCounterTopic;
        public RelativeLayout relativeLayout;
        public ImageView imgStatisticTopic;

        public ViewHolder(View view) {
            super(view);
            this.textTopic = (TextView) view.findViewById(R.id.text_item_statistic_topic);
            this.textCounterTopic = (TextView) view.findViewById(R.id.number_clicked_statistic_topic);
            this.relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_statistic_topic);
            this.imgStatisticTopic = (ImageView) view.findViewById(R.id.image_item_statistic_topic);
            this.idTopic = 0;
        }
    }

    public StatisticAdapter(ArrayList<StatisticTopic> statisticTopicItems) {
        this.statisticTopicList = statisticTopicItems;
        this.countItems = statisticTopicItems.size();
    }

    @Override
    public StatisticAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_statistic_topic, parent, false);

        //there is programmatically change layout: size, paddings, margin, etc...
        dba = new DatabaseHandler(parent.getContext());
        //dbInital = new InitalDatabaseHandler(parent.getContext());

        StatisticAdapter.ViewHolder vh = new StatisticAdapter.ViewHolder(v);

        return vh;
    }

    //refresh recycler item

    @Override
    public void onBindViewHolder(StatisticAdapter.ViewHolder holder, final int position) {

        holder.textTopic.setText(statisticTopicList.get(position).getTextTopic());
        holder.idTopic = statisticTopicList.get(position).getIdTopic();
        holder.textCounterTopic.setText("Number of Clicks = " +
                String.valueOf(statisticTopicList.get(position).getCounterTopic()));


//        if (position == clickedPosition){
//            holder.relativeLayout.setBackgroundResource(R.drawable.bg_current_topic);
//        } else {
//            holder.relativeLayout.setBackgroundResource(R.drawable.ripple_topic_new);
//        }

        if (dba.getTopicCountByIdParent(holder.idTopic) > 0) {
            holder.imgStatisticTopic.setImageResource(R.drawable.ic_chevron_color_right);
        } else {
            holder.imgStatisticTopic.setImageResource(R.drawable.ic_three_circle_green);
        }

        if (position == (countItems - 1)) {
            dba.close();
            //dbInital.close();
        }

    }


    @Override
    public int getItemCount() {
        return  statisticTopicList.size();
    }


}
