package io.github.ziginsider.ideographicapp;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.ckenergy.stackcard.stackcardlayoutmanager.StackCardLayoutManager;
import com.ckenergy.stackcard.stackcardlayoutmanager.StackCardPostLayout;

import data.RecyclerViewCardStackAdapter;

public class CardStackActivity extends BaseCardStackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_stack);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_stack_list);

        StackCardLayoutManager stackCardLayoutManager =
                new StackCardLayoutManager
                        (StackCardLayoutManager.VERTICAL,
                                false,
                                new StackCardPostLayout());
        stackCardLayoutManager.setStackOrder(StackCardLayoutManager.OUT_STACK_ORDER);
        stackCardLayoutManager.setNumberOrder(StackCardLayoutManager.NEGATIVE_ORDER);
        RecyclerViewCardStackAdapter adapter = new RecyclerViewCardStackAdapter(20);

        initRecyclerView(recyclerView, stackCardLayoutManager, adapter);
    }
}
