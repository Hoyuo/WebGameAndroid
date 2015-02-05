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
import android.view.View;

import com.example.joyPadView.JoystickView;
import com.example.joyPadView.JoystickView.OnJoystickMoveListener;

public class DPadActivity extends Activity {
	SocketIO socket = null;
	private JoystickView joystick;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		setContentView(R.layout.activity_dpad);

		joystick = (JoystickView) findViewById(R.id.dpadJoyStickView);
		joystick.setOnJoystickMoveListener(joystickMoveListener,
				JoystickView.DEFAULT_LOOP_INTERVAL);
	}

	JoystickView.OnJoystickMoveListener joystickMoveListener = new OnJoystickMoveListener() {
		public void onValueChanged(int angle, int power, int direction) {
			// TODO Auto-generated method stub
			Log.d("DAPD", " " + String.valueOf(angle) + "¡Æ");
		}
	};

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
