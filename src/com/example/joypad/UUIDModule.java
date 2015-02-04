package com.example.joypad;

import android.content.Context;
import android.telephony.TelephonyManager;
import java.util.UUID;

public class UUIDModule {
	public static String CreateUUID(Context ctx) {
		final TelephonyManager mTelephonyManager = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + mTelephonyManager.getDeviceId();
		tmSerial = "" + mTelephonyManager.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						ctx.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(((long) androidId.hashCode()),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String deviceId = deviceUuid.toString();

		return deviceId;
	}
}
