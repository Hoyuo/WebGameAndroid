package com.example.joypad;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class Communication {

	SocketIO socket = null;
	Context ctx;
	String URL;
	String userId;

	public Communication(Context ctx, String URL, String userId) {
		this.ctx = ctx;
		this.URL = URL;
		this.userId = userId;
		SocketConnection();
	}

	private void SocketConnection() {
		try {
			socket = new SocketIO(URL);
			socket.connect(new IOCallback() {
				@Override
				public void onMessage(JSONObject json, IOAcknowledge ack) {
					try {
						Log.d("onMessage", "Server said:" + json.toString(2));
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
				}
			});
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String uuid = UUIDModule.CreateUUID(ctx);
		socket.emit("UUID", uuid, userId);
	}

	public void emit(String event, Object... args) {

		if (socket == null || socket.isConnected() == false)
			SocketConnection();

		socket.emit(event, args);
	}

	public void close() {
		socket.disconnect();
	}

}
