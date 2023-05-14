package com.example.gamepilot;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

    // поля
    private Thread thread;
    private boolean isPlaying;
    private Background background1, background2;
    private int screenX, screenY;
    private Paint paint;
    private float screenRatioX, screenRatioY;
    private Flight flight;


    public GameView(Context context, int screenX, int screenY) {
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;

        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;


        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());
        background2.setX(screenX);
        paint = new Paint();
        flight = new Flight(screenX, screenY, getResources());
    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            sleep();
        }
    }

    private void update() {
        background1.setX(background1.getX() - (int) (10 * screenRatioX));
        background2.setX(background2.getX() - (int) (10 * screenRatioX));
        if ((background1.getX() + background1.getBackground().getWidth()) <= 0) {
            background1.setX(screenX);
        }
        if ((background2.getX() + background2.getBackground().getWidth()) <= 0) {
            background2.setX(screenX);
        }

        if (flight.isGoingUp()) {
            flight.setY(flight.getY() - (int) (30 * screenRatioY));
        } else {
            flight.setY(flight.getY() + (int) (30 * screenRatioY));
        }
        if (flight.getY() < 0) {
            flight.setY(0);
        } else if (flight.getY() >= screenY - flight.getHeight()) {
            flight.setY(screenY - flight.getHeight());
        }
    }

    private void draw() {

        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.getBackground(), background1.getX(), background1.getY(), paint);
            canvas.drawBitmap(background2.getBackground(), background2.getX(), background2.getY(), paint);


            canvas.drawBitmap(flight.getFlight(), flight.getX(), flight.getY(), paint);


            getHolder().unlockCanvasAndPost(canvas);
        }
    }


    private void sleep() {
        try {

            Thread.sleep(16);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void resumeThread() {

        isPlaying = true;

        thread = new Thread(this);

        thread.start();
    }

    public void pauseThread() {
        try {

            isPlaying = false;

            thread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // обработка событий касания экрана
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                // если пользователь нажал на левую сторону экрана
                if (event.getX() < (screenX / 2)) {
                    // то движение самолёта вверх
                    flight.setGoingUp(true);
                } else if (event.getX() >= (screenX / 2)){

                }
                break;
            case MotionEvent.ACTION_MOVE: 

                break;
            case MotionEvent.ACTION_UP: 
              
                flight.setGoingUp(false);
                break;
        }

        return false; 
    }
}
