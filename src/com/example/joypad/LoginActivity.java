package com.example.joypad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	EditText email;
	EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		startActivity(new Intent(this, Splash_Activity.class));
		Button loginBtn = (Button) findViewById(R.id.loginBtn);
		email = (EditText) findViewById(R.id.idInput);
		password = (EditText) findViewById(R.id.passwordInput);

		loginBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Thread thread = new Thread() {
					public void run() {
						HttpClient httpClient = new DefaultHttpClient();

						String urlString = "http://210.118.74.89:3000/LOGINMOBILE";
						try {
							URI url = new URI(urlString);

							HttpPost httpPost = new HttpPost();
							httpPost.setURI(url);

							List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(
									2);
							nameValuePairs.add(new BasicNameValuePair("userId",
									email.getText().toString()));
							nameValuePairs.add(new BasicNameValuePair(
									"password", password.getText().toString()));

							httpPost.setEntity(new UrlEncodedFormEntity(
									nameValuePairs));

							HttpResponse response = httpClient
									.execute(httpPost);
							String responseString = EntityUtils.toString(
									response.getEntity(), HTTP.UTF_8);
							BufferedReader reader = new BufferedReader(
									new InputStreamReader(response.getEntity()
											.getContent(), "UTF-8"));
							String json = reader.readLine();
							JSONTokener tokener = new JSONTokener(json);
							JSONArray finalResult = new JSONArray(tokener);

							Log.d("Test", finalResult.getString(1));

						} catch (URISyntaxException e) {
							e.printStackTrace();
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				};

				thread.start();
			}
		});

	}
}
