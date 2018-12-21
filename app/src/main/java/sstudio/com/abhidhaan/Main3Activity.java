package sstudio.com.abhidhaan;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class Main3Activity extends Activity {
    EditText e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        setTheme(R.style.dialog);
        Log.e("host android version :", "" + android.os.Build.VERSION.SDK_INT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.x = 0;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.y = 0;
        this.getWindow().setAttributes(params);
        e = (EditText) findViewById(R.id.editD);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData.Item clipData = clipboard.getPrimaryClip().getItemAt(0);
        String req = clipData.getText() + "";
        getMean(req);
        Button button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMean(e.getText().toString());
            }
        });

    }

    public void getMean(String req) {
        Log.e("req", "" + req);
        EditText edit = (EditText) findViewById(R.id.editD);
        edit.setText(req);
        Dbassist dbHeplper;
        dbHeplper = new Dbassist(Main3Activity.this);
        boolean langE = true;
        List<String> listUsers = dbHeplper.getAllUsers(req.trim(), langE);
        if (listUsers != null) {
            ListView listviewm = (ListView) findViewById(R.id.lvdiag);
            ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.layout, R.id.textview2,
                    listUsers);
            listviewm.setAdapter(adapter);
            if (listviewm.getItemAtPosition(0).toString().contains("No results found.")) {
                Toast.makeText(this, "Abhidhaan: No results found.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.push_up_out, R.anim.push_up_in);
        super.onBackPressed();
    }
}
