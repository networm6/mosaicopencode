package com.ted.mosaicapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;

public class ConvexHull {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private Stack<Point2D> hull = new Stack<>();

    static {
    }

    public ConvexHull(Point2D[] point2DArr) {
        Point2D point2D;
        if (point2DArr == null) {
            throw new IllegalArgumentException("argument is null");
        } else if (point2DArr.length != 0) {
            int length = point2DArr.length;
            Point2D[] point2DArr2 = new Point2D[length];
            int i = 0;
            while (i < length) {
                if (point2DArr[i] != null) {
                    point2DArr2[i] = point2DArr[i];
                    i++;
                } else {
                    throw new IllegalArgumentException("points[" + i + "] is null");
                }
            }
            Arrays.sort(point2DArr2);
            int i2 = 1;
            Arrays.sort(point2DArr2, 1, length, point2DArr2[0].polarOrder());
            this.hull.push(point2DArr2[0]);
            while (i2 < length && point2DArr2[0].equals(point2DArr2[i2])) {
                i2++;
            }
            if (i2 != length) {
                int i3 = i2 + 1;
                while (i3 < length && Point2D.ccw(point2DArr2[0], point2DArr2[i2], point2DArr2[i3]) == 0) {
                    i3++;
                }
                this.hull.push(point2DArr2[i3 - 1]);
                while (i3 < length) {
                    Object pop = this.hull.pop();
                    while (true) {
                        point2D = (Point2D) pop;
                        if (Point2D.ccw(this.hull.peek(), point2D, point2DArr2[i3]) > 0) {
                            break;
                        }
                        pop = this.hull.pop();
                    }
                    this.hull.push(point2D);
                    this.hull.push(point2DArr2[i3]);
                    i3++;
                }
            }
        } else {
            throw new IllegalArgumentException("array is of length 0");
        }
    }

    private Iterable<Point2D> hull() {
        Stack stack = new Stack();
        Iterator it = this.hull.iterator();
        while (it.hasNext()) {
            stack.push((Point2D) it.next());
        }
        return stack;
    }

    private boolean isConvex() {
        int size = this.hull.size();
        if (size <= 2) {
            return true;
        }
        Point2D[] point2DArr = new Point2D[size];
        int i = 0;
        for (Point2D point2D : hull()) {
            point2DArr[i] = point2D;
            i++;
        }
        int i2 = 0;
        while (i2 < size) {
            int i3 = i2 + 1;
            if (Point2D.ccw(point2DArr[i2], point2DArr[i3 % size], point2DArr[(i2 + 2) % size]) <= 0) {
                return false;
            }
            i2 = i3;
        }
        return true;
    }

    public ArrayList<Float> get_list() {
        ArrayList<Float> arrayList = new ArrayList<>();
        Iterator it = this.hull.iterator();
        while (it.hasNext()) {
            Point2D point2D = (Point2D) it.next();
            arrayList.add(Float.valueOf((float) point2D.x()));
            arrayList.add(Float.valueOf((float) point2D.y()));
        }
        return arrayList;
    }
}

