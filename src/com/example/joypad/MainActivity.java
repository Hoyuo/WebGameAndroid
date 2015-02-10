package com.example.joypad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startActivity(new Intent(this, Splash_Activity.class));
		
		Button apad = (Button) findViewById(R.id.apadBtn);
		apad.setOnClickListener(this);

		Button dpad = (Button) findViewById(R.id.dpadBtn);
		dpad.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.apadBtn:
			Intent apad = new Intent(this, APadActivity.class);
			startActivity(apad);
			break;
		case R.id.dpadBtn:
			Intent dpad = new Intent(this, DPadActivity.class);
			startActivity(dpad);
			break;
		default:
			break;
		}

	}

}
