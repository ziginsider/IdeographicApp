package data;

import android.content.Context;

import java.util.ArrayList;

import model.RecentTopics;

/**
 * Created by zigin on 08.11.2016.
 */

public class AsyncProvider {

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
}

