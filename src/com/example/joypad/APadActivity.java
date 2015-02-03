package com.example.joypad;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class APadActivity extends Activity {

	SocketIO socket = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		setContentView(R.layout.activity_apad);
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
		if (socket != null)
			socket.disconnect();

		super.onDestroy();
	}

	public void PadSender(View v) {
		if (socket == null)
			SocketConnection("http://210.118.74.89:13000");

		switch (v.getId()) {
		case R.id.upBtn:
			socket.emit("btn", "up");
			break;

		case R.id.downBtn:
			socket.emit("btn", "down");
			break;

		case R.id.leftBtn:
			socket.emit("btn", "left");
			break;

		case R.id.rightBtn:
			socket.emit("btn", "right");
			break;

		default:
			break;
		}
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
