package com.ted.mosaicapp;

import android.view.MotionEvent;
import android.view.View;
import com.ted.mosaicapp.Blur_View;

final class TouchListener implements View.OnTouchListener {
    final /* synthetic */ Blur_View this$0;

    public TouchListener(Blur_View blur_View) {
        this.this$0 = blur_View;
    }

    

    public boolean onTouch(View view, MotionEvent motionEvent) {
        motionEvent.getX();
        motionEvent.getY();
        switch (motionEvent.getAction() & 255) {
            case 0:
                Blur_View.access$002(this.this$0, 1);
                Blur_View.access$100(this.this$0, motionEvent);
                break;
            case 1:
                Blur_View.access$002(this.this$0, 0);
                break;
            case 2:
                if (Blur_View.access$000(this.this$0) != 0) {
                    Blur_View.access$300(this.this$0, motionEvent);
                    break;
                }
                break;
            case 5:
                if (Blur_View.access$000(this.this$0) != 2) {
                    Blur_View.access$002(this.this$0, 2);
                    Blur_View.access$200(this.this$0, motionEvent);
                    break;
                }
                break;
            case 6:
                Blur_View.access$002(this.this$0, 0);
                break;
        }
        return true;
    }
}

