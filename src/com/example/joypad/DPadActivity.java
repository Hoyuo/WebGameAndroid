package com.example.joypad;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.joyPadView.JoystickView;
import com.example.joyPadView.JoystickView.OnJoystickMoveListener;

public class DPadActivity extends Activity {

	private JoystickView joystick;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		setContentView(R.layout.activity_dpad);

		joystick = (JoystickView) findViewById(R.id.dpadJoyStickView);
		joystick.setOnJoystickMoveListener(new OnJoystickMoveListener() {
			@Override
			public void onValueChanged(int angle, int power, int direction) {
				// TODO Auto-generated method stub
				Log.d("DAPD", " " + String.valueOf(angle) + "¡Æ");
			}
		}, JoystickView.DEFAULT_LOOP_INTERVAL);
	}
}
