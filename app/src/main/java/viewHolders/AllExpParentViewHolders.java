package viewHolders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import io.github.ziginsider.ideographicapp.R;

/**
 * Created by zigin on 12.12.2016.
 */

public class AllExpParentViewHolders extends ParentViewHolder {

    public TextView textExp;
    public ImageButton _imageButton;
    public int idParentTopic;

    public AllExpParentViewHolders(View itemView) {

        super(itemView);
        textExp = (TextView) itemView.findViewById(R.id.text_all_exp_parent);
        _imageButton = (ImageButton) itemView.findViewById(R.id.img_expandable);
        idParentTopic = 0;
    }
}
