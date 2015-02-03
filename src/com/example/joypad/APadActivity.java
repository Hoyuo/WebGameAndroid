package com.example.joypad;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class APadActivity extends Activity implements OnTouchListener {

	SocketIO socket = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		setContentView(R.layout.activity_apad);
		Button up = (Button) findViewById(R.id.upBtn);
		Button down = (Button) findViewById(R.id.downBtn);
		Button left = (Button) findViewById(R.id.leftBtn);
		Button right = (Button) findViewById(R.id.rightBtn);
		up.setOnTouchListener(this);
		down.setOnTouchListener(this);
		left.setOnTouchListener(this);
		right.setOnTouchListener(this);
	}

	private void SocketConnection(String server) {
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
					System.out.println("Server said: " + data);
				}

				@Override
				public void onError(SocketIOException socketIOException) {
					System.out.println("an Error occured");
					socketIOException.printStackTrace();
				}

				@Override
				public void onDisconnect() {
					System.out.println("Connection terminated.");
				}

				@Override
				public void onConnect() {
					System.out.println("Connection established");
				}

				@Override
				public void on(String event, IOAcknowledge ack, Object... args) {
					System.out
							.println("Server triggered event '" + event + "'");
				}
			});
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

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int f = -1;

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			f = 0;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			f = 1;
		}

		if (f != -1) {
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
