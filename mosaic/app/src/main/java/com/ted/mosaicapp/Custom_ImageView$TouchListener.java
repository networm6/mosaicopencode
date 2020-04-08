package com.ted.mosaicapp;


import android.view.MotionEvent;
import android.view.View;
import com.ted.mosaicapp.Custom_ImageView;

final class Custom_ImageView$TouchListener implements View.OnTouchListener {
    final /* synthetic */ Custom_ImageView this$0;

    public Custom_ImageView$TouchListener(Custom_ImageView custom_ImageView) {
        this.this$0 = custom_ImageView;
    }

    

    public boolean onTouch(View view, MotionEvent motionEvent) {
        motionEvent.getX();
        motionEvent.getY();
        switch (motionEvent.getAction() & 255) {
            case 0:
                Custom_ImageView.access$002(this.this$0, 1);
                Custom_ImageView.access$100(this.this$0, motionEvent);
                break;
            case 1:
                Custom_ImageView.access$002(this.this$0, 0);
                break;
            case 2:
                if (Custom_ImageView.access$000(this.this$0) != 0) {
                    Custom_ImageView.access$300(this.this$0, motionEvent);
                    break;
                }
                break;
            case 5:
                if (Custom_ImageView.access$000(this.this$0) != 2) {
                    Custom_ImageView.access$002(this.this$0, 2);
                    Custom_ImageView.access$200(this.this$0, motionEvent);
                    break;
                }
                break;
            case 6:
                Custom_ImageView.access$002(this.this$0, 0);
                break;
        }
        return true;
    }
}

