package com.ssm.return1990.webgameandroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ssm.return1990.webgameandroid.JoystickView.OnJoystickMoveListener;

public class DPadActivity extends Activity {
    private JoystickView joystick;
    Communication cm = null;
    int dir = 0;
    Vibrator mVibrator;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        setContentView(R.layout.activity_dpad);

        joystick = (JoystickView) findViewById(R.id.dpadJoyStickView);
        joystick.setOnJoystickMoveListener(joystickMoveListener,
                JoystickView.DEFAULT_LOOP_INTERVAL);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        String userId = Server.userId;
        cm = new Communication(getApplicationContext(),
                Server.IP, userId);
    }

    OnJoystickMoveListener joystickMoveListener = new OnJoystickMoveListener() {
        public void onValueChanged(int angle, int power, int direction) {
            if (!(direction == JoystickView.FRONT_RIGHT || direction == JoystickView.RIGHT_BOTTOM ||
                    direction == JoystickView.BOTTOM_LEFT || direction == JoystickView.LEFT_FRONT)) {
                if (dir == -1) {
                    dir = direction;
                    if (dir != 0) {
                        cm.emit("pad", sendToString(dir), 0);
                    }
                } else if (dir != direction) {
                    if (dir != 0) {
                        cm.emit("pad", sendToString(dir), 1);
                    }
                    if (direction != 0) {
                        cm.emit("pad", sendToString(direction), 0);
                    }
                    dir = direction;
                }
            }
        }
    };

    public String sendToString(int direction) {
        String send = "";
        switch (direction) {
            case JoystickView.FRONT:
                send = "up";
                break;
            case JoystickView.BOTTOM:
                send = "down";
                break;
            case JoystickView.RIGHT:
                send = "left";
                break;
            case JoystickView.LEFT:
                send = "right";
                break;
            default:
                send = "null";
                break;
        }
        return send;
    }

    public void BtnSender(View v) {
        String btn = "";
        switch (v.getId()) {
            case R.id.btnA:
                mVibrator.vibrate(5);
                btn = "A";
                break;

            case R.id.btnB:
                mVibrator.vibrate(5);
                btn = "B";
                break;

            case R.id.selectBtn:
                btn = "select";
                break;

            case R.id.startBtn:
                btn = "start";
                break;

            default:
                break;
        }
        if (!btn.equals("")) {
            cm.emit("btn", btn);
        }
    }
}
