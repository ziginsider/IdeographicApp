package data;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.ziginsider.ideographicapp.R;
import model.Expressions;


/**
 * Created by zigin on 27.09.2016.
 */

public class CustomListViewExpAdapter extends ArrayAdapter<Expressions> {

    private int layoutResourse;
    private Activity activity;
    private ArrayList<Expressions> ExpList = new ArrayList<>();

    public CustomListViewExpAdapter(Activity act, int resource, ArrayList<Expressions> data) {
        super(act, resource, data);
        layoutResourse = resource;
        activity = act;
        ExpList = data;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public int getCount() {
        return ExpList.size();
    }

    @Nullable
    @Override
    public Expressions getItem(int position) {
        return ExpList.get(position);
    }

    @Override
    public int getPosition(Expressions item) {
        return super.getPosition(item);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewExpHolder holder = null;

        if (row == null || (row.getTag() == null)) {

            LayoutInflater inflater = LayoutInflater.from(activity);
            row = inflater.inflate(layoutResourse, parent, false);

            holder = new ViewExpHolder();

            holder.ExpText = (TextView) row.findViewById(R.id.txt_view_exp);

            row.setTag(holder);

        } else {

            holder = (ViewExpHolder) row.getTag();
        }

        holder.exp = getItem(position);

        holder.ExpText.setText(holder.exp.getExpText());

        return row;
    }

    public class ViewExpHolder {
        Expressions exp;
        TextView ExpText;

    }
}
