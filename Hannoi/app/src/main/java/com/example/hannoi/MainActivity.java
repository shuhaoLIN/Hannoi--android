package com.example.hannoi;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.*;
import com.example.hannoi.musicService.MyMusicService;
import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private  AbsoluteLayout MainLayout;
    private static boolean IsGameOver = false;
    private Tower tower;
    private int creenWidth;
    private int creenHeight;
    private int N;
    private char[] towerName = {'A','B','C'};

    private  Button choiceGradeButton;  //choice_grade
    private  Button reStartButton;  //re_start
    private  Button getRankingButton; //get_ranking
    private  Button nextMusicButton;

    private  int numOfMusic = 0;
    private  MediaPlayer player;
    private int[] musics = {R.raw.ai_ru_cao_shui, R.raw.lao_nan_hai, R.raw.zhiwudazhanjiangshi};

    static TextView score_text;

    public static int numOfStep = 0;

   // MediaPlayer player;
    //int[] musics = {R.raw.ai_ru_cao_shui, R.raw.lao_nan_hai, R.raw.zhiwudazhanjiangshi};

    /**
     * 设置步数问题。
     */
    public static void addNumOfStep(){
        numOfStep++;
        updateScore();
    }
    public static void resetNumOfStep(){
        numOfStep = 0;
        updateScore();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取到屏幕的高度宽度
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);

        creenWidth = wm.getDefaultDisplay().getWidth();
        creenHeight = wm.getDefaultDisplay().getHeight();
        Log.d("ok",creenWidth+"+"+creenHeight);

        MainLayout = (AbsoluteLayout) findViewById(R.id.activity_main);

        inits();
        N = 3;

        tower = new Tower(towerName, this, MainLayout, creenWidth, creenHeight,N);
        int discHeight = 360 / N;
        tower.setDiscHeight(discHeight);  //
        tower.setMaxDiscWidth((creenWidth - 400) / 3 );   // 最大 除以三是因为分成三个柱子:减去400，是因为分成三个柱子，然后有4个空隙每一个400.
        tower.setMinDiscWidth((creenWidth - 400) / 3 / 4);  //最小

        tower.putDiscOnTower();
        tower.drawDiscAndPoint();

        //还有再进行测试才行。。。。。。。。
        MainLayout.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {

            @Override
            public void onDraw() {
                if(IsGameOver){
                    Toast.makeText(MainActivity.this,"Game Over!!!",Toast.LENGTH_SHORT).show();
                    showGameOverDialog();
                    IsGameOver = false;//返回到没有结束状态。
                }
            }
        });
        MainLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if(IsGameOver){
                    //Toast.makeText(MainActivity.this,"Game Over!!!",Toast.LENGTH_SHORT).show();
                    showGameOverDialog();
                    IsGameOver = false;//返回到没有结束状态。
                }
                return true;
            }
        });

        //导致帧率过高，怎么处理呢？
        new Thread(new Runnable() {
            @Override
            public void run() {
                //设置动画背景
                AnimationDrawable drawable = (AnimationDrawable)MainLayout.getBackground();
                drawable.start();
            }
        });

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //设置背景音乐
                player = MediaPlayer.create(MainActivity.this, R.raw.ai_ru_cao_shui);
                player.start();
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.start();
                    }
                });
//            }
//        }).start();
//        Intent intent = new Intent(this, MyMusicService.class);
//        startService(intent);

    }

    @Override
    public void finish() {
        player.stop();
        player.release();
//        Intent intent = new Intent(this, MyMusicService.class);
//        stopService(intent);
        super.finish();
    }

    public static boolean getIsGameOver() {
        return IsGameOver;
    }

    public static void setIsGameOver(boolean isGameOver) {
        IsGameOver = isGameOver;
    }
    public void inits(){
        choiceGradeButton = (Button)findViewById(R.id.choice_grade);
        reStartButton = (Button)findViewById(R.id.re_start);
        getRankingButton = (Button)findViewById(R.id.get_ranking);

        choiceGradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoiceGradeDialog();
            }
        });
        reStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
        getRankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRankingDialog();
            }
        });

        score_text = (TextView)findViewById(R.id.score_text);
//        score_text = new TextView(MainActivity.this);
//        score_text.setX(creenWidth / 2 - 20);
//        score_text.setY(creenHeight - 40); //设置位置。
//        score_text.setText(0+"");
//        score_text.setTextSize(50);
//        MainLayout.addView(score_text,new FrameLayout.LayoutParams(100,100));

        //创建数据库
        LitePal.getDatabase();

        //绑定nextMusicButton
        nextMusicButton = (Button)findViewById(R.id.next_music);
        nextMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
                        player.stop();
                        player.release();
                        player = null;
                        numOfMusic = (numOfMusic + 1) % 3;
                        player = MediaPlayer.create(MainActivity.this,musics[numOfMusic]);
                        player.start();
