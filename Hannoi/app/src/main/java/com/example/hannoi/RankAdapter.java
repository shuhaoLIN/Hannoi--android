package com.example.hannoi;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

import static org.litepal.LitePalApplication.getContext;

/**
 * Created by lenovo on 2019/2/28.
 */
public class RankAdapter extends BaseAdapter {
    private  List<String> nameList;
    private  List<Integer> scoreList;
    private   Context context;

    public RankAdapter(Context context){
        this.context = context;
    }
    public void setNameListAndScoreList(ArrayList<String> name, ArrayList<Integer> score){
        this.nameList = name;
        this.scoreList = score;
    }

    @Override
    public int getCount() {
        return nameList.size();
    }

    @Override
    public Object getItem(int position) {
        return nameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.rank_item, parent, false);
        TextView number = (TextView)view.findViewById(R.id.number_list);
        TextView name = (TextView)view.findViewById(R.id.name_list);
        TextView score = (TextView)view.findViewById(R.id.score_list);
        number.setText((position+1)+"");
        name.setText(nameList.get(position)+"");
        score.setText(scoreList.get(position)+"");
        return view;
    }

}
