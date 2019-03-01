package com.example.hannoi;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;

/**
 * Created by lenovo on 2019/2/22.
 */
public class Tower {

    private int numOfDisc = 3;
    private Disc[] discs;
    private int maxDiscWidth, minDiscWidth, discHeight;
    private char[] towerName; //其实就是三个柱子的名字，保存一下，可能换
    private TowerPoint[] pointA, pointB, pointC;  //三个柱子上面的点
    private  Context MainContext;
    private AbsoluteLayout layout;
    private int creenW;
    private int creenH;

    final int POINTWH = 60;

    private MouseListener discMouseListener;

    Tower(char[] towerName, Context context, AbsoluteLayout layout, int creenW, int creenH , int numOfDisc){

        this.towerName = towerName;
        this.MainContext = context;
        this.layout = layout;
        this.creenW = creenW;
        this.creenH = creenH;
        this.numOfDisc = numOfDisc;
        discs = new Disc[numOfDisc];
        pointA = new TowerPoint[numOfDisc];
        pointB = new TowerPoint[numOfDisc];
        pointC = new TowerPoint[numOfDisc];
        discMouseListener = new MouseListener(pointA, pointB, pointC,layout); //后面再进行补充
    }

    public int getNumOfDisc() {
        return numOfDisc;
    }
    public void setNumOfDisc(int numOfDisc) {
        this.numOfDisc = numOfDisc;
    }
    public int getMaxDiscWidth() {
        return maxDiscWidth;
    }
    public void setMaxDiscWidth(int maxDiscWidth) {
        this.maxDiscWidth = maxDiscWidth;
    }
    public int getMinDiscWidth() {
        return minDiscWidth;
    }
    public void setMinDiscWidth(int minDiscWidth) {
        this.minDiscWidth = minDiscWidth;
    }
    public int getDiscHeight() {
        return discHeight;
    }
    public void setDiscHeight(int discHeight) {
        this.discHeight = discHeight;
    }
    public Context getMainContext() {
        return MainContext;
    }
    public void setMainContext(Context mainContext) {
        MainContext = mainContext;
    }

    public void putDiscOnTower(){
        int n = (maxDiscWidth - minDiscWidth)/numOfDisc;
        int locationX = 100 + maxDiscWidth / 2 - minDiscWidth / 2;//最小的那个的左上角
        int locationY = (creenH - getNumOfDisc() * getDiscHeight()) / 2; //这个是高度

        for(int i=0;i<discs.length;i++){
            discs[i] = new Disc(MainContext);
            discs[i].setBackgroundColor(Color.GREEN);
            discs[i].setNumber(i);
            int discWidth = minDiscWidth + i * n; //定义宽度
            discs[i].setW(discWidth);
            discs[i].setH(discHeight);
            int X = locationX  - i * n / 2;
            int Y = locationY + i * discHeight;
            discs[i].setX(X);
            discs[i].setY(Y);
            discs[i].setBackgroundResource(R.drawable.disc2);
            //这里实现将每一个disc都添加上监听器
            discs[i].setOnTouchListener(discMouseListener);
        }

        //设置柱子A
        locationX = 100 + maxDiscWidth / 2 - POINTWH / 2;  //减去point的宽度。
        int distanceOfPoint = discHeight / 2;
        for(int i=0;i<pointA.length;i++){
            pointA[i] = new TowerPoint(getMainContext(),locationX,locationY + distanceOfPoint,i);
            distanceOfPoint += discHeight;
        }
        //设置B
        locationX += 100 + maxDiscWidth; //跨过了100
        distanceOfPoint = discHeight / 2;
        for(int i=0;i<pointB.length;i++){
            pointB[i] = new TowerPoint(getMainContext(), locationX, locationY + distanceOfPoint,i);
            distanceOfPoint += discHeight;
        }
        //设置C
        locationX += 100 + maxDiscWidth;
        distanceOfPoint = discHeight / 2;
        for(int i=0;i<pointC.length;i++){
            pointC[i] = new TowerPoint(getMainContext(), locationX, locationY + distanceOfPoint,i);
            distanceOfPoint += discHeight;
        }

        //初始化将disc绑定在pointa上面。
        for(int i=0;i<pointA.length;i++){
            pointA[i].putDisc(discs[i]);
            discs[i].setPoint(pointA[i]);
        }
    }

    public void drawDiscAndPoint(){
        for(int i=0; i<pointA.length;i++){
            //pointA[i].setText(i+"");
            layout.addView(pointA[i], new FrameLayout.LayoutParams(POINTWH,POINTWH));
            layout.addView(pointB[i], new FrameLayout.LayoutParams(POINTWH,POINTWH));
            layout.addView(pointC[i], new FrameLayout.LayoutParams(POINTWH,POINTWH));
            Log.d("pointa",""+pointA[i].getX());
        }
        for(int i=0;i<discs.length;i++){
            //discs[i].setText(i+"");
            layout.addView(discs[i] , new FrameLayout.LayoutParams(discs[i].getW(),
                    discs[i].getH()));
            Log.d("test",""+discs[i].getX()+"+"+discs[i].getH());
        }
    }

    public void releseDiscAndPoint(){
        for(int i=0; i<pointA.length;i++){
            layout.removeView(pointA[i]);
            layout.removeView(pointB[i]);
            layout.removeView(pointC[i]);
            layout.removeView(discs[i]);
        }
    }
}
