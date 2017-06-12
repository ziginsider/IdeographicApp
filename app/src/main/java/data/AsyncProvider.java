package data;

import android.content.Context;

import java.util.ArrayList;

import model.CardTopic;
import model.RecentTopics;
import model.StatisticTopic;

/**
 * Created by zigin on 08.11.2016.
 */


public class AsyncProvider {

    private PersistantStorage storage;

    public AsyncProvider() {
    }

    public void setRecentTopic(Context context, int topicId)
    {
        int currentWeight = 0;
        int setWeight = 0;
        RecentTopics currentRecentTopic;
        int maxTotalRecent = 20;

        InitalDatabaseHandler dba = new InitalDatabaseHandler(context);
        DatabaseHandler dba_data = new DatabaseHandler(context);

        ArrayList<RecentTopics> recentList = dba.getRecentTopicsList();

        //if the topic is on the recent
        if (dba.getRecentCountByIdTopic(topicId) > 0) {

            currentRecentTopic = dba.getRecentTopicByTopicId(topicId);
            currentWeight = currentRecentTopic.getTopicWeight();
            setWeight = dba.getRecentMaxWeight();

            for(int i = 0; i < recentList.size(); i++) {

                if (recentList.get(i).getTopicWeight() > currentWeight) {

                    //recentList.get(i).downTopicWeight();
                    dba.updateTopicWeightByIdTopic(recentList.get(i).getTopicId(),
                            (recentList.get(i).getTopicWeight() - 1));

                } else if(recentList.get(i).getTopicWeight() == currentWeight) {

                    //recentList.get(i).setTopicWeight(setWeight);
                    dba.updateTopicWeightByIdTopic(recentList.get(i).getTopicId(), setWeight);
                }
            }
        } else { //if the topic isn't on the recent


            int totalRecent = dba.getTotalRecentTopics();
            //if it is first recent
            if (totalRecent == 0) {

                currentRecentTopic = new RecentTopics(
                        dba_data.getTopicById(topicId).getTopicText(),
                        topicId,
                        1);
                dba.addRecentTopic(currentRecentTopic);

            } else if (totalRecent < maxTotalRecent) {

                setWeight = dba.getRecentMaxWeight() + 1;

                currentRecentTopic = new RecentTopics(
                        dba_data.getTopicById(topicId).getTopicText(),
                        topicId,
                        setWeight);
                dba.addRecentTopic(currentRecentTopic);
            } else {

                for(int i = 0; i < recentList.size(); i++) {

                    dba.updateTopicWeightByIdTopic(recentList.get(i).getTopicId(),
                            (recentList.get(i).getTopicWeight() - 1));
                }

                dba.deleteLastRecentTopic();

                setWeight = maxTotalRecent;

                currentRecentTopic = new RecentTopics(
                        dba_data.getTopicById(topicId).getTopicText(),
                        topicId,
                        setWeight);
                dba.addRecentTopic(currentRecentTopic);
            }
        }
        dba.close();
        dba_data.close();
    }

    public void setStatisticTopic(Context context, int topicId) {

        InitalDatabaseHandler dba_inital = new InitalDatabaseHandler(context);
        DatabaseHandler dba_data = new DatabaseHandler(context);

        if (dba_inital.isTopicInStatisticList(topicId)) {

            dba_inital.upTopicCounterByIdTopic(topicId);

        } else {
            StatisticTopic statisticTopic = new StatisticTopic();

            statisticTopic.setTextTopic(dba_data.getTopicById(topicId).getTopicText());
            statisticTopic.setIdTopic(topicId);
            statisticTopic.setCounterTopic(1);

            dba_inital.addStatisticTopic(statisticTopic);
        }
        dba_data.close();
        dba_inital.close();
    }

    public void setNewCard(Context context) {
        InitalDatabaseHandler dba_init = new InitalDatabaseHandler(context);
        int newId;

        newId = dba_init.addCardTopic(new CardTopic("Topics", 0));

        PersistantStorage.init(context);
        PersistantStorage.addProperty(Constants.CURRENT_CARD, String.valueOf(newId));

        int currentCountCards = 1;
        if (PersistantStorage.getProperty(Constants.CURRENT_COUNT_CARD) != null) {
            currentCountCards = Integer.valueOf(PersistantStorage.getProperty(Constants.CURRENT_COUNT_CARD)) + 1;
        }
        PersistantStorage.addProperty(Constants.CURRENT_COUNT_CARD, String.valueOf(currentCountCards));

        dba_init.close();
    }

    public void updateCardByIdCard(Context context, int idCard, int idTopic) {
        InitalDatabaseHandler dba_init = new InitalDatabaseHandler(context);
        DatabaseHandler dba_data = new DatabaseHandler(context);

        dba_init.updateCardTopicByIdCardTopic(idCard,
                idTopic,
                dba_data.getTopicNameById(idTopic));

        dba_init.close();
        dba_data.close();
    }

    public void deleteCardByIdCard(Context context, int idCard) {
        InitalDatabaseHandler dba_init = new InitalDatabaseHandler(context);

        dba_init.deleteCardTopicByIdCardTopic(idCard);

        int currentCountCards = 1;
        PersistantStorage.init(context);
        if (PersistantStorage.getProperty(Constants.CURRENT_COUNT_CARD) != null) {
            currentCountCards = Integer.valueOf(PersistantStorage.getProperty(Constants.CURRENT_COUNT_CARD)) - 1;
        }
        PersistantStorage.addProperty(Constants.CURRENT_COUNT_CARD, String.valueOf(currentCountCards));

        dba_init.close();
    }
}

