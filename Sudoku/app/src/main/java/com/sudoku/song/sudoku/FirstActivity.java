package com.sudoku.song.sudoku;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.purplebrain.adbuddiz.sdk.AdBuddiz;

public class FirstActivity extends ActionBarActivity {


    static {
         System.loadLibrary("GsudoStr");
    }
    public native String getSudoString_jni();

    private Button easy;
    private Button medium;
    private Button hard;
    private GameMath game = GameMath.singleInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AdBuddiz.setPublisherKey("33554bd0-db51-43da-b40d-2d0810cffee5");
        AdBuddiz.cacheAds(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);


        AdBuddiz.showAd(this);

        easy = (Button) findViewById(R.id.btn_level_1);
        medium = (Button) findViewById(R.id.btn_level_2);
        hard = (Button) findViewById(R.id.btn_level_3);
        easy.setOnClickListener(new BtnOnClickListener(GameMath.DifficultyLevel.EASY));
        medium.setOnClickListener(new BtnOnClickListener(GameMath.DifficultyLevel.MEDIUM));
        hard.setOnClickListener(new BtnOnClickListener(GameMath.DifficultyLevel.HARD));



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AdBuddiz.onDestroy();
    }

    class BtnOnClickListener implements View.OnClickListener {
        private GameMath.DifficultyLevel level;

        public BtnOnClickListener(GameMath.DifficultyLevel level) {
            this.level = level;
        }

        @Override
        public void onClick(View v) {
            String str =null;
          try{
              str = getSudoString_jni();
          }catch (Exception e)
          {
              e.printStackTrace();
          }
            //str = getString(R.string.skd_string1);


            //getString(R.string.skd_string1);
            game = new GameMath(str, level);
            GameMath.singleInstance = game;
            Intent myIntent = new Intent();
            myIntent.setClass(FirstActivity.this, MainActivity.class);
            FirstActivity.this.startActivity(myIntent);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_first, menu);
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

            default: {
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void ShowAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About Sudoku")
                .setMessage("Email:" + getString(R.string.Email) + "\n" + "Name:" + getString(R.string.myName) + "\n" + getString(R.string.aboutMessage))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }
}
