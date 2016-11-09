package data;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.github.ziginsider.ideographicapp.R;
import model.Expressions;
import model.FavoriteExpressions;

/**
 * Created by zigin on 26.10.2016.
 */

public class RecyclerExpAdapter extends RecyclerView.Adapter<RecyclerExpAdapter.ViewHolder> {

    private int clickedPosition=-1;
    private ArrayList<Expressions> mExpList;
    InitalDatabaseHandler dbInital;
    private int countItems;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textExp;
        public RelativeLayout relativeLayout;
        public ImageView imgFavoriteAdd;

        public ViewHolder(View view) {
            super(view);
            this.textExp = (TextView) view.findViewById(R.id.txt_view_exp);
            this.relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_exp_content);
            this.imgFavoriteAdd = (ImageView) view.findViewById(R.id.img_favorite_add);
        }
    }

    public RecyclerExpAdapter(ArrayList<Expressions> exp) {
        this.mExpList = exp;
        this.countItems = exp.size();
    }

    @Override
    public RecyclerExpAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_exp_item, parent, false);

        //there is programmatically change layout: size, paddings, margin, etc...
        dbInital = new InitalDatabaseHandler(parent.getContext());

        RecyclerExpAdapter.ViewHolder vh = new RecyclerExpAdapter.ViewHolder(v);

        return vh;
    }

    //refresh recycler item

    @Override
    public void onBindViewHolder(final RecyclerExpAdapter.ViewHolder holder, final int position) {

        holder.textExp.setText(mExpList.get(position).getExpText());

        if (position == clickedPosition){
            holder.relativeLayout.setBackgroundResource(R.drawable.bg_current_topic);
        } else {
            holder.relativeLayout.setBackgroundResource(R.drawable.ripple_exp_new);
        }

        if (dbInital.isExpInFavoriteList(mExpList.get(position).getExpId())) {
            holder.imgFavoriteAdd.setImageResource(R.drawable.bookmark_ok);
        } else {
            holder.imgFavoriteAdd.setImageResource(R.drawable.bookmark_no);
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

        holder.imgFavoriteAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if (dbInital.isExpInFavoriteList(mExpList.get(position).getExpId())) {

                Toast toast = Toast.makeText(v.getContext(),
                        "Removed from favorite list", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                LinearLayout toastContainer = (LinearLayout) toast.getView();
                ImageView imgBookmarkRemove = new ImageView(v.getContext());
                imgBookmarkRemove.setImageResource(R.drawable.bookmark_remove);
                toastContainer.addView(imgBookmarkRemove, 0);
                toast.show();

                dbInital.deleteFavoriteExp(mExpList.get(position).getExpId());

            } else {

                Toast toast = Toast.makeText(v.getContext(),
                        "Added to favorite list", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                LinearLayout toastContainer = (LinearLayout) toast.getView();
                ImageView imgBookmarkOk = new ImageView(v.getContext());
                imgBookmarkOk.setImageResource(R.drawable.bookmark_ok_2);
                toastContainer.addView(imgBookmarkOk, 0);
                toast.show();

                FavoriteExpressions favoriteExp = new FavoriteExpressions();
                favoriteExp.setTextExp(mExpList.get(position).getExpText());
                favoriteExp.setIdExp(mExpList.get(position).getExpId());
                favoriteExp.setIdParentTopic(mExpList.get(position).getExpParentId());

                dbInital.addFavoriteExp(favoriteExp);

            }




                notifyDataSetChanged();
            }
        });


        if (position == (countItems - 1)) {
            dbInital.close();
        }
    }

    @Override
    public int getItemCount() {
        return  mExpList.size();
    }
}
