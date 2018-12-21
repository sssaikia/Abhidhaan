package sstudio.com.abhidhaan;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import java.io.IOException;
import java.util.List;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class MainActivity extends AppCompatActivity {
    Dbassist dbHeplper;
    Button button;
    AutoCompleteTextView editText;
    TextView textview;
    String req;
    ListView lvUsers;
    ListAdapter adapter;
    ToggleButton switchB;
    boolean runService = true, langE = true, firstrun = true;
    ImageButton setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        editText = (AutoCompleteTextView) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        textview = (TextView) findViewById(R.id.textview2);
        lvUsers = (ListView) findViewById(R.id.lvUsers);
        SharedPreferences pref = getSharedPreferences("firstRun", Activity.MODE_PRIVATE);
        firstrun = pref.getBoolean("firstRun", true);
        switchB = (ToggleButton) findViewById(R.id.switchabc);
        if (firstrun) {
            tapTerget(switchB);
        }

        if (runService) {
            Intent in = new Intent(this, ClipService.class);
            startService(in);
        }
        switchB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator.ofFloat(view, "rotation", 0, 360).start();
            }
        });
        setting = (ImageButton) findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setting();
            }
        });
        switchB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    editText.setHint("Enter a word.");
                    button.setText("Search");
                    langE = true;
                    Toast.makeText(MainActivity.this, "Mode: Eng to Asm", Toast.LENGTH_SHORT).show();
                } else {
                    editText.setHint("শব্দটো লিখক.");
                    button.setText("সন্ধান");
                    langE = false;
                    Toast.makeText(MainActivity.this, "Mode: Asm to Eng", Toast.LENGTH_SHORT).show();
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                req = editable.toString();
                if (!req.isEmpty() && !editText.getText().toString().trim().equals("")) {
                    List<String> listUsers = dbHeplper.getUsers(req, langE);
                    if (listUsers != null) {
                        ArrayAdapter<String> nn = new ArrayAdapter<String>(getApplicationContext()
                                , android.R.layout.select_dialog_item, listUsers);
                        editText.setAdapter(nn);
                        editText.setThreshold(1);

                    }
                }
            }
        });
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().trim().equals(""))
                    editText.showDropDown();
            }
        });
        editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                button.callOnClick();
                Log.e("onclicked", "called");
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                req = editText.getText().toString().trim();
                if (!req.isEmpty() && !editText.getText().toString().trim().equals("")) {
                    List<String> listUsers = dbHeplper.getAllUsers(req, langE);
                    if (listUsers != null) {
                        adapter = new ArrayAdapter<String>(getApplicationContext(),
                                R.layout.layout, R.id.textview2,
                                listUsers);
                        lvUsers.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Type a word.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dbHeplper = new Dbassist(getApplicationContext());
        try {
            dbHeplper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSearchPrompt(int view) {
        new MaterialTapTargetPrompt.Builder(MainActivity.this)
                .setPrimaryText("Enable or disble service.")
                .setSecondaryText("Here you can manage the setting " +
                        "for the smart finding of word meanings on any " +
                        "screen/App across your device using the 'Smart Lex' feature. ")
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setTarget(findViewById(view))
                .setBackgroundColour(ContextCompat.getColor(this, R.color.colorPrimary))
                .setFocalColour(ContextCompat.getColor(this, R.color.focalColor))
                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                        if (tappedTarget) {
                            setting();
                        }
                    }

                    @Override
                    public void onHidePromptComplete() {

                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Intent intent = new Intent(this, Main2Activity.class);
                startActivity(intent);
                return true;
            case R.id.setting:
                setting();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    boolean doubleBackToExitPressedOnce=false;
    @Override
    public void onBackPressed() {
        if (!runService) {
            stopService(new Intent(this, ClipService.class));
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            try {
                am.killBackgroundProcesses("sstudio.com.abhidhaan");
            } catch (Exception e) {
                Log.e("kill service failed:", "" + e);
            }
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void setting() {
        View checkBoxSetting = View.inflate(this, R.layout.keep_service_check_box, null);
        CheckBox checkBox1 = (CheckBox) checkBoxSetting.findViewById(R.id.checkboxSetting);
        MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(this);
        checkBox1.setChecked(true);
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    runService = true;
                } else {
                    runService = false;
                }
            }
        });
        builder.setTitle("Smart lexicon?");
        builder.setIcon(R.drawable.splash);
        builder.withIconAnimation(true);
        builder.setDescription("\nEnabling this will help you to find meanings to any word in any window.\n" +
                "just select and copy a word to find the meaning using Abhidhaan.");
        builder.setCustomView(checkBoxSetting);
        MaterialStyledDialog dialog = builder.build();
        dialog.show();
    }

    public void tapTerget(ToggleButton i) {

        SharedPreferences pref = getSharedPreferences("firstRun", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("firstRun", false);
        editor.apply();
        new MaterialTapTargetPrompt.Builder(this)
                .setPrimaryText("Switch language here.")
                .setAnimationInterpolator(new AccelerateInterpolator())
                .setSecondaryText("Click on this button to alternate your preference between Assamese and English.")
                .setTarget(i)
                .setFocalColour(ContextCompat.getColor(this, R.color.focalColor))
                .setBackgroundColour(ContextCompat.getColor(this, R.color.colorPrimary))
                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {

                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {

                    }

                    @Override
                    public void onHidePromptComplete() {
                        showSearchPrompt(R.id.setting);
                    }
                }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

