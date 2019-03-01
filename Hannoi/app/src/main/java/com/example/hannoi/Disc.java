package com.example.hannoi;

import android.content.Context;
import android.widget.Button;

/**
 * Created by lenovo on 2019/2/22.
 */
public class Disc extends Button{

    public Disc(Context context) {
        super(context);
    }
    private  int number;
    private  TowerPoint point;
    private  int W;
    private  int H;

    public int getW() {
        return W;
    }

    public int getNumber() {
        return number;
    }

    public void setW(int w) {
        W = w;
    }

    public int getH() {
        return H;
    }

    public void setH(int h) {
        H = h;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public TowerPoint getPoint() {
        return point;
    }

    public void setPoint(TowerPoint point) {
        this.point = point;
    }

}
