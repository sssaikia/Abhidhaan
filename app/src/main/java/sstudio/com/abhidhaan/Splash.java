package sstudio.com.abhidhaan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Alan on 1/14/2017.
 */

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                stuff();
                finish();
            }
        }).start();
    }
    public void stuff(){
        /*for (int i=0;i<50;i+=5){
            try {
                Thread.sleep(100);
                Log.e("hndlr","called"+i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        Intent sartapp=new Intent(this,MainActivity.class);
        startActivity(sartapp);
    }
}
