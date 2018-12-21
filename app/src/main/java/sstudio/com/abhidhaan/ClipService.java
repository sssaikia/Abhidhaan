package sstudio.com.abhidhaan;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class ClipService extends Service {

    public ClipService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager mgr = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wakeLock.acquire();
        Log.e("service", "started");
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipboardManager.OnPrimaryClipChangedListener mPrimaryChangeListener = new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData.Item clipData = clipboard.getPrimaryClip().getItemAt(0);
                String req = clipData.getText() + "";
                req = req.trim();
                //Toast.makeText(getApplicationContext(), "Text copied.. "+req, Toast.LENGTH_SHORT).show();
                // this will be called whenever you copy something to the clipboard
                Log.e("clipboardmanager", "after copydetected");
                if (!req.contains(" ")) {
                    Intent intent = new Intent(ClipService.this, Main3Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(ClipService.this, "Abhidhaan: contains space, aborted!", Toast.LENGTH_SHORT).show();
                }
                //overridePendingTransition(R.anim.slide_up_out, slide_up);
                Log.e("after", " activity called" + req);
            }
        };

        clipboard.addPrimaryClipChangedListener(mPrimaryChangeListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
