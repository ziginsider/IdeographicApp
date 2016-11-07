package data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.ziginsider.ideographicapp.R;
import model.DoubleItem;
import model.Topics;

/**
 * Created by zigin on 25.10.2016.
 */

public class RecyclerTopicAdapter extends RecyclerView.Adapter<RecyclerTopicAdapter.ViewHolder> {


    //private DatabaseHandler dba;
    //private int clickedPosition;
    private ArrayList<Topics> mTopicsList;
    private int countItem;
    private PersistantStorage storage;
    private String mNameSelectTopic;
    private DatabaseHandler dba;
    //public static OnItemClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textTopic;
        public int idTopic;
        public RelativeLayout relativeLayout;
        public ImageView imageNextItem; // topic or expression

        public ViewHolder(final View view) {
            super(view);
            this.textTopic = (TextView) view.findViewById(R.id.text_item_topic_content);
            this.relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_topic_content);
            this.idTopic = 0;
            this.imageNextItem = (ImageView) view.findViewById(R.id.image_item_topic_content);
//            view.setOnClickListener( new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    if (RecyclerTopicAdapter.listener != null) {
//                        RecyclerTopicAdapter.listener.onItemClick(view, getLayoutPosition());
//                    }
//                }
//            });
        }
    }

    public RecyclerTopicAdapter(ArrayList<Topics> topics) {
        this.mTopicsList = topics;
        this.countItem = topics.size();

    }

    @Override
    public RecyclerTopicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_topic_item, parent, false);

        //there is programmatically change layout: size, paddings, margin, etc...

        dba = new DatabaseHandler(parent.getContext());
        storage.init(parent.getContext());
        if (mTopicsList.get(0).getTopicParentId() == 0) {

            mNameSelectTopic = storage.getProperty(Constants.TOPICS_ROOT_NAME);

        } else {//TODO optimization
            mNameSelectTopic = storage.getProperty(dba.getTopicById(mTopicsList.get(0).
                    getTopicParentId()).
                    getTopicText());
        }

        RecyclerTopicAdapter.ViewHolder vh = new RecyclerTopicAdapter.ViewHolder(v);

        return vh;
    }

    //refresh recycler item

    @Override
    public void onBindViewHolder(RecyclerTopicAdapter.ViewHolder holder, final int position) {

        holder.textTopic.setText(mTopicsList.get(position).getTopicText());
        holder.idTopic = mTopicsList.get(position).getTopicId();

//        if (position == clickedPosition){
//            holder.relativeLayout.setBackgroundResource(R.drawable.bg_current_topic);
//        } else {
//            holder.relativeLayout.setBackgroundResource(R.drawable.ripple_topic_new);
//        }

        if (mTopicsList.get(position).getTopicText().equals(mNameSelectTopic)) {
            holder.relativeLayout.setBackgroundResource(R.drawable.bg_current_topic);
        } else {
            holder.relativeLayout.setBackgroundResource(R.drawable.ripple_topic_new);
        }

        if (dba.getTopicCountByIdParent(holder.idTopic) > 0) {
            holder.imageNextItem.setImageResource(R.drawable.ic_chevron_color_right);
        } else {
            holder.imageNextItem.setImageResource(R.drawable.ic_three_circle_green);
        }

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                //set the position
//                clickedPosition = position;
//
//                //notify the data has changed
//                notifyDataSetChanged();
//                //notifyItemChanged(position);
//            }
//        });

//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//
//            @Override
//            public boolean onLongClick(View v) {
//                clickedPosition = position;
//                notifyDataSetChanged();
//                return  true;
//            }
//        });

        if (position == (countItem - 1)) {

            dba.close();
        }

    }

    @Override
    public int getItemCount() {
        return  mTopicsList.size();
    }

//    public interface OnItemClickListener{
//        void onItemClick(View view, int position);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener){
//        this.listener = listener;
//    }
//
//    public void setClickedPosition(int clickedPosition) {
//        this.clickedPosition = clickedPosition;
//    }



}


