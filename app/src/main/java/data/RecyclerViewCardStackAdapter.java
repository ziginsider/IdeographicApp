package data;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ckenergy.stackcard.stackcardlayoutmanager.ItemTouchHelperCallBack;

import java.util.ArrayList;
import java.util.List;

import io.github.ziginsider.ideographicapp.R;
import io.github.ziginsider.ideographicapp.WorkActivityRecycler_;
import model.CardData;
import model.ItemData;

/**
 * Created by zigin on 03.06.2017.
 */

public class RecyclerViewCardStackAdapter extends RecyclerView.Adapter<RecyclerViewCardStackAdapter.CardStackViewHolder> implements ItemTouchHelperCallBack.onSwipListener {

    //int[] mImgs = {R.drawable.img_1,R.drawable.img_2,R.drawable.img_3,R.drawable.img_4};
    private  ArrayList<CardData> mCardList;

    private InitalDatabaseHandler dba;
    private DatabaseHandler dba_data;

    Context context;

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
            card.mPosition = i + 1;
            //card.mImgRes = mImgs[i% mImgs.length];
            cards.add(card);
        }
    }

    @Override
    public CardStackViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        context = parent.getContext();
        dba = new InitalDatabaseHandler(context);
        dba_data = new DatabaseHandler(context);


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_stack_view, parent, false);
        return new CardStackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardStackViewHolder holder, final int position) {
        holder.top.setText(String.valueOf(cards.get(position).mPosition));
        holder.tabsNames.setText(mCardList.get(position).getNameTopics());

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

        holder.recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Click " + mCardList.get(position).getNameTopics(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Button Click " + mCardList.get(position).getNameTopics(), Toast.LENGTH_SHORT).show();
                PersistantStorage.init(v.getContext());
                PersistantStorage.addProperty(Constants.CURRENT_CARD,
                        String.valueOf(mCardList.get(position).getCardUniqueId()));

                ArrayList<Integer> idTopicsPageList = new ArrayList<Integer>();
                //idTopicsPageList.clear();

                int currentId = mCardList.get(position).getCardTopicId();
                //add to recent topics
                //TODO recent add 2
//                afterItemClickTask = new AfterItemClickTask(RecentTopicActivity.this);
//                afterItemClickTask.execute(currentId);

                idTopicsPageList.add(currentId);
                do {
                    currentId = dba_data.getTopicById(currentId).getTopicParentId();
                    idTopicsPageList.add(currentId);

                } while (currentId != 0);

                //Activity activity = (Activity) v.getContext();

                Intent i = new Intent(v.getContext(),
                        WorkActivityRecycler_.class);
                i.putExtra(Constants.EXTRA_TOPICS_OPEN_TABS, idTopicsPageList);
                context.startActivity(i);
                Activity a = (Activity)context;
                a.finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void remove(int position) {

        PersistantStorage.init(context);
        int currentCardId = Integer.valueOf(PersistantStorage.getProperty(Constants.CURRENT_CARD));
        if (mCardList.get(position).getCardUniqueId() == currentCardId) {
            int newCurrentId = dba.getCardLastId();
            PersistantStorage.addProperty(Constants.CURRENT_CARD, String.valueOf(newCurrentId));
        }
        dba.deleteCardTopicByIdCardTopic(mCardList.get(position).getCardUniqueId());

        int currentCountCards = 1;
        //PersistantStorage.init(context);
        if (PersistantStorage.getProperty(Constants.CURRENT_COUNT_CARD) != null) {
            currentCountCards = Integer.valueOf(PersistantStorage.getProperty(Constants.CURRENT_COUNT_CARD)) - 1;
        }
        PersistantStorage.addProperty(Constants.CURRENT_COUNT_CARD, String.valueOf(currentCountCards));

        cards.remove(position);
        mCardList.remove(position);
        notifyItemRemoved(position);
    }


    class CardStackViewHolder extends RecyclerView.ViewHolder {

        private TextView top;
        private TextView tabsNames;
        private RecyclerView recycler;
        private ImageView closeIcon;
        private Button buttonSelect;
        //private RelativeLayout relativeLayout;
        //private ImageView img;

        CardStackViewHolder(View view) {
            super(view);
            top = (TextView) view.findViewById(R.id.card_stack_top_text);
            tabsNames = (TextView) view.findViewById(R.id.card_tab_names);
            recycler = (RecyclerView) view.findViewById(R.id.card_stack_topic_recycler);
            closeIcon = (ImageView) view.findViewById(R.id.card_stack_close_icon);
            buttonSelect = (Button) view.findViewById(R.id.button_card_stack_click);
            //relativeLayout = (RelativeLayout) view.findViewById(R.id.card_stack_click_relative);
            //img = (ImageView) view.findViewById(R.id.img);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        dba.close();
        super.finalize();
    }
}
