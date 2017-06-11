package io.github.ziginsider.ideographicapp;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.widget.RecyclerView;

import com.ckenergy.stackcard.stackcardlayoutmanager.StackCardLayoutManager;
import com.ckenergy.stackcard.stackcardlayoutmanager.StackCardPostLayout;

import java.util.ArrayList;

import data.Constants;
import data.DatabaseHandler;
import data.InitalDatabaseHandler;
import data.RecyclerViewCardStackAdapter;
import model.CardData;
import model.RecentTopics;

public class CardStackActivity extends BaseCardStackActivity {

    InitalDatabaseHandler dba;
    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dba = new InitalDatabaseHandler(this);
        dbHandler = new DatabaseHandler(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_stack_list);

//        ///////////////////////
//        ArrayList<CardData> data = new ArrayList<>();
//        for(int i = 0; i < 20; i++) {
//            String name;
//            int type;
//            ArrayList<Integer> childTypes = new ArrayList<Integer>();
//            ArrayList<String> childNames = new ArrayList<String>();
//            childNames.add("one one one");
//            childNames.add("two second two");
//            childNames.add("three...");
//            childNames.add("four");
//            childNames.add("five");
//            childNames.add("six");
//            childNames.add("seven");
//            childNames.add("eight");
//            childNames.add("nine");
//            childNames.add("ten pereten");
//            childNames.add("eleven");
//            childNames.add("twelve");
//            childNames.add("thirteen");
//            childNames.add("fourteen");
//            childNames.add("fifteen");
//            childNames.add("sixteen");
//            childNames.add("seventeen");
//            childNames.add("eighteen");
//            childNames.add("nineteen");
//            childNames.add("twenty");
//
//            if (i < 10) {
//                name = "Topics  Man  Behaviour";
//                type = Constants.CARD_TYPE_TOPIC;
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//                childTypes.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
//            } else {
//                name = "Topics  Circumstances";
//                type = Constants.CARD_TYPE_EXP;
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//                childTypes.add(Constants.IMAGE_TYPE_EXP);
//            }
//            data.add(new CardData(type, name, childNames, childTypes));
//
//        }



        /////////////////////////////

        ArrayList<CardData> data = getCardsData();

        setContentView(R.layout.activity_card_stack);
        StackCardLayoutManager stackCardLayoutManager =
                new StackCardLayoutManager
                        (StackCardLayoutManager.VERTICAL,
                                false,
                                new StackCardPostLayout());
        stackCardLayoutManager.setNumberOrder(StackCardLayoutManager.NEGATIVE_ORDER);
        stackCardLayoutManager.setStackOrder(StackCardLayoutManager.OUT_STACK_ORDER);
        RecyclerViewCardStackAdapter adapter = new RecyclerViewCardStackAdapter(data);

        initRecyclerView(recyclerView, stackCardLayoutManager, adapter);
    }

    private ArrayList<CardData> getCardsData() {
        ArrayList<CardData> data = new ArrayList<>();
        ArrayList<RecentTopics> recentTopics = dba.getRecentTopicsList();

        for (RecentTopics recentTopic: recentTopics) {
            ArrayList<String> childTopicsNames = new ArrayList<>();
            ArrayList<Integer> childTopicsImgType = new ArrayList<>();
            childTopicsNames = dbHandler.getTopicNamesByIdParent(recentTopic.getTopicId());

            if (childTopicsNames.isEmpty()) {
                childTopicsNames = dbHandler.getExpNamesByIdParent(recentTopic.getTopicId());

                for (int i = 0; i < childTopicsNames.size(); i++) {
                    childTopicsImgType.add(Constants.IMAGE_TYPE_EXP);
                }

                data.add(new CardData(recentTopic.getTopicId(),
                        recentTopic.getTopicText(), //TODO topics text >>>>
                        childTopicsNames,
                        childTopicsImgType));
            } else {

                ArrayList<Integer> idTopicsChild = dbHandler
                        .getTopicIdsByIdParent(recentTopic.getTopicId());

                for (int id : idTopicsChild) {

                    if (dbHandler.getTopicByIdParent(id).isEmpty()) {
                        childTopicsImgType.add(Constants.IMAGE_TYPE_TOPIC_LEAF);
                    } else  {
                        childTopicsImgType.add(Constants.IMAGE_TYPE_TOPIC_BRANCH);
                    }
                }
                data.add(new CardData(recentTopic.getTopicId(),
                        recentTopic.getTopicText(),
                        childTopicsNames,
                        childTopicsImgType));
            }
        }

        return  data;
    }

    @Override
    protected void onDestroy() {
        dba.close();
        dbHandler.close();
        super.onDestroy();
    }
}
