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
import model.ItemData;

/**
 * Created by zigin on 09.06.2017.
 */

public class RecyclerTopicCardStackAdapter extends RecyclerView.Adapter<RecyclerTopicCardStackAdapter.ViewHolder> {

    private ArrayList<ItemData> mItemList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textItem;
        private ImageView imageItem;
        private RelativeLayout relativeItem;
        public ViewHolder(final View itemView) {
            super(itemView);
            this.relativeItem = (RelativeLayout) itemView.findViewById(R.id.relative_card_stack_topic_content);
            this.textItem = (TextView) itemView.findViewById(R.id.text_topic_card_stack_item);
            this.imageItem = (ImageView) itemView.findViewById(R.id.image_topic_card_stack_item);
        }
    }

    public RecyclerTopicCardStackAdapter(ArrayList<ItemData> items) {
        this.mItemList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_topic_card_stack, parent, false);
        return new RecyclerTopicCardStackAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerTopicCardStackAdapter.ViewHolder holder, int position) {

        ItemData mCurrentItem = mItemList.get(position);

        holder.textItem.setText(mCurrentItem.getNameItem());

        if (mCurrentItem.getImageItemType() == Constants.IMAGE_TYPE_TOPIC_BRANCH) {
            holder.relativeItem.setBackgroundResource(R.drawable.ripple_topic_new);
            holder.imageItem.setImageResource(R.drawable.ic_chevron_color_right);
        }
        if (mCurrentItem.getImageItemType() == Constants.IMAGE_TYPE_TOPIC_LEAF) {
            holder.relativeItem.setBackgroundResource(R.drawable.ripple_topic_new);
            holder.imageItem.setImageResource(R.drawable.ic_three_circle_green);
        }
        if (mCurrentItem.getImageItemType() == Constants.IMAGE_TYPE_EXP) {
            holder.relativeItem.setBackgroundResource(R.drawable.ripple_exp_new);
            holder.imageItem.setImageResource(R.drawable.bookmark_no);
        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
