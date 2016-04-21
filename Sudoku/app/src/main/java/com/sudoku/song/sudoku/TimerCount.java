package com.sudoku.song.sudoku;

/**
 * Created by song on 2015/4/3.
 */

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;


public class TimerCount{
    private Timer timer;
    private long seconds;
    private String result;

    static  Handler handler = null;
    static final int CTM = 102;

    public TimerCount()
    {
        timer = new Timer();
        startClock();
    }

    private void startClock(){
        seconds = 0;
        TimerTask tmTask = new TimerTask() {
            @Override
            public void run() {
                Message meg = new Message();
                meg.what = CTM;
                seconds++;
                result = formatSecondsTime(seconds);
                meg.obj = result;
                handler.sendMessage(meg);
            }
        };
        timer.schedule(tmTask,1000,2000);
    }

    private void stopClock()
    {
        if (timer!= null){
            timer.cancel();
            timer.purge();

        }
    }

    public String getTime()
    {
        this.startClock();
        return result;
    }

    public void stopTimer()
    {
            this.stopClock();
    }

    private String formatSecondsTime(long _seconds){
        long hours = 0;
        long minutes = 0;
        long seconds = 0;

        hours = _seconds/3600;
        seconds = _seconds%3600;
        minutes = seconds/60;
        seconds = seconds%60;

        String st= addZeroBeforeNumber(hours) + ":"+ addZeroBeforeNumber(minutes)
                +":"+addZeroBeforeNumber(seconds);

        return st;
    }

    /**
     * 加0处理，不足两位时补0
     * @param number
     * @return
     */
    public  String addZeroBeforeNumber(long number){
        StringBuilder sb = new StringBuilder();
        if(number >= 0 && number <= 9){
            return sb.append("0").append(number).toString();
        }
        return sb.append(number).toString();
    }

}
