package data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.ziginsider.ideographicapp.R;
import model.DoubleItem;
import model.Expressions;
import model.Topics;

/**
 * Created by zigin on 20.10.2016.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

//    private ArrayList<Expressions> mExpList;
//    private ArrayList<String> mTopicNameList;
    private ArrayList<DoubleItem> mDoubleItems;
    //private DatabaseHandler dba;
    private int clickedPosition;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textExp;
        private TextView textParentTopic;
        private int idParentTopic;
        private RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);
            this.textExp = (TextView) view.findViewById(R.id.txt_item_exp_rv);
            this.textParentTopic = (TextView) view.findViewById(R.id.txt_item_topic_rv);
            this.relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_layout_recycler_item);
            this.idParentTopic = 0;
        }
    }

    public RecyclerAdapter(ArrayList<DoubleItem> doubleItems) {
        this.mDoubleItems = doubleItems;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);

        //there is programmatically change layout: size, paddings, margin, etc...

        return new ViewHolder(v);
    }

    //refresh recycler item

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.textExp.setText(mDoubleItems.get(position).getExp().getExpText());
        holder.textParentTopic.setText(mDoubleItems.get(position).getTopic().getTopicText());
        holder.idParentTopic = mDoubleItems.get(position).getExp().getExpParentId();

        //final DoubleItem item = mDoubleItems.get(position);

        if (position == clickedPosition){
            holder.relativeLayout.setBackgroundResource(R.drawable.bg_current_topic);
        } else {
            holder.relativeLayout.setBackgroundResource(R.drawable.ripple_bg_exp);
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

    }


    @Override
    public int getItemCount() {
        return  mDoubleItems.size();
    }


}
