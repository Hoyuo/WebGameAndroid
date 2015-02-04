package com.example.joypad;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class APadActivity extends Activity implements OnTouchListener {

	SocketIO socket = null;
	Button up;
	Button down;
	Button left;
	Button right;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		setContentView(R.layout.activity_apad);
		up = (Button) findViewById(R.id.upBtn);
		down = (Button) findViewById(R.id.downBtn);
		left = (Button) findViewById(R.id.leftBtn);
		right = (Button) findViewById(R.id.rightBtn);

		up.setOnTouchListener(this);
		down.setOnTouchListener(this);
		left.setOnTouchListener(this);
		right.setOnTouchListener(this);
	}

	private void SocketConnection(String server) {
		
		String uuid = UUIDModule.CreateUUID(getApplicationContext());
		try {
			socket = new SocketIO(server);
			socket.connect(new IOCallback() {
				@Override
				public void onMessage(JSONObject json, IOAcknowledge ack) {
					try {
						System.out.println("Server said:" + json.toString(2));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onMessage(String data, IOAcknowledge ack) {

				}

				@Override
				public void onError(SocketIOException socketIOException) {
					socketIOException.printStackTrace();
				}

				@Override
				public void onDisconnect() {
				}

				@Override
				public void onConnect() {
				}

				@Override
				public void on(String event, IOAcknowledge ack, Object... args) {
					Log.d("test ¿ì¼±", "Server said : " + event + " : "
							+ (String) args[0]);
				}
			});
			socket.emit("UUID", uuid);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (socket != null) {
			socket.disconnect();
			socket = null;
		}
		super.onDestroy();
	}

	public boolean onTouch(View v, MotionEvent event) {

		int f = -1;

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			f = 0;
		
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			f = 1;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			f = 2;
		}

		ViewTouchCheck(event.getX(), event.getY());
		if (f != -1 && f != 2) {
			if (socket == null)
				SocketConnection("http://210.118.74.89:13000");

			String send = "";
			switch (v.getId()) {
			case R.id.upBtn:
				send = "upBtn";
				break;

			case R.id.downBtn:
				send = "downBtn";
				break;

			case R.id.leftBtn:
				send = "leftBtn";
				break;

			case R.id.rightBtn:
				send = "rightBtn";
				break;

			default:
				break;
			}
			socket.emit("pad", send, f);
		}
		return false;
	}

	public void ViewTouchCheck(float _x, float _y) {
		int x = (int) _x;
		int y = (int) _y;

		Rect rUp = new Rect();
		up.getGlobalVisibleRect(rUp);
		Rect rDown = new Rect();
		down.getGlobalVisibleRect(rDown);
		Rect rLeft = new Rect();
		left.getGlobalVisibleRect(rLeft);
		Rect rRight = new Rect();
		right.getGlobalVisibleRect(rRight);

		Log.i("TEST", "X " + x + "Y " + y);

	}

	public void BtnSender(View v) {
		if (socket == null)
			SocketConnection("http://210.118.74.89:13000");

		switch (v.getId()) {
		case R.id.aBtn:
			socket.emit("btn", "A");
			break;

		case R.id.bBtn:
			socket.emit("btn", "B");
			break;

		case R.id.xBtn:
			socket.emit("btn", "X");
			break;

		case R.id.yBtn:
			socket.emit("btn", "Y");
			break;

		default:
			break;
		}
	}
}
