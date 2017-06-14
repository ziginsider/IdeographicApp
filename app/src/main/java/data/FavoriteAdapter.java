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
import model.FavoriteExpressions;
import model.RecentTopics;

/**
 * Created by zigin on 09.11.2016.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private ArrayList<FavoriteExpressions> favoriteExpList;
    //private int clickedPosition;
    private DatabaseHandler dba;
    private InitalDatabaseHandler dbInital;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textExp;
        //private int idExp;
        private TextView textParentTopic;
        //private RelativeLayout relativeLayout;
        private ImageView imgFavoriteExp;

        public ViewHolder(View view) {
            super(view);
            this.textExp = (TextView) view.findViewById(R.id.txt_item_favorite_exp);
            this.textParentTopic = (TextView) view.findViewById(R.id.txt_item_favorite_parent_topic);
            //this.relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_layout_favorite_exp);
            this.imgFavoriteExp = (ImageView) view.findViewById(R.id.img_item_favorite_exp);
            //this.idExp = 0;
        }
    }

    public FavoriteAdapter(ArrayList<FavoriteExpressions> favoriteItems) {
        this.favoriteExpList = favoriteItems;
    }

    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_favorite_exp, parent, false);

        //there is programmatically change layout: size, paddings, margin, etc...
        dba = new DatabaseHandler(parent.getContext());
        dbInital = new InitalDatabaseHandler(parent.getContext());

        return new FavoriteAdapter.ViewHolder(v);
    }

    //refresh recycler item

    @Override
    public void onBindViewHolder(FavoriteAdapter.ViewHolder holder, final int position) {

        final FavoriteExpressions mCurrentFavoriteItem = favoriteExpList.get(position);

        holder.textExp.setText(mCurrentFavoriteItem.getTextExp());
        holder.textParentTopic.setText(dba.
           getTopicById(mCurrentFavoriteItem.
                getIdParentTopic()).
           getTopicText());

//        if (position == clickedPosition){
//            holder.relativeLayout.setBackgroundResource(R.drawable.bg_current_topic);
//        } else {
//            holder.relativeLayout.setBackgroundResource(R.drawable.ripple_bg_exp);
//        }

        if (dbInital.isExpInFavoriteList(mCurrentFavoriteItem.getIdExp())) {
            holder.imgFavoriteExp.setImageResource(R.drawable.bookmark_ok);
        } else {
            holder.imgFavoriteExp.setImageResource(R.drawable.bookmark_no);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) view.getContext().
                        getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(mCurrentFavoriteItem.getTextExp(),
                        mCurrentFavoriteItem.getTextExp());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(view.getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();

            }
        });
//
        holder.imgFavoriteExp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    dbInital.deleteFavoriteExp(mCurrentFavoriteItem.getIdExp());

                    favoriteExpList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, favoriteExpList.size());

                } catch (Exception e) {
                    //notifyDataSetChanged();

                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return  favoriteExpList.size();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        dba.close();
        dbInital.close();
        super.onDetachedFromRecyclerView(recyclerView);
    }
}
