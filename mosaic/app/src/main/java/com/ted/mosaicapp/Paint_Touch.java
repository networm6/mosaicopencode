package com.ted.mosaicapp;

import android.app.ProgressDialog;
import android.view.MotionEvent;
import android.view.View;
import com.ted.mosaicapp.Custom_ImageView;

final class Paint_Touch implements View.OnTouchListener {
    final /* synthetic */ Custom_ImageView this$0;

    public Paint_Touch(Custom_ImageView custom_ImageView) {
        this.this$0 = custom_ImageView;
    }

    

    public boolean onTouch(View view, MotionEvent motionEvent) {
        this.this$0.update_rect();
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (x < ((float) Custom_ImageView.access$600(this.this$0)[0]) || x > ((float) (Custom_ImageView.access$600(this.this$0)[0] + Custom_ImageView.access$600(this.this$0)[2])) || y < ((float) Custom_ImageView.access$600(this.this$0)[1]) || y > ((float) (Custom_ImageView.access$600(this.this$0)[1] + Custom_ImageView.access$600(this.this$0)[3]))) {
            return true;
        }
        switch (motionEvent.getAction() & 255) {
            case 0:
            case 2:
                Custom_ImageView.access$700(this.this$0).add(Float.valueOf(x));
                Custom_ImageView.access$700(this.this$0).add(Float.valueOf(y));
                this.this$0.invalidate();
                break;
            case 1:
                Custom_ImageView.access$700(this.this$0).add(Float.valueOf(x));
                Custom_ImageView.access$700(this.this$0).add(Float.valueOf(y));
              //  Custom_ImageView.access$802(this.this$0, ProgressDialog.show(this.this$0.ctn, this.this$0.message1, this.this$0.message2, true));
                new Thread(new Custom_ImageView$Paint_Touch$1(this)).start();
                break;
        }
        return true;
    }
}

