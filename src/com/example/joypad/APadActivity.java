package com.example.joypad;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

import com.example.joyPadView.APadView;

public class APadActivity extends Activity implements OnTouchListener {
	LinearLayout aPad;
	APadView dpc;
	int dir = -1;
	int direction = APadView.D_NONE;
	Communication cm = null;
	Vibrator mVibrator;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		setContentView(R.layout.activity_apad);

		dpc = new APadView(R.drawable.dpad_normal, R.drawable.dpad_up,
				R.drawable.dpad_down, R.drawable.dpad_left,
				R.drawable.dpad_right);

		aPad = (LinearLayout) findViewById(R.id.layoutAPad);
		aPad.setOnTouchListener(this);
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

	public boolean onTouch(View v, MotionEvent event) {
		dpc.onTouch(v, event);
		direction = dpc.getDirection();

		if (dir == -1) {
			dir = direction;
			if (dir != 0)
				cm.emit("pad", sendToString(dir), 0);
		} else if (dir != direction) {
			if (dir != 0)
				cm.emit("pad", sendToString(dir), 1);
			if (direction != 0)
				cm.emit("pad", sendToString(direction), 0);
			dir = direction;
		}
		return true;
	}

	public String sendToString(int direction) {
		String send = "";
		switch (direction) {
		case APadView.D_UP:
			send = "up";
			break;
		case APadView.D_DOWN:
			send = "down";
			break;
		case APadView.D_LEFT:
			send = "left";
			break;
		case APadView.D_RIGHT:
			send = "right";
			break;
		default:
			send = "null";
			break;
		}
		return send;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		cm.close();
		super.onDestroy();
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
