package com.ted.mosaicapp;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class Blur_View extends ImageView {
    private static float sx;
    private static float sy;
    public int Blur = 1;
    private final int DRAG = 1;
    private float MIN_ZOOM_POINTER_DISTANCE = 5.0f;
    private final int NONE = 0;
    private final int ZOOM = 2;
    PointF a;
    PointF b;
    private boolean is_touch = false;
    private int mode = 0;
    private double originalDistance = 0.0d;
    private float originalX = 0.0f;
    private float originalY = 0.0f;
    private Paint p = new Paint();

    public Blur_View(Context context) {
        super(context);
        init();
    }

    public Blur_View(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public Blur_View(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    static /* synthetic */ int access$000(Blur_View blur_View) {
        return blur_View.mode;
    }

    static /* synthetic */ int access$002(Blur_View blur_View, int i) {
        blur_View.mode = i;
        return i;
    }

    static /* synthetic */ void access$100(Blur_View blur_View, MotionEvent motionEvent) {
        blur_View.touchDown(motionEvent);
    }

    static /* synthetic */ void access$200(Blur_View blur_View, MotionEvent motionEvent) {
        blur_View.touchZoomDown(motionEvent);
    }

    static /* synthetic */ void access$300(Blur_View blur_View, MotionEvent motionEvent) {
        blur_View.touchMove(motionEvent);
    }

    static /* synthetic */ boolean access$602(Blur_View blur_View, boolean z) {
        blur_View.is_touch = z;
        return z;
    }

    static /* synthetic */ void access$700(Blur_View blur_View) {
        blur_View.process();
    }

    public static int[] getBitmapPositionInsideImageView(ImageView imageView) {
        int[] iArr = new int[4];
        if (imageView == null || imageView.getDrawable() == null) {
            return iArr;
        }
        float[] fArr = new float[9];
        imageView.getImageMatrix().getValues(fArr);
        float f = fArr[0];
        float f2 = fArr[4];
        sx = f;
        sy = f2;
        int intrinsicWidth = imageView.getDrawable().getIntrinsicWidth();
        int intrinsicHeight = imageView.getDrawable().getIntrinsicHeight();
        int round = Math.round(((float) intrinsicWidth) * f);
        int round2 = Math.round(((float) intrinsicHeight) * f2);
        iArr[2] = round;
        iArr[3] = round2;
        iArr[0] = (imageView.getWidth() - round) / 2;
        iArr[1] = (imageView.getHeight() - round2) / 2;
        return iArr;
    }

    private Double getDistanceOfPointers(MotionEvent motionEvent) {
        float x = motionEvent.getX(0) - motionEvent.getX(1);
        float y = motionEvent.getY(0) - motionEvent.getY(1);
        return Double.valueOf(Math.sqrt((double) ((x * x) + (y * y))));
    }

    private void init() {
        setOnTouchListener(new Blur_View$My_Touch(this));
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setColor(-65536);
    }

    private void process() {
        if (this.a != null && this.b != null) {
            float f = (this.a.x < this.b.x ? this.a : this.b).x;
            float f2 = (this.a.x < this.b.x ? this.b : this.a).x;
            float f3 = (this.a.y < this.b.y ? this.a : this.b).y;
            float f4 = (this.a.y < this.b.y ? this.b : this.a).y;
            int[] bitmapPositionInsideImageView = getBitmapPositionInsideImageView(this);
            float f5 = f - ((float) bitmapPositionInsideImageView[0]);
            float f6 = f4 - ((float) bitmapPositionInsideImageView[1]);
            float f7 = f5 / sx;
            float f8 = (f2 - ((float) bitmapPositionInsideImageView[0])) / sx;
            float f9 = (f3 - ((float) bitmapPositionInsideImageView[1])) / sy;
            float f10 = f6 / sy;
            Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
            if (f7 < 0.0f) {
                f7 = 0.0f;
            }
            if (f9 < 0.0f) {
                f9 = 0.0f;
            }
            if (f8 > ((float) bitmap.getWidth())) {
                f8 = (float) bitmap.getWidth();
            }
            if (f10 > ((float) bitmap.getHeight())) {
                f10 = (float) bitmap.getHeight();
            }
            if (f9 > ((float) bitmap.getHeight()) || f10 < 0.0f) {
                Log.e("TTT", "Event Cancel");
                return;
            }
            int i = (int) ((f8 - f7) + 1.0f);
            int i2 = (int) ((f10 - f9) + 1.0f);
            Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            Rect rect = new Rect((int) f7, (int) f9, (int) f8, (int) f10);
            Rect rect2 = new Rect(0, 0, i, i2);
            canvas.drawBitmap(bitmap, rect, rect2, new Paint());
            Bitmap blurBitmap = blurBitmap(createBitmap, this.Blur);
            createBitmap.recycle();
            new Canvas(bitmap).drawBitmap(blurBitmap, rect2, rect, new Paint());
            blurBitmap.recycle();
        }
    }

    private boolean scaleImage(double d) {
        double d2 = d / this.originalDistance;
        if (d2 > 1.0d) {
            double width = (double) getWidth();
            double d3 = d2 - 1.0d;
            Double.isNaN(width);
            double d4 = (width * d3) / 2.0d;
            double height = (double) getHeight();
            Double.isNaN(height);
            double d5 = (height * d3) / 2.0d;
            double left = (double) getLeft();
            Double.isNaN(left);
            double right = (double) getRight();
            Double.isNaN(right);
            int i = (int) (right + d4);
            double top = (double) getTop();
            Double.isNaN(top);
            double bottom = (double) getBottom();
            Double.isNaN(bottom);
            setFrame((int) (left - d4), (int) (top - d5), i, (int) (bottom + d5));
            return true;
        } else if (d2 >= 1.0d) {
            return true;
        } else {
            double width2 = (double) getWidth();
            double d6 = 1.0d - d2;
            Double.isNaN(width2);
            double d7 = (width2 * d6) / 2.0d;
            double height2 = (double) getHeight();
            Double.isNaN(height2);
            double d8 = (height2 * d6) / 2.0d;
            double left2 = (double) getLeft();
            Double.isNaN(left2);
            double right2 = (double) getRight();
            Double.isNaN(right2);
            double top2 = (double) getTop();
            Double.isNaN(top2);
            double bottom2 = (double) getBottom();
            Double.isNaN(bottom2);
            setFrame((int) (left2 + d7), (int) (top2 + d8), (int) (right2 - d7), (int) (bottom2 - d8));
            return true;
        }
    }

    private void touchDown(MotionEvent motionEvent) {
        this.originalX = motionEvent.getX();
        this.originalY = motionEvent.getY();
    }

    private void touchMove(MotionEvent motionEvent) {
        float x = motionEvent.getX() - this.originalX;
        float y = motionEvent.getY() - this.originalY;
        if (this.mode == 1) {
            int left = getLeft();
            int right = getRight();
            int i = (int) (((float) right) + x);
            setFrame((int) (((float) left) + x), (int) (((float) getTop()) + y), i, (int) (((float) getBottom()) + y));
        } else if (this.mode == 2) {
            double doubleValue = getDistanceOfPointers(motionEvent).doubleValue();
            if (Math.abs(doubleValue - this.originalDistance) > ((double) this.MIN_ZOOM_POINTER_DISTANCE)) {
                scaleImage(doubleValue);
                this.originalDistance = doubleValue;
            }
            this.originalDistance = doubleValue;
        }
    }

    private void touchZoomDown(MotionEvent motionEvent) {
        this.originalDistance = getDistanceOfPointers(motionEvent).doubleValue();
    }

    public Bitmap blurBitmap(Bitmap bitmap, int i) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        RenderScript create = RenderScript.create(getContext().getApplicationContext());
        ScriptIntrinsicBlur create2 = ScriptIntrinsicBlur.create(create, Element.U8_4(create));
        Allocation createFromBitmap = Allocation.createFromBitmap(create, bitmap);
        Allocation createFromBitmap2 = Allocation.createFromBitmap(create, createBitmap);
        create2.setRadius((float) i);
        create2.setInput(createFromBitmap);
        create2.forEach(createFromBitmap2);
        createFromBitmap2.copyTo(createBitmap);
        bitmap.recycle();
        create.destroy();
        return createBitmap;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.is_touch) {
            canvas.drawRect((this.a.x < this.b.x ? this.a : this.b).x, (this.a.y < this.b.y ? this.a : this.b).y, (this.a.x < this.b.x ? this.b : this.a).x, (this.a.y < this.b.y ? this.b : this.a).y, this.p);
        }
    }

    public void set_mode(int i) {
        if (i == 1) {
            setOnTouchListener(new TouchListener(this));
        } else {
            setOnTouchListener(new Blur_View$My_Touch(this));
        }
    }
}

