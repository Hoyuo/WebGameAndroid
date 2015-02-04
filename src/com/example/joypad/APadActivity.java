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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

import com.example.joyPadView.APadView;

public class APadActivity extends Activity implements OnTouchListener {
	LinearLayout aPad;
	APadView dpc;

	int direction = APadView.D_NONE;
	SocketIO socket = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		setContentView(R.layout.activity_apad);

		dpc = new APadView(R.drawable.dpad_normal, R.drawable.dpad_up,
				R.drawable.dpad_down, R.drawable.dpad_left,
				R.drawable.dpad_right);

		aPad = (LinearLayout) findViewById(R.id.layoutAPad);
		aPad.setOnTouchListener(this);
	}

	public boolean onTouch(View v, MotionEvent event) {
		dpc.onTouch(v, event);
		direction = dpc.getDirection();
		String send = null;
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
		Log.d("Test", send);

		return true;
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
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String uuid = UUIDModule.CreateUUID(getApplicationContext());
		socket.emit("UUID", uuid);
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

	// if (socket == null)
	// SocketConnection("http://210.118.74.89:13000");
	// socket.emit("pad", direction, 0);

	public void BtnSender(View v) {
		if (socket == null)
			SocketConnection("http://210.118.74.89:13000");

		switch (v.getId()) {
		case R.id.btnA:
			socket.emit("btn", "A");
			break;

		case R.id.btnB:
			socket.emit("btn", "B");
			break;

		case R.id.selectBtn:
			socket.emit("btn", "select");
			break;

		case R.id.startBtn:
			socket.emit("btn", "start");
			break;

		default:
			break;
		}
	}
}
