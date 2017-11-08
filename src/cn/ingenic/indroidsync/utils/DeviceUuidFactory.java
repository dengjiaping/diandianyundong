package cn.ingenic.indroidsync.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class DeviceUuidFactory {
    protected static final String PREFS_FILE = "device_id.xml";
    protected static final String PREFS_DEVICE_ID = "device_id";

    private static UUID uuid;

    public static void initialize(Context context) {
        if( uuid ==null ) {
            synchronized (DeviceUuidFactory.class) {
                if( uuid == null) {
                    final SharedPreferences prefs = context.getSharedPreferences( PREFS_FILE, 0);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null );

                    if (id != null) {
                        // Use the ids previously computed and stored in the prefs file
                        Log.v("DeviceUuidFactory", "uuid exists: " + id);
                        uuid = UUID.fromString(id);

                    } else {

                        final String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

                        // Use the Android ID unless it's broken, in which case fallback on deviceId,
                        // unless it's not available, then fallback on a random number which we store
                        // to a prefs file
                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                                Log.v("DeviceUuidFactory", "gen random uuid: " + uuid);
                            } else {
                                final String deviceId = ((TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE )).getDeviceId();
                                uuid = deviceId!=null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                                Log.v("DeviceUuidFactory", "get device id: " + deviceId);
                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }

                        // Write the value out to the prefs file
                        prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString() ).commit();

                    }

                }
            }
        }

    }

    public static UUID getDeviceUuid() {
        if (uuid == null) {
            throw new IllegalStateException("Not inited");
        }
        return uuid;
    }

    public static String getDeviceSerial() {
        return getDeviceUuid().toString().replace("-", "");
    }
}
