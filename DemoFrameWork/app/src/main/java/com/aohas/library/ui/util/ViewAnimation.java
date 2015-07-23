package com.aohas.library.ui.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.aohas.library.Library;

/**
 * Created by liuyu on 14-5-11.
 */
public class ViewAnimation {
    public static void changeViewVorG(final View view, final int animOpen, final int animClose, boolean isToVisible) {
        int anim;
        final int visibleOrGone;
        if(isToVisible && view.getVisibility() == View.VISIBLE){
            return;
        }
        if(!isToVisible && (view.getVisibility() == View.GONE || view.getVisibility() == View.INVISIBLE)){
            return;
        }
        if (isToVisible) {
            anim = animOpen;
            visibleOrGone = View.VISIBLE;
        } else {
            anim = animClose;
            visibleOrGone = View.GONE;
        }

        Animation animation = AnimationUtils.loadAnimation(Library.context, anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(visibleOrGone);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        view.startAnimation(animation);
    }
}
