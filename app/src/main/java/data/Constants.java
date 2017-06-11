package data;

/**
 * Created by zigin on 20.09.2016.
 */
public class Constants {
    public static final String DATABASE_NAME = "Ideographic.db3";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_EXP_NAME = "Expressions";
    public static final String EXP_TEXT = "ExText";
    public static final String EXP_PARENT_ID = "IdTopic";
    public static final String TABLE_TOPIC_NAME = "Topics";
    public static final String TOPIC_TEXT = "TopicText";
    public static final String TOPIC_PARENT_ID = "IdParent";
    public static final String TOPIC_LABELS = "TopicLabels";
    public static final String KEY_ID = "_id";
    public static final String LOG_TAG = "Database Handler";

    public static final String TOPICS_ROOT_NAME = "Topics";

    public static final String BUNDLE_ID_TOPIC = "idtopicbundle";
    public static final String EXTRA_TOPICS_OPEN_TABS = "topicopentabs";

    public static final String INITAL_DATABASE_NAME = "Initaldb.db3";
    public static final int INITAL_DATABASE_VERSION = 1;

    public static final String RECENT_TABLE_NAME = "RecentTopics";
    public static final String RECENT_TOPIC_TEXT = "TextTopic";
    public static final String RECENT_TOPIC_ID = "IdTopic";
    public static final String RECENT_TOPIC_WEIGHT = "WeightTopic";

    public static final String FAVORITE_TABLE_NAME = "FavoriteExpTable";
    public static final String FAVORITE_EXP_TEXT = "FavoriteTextExp";
    public static final String FAVORITE_EXP_ID = "FavoriteIdExp";
    public static final String FAVORITE_PARENT_TOPIC_ID = "FavoriteParentTopicId";

    public static final String STATISTIC_TABLE_NAME = "StatisticTopicsTable";
    public static final String STATISTIC_TOPIC_TEXT = "StatisticTextTopic";
    public static final String STATISTIC_TOPIC_ID = "StatisticIdTopic";
    public static final String STATISTIC_TOPIC_COUNTER = "StatisticCounterTopic";

    public static final int IMAGE_TYPE_TOPIC_BRANCH = 1;
    public static final int IMAGE_TYPE_TOPIC_LEAF = 2;
    public static final int IMAGE_TYPE_EXP = 3;


}
