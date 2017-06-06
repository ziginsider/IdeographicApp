package viewHolders;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import io.github.ziginsider.ideographicapp.R;

/**
 * Created by zigin on 12.12.2016.
 */

public class AllExpChildViewHolders extends ChildViewHolder {

    public TextView textTranscription, textDefenition, textParentTopic;
    public int idParentTopic;

    public  AllExpChildViewHolders(View itemView) {

        super(itemView);
        textTranscription = (TextView) itemView.findViewById(R.id.trans_all_exp_child);
        textDefenition = (TextView) itemView.findViewById(R.id.def_all_exp_child);
        textParentTopic = (TextView) itemView.findViewById(R.id.topic_all_exp_child);
    }
}
