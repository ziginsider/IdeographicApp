package io.github.ziginsider.ideographicapp;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.widget.RecyclerView;

import com.ckenergy.stackcard.stackcardlayoutmanager.StackCardLayoutManager;
import com.ckenergy.stackcard.stackcardlayoutmanager.StackCardPostLayout;

import java.util.ArrayList;

import data.Constants;
import data.RecyclerViewCardStackAdapter;
import model.CardData;

public class CardStackActivity extends BaseCardStackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_stack);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_stack_list);

        ///////////////////////
        ArrayList<CardData> data = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            String name;
            int type;
            ArrayList<Integer> childTypes = new ArrayList<Integer>();
            ArrayList<String> childNames = new ArrayList<String>();
            childNames.add("one one one");
            childNames.add("two second two");
            childNames.add("three...");
            childNames.add("four");
            childNames.add("five");
            childNames.add("six");
            childNames.add("seven");
            childNames.add("eight");
            childNames.add("nine");
            childNames.add("ten pereten");
            childNames.add("eleven");
            childNames.add("twelve");
            childNames.add("thirteen");
            childNames.add("fourteen");
            childNames.add("fifteen");
            childNames.add("sixteen");
            childNames.add("seventeen");
            childNames.add("eighteen");
            childNames.add("nineteen");
            childNames.add("twenty");

            if (i < 10) {
                name = "Topics  Man  Behaviour";
                type = Constants.CARD_TYPE_TOPIC;
                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
            } else {
                name = "Topics  Circumstances";
                type = Constants.CARD_TYPE_EXP;
                childTypes.add(Constants.IMAGE_TYPE_EXP);
                childTypes.add(Constants.IMAGE_TYPE_EXP);
                childTypes.add(Constants.IMAGE_TYPE_EXP);
                childTypes.add(Constants.IMAGE_TYPE_EXP);
                childTypes.add(Constants.IMAGE_TYPE_EXP);
                childTypes.add(Constants.IMAGE_TYPE_EXP);
                childTypes.add(Constants.IMAGE_TYPE_EXP);
                childTypes.add(Constants.IMAGE_TYPE_EXP);
                childTypes.add(Constants.IMAGE_TYPE_EXP);
                childTypes.add(Constants.IMAGE_TYPE_EXP);
                childTypes.add(Constants.IMAGE_TYPE_EXP);
                childTypes.add(Constants.IMAGE_TYPE_EXP);
                childTypes.add(Constants.IMAGE_TYPE_EXP);
                childTypes.add(Constants.IMAGE_TYPE_EXP);
                childTypes.add(Constants.IMAGE_TYPE_EXP);
                childTypes.add(Constants.IMAGE_TYPE_EXP);
                childTypes.add(Constants.IMAGE_TYPE_EXP);
                childTypes.add(Constants.IMAGE_TYPE_EXP);
                childTypes.add(Constants.IMAGE_TYPE_EXP);
                childTypes.add(Constants.IMAGE_TYPE_EXP);
            }
            data.add(new CardData(type, name, childNames, childTypes));

        }



        /////////////////////////////

        StackCardLayoutManager stackCardLayoutManager =
                new StackCardLayoutManager
                        (StackCardLayoutManager.VERTICAL,
                                false,
                                new StackCardPostLayout());
        stackCardLayoutManager.setStackOrder(StackCardLayoutManager.OUT_STACK_ORDER);
        stackCardLayoutManager.setNumberOrder(StackCardLayoutManager.NEGATIVE_ORDER);
        RecyclerViewCardStackAdapter adapter = new RecyclerViewCardStackAdapter(data);

        initRecyclerView(recyclerView, stackCardLayoutManager, adapter);
    }
}
