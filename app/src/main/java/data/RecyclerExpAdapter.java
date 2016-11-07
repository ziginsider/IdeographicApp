package data;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.ziginsider.ideographicapp.R;
import model.Expressions;

/**
 * Created by zigin on 26.10.2016.
 */

public class RecyclerExpAdapter extends RecyclerView.Adapter<RecyclerExpAdapter.ViewHolder> {

    private int clickedPosition=-1;
    private ArrayList<Expressions> mExpList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textExp;
        public RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);
            this.textExp = (TextView) view.findViewById(R.id.txt_view_exp);
            this.relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_exp_content);
        }
    }

    public RecyclerExpAdapter(ArrayList<Expressions> exp) {
        this.mExpList = exp;
    }

    @Override
    public RecyclerExpAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_exp_item, parent, false);

        //there is programmatically change layout: size, paddings, margin, etc...

        RecyclerExpAdapter.ViewHolder vh = new RecyclerExpAdapter.ViewHolder(v);

        return vh;
    }

    //refresh recycler item

    @Override
    public void onBindViewHolder(RecyclerExpAdapter.ViewHolder holder, final int position) {

        holder.textExp.setText(mExpList.get(position).getExpText());

        if (position == clickedPosition){
            holder.relativeLayout.setBackgroundResource(R.drawable.bg_current_topic);
        } else {
            holder.relativeLayout.setBackgroundResource(R.drawable.ripple_exp_new);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(mExpList.get(position).getExpText(), mExpList.get(position).getExpText());
                clipboard.setPrimaryClip(clip);
                //set the position
                clickedPosition = position;
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
        return  mExpList.size();
    }
}