//                    }
//                }).start();

            }
        });
    }

    /**
     * 显示排行榜
     */
    public void showRankingDialog(){
        AlertDialog.Builder rankingDialog = new AlertDialog.Builder(MainActivity.this);
        View rankingView = LayoutInflater.from(MainActivity.this).inflate(R.layout.rank_list, null);

        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<Integer> scoreList = new ArrayList<>();
        //获取到数据
        int i;
        switch (N){
            case 3:
                List<RankPrimary> primarys = LitePal.select("name","score")
                        .order("score").find(RankPrimary.class);
                i = 1;
                for(RankPrimary primary :
                        primarys){
                    Log.d("primary", primary.getName()+"+"+primary.getScore());
                    nameList.add(primary.getName());
                    scoreList.add(primary.getScore());
                }
                break;
            case 4:
                List<RankIntermediate> intermediates = LitePal.select("name","score")
                        .order("score").find(RankIntermediate.class);
                i = 1;
                for(RankIntermediate intermediate :
                        intermediates){
                    Log.d("intermediate", intermediate.getName()+"+"+intermediate.getScore());
                    nameList.add(intermediate.getName());
                    scoreList.add(intermediate.getScore());
                }
                break;
            case 5:
                List<RankSenior> seniors = LitePal.select("name","score")
                        .order("score").find(RankSenior.class);
                i = 1;
                for(RankSenior senior :
                        seniors){
                    Log.d("senior", senior.getName()+"+"+senior.getScore());
                    nameList.add(senior.getName());
                    scoreList.add(senior.getScore());
                }
                break;
            default:
                break;
        }
        //已经获取到相应的数据到了nameList，scoreList
        ListView rankingListView = (ListView)rankingView.findViewById(R.id.rank_list_view);
        RankAdapter rankAdapter = new RankAdapter(MainActivity.this);
        rankAdapter.setNameListAndScoreList(nameList, scoreList);
        rankingListView.setAdapter(rankAdapter);

        rankingDialog.setView(rankingView);
        final AlertDialog dialog = rankingDialog.show();

        Button back = (Button)rankingView.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     *更新Score
     */
    public static void updateScore(){
        score_text.setText(numOfStep+"");
    }
    /**
     * 显示选择难度界面。
     */
    public void showChoiceGradeDialog(){
        AlertDialog.Builder choiceGradeDialog = new AlertDialog.Builder(MainActivity.this);
        View choiceView = LayoutInflater.from(MainActivity.this).inflate(R.layout.choice_grade,null);
        choiceGradeDialog.setView(choiceView);

        final AlertDialog dialog = choiceGradeDialog.show();
        Button primary = (Button)choiceView.findViewById(R.id.primary);
        Button intermediate = (Button)choiceView.findViewById(R.id.intermediate);
        Button senior = (Button)choiceView.findViewById(R.id.senior);

        primary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                N = 3;
                startGame();
                dialog.cancel();
            }
        });
        intermediate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                N = 4;
                startGame();
                dialog.cancel();
            }
        });
        senior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                N = 5;
                startGame();
                dialog.cancel();
            }
        });
    }

    /**
     * 展示游戏结束
     */
    public void showGameOverDialog(){
        final AlertDialog.Builder OverDialog =
                new AlertDialog.Builder(MainActivity.this);
        final View dialogView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.game_over, null);
        OverDialog.setView(dialogView);
        Button cancle = (Button)dialogView.findViewById(R.id.cancel_button);
        Button commit = (Button)dialogView.findViewById(R.id.commit_button);
        final EditText input_name = (EditText) dialogView.findViewById(R.id.input_name);
        TextView show_score = (TextView)dialogView.findViewById(R.id.show_score);
        show_score.setText(numOfStep+"");

        final AlertDialog dialog = OverDialog.show();
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                startGame();//重新开始游戏
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = input_name.getText().toString();
                saveRank(name, numOfStep);
                //这里做存储起来
                Toast.makeText(MainActivity.this,"确认提交",Toast.LENGTH_SHORT).show();
                 dialog.cancel();
                 startGame();//重新开始游戏
            }
        });
        OverDialog.setCancelable(false);
    }

    /**
     * 存储赢者的排名
     */
    public void saveRank(String name, int numOfStep){
        switch (N){
            case 3:
                RankPrimary primary = new RankPrimary();
                primary.setName(name);
                primary.setScore(numOfStep);
                primary.setDifficult(N);
                primary.save();
                break;
            case 4:
                RankIntermediate intermediate = new RankIntermediate();
                intermediate.setName(name);
                intermediate.setScore(numOfStep);
                intermediate.setDifficult(4);
                intermediate.save();
                break;
            case 5:
                RankSenior senior = new RankSenior();
                senior.setName(name);
                senior.setScore(numOfStep);
                senior.setDifficult(5);
                senior.save();
                break;
            default:
                break;
        }
    }

    /**
     * 获取数据库中的数据，并返回
     */
    public LitePalSupport getAndShowRankImfor(){
        return null;
    }
    /**
     * 重新开始游戏
     */
    public void startGame(){
        tower.releseDiscAndPoint();
        tower = new Tower(towerName, this, MainLayout, creenWidth, creenHeight,N);
        int discHeight = 360 / N;
        if(N == 4){
            discHeight = (creenHeight - 400) / N;
        }else if(N == 5){
            discHeight = (creenHeight - 300) / N;
        }

        tower.setDiscHeight(discHeight);  //
        tower.setMaxDiscWidth((creenWidth - 400) / 3 );   // 最大 除以三是因为分成三个柱子:减去400，是因为分成三个柱子，然后有4个空隙每一个400.
        tower.setMinDiscWidth((creenWidth - 400) / 3 / 4);  //最小

        tower.putDiscOnTower();
        tower.drawDiscAndPoint();

        resetNumOfStep(); //步数清零
    }
}
