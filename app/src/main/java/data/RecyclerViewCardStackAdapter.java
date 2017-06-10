package data;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ckenergy.stackcard.stackcardlayoutmanager.ItemTouchHelperCallBack;

import java.util.ArrayList;
import java.util.List;

import io.github.ziginsider.ideographicapp.R;
import model.CardData;
import model.ItemData;

/**
 * Created by zigin on 03.06.2017.
 */

public class RecyclerViewCardStackAdapter extends RecyclerView.Adapter<RecyclerViewCardStackAdapter.CardStackViewHolder> implements ItemTouchHelperCallBack.onSwipListener {

    //int[] mImgs = {R.drawable.img_1,R.drawable.img_2,R.drawable.img_3,R.drawable.img_4};
    private  ArrayList<CardData> mCardList;

    @Override
    public void onSwip(RecyclerView.ViewHolder viewHolder, int position) {
        remove(position);
    }

    class Bean {
        int mPosition;
    }

    List<Bean> cards = new ArrayList<>();

    public RecyclerViewCardStackAdapter(ArrayList<CardData> cardsList) {

        this.mCardList = cardsList;

        for (int i = 0; cardsList.size() > i; ++i) {
            Bean card = new Bean();
            card.mPosition = i;
            //card.mImgRes = mImgs[i% mImgs.length];
            cards.add(card);
        }
    }

    @Override
    public CardStackViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_stack_view, parent, false);
        return new CardStackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardStackViewHolder holder, final int position) {
        holder.top.setText(String.valueOf(cards.get(position).mPosition));
        holder.tabsNames.setText(mCardList.get(position).getParentTopics());

        ArrayList<ItemData> items = new ArrayList<>();

        for(int i = 0; i < mCardList.get(position).getChildTypes().size(); i++) {

            items.add(new ItemData(mCardList.get(position).getChildNames().get(i),
                    mCardList.get(position).getChildTypes().get(i)));
        }

        holder.recycler.setHasFixedSize(true);
        holder.recycler.setLayoutManager(new LinearLayoutManager(holder.recycler.getContext()));
        holder.recycler.setAdapter(new RecyclerTopicCardStackAdapter(items));


        Log.d(this.getClass().getSimpleName(), "position:" + position);

        //click close icon
        holder.closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void remove(int position) {
        cards.remove(position);
        mCardList.remove(position);
        notifyItemRemoved(position);
    }


    class CardStackViewHolder extends RecyclerView.ViewHolder {

        private TextView top;
        private TextView tabsNames;
        private RecyclerView recycler;
        private ImageView closeIcon;
        //private ImageView img;

        CardStackViewHolder(View view) {
            super(view);
            top = (TextView) view.findViewById(R.id.card_stack_top_text);
            tabsNames = (TextView) view.findViewById(R.id.card_tab_names);
            recycler = (RecyclerView) view.findViewById(R.id.card_stack_topic_recycler);
            closeIcon = (ImageView) view.findViewById(R.id.card_stack_close_icon);
            //img = (ImageView) view.findViewById(R.id.img);
        }
    }
}
