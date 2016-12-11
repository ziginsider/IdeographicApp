package io.github.ziginsider.ideographicapp;

/**
 * Created by zigin on 23.11.2016.
 */

import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class Intro extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        addSlide(AppIntroFragment.newInstance("Welcome!",
                "He believed that he knew now how they had all managed to depart without leaving any trace behind them...",
                R.drawable.ic_intro_1,
                Color.parseColor("#FF03A9F4")
        ));

        addSlide(AppIntroFragment.newInstance("Recent topics...",
                "The dogs had struck something immediately, but before they set out a deputy found, wedged into a split plank on the side of the church, a scrap of paper...",
                R.drawable.ic_intro_2,
                Color.parseColor("#FF1EB2F5")
        ));

        addSlide(AppIntroFragment.newInstance("Favorite expressions...",
                "You won’t find him here. He quit this morning...",
                R.drawable.ic_intro_3,
                Color.parseColor("#FF3EB6EC")
        ));

        addSlide(AppIntroFragment.newInstance("Start work...",
                "He could hear the mop in the back of the hall or maybe the kitchen...",
                R.drawable.ic_intro_4,
                Color.parseColor("#FF5FBFEB")
        ));
    }

    @Override
    public void onSkipPressed() {
        // Do something when users tap on Skip button.
        this.finish();
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        this.finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something when slide is changed
    }
}
