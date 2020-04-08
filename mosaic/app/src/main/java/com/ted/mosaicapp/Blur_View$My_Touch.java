package com.ted.mosaicapp;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import com.ted.mosaicapp.Blur_View;

final class Blur_View$My_Touch implements View.OnTouchListener {
    final /* synthetic */ Blur_View this$0;

    

    public Blur_View$My_Touch(Blur_View blur_View) {
        
		this.this$0 = blur_View;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        switch (motionEvent.getAction() & 255) {
            case 0:
                this.this$0.a = new PointF(x, y);
                Blur_View.access$602(this.this$0, true);
                break;
            case 1:
                Blur_View.access$700(this.this$0);
                Blur_View.access$602(this.this$0, false);
                this.this$0.invalidate();
                break;
            case 2:
                this.this$0.b = new PointF(x, y);
                this.this$0.invalidate();
                break;
        }
        return true;
    }
}

