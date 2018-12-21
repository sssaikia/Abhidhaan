package sstudio.com.abhidhaan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

public class Main2Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ((ImageView)findViewById(R.id.imageView2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crdt();
            }
        });
    }

    public void crdt() {
        MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(this);
        SpannableString s = SpannableString.valueOf("The database was obtained " +
                "from xobdo.org");
        TextView tv = new TextView(this);
        Linkify.addLinks(s, Linkify.WEB_URLS);
        tv.setText(s);
        tv.setTextSize(14);
        builder.setPositiveText("ok").withDialogAnimation(true);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setPadding(30, 15, 30, 30);

        builder.setIcon(R.drawable.splash);
        builder.setCustomView(tv);
        MaterialStyledDialog b = builder.build();
        b.show();
    }

   /* public void contct() {
        MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(this);
        SpannableString s = SpannableString.valueOf("Abhidhaan is developed by sstudio." +
                " For support or feedback mail to contact.mail.sstudio@gmail.com\n" +
                "Thank you.");
        TextView tv = new TextView(this);
        Linkify.addLinks(s, Linkify.EMAIL_ADDRESSES);
        tv.setText(s);
        tv.setTextSize(14);
        //builder.setTitle("Contact sstudio.");
        builder.setPositiveText("ok");
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        //tv.setPaddingRelative(20,13,4,20);
        tv.setPadding(30, 20, 20, 30);
        builder.setHeaderDrawable(R.drawable.logo);
        builder.setHeaderScaleType(ImageView.ScaleType.FIT_XY);
        builder.setCustomView(tv);
        builder.withDialogAnimation(true);
        builder.setHeaderColor(R.color.background);
        MaterialStyledDialog b = builder.build();
        b.show();
    }
*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
