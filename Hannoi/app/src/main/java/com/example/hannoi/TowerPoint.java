package com.example.hannoi;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by lenovo on 2019/2/22.
 */
public class TowerPoint extends Button{
    private boolean haveDisc;  //标记是否有碟子在上面
    private  Disc disc = null;
    private  int dx,dy;
    private  int arrayIndex;

    TowerPoint(Context context, int x, int y,int index){
        super(context);
        this.setX(x);
        this.setY(y);
        dx = 0;
        dy = 0;
        this.arrayIndex = index;
        this.haveDisc = false;
    }

    public int getArrayIndex() {
        return arrayIndex;
    }

    public boolean isHaveDisc() {
        return haveDisc;
    }

    public void setHaveDisc(boolean haveDisc) {
        this.haveDisc = haveDisc;
    }

    public Disc getDisc() {
        return disc;
    }

    public void setDisc(Disc disc) {
        this.disc = disc;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    /**
     * 判断两个点是否相同
     * @param p
     * @return
     */
    public boolean equals(TowerPoint p){
        if(p.getX() == this.getX()  && p.getY() == this.getY()){
            return true;
        }else{
            return false;
        }
    }

    /**
     *
     * @param button  这个用于传递组件
     */
    public void putDisc(TextView button){
        this.disc = (Disc) button;  //获取到要放在这里的碟子
        dx =(int)( disc.getX() - this.getX() );
        dy =(int)(disc.getY() - this.getY());
        haveDisc = true;
    }

    public void releseDisc(){
        this.disc = null;
        dx = dy = 0;
        haveDisc = false;
    }
}
