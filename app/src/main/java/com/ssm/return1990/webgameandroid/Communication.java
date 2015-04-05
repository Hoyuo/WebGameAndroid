package com.ssm.return1990.webgameandroid;

import android.content.Context;
import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;


public class Communication {
    private Context ctx;
    private String URL;
    private String userId;

    private Socket mSocket;

    {
        try {
            mSocket = IO.socket(Server.IP);
        } catch (URISyntaxException e) {
        }
    }

    public Communication(Context ctx, String URL, String userId) {
        this.ctx = ctx;
        this.URL = URL;
        this.userId = userId;
        SocketConnection();
    }

    private void SocketConnection() {
        mSocket.connect();
    }

    public void emit(String event, Object... args) {
        if (mSocket == null || !mSocket.connected())
            SocketConnection();
        if (args.length == 1)
            mSocket.emit(event, userId, args[0].toString());
        else
            mSocket.emit(event, userId, args[0].toString(), args[1].toString());
    }

    public void close() {
        mSocket.disconnect();
    }

}
