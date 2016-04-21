package com.sudoku.song.sudoku;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

/**
 * Created by song on 2015/3/19.
 */
public class GameSfView extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder surfaceHolder;
    private  boolean isClicked;
    private  int selected_X = 0;
    private int selected_Y = 0;

    private float width;
    private float height;

    private int numberSteps;

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    private  GameMath game = GameMath.singleInstance;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ViewGroup.LayoutParams lp = this.getLayoutParams();
        lp.height = getWidth();
        this.setLayoutParams(lp);
        draw();

    }

    public GameSfView(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        isClicked = false;
        numberSteps = 0;
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()!=MotionEvent.ACTION_DOWN)
        {
            return  super.onTouchEvent(event);
        }

        isClicked = true;
        selected_X = (int)((event.getX())/width);
        selected_Y= (int)((event.getY())/height);

        if(!game.isVaildClick(selected_X, selected_Y)){
            return super.onTouchEvent(event);
        }
        if(!game.getInitNumString(selected_X,selected_Y).equals(""))
        {
            return super.onTouchEvent(event);
        }


        game.setTempSdk(selected_X,selected_Y,game.getCurSelectNum());
        draw();
        numberSteps++;
        if (game.isWin())
        {
            showWinDialog();
        }

        return super.onTouchEvent(event);
    }

    private void showWinDialog() {
        AlertDialog.Builder build=new AlertDialog.Builder(getContext());
        build.setTitle("Congratulations!\n")
                .setMessage("Win!!\nyou just use"+ numberSteps +" steps *^_^*")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        build.show();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w/9f;
        this.height = w/9f;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void draw() {
        synchronized(surfaceHolder){
            // 获取Canvas对象
            // 锁定画布，一般在锁定后就可以通过其返回的画布对象Canvas，在其上面画图等操作了。
            Canvas canvas = null;
            Paint paint = new Paint();
            try{
                canvas = surfaceHolder.lockCanvas(); // 锁住Canvas


                // 清理屏幕 绘制背景
                initGameView(canvas, paint);
                //重绘surfaceview 填充新增的数据
                inflateNewNum(canvas,paint);
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                if(canvas != null)
                    // 结束锁定画图，并提交改变。
                    surfaceHolder.unlockCanvasAndPost(canvas);  // 解锁Canvas，更新
            }
        }
    }

    private void initGameBg(Canvas canvas,Paint paint) {
        //清理背景,游戏中将换成具体的背景贴图
        canvas.drawColor(Color.WHITE);// drawRect(0, 0, getWidth(), getHeight(), paint);
//        paint.setColor(Color.WHITE);
//        canvas.drawRect(0,0,getWidth(),getWidth(),paint);
        //默认存在的提示数字有灰色背景以示区分
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(!game.getInitNumString(i, j).equals("")){
                    paint.setColor(Color.GRAY);
                    canvas.drawRect(i*width, j*height, i*width+width, j*height+width, paint);
                }
            }
        }
        if(isClicked && game.isOpenHelp()){
            //绘制基于点击点坐标的横竖提示条
            drawHelpLineViaSelectedPosition(canvas, paint);
        }
        //绘制游戏方格下方按钮区域 设置为黑色背景
        //paint.setColor(Color.BLACK);
        //canvas.drawRect(0,getWidth(),getWidth(), getHeight(), paint);
    }

    private void initGameLine(Canvas canvas,Paint paint) {
        //设置画笔颜色
        paint.setColor(Color.BLACK);
        //画横线
        for(int i=1;i<9;i++){
            canvas.drawLine(0 ,i*height, getWidth(), i*height, paint);
        }
        //画竖线
        for(int i=1;i<9;i++){
            canvas.drawLine(i*width,0, i*width,getWidth(), paint);

        }
        //画三道横粗线
        paint.setStrokeWidth(4);
        for(int i=1;i<4;i++){
            canvas.drawLine(0 ,i*height*3, getWidth(), i*height*3, paint);
        }
        //画三道竖粗线
        for(int i=1;i<9;i++){
            canvas.drawLine(i*width*3,0, i*width*3,getWidth(), paint);
        }
    }

    private void initGameView(Canvas canvas,Paint paint){
        //bgBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.background);
        //初始化游戏背景
        initGameBg(canvas,paint);
        //初始化游戏线条
        initGameLine(canvas,paint);
        //初始化游戏数据
        initFirstNumber(canvas,paint);

    }
    /**
     * 默认以存在的提示数字有灰色背景以示区分
     * @param canvas
     * @param paint
     */
    private void initFirstNumber(Canvas canvas, Paint paint) {
        setFontStyle(paint);
        //设置数字在单元格里显示居中
        Paint.FontMetrics fm = paint.getFontMetrics();
         float x = width/2;
        float y = height/2-(fm.ascent+fm.descent)/2;
        //绘制初始化谜题的数据
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(game.getInitNumString(i, j).equals("")){
                    continue;
                }
                canvas.drawText(game.getInitNumString(i, j), i*width+x, j*height+y, paint);
            }
        }
    }

    /**
     * 绘制新填入的数据
     * @param canvas
     * @param paint
     */
    private void inflateNewNum(Canvas canvas, Paint paint) {
        setFontStyle(paint);
        //设置数字在单元格里显示居中
        Paint.FontMetrics fm = paint.getFontMetrics();
        paint.setColor(Color.RED);
        float x = width/2;
        float y = height/2-(fm.ascent+fm.descent)/2;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                {
                    if (game.getInitNumString(i,j)=="")
                    {
                        canvas.drawText(game.getTempNumString(i, j), i*width+x, j*height+y, paint);
                    }
                }

            }
        }
    }

    /**
     * 绘制基于点击点坐标的横竖提示条
     */
    private void drawHelpLineViaSelectedPosition(Canvas canvas, Paint paint) {
        setFontStyle(paint);
        //设置数字在单元格里显示居中
        Paint.FontMetrics fm = paint.getFontMetrics();
        float x = width/2;
        float y = height/2-(fm.ascent+fm.descent)/2;
        int i = selected_X;
        int j = selected_Y;
        //绘制基于点击点坐标的横竖提示条
        paint.setColor(Color.GREEN);
        canvas.drawRect(i*width,0,i*width+width, 9*height, paint);
        canvas.drawRect(0,j*height,getWidth(), j*height+height, paint);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * 字体风格设置
     * @param paint
     */
    private void setFontStyle(Paint paint){
        paint.setColor(Color.BLUE);
        //设置为实心
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(height*0.75f);
        //设置抗锯齿
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
    }



}
