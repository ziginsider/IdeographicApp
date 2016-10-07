package data;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.ziginsider.ideographicapp.R;

/**
 * Created by zigin on 29.09.2016.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private final ArrayList<String> mFragmentTitleList = new ArrayList<>();
    Context context;
    ViewPager viewPager;
    TabLayout tabLayout;

    public ViewPagerAdapter(FragmentManager manager, Context context, ViewPager viewPager,
                            TabLayout tabLayout) {
        super(manager);
        this.context = context;
        this.viewPager = viewPager;
        this.tabLayout = tabLayout;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void removeFragment(int position) {
        removeTab(position);
        Fragment fragment = mFragmentList.get(position);
        mFragmentList.remove(fragment);
        mFragmentTitleList.remove(position);
        destroyFragmentView(viewPager, position, fragment);
        notifyDataSetChanged();
    }

    public View getTabView(final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab_item, null);
        TextView tabItemName = (TextView) view.findViewById(R.id.text_tab_item);
        ImageView tabItemAvatar =
                (ImageView) view.findViewById(R.id.image_tab_item);
//        ImageButton remove = (ImageButton) view.findViewById(R.id.imageButtonRemove);
//        remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("Remove", "Remove");
//                removeFragment(position);
//            }
//        });

        tabItemName.setText(mFragmentTitleList.get(position));
        //tabItemName.setTextColor(context.getResources().getColor(android.R.color.background_light));

        switch (mFragmentTitleList.get(position)) {
            case "Man":
                tabItemAvatar.setImageResource(R.drawable.ic_man);
                break;
            case "Impact":
                tabItemAvatar.setImageResource(R.drawable.check);
                break;
            case "Character":
                tabItemAvatar.setImageResource(R.drawable.volcano);
                break;
            case "Work":
                tabItemAvatar.setImageResource(R.drawable.lampe);
                break;
            default:
                //tabItemAvatar.setImageResource(R.drawable.ic_merge_right);
                break;
        }

        return view;
    }

    public void destroyFragmentView(ViewGroup container, int position, Object object) {
        FragmentManager manager = ((Fragment) object).getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove((Fragment) object);
        trans.commit();
    }

    public void removeTab(int position) {
        if (tabLayout.getChildCount() > 0) {
            tabLayout.removeTabAt(position);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

}
