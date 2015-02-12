package com.example.joypad;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

import com.example.joyPadView.JoystickView;
import com.example.joyPadView.JoystickView.OnJoystickMoveListener;

public class DPadActivity extends Activity {
	private JoystickView joystick;
	Communication cm = null;
	int dir = -1;
	Vibrator mVibrator;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		setContentView(R.layout.activity_dpad);

		joystick = (JoystickView) findViewById(R.id.dpadJoyStickView);
		joystick.setOnJoystickMoveListener(joystickMoveListener,
				JoystickView.DEFAULT_LOOP_INTERVAL);
		mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		String userId = getPreference();
		cm = new Communication(getApplicationContext(),
				"http://210.118.74.89:3000", userId);
	}

	private String getPreference() {
		SharedPreferences prefs = getSharedPreferences("userId", MODE_PRIVATE);
		String userId = prefs.getString("userId", "");
		return userId;
	}

	JoystickView.OnJoystickMoveListener joystickMoveListener = new OnJoystickMoveListener() {
		public void onValueChanged(int angle, int power, int direction) {
			int temp;
			switch (direction) {
			case JoystickView.FRONT:
			case JoystickView.FRONT_RIGHT:
				temp = JoystickView.FRONT;
				break;
			case JoystickView.RIGHT:
			case JoystickView.RIGHT_BOTTOM:
				temp = JoystickView.RIGHT;
				break;
			case JoystickView.BOTTOM:
			case JoystickView.BOTTOM_LEFT:
				temp = JoystickView.BOTTOM;
				break;
			case JoystickView.LEFT:
			case JoystickView.LEFT_FRONT:
				temp = JoystickView.LEFT;
				break;
			default:
				temp = 0;
			}

			if (dir == -1) {
				dir = temp;
				if (dir != 0)
					cm.emit("pad", sendToString(dir), 0);
			} else if (dir != temp) {
				if (dir != 0)
					cm.emit("pad", sendToString(dir), 1);
				if (temp != 0)
					cm.emit("pad", sendToString(temp), 0);
				dir = temp;
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
		case JoystickView.LEFT:
			send = "left";
			break;
		case JoystickView.RIGHT:
			send = "right";
			break;
		default:
			send = "null";
			break;
		}
		return send;
	}

	public void BtnSender(View v) {

		switch (v.getId()) {
		case R.id.btnA:
			mVibrator.vibrate(5);
			cm.emit("btn", "A");
			break;

		case R.id.btnB:
			mVibrator.vibrate(5);
			cm.emit("btn", "B");
			break;

		case R.id.selectBtn:
			cm.emit("btn", "select");
			break;

		case R.id.startBtn:
			cm.emit("btn", "start");
			break;

		default:
			break;
		}
	}
}
