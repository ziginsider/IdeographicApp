package animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by zigin on 16.10.2016.
 */

public class ResizeAnimation extends Animation {
    final int startWidth;
    final int startHeight;
    final int targetWidth;
    final int targetHeight;
    View view;

    public ResizeAnimation(View view, int targetWidth, int targetHeight) {
        this.view = view;
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        startWidth = view.getWidth();
        startHeight = view.getHeight();

    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newWidth = (int) (startWidth + (targetWidth - startWidth) * interpolatedTime);
        int newHeight = (int) (startHeight +(targetHeight -startHeight) * interpolatedTime);
        view.getLayoutParams().width = newWidth;
        view.getLayoutParams().height = newHeight;
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
