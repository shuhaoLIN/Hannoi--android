package com.example.hannoi;

import org.litepal.crud.LitePalSupport;

/**
 * Created by lenovo on 2019/2/27.
 */
public class RankIntermediate extends LitePalSupport{
    private int id;
    private String name;
    private int score;
    private int difficult;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDifficult() {
        return difficult;
    }

    public void setDifficult(int difficult) {
        this.difficult = difficult;
    }
}
