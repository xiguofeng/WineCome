
package com.xgf.winecome.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xgf.winecome.R;

public class CustomProgressDialog2 extends Dialog {

    Context context;

    public CustomProgressDialog2(Context context) {
        super(context, R.style.loading_dialog);
        this.context = context;
        this.setCanceledOnTouchOutside(false);
        init();
    }

    void init() {

        LinearLayout contentView = new LinearLayout(context);
        contentView.setMinimumHeight(60);
        contentView.setGravity(Gravity.CENTER);
        contentView.setOrientation(LinearLayout.HORIZONTAL);

        ImageView image = new ImageView(context);
        image.setImageResource(R.drawable.wait);
        Animation anim = AnimationUtils.loadAnimation(context,
                R.anim.rotate_repeat);
        LinearInterpolator lir = new LinearInterpolator();
        anim.setInterpolator(lir);
        image.setAnimation(anim);

        contentView.addView(image);
        setContentView(contentView);

    }

}
