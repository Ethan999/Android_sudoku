package com.sudoku.song.sudoku;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {


    private Button[] btn = new Button[10];
    private GameMath game;
    private TextView txtTime;
    private TimerCount timCout;


    @Override
    protected void onDestroy() {
        timCout.stopTimer();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        game = GameMath.singleInstance;

        findBtnViewById();
        setBtnOnClickLitener();
        this.timCout = new TimerCount();
        showTime();
    }

    private void showTime(){
       TimerCount.handler = new Handler()
       {

           @Override
           public void handleMessage(Message msg) {

               switch (msg.what){
                   case TimerCount.CTM :{
                       txtTime.setText(msg.obj.toString());
                       break;
                                          }
               }
               super.handleMessage(msg);
           }
       };
    }

    private void findBtnViewById() {
        btn[1] = (Button) findViewById(R.id.num_1);
        btn[2] = (Button) findViewById(R.id.num_2);
        btn[3] = (Button) findViewById(R.id.num_3);
        btn[4] = (Button) findViewById(R.id.num_4);
        btn[5] = (Button) findViewById(R.id.num_5);
        btn[6] = (Button) findViewById(R.id.num_6);
        btn[7] = (Button) findViewById(R.id.num_7);
        btn[8] = (Button) findViewById(R.id.num_8);
        btn[9] = (Button) findViewById(R.id.num_9);
        btn[0] = (Button) findViewById(R.id.undo);

        txtTime = (TextView)findViewById(R.id.txtTimer);

    }

    private void setBtnOnClickLitener() {
        for (int i = 0; i <= GameMath.NORMS; i++) {
            btn[i].setOnClickListener(new BtnOnClickListener(i));
        }
    }

    class BtnOnClickListener implements View.OnClickListener {
        int id;

        public BtnOnClickListener(int id) {
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            game.setCurSelectNum(id);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.about_game: {
                ShowAboutDialog();
                break;
            }
            case R.id.open_help_line: {
                if (game.isOpenHelp()) {
                    game.setOpenHelp(false);
                    item.setTitle(R.string.open_help_line);
                } else {
                    game.setOpenHelp(true);
                    item.setTitle(R.string.close_help_line);
                }
                break;
            }

            default: {
                break;
            }
        }
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    private void ShowAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About MiniSudoku")
                .setMessage("Email:" + getString(R.string.Email) + "\n" + "Name:" + getString(R.string.myName) + "\n" + getString(R.string.aboutMessage))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }
}