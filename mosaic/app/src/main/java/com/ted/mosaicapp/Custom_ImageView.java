package com.ted.mosaicapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import java.util.ArrayList;

public class Custom_ImageView extends ImageView {
    private final int DRAG = 1;
    private float MIN_ZOOM_POINTER_DISTANCE = 5.0f;
    private final int NONE = 0;
    private final int ZOOM = 2;
    public int action = 0;
    public Activity ctn = null;
    private ProgressDialog dialog;
    public int grid_h = 4;
    public int grid_w = 4;
    private ArrayList<Float> list = new ArrayList<>();
    public String message1;
    public String message2;
    private int mode = 0;
    private double originalDistance = 0.0d;
    private float originalX = 0.0f;
    private float originalY = 0.0f;
    private Paint paint = new Paint();
    public int[] pixels;
    public int stride;
    private int[] temp_rect;

    public Custom_ImageView(Context context) {
        super(context);
        init();
    }

    public Custom_ImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public Custom_ImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    static /* synthetic */ int access$000(Custom_ImageView custom_ImageView) {
        return custom_ImageView.mode;
    }

    static /* synthetic */ int access$002(Custom_ImageView custom_ImageView, int i) {
        custom_ImageView.mode = i;
        return i;
    }

    static /* synthetic */ void access$100(Custom_ImageView custom_ImageView, MotionEvent motionEvent) {
        custom_ImageView.touchDown(motionEvent);
    }

    static /* synthetic */ void access$200(Custom_ImageView custom_ImageView, MotionEvent motionEvent) {
        custom_ImageView.touchZoomDown(motionEvent);
    }

    static /* synthetic */ void access$300(Custom_ImageView custom_ImageView, MotionEvent motionEvent) {
        custom_ImageView.touchMove(motionEvent);
    }

    static /* synthetic */ int[] access$600(Custom_ImageView custom_ImageView) {
        return custom_ImageView.temp_rect;
    }

    static /* synthetic */ ArrayList access$700(Custom_ImageView custom_ImageView) {
        return custom_ImageView.list;
    }

    static /* synthetic */ ProgressDialog access$800(Custom_ImageView custom_ImageView) {
        return custom_ImageView.dialog;
    }

    static /* synthetic */ ProgressDialog access$802(Custom_ImageView custom_ImageView, ProgressDialog progressDialog) {
        custom_ImageView.dialog = progressDialog;
        return progressDialog;
    }

    static /* synthetic */ void access$900(Custom_ImageView custom_ImageView) {
        custom_ImageView.mosaic_action();
    }

    private boolean contains(Point point, Point[] pointArr) {
        Point point2 = point;
        Point[] pointArr2 = pointArr;
        int length = pointArr2.length - 1;
        boolean z = false;
        for (int i = 0; i < pointArr2.length; i++) {
            if ((pointArr2[i].y > point2.y) != (pointArr2[length].y > point2.y) && point2.x < (((pointArr2[length].x - pointArr2[i].x) * (point2.y - pointArr2[i].y)) / (pointArr2[length].y - pointArr2[i].y)) + pointArr2[i].x) {
                z = !z;
            }
            length = i;
        }
        return z;
    }

    private void convex() {
        if (this.list.size() > 0) {
            Point2D[] point2DArr = new Point2D[(this.list.size() / 2)];
            int i = 0;
            for (int i2 = 0; i2 < this.list.size(); i2 += 2) {
                point2DArr[i] = new Point2D((double) this.list.get(i2).floatValue(), (double) this.list.get(i2 + 1).floatValue());
                i++;
            }
            ConvexHull convexHull = new ConvexHull(point2DArr);
            this.list.clear();
            this.list = convexHull.get_list();
        }
    }

