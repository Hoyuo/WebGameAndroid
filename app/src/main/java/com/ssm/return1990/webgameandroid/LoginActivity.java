package com.ssm.return1990.webgameandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        setContentView(R.layout.activity_login);
        startActivity(new Intent(this, Splash_Activity.class));
        email = (EditText) findViewById(R.id.idInput);
        password = (EditText) findViewById(R.id.passwordInput);
    }

    public void LoginButtonClick(View v) {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    email.setText("");
                    password.setText("");
                    Toast.makeText(LoginActivity.this, "LoginError",
                            Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                } else if(msg.what == 2) {
                    email.setText("");
                    password.setText("");
                    Toast.makeText(LoginActivity.this, "Server Disconncet",
                            Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                }
            }
        };

        Thread thread = new Thread() {
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();

                String urlString = Server.IP + "/LOGINMOBILE";
                try {
                    URI url = new URI(urlString);

                    HttpPost httpPost = new HttpPost();
                    httpPost.setURI(url);

                    List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(
                            2);
                    nameValuePairs.add(new BasicNameValuePair("userId", email
                            .getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("password",
                            password.getText().toString()));

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);
                    String responseString = EntityUtils.toString(
                            response.getEntity(), HTTP.UTF_8);

                    JSONObject responseJSON = new JSONObject(responseString);

                    Log.d("Login", "진행중 ");

                    Log.d("Test", responseJSON.get("status").toString());

                    if (responseJSON.get("status").toString().equals("200")) {
                        setPreference();
                        Intent i = new Intent();
                        i.setClassName("com.ssm.return1990.webgameandroid",
                                "com.ssm.return1990.webgameandroid.MainActivity");
                        startActivity(i);
                        finish();
                        interrupt();
                    } else {
                        Message message = Message.obtain();
                        message.what = 1;
                        handler.sendMessage(message);
                        interrupt();
                    }
                } catch(HttpHostConnectException e1) {
                    Message message = Message.obtain();
                    message.what = 2;
                    handler.sendMessage(message);
                } catch (URISyntaxException | JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void setPreference() {
        SharedPreferences prefs = getSharedPreferences("userId", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userId", email.getText().toString());
        editor.apply();
        Server.userId = email.getText().toString();
    }
}
