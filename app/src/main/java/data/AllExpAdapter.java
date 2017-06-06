package data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

import io.github.ziginsider.ideographicapp.R;
import model.AllExpChild;
import model.AllExpParent;
import viewHolders.AllExpChildViewHolders;
import viewHolders.AllExpParentViewHolders;

/**
 * Created by zigin on 12.12.2016.
 */

public class AllExpAdapter extends ExpandableRecyclerAdapter<AllExpParentViewHolders, AllExpChildViewHolders> {

    LayoutInflater inflater;

    public AllExpAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindChildViewHolder(AllExpChildViewHolders allExpChildViewHolders, int i, Object o) {

        AllExpChild child = (AllExpChild) o;
        allExpChildViewHolders.textTranscription.setText(child.getTextTranscription());
        allExpChildViewHolders.textDefenition.setText(child.getTextDefinition());
        allExpChildViewHolders.textParentTopic.setText(child.getTextParentTopic());
    }

    @Override
    public void onBindParentViewHolder(AllExpParentViewHolders allExpParentViewHolders, int i, Object o) {
        AllExpParent parent = (AllExpParent) o;
        allExpParentViewHolders.textExp.setText(parent.getTextExp());
    }

    @Override
    public AllExpParentViewHolders onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.adapter_all_exp_parent, viewGroup, false);
        return new AllExpParentViewHolders(view);
    }

    @Override
    public AllExpChildViewHolders onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.adapter_all_exp_child, viewGroup, false);
        return new AllExpChildViewHolders(view);
    }
}
