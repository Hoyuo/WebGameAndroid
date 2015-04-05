package com.ssm.return1990.webgameandroid;

import android.view.MotionEvent;
import android.view.View;

public class APadView {
    public static final int D_NONE = 0;
    public static final int D_UP = 1;
    public static final int D_DOWN = 2;
    public static final int D_LEFT = 3;
    public static final int D_RIGHT = 4;

    private int direction = D_NONE;
    private int imageNormal, imageUp, imageDown, imageLeft, imageRight;

    public APadView(int imageNormal, int imageUp, int imageDown, int imageLeft,
                    int imageRight) {
        this.imageNormal = imageNormal;
        this.imageUp = imageUp;
        this.imageDown = imageDown;
        this.imageLeft = imageLeft;
        this.imageRight = imageRight;
    }

    public void onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN
                || action == MotionEvent.ACTION_MOVE) {
            float x = event.getX();
            float y = event.getY();
            if (x >= v.getWidth() / 3 && x <= v.getWidth() * 2 / 3 && y >= 0
                    && y <= v.getHeight() * 1 / 3) {
                direction = D_UP;
                v.setBackgroundResource(imageUp);
            } else if (x >= v.getWidth() / 3 && x <= v.getWidth() * 2 / 3
                    && y >= v.getHeight() * 2 / 3 && y <= v.getHeight()) {
                direction = D_DOWN;
                v.setBackgroundResource(imageDown);
            } else if (x >= 0 && x <= v.getWidth() / 3
                    && y >= v.getHeight() * 1 / 3 && y <= v.getHeight() * 2 / 3) {
                direction = D_LEFT;
                v.setBackgroundResource(imageLeft);
            } else if (x >= v.getWidth() * 2 / 3 && x <= v.getWidth()
                    && y >= v.getHeight() * 1 / 3 && y <= v.getHeight() * 2 / 3) {
                direction = D_RIGHT;
                v.setBackgroundResource(imageRight);
            } else {
                direction = D_NONE;
                v.setBackgroundResource(imageNormal);
            }
        } else if (action == MotionEvent.ACTION_UP) {
            direction = D_NONE;
            v.setBackgroundResource(imageNormal);
        }
    }

    public int getDirection() {
        return direction;
    }
}