    private Bitmap do_mosaic(Bitmap bitmap, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        Bitmap bitmap2 = bitmap;
        Point[] pointArr = new Point[(this.list.size() / 2)];
        int i7 = 0;
        int i8 = 0;
        while (i7 < this.list.size()) {
            Point point = new Point(this);
            point.x = (double) this.list.get(i7).floatValue();
            point.y = (double) this.list.get(i7 + 1).floatValue();
            pointArr[i8] = point;
            i7 += 2;
            i8++;
        }
        int i9 = i3;
        int i10 = 0;
        while (i10 < i9) {
            int i11 = i4;
            int i12 = 0;
            while (i12 < i11) {
                int i13 = i + i10;
                if (i13 >= 0 && i13 < bitmap.getWidth() && (i5 = i2 + i12) >= 0 && i5 < bitmap.getHeight()) {
                    int pixel = bitmap2.getPixel(i13, i5);
                    int i14 = 0;
                    while (i14 < this.grid_h) {
                        int i15 = 0;
                        while (i15 < this.grid_w) {
                            int i16 = i13 + i15;
                            int i17 = i5 + i14;
                            if (i16 >= bitmap.getWidth() || i17 >= bitmap.getHeight() || i16 < 0 || i17 < 0) {
                                i6 = i13;
                            } else {
                                Point point2 = new Point(this);
                                i6 = i13;
                                point2.x = (double) i16;
                                point2.y = (double) i17;
                                if (contains(point2, pointArr)) {
                                    this.pixels[(i17 * this.stride) + i16] = pixel;
                                }
                            }
                            i15++;
                            i13 = i6;
                            int i18 = i3;
                        }
                        int i19 = i13;
                        i14++;
                        int i20 = i3;
                    }
                }
                i12 += this.grid_h;
                int i21 = i3;
            }
            i10 += this.grid_w;
            i9 = i3;
        }
        bitmap.setPixels(this.pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return bitmap2;
    }

    private Bitmap fill(Bitmap bitmap, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        Point[] pointArr = new Point[(this.list.size() / 2)];
        int i7 = 0;
        int i8 = 0;
        while (i7 < this.list.size()) {
            Point point = new Point(this);
            point.x = (double) this.list.get(i7).floatValue();
            point.y = (double) this.list.get(i7 + 1).floatValue();
            pointArr[i8] = point;
            i7 += 2;
            i8++;
        }
        int argb = Color.argb(255, 0, 0, 0);
        int i9 = i3;
        for (int i10 = 0; i10 < i9; i10 += this.grid_w) {
            int i11 = i4;
            for (int i12 = 0; i12 < i11; i12 += this.grid_h) {
                int i13 = i + i10;
                if (i13 >= 0 && i13 < bitmap.getWidth() && (i5 = i2 + i12) >= 0 && i5 < bitmap.getHeight()) {
                    for (int i14 = 0; i14 < this.grid_h; i14++) {
                        int i15 = 0;
                        while (i15 < this.grid_w) {
                            int i16 = i13 + i15;
                            int i17 = i5 + i14;
                            if (i16 >= bitmap.getWidth() || i17 >= bitmap.getHeight() || i16 < 0 || i17 < 0) {
                                i6 = i13;
                            } else {
                                Point point2 = new Point(this);
                                i6 = i13;
                                point2.x = (double) i16;
                                point2.y = (double) i17;
                                if (contains(point2, pointArr)) {
                                    this.pixels[(i17 * this.stride) + i16] = argb;
                                }
                            }
                            i15++;
                            i13 = i6;
                        }
                        int i18 = i13;
                    }
                }
            }
        }
        bitmap.setPixels(this.pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return bitmap;
    }

    private int find_max_x() {
        float floatValue = this.list.get(0).floatValue();
        for (int i = 2; i < this.list.size(); i += 2) {
            if (floatValue < this.list.get(i).floatValue()) {
                floatValue = this.list.get(i).floatValue();
            }
        }
        return (int) floatValue;
    }

    private int find_max_y() {
        float floatValue = this.list.get(1).floatValue();
        for (int i = 3; i < this.list.size(); i += 2) {
            if (floatValue < this.list.get(i).floatValue()) {
                floatValue = this.list.get(i).floatValue();
            }
        }
        return (int) floatValue;
    }

    private int find_min_x() {
        float floatValue = this.list.get(0).floatValue();
        for (int i = 2; i < this.list.size(); i += 2) {
            if (floatValue > this.list.get(i).floatValue()) {
                floatValue = this.list.get(i).floatValue();
            }
        }
        return (int) floatValue;
    }

    private int find_min_y() {
        float floatValue = this.list.get(1).floatValue();
        for (int i = 3; i < this.list.size(); i += 2) {
            if (floatValue > this.list.get(i).floatValue()) {
                floatValue = this.list.get(i).floatValue();
            }
        }
        return (int) floatValue;
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
        set_mode(0);
        this.paint = new Paint(1);
        this.paint.setColor(-65536);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(2.0f);
    }

    private void mosaic_action() {
        convex();
        Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
        float width = ((float) bitmap.getWidth()) / ((float) this.temp_rect[2]);
        float height = ((float) bitmap.getHeight()) / ((float) this.temp_rect[3]);
        for (int i = 0; i < this.list.size(); i += 2) {
            int i2 = i + 1;
            this.list.set(i, Float.valueOf((this.list.get(i).floatValue() - ((float) this.temp_rect[0])) * width));
            this.list.set(i2, Float.valueOf((this.list.get(i2).floatValue() - ((float) this.temp_rect[1])) * height));
        }
        int find_min_x = find_min_x();
        int find_max_x = find_max_x();
        int find_min_y = find_min_y();
        int i3 = find_max_x - find_min_x;
        int find_max_y = find_max_y() - find_min_y;
        switch (this.action) {
            case 0:
                do_mosaic(bitmap, find_min_x, find_min_y, i3, find_max_y);
                break;
            case 1:
                fill(bitmap, find_min_x, find_min_y, i3, find_max_y);
                break;
        }
        this.list.clear();
        postInvalidate();
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

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.list.size() > 0) {
            Path path = new Path();
            path.moveTo(this.list.get(0).floatValue(), this.list.get(1).floatValue());
            for (int i = 2; i < this.list.size(); i += 2) {
                path.lineTo(this.list.get(i).floatValue(), this.list.get(i + 1).floatValue());
            }
            canvas.drawPath(path, this.paint);
        }
    }

    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        this.pixels = new int[(bitmap.getWidth() * bitmap.getHeight())];
        this.stride = bitmap.getWidth();
        bitmap.getPixels(this.pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
    }

    public void set_mode(int i) {
        if (i == 1) {
            setOnTouchListener(new Custom_ImageView$TouchListener(this));
        } else {
            setOnTouchListener(new Paint_Touch(this));
        }
    }

    public void update_rect() {
        this.temp_rect = getBitmapPositionInsideImageView(this);
    }
}

