package com.example.hannoi;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Toast;

/**
 * Created by lenovo on 2019/2/22.
 */
public class MouseListener implements View.OnTouchListener , View.OnClickListener{
    final int[] lastX = new int[1];
    final int[] lastY = new int[1];
    final int[] moveDownX = new int[1];
    //这是开始时候的位置
    final int[] startl = new int[1];
    final int[] startb = new int[1];
    final int[] startr = new int[1];
    final int[] startt = new int[1];
    //这是拖曳结束的时候的位置
    private int l;
    private  int b;
    private int r;
    private  int t;
    //存储point，用作为后面的判断使用的。
    private   TowerPoint[] pointA,pointB,pointC;
    //表示是否进行转移
    private  Boolean IsMove = false;
    //储存一下移动的disc和point，以及最后的point
    private  Disc disc;
    private  TowerPoint startPoint, endPoint;
    //存储mainlayout
    private   AbsoluteLayout layout;
    MouseListener(TowerPoint[] A,TowerPoint[] B,TowerPoint[] C, AbsoluteLayout layout ){
        pointA = A;
        pointB = B;
        pointC = C;
        this.layout = layout;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        disc = (Disc)v;
        startPoint = disc.getPoint();
        if( !isCanMove(disc, startPoint)){
            return false;
        }
        //获取到按下去的位置
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            startl[0] = v.getLeft();
            startb[0] = v.getBottom();
            startr[0] = v.getRight();
            startt[0] = v.getTop();
            lastX[0] = (int) event.getRawX();
            lastY[0] = (int) event.getRawY();
            moveDownX[0] = (int) event.getRawX();
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            //获取x，y的偏移量
            int nowX = (int)event.getRawX();
            int nowY = (int)event.getRawY();
            int dx = (nowX - lastX[0]);
            int dy = (nowY - lastY[0]);
            //获取控件的上下左右信息，加上偏移量得到新的位置
            l = v.getLeft() + dx;
            b = v.getBottom() + dy;
            r = v.getRight() + dx;
            t = v.getTop() + dy;
            //使用view重画控件
            v.layout(l,t,r,b);

            lastX[0] = (int)event.getRawX();
            lastY[0] = (int)event.getRawY(); //更新lastX，Y
            v.postInvalidate();
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
            IsMove = false;
            //搜索到相应的位置。
            isConnectHere(v,pointA);
            isConnectHere(v,pointB);
            isConnectHere(v,pointC);

            if(true == IsMove){
                int endX = (int) endPoint.getX();
                int endY = (int) endPoint.getY();
                int dx = (int) (disc.getLeft() - disc.getX());
                int dy = (int) (disc.getTop() - disc.getY());
                v.layout(endX + startPoint.getDx() + dx,endY + startPoint.getDy() + dy,
                        endX + startPoint.getDx() + v.getWidth() + dx,
                        endY + startPoint.getDy() + v.getHeight() + dy);
                IsMove = false;
                startPoint.releseDisc();
                endPoint.putDisc(disc);
                disc.setPoint(endPoint);//相互关联
                MainActivity.addNumOfStep(); //步数加起来。

            }
            else {
                v.layout(startl[0],startt[0],startr[0],startb[0]);
            }
            v.postInvalidate();
        }
        if(isGameOver()){
            MainActivity.setIsGameOver(true);// 标记游戏结束
        }
        return false;
    }

    /**
     * 实现判断是否能够移动。
     * @param disc 移动的碟子
     * @param startPoint 开始的点。
     * @return
     */
    public boolean isCanMove(Disc disc ,TowerPoint startPoint){
        int index = startPoint.getArrayIndex();
        if(index == 0){
            return true;
        }
        TowerPoint[] nowPointArray;
        if(pointA[index] == startPoint){
            nowPointArray = pointA;
        }else if(pointB[index] == startPoint){
            nowPointArray = pointB;
        }else{
            nowPointArray = pointC;
        }
        if(nowPointArray[index - 1].isHaveDisc()){
            return false;
        }
        return true;
    }

    /**
     * 实现判断出endpoint的位置，以及改变ismove
     * @param v
     * @param thisPointArray
     * @return
     */
    public void isConnectHere(View v, TowerPoint[] thisPointArray){
        for(int i=0;i<thisPointArray.length;i++){
            //包含在里面
            if(thisPointArray[i].isHaveDisc()){
                continue;
            }
            if(thisPointArray[i].getX() > v.getX() && thisPointArray[i].getX() < v.getX() + v.getWidth()
                    &&thisPointArray[i].getY() > v.getY() && thisPointArray[i].getY() < v.getY() + v.getHeight()){
                if(thisPointArray.length - 1 == i){
                    IsMove = true;
                    endPoint = thisPointArray[i];
                    break;
                }
                else{
                    if(thisPointArray[i+1].isHaveDisc() && thisPointArray[i + 1].getDisc().getNumber() > disc.getNumber()){
                        IsMove = true;
                        endPoint = thisPointArray[i];
                        break;
                    }
                }
            }
        }
    }

    /**
     * 实现判断是否是结束游戏了。
     * @return
     */
    public boolean isGameOver(){
        if(pointC[0].isHaveDisc()){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        Log.d("click","pppp");
    }
}



//Log.d("disc",disc.getX()+"++"+disc.getY());
//        Log.d("start",startPoint.getX()+"++"+startPoint.getY());
//        // Log.d("end",endPoint.getX()+"++"+endPoint.getY());
//        //Log.d("ende",endPoint.getTop()+":TOP\n"+endPoint.getLeft()+":LEFT");
//        Log.d("start3","ACTION_UP!");


//判断是否画出屏幕
//判断四个实际位置,如果有一边已经划出屏幕,那就把这边位置设置为0
//然后相反的边的位置就设置成控件的高度或者宽度
//                    if (l < 0) {
//                        l = 0;
//                        r = l + v.getWidth();
//                    }
//
//                    if (t < 0) {
//                        t = 0;
//                        b = t + v.getHeight();
//
//                    }
//
//                    if (r > width) {
//                        r = width;
//                        l = r - v.getWidth();
//                    }
//
//                    if (b > height) {
//                        b = height;
//                        l = b - v.getHeight();
//                    }

//            for(int i=0;i<pointA.length;i++){
//                //包含在里面
//                if(pointA[i].getX() > l && pointA[i].getX() < r
//                        &&pointA[i].getY() > t && pointA[i].getY() < b){
//                    if(pointA.length - 1 == i){
//                        IsMove = true;
//                        endPoint = pointA[i];
//                        break;
//                    }
//                    else{
//                        if(pointA[i + 1].getDisc().getNumber() > ((Disc)v).getNumber()){
//                            IsMove = true;
//                            endPoint = pointA[i];
//                            break;
//                        }
//                    }
//                }
//            }
//            for(int i=0;i<pointC.length;i++){
//                //包含在里面
//                if(pointC[i].getX() > l && pointC[i].getX() < r
//                        &&pointC[i].getY() > t && pointC[i].getY() < b){
//                    if(pointC.length - 1 == i){
//                        IsMove = true;
//                        endPoint = pointC[i];
//                        break;
//                    }
//                    else{
//                        if(pointC[i + 1].getDisc().getNumber() > ((Disc)v).getNumber()){
//                            IsMove = true;
//                            endPoint = pointC[i];
//                            break;
//                        }
//                    }
//                }
//            }
