package com.sudoku.song.sudoku;

import android.content.Context;

import java.util.Random;

/**
 * Created by song on 2015/3/19.
 * the main mathematical about the sudoku
 * how to win the game
 */
public class GameMath {

    public static GameMath singleInstance = null;

    public static  final int NORMS = 9;

    public enum DifficultyLevel {
       EASY, MEDIUM, HARD
   }

    private  int[]correctSdk=new int[NORMS*NORMS];
    private int[]initSdk=new int[NORMS*NORMS];
    private int[]tempSdk = new int[NORMS*NORMS];

    private int curSelectNum;

    private boolean isOpenHelp;

    public GameMath(String str, DifficultyLevel Level)
    {
        for (int i=0;i<NORMS*NORMS;i++)
        {
            correctSdk[i]=initSdk[i]=tempSdk[i]=str.charAt(i)-'0';
        }
        initSdkSting(Level);
    }


    /**
     * according to the different levels ,generate the initial sudoku
     */
    private void initSdkSting(DifficultyLevel level) {
           int nRemove = 0;
             switch (level) {
                  case EASY:
                          nRemove = 20;
                            break;
               case MEDIUM:
                        nRemove = 40;
                     break;
                case HARD:
                     nRemove = 60;
                   break;
         default:
                     break;
                  }
               Random random = new Random();
               for (int i = 0; i < nRemove; i++) {
                      int x = random.nextInt(NORMS);
                       int y = random.nextInt(NORMS);
                   initSdk[x+y*NORMS] =tempSdk[x+y*NORMS] =0;
                }

          }


/**
 * check the  game is win or not
 */
    public boolean isWin()
    {
        boolean flag=true;
        for (int i=0;i<NORMS*NORMS;i++)
        {

            if (tempSdk[i]==0 || tempSdk[i]!= correctSdk[i])
            {
                flag = false;
                break;
            }
        }
        return  flag;
    }

public boolean isOpenHelp()
{
    return isOpenHelp;
}
    public void  setOpenHelp(boolean isOpenHelp)
    {
        this.isOpenHelp=isOpenHelp;
    }

    public int getCurSelectNum()
    {
        return curSelectNum;
    }

    public void setCurSelectNum(int curSelectNum)
    {
        this.curSelectNum = curSelectNum;
    }

    public boolean isVaildClick(int x, int y)
    {
        if (x>NORMS-1||x<0)
        {
            return false;
        }
        if (y>NORMS-1||y<0)
        {
            return false;
        }
        return true;
    }

    public void setTempSdk(int x, int y, int value)
    {
        tempSdk[x+y*NORMS]= value;
    }

    /**
     * according the x and y ,return the correct answer
     */
    public String showCorrectNum(int x,int y)
    {
        if (initSdk[x+y*NORMS]==0) {
            return correctSdk[x + y * NORMS]+"";
        }
        return "";
    }

    public String getInitNumString(int x,int y)
    {
        if(initSdk[x+y*NORMS]==0)
        {
            return "";
        }
       return initSdk[x+y*NORMS]+"";
    }

    public String getTempNumString(int x,int y)
    {
        if(tempSdk[x+y*NORMS]==0)
        {
            return "";
        }
        return tempSdk[x+y*NORMS]+"";
    }




}
