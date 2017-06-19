package vn.manroid.devchat.activity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import vn.manroid.devchat.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtFont,txtMarqueeText;
    private Button btnStartChat,btnShare,btnInfor,btnLogOut;
    private Intent intent;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txtFont = (TextView) findViewById(R.id.txtFontMain);
        txtMarqueeText = (TextView) findViewById(R.id.MarqueeText);

        txtFont.setTypeface(Typeface.createFromAsset(getAssets(),
                "myfont.ttf"));
        txtMarqueeText.setSelected(true);


        btnStartChat = (Button) findViewById(R.id.btnStartChat);
        btnInfor = (Button) findViewById(R.id.btnInfor);
        btnShare = (Button) findViewById(R.id.btnShare);
        btnLogOut = (Button) findViewById(R.id.btnLogOut);

        btnStartChat.setOnClickListener(this);
        btnInfor.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnStartChat:

                intent = new Intent(MainActivity.this,ListUserActivity.class);
                startActivity(intent);
                break;

            case R.id.btnInfor:
                Toast.makeText(this, "Ứng dụng được phát triển bởi Thư Nguyễn", Toast.LENGTH_SHORT).show();
                String url = null;
                Intent intent = null;
                url = "https://www.facebook.com/profile.php?id=100007837202373";
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "Couldn't launch the bug reporting website", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btnShare:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
                {
                    String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this); // Need to change the build to API 19

                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Chia sẻ ứng dụng cho bạn bè ....!!!");

                    if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
                    // any app that support this intent.
                    {
                        sendIntent.setPackage(defaultSmsPackageName);
                    }
                    startActivity(sendIntent);

                } else // For early versions, do what worked for you before.
                {
                    Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", "phoneNumber");
                    smsIntent.putExtra("sms_body", "message");
                    startActivity(smsIntent);
                }
                break;

            case R.id.btnLogOut:
                try {
                    LogInActivity.sendOjectLoginSuccessfull(false);

                    LogInActivity.sendObjectFacebookSuccessfull(false);

                    FirebaseAuth.getInstance().signOut();

                    //Logout facebook
                    LoginManager.getInstance().logOut();

                    intent = new Intent(v.getContext(),LogInActivity.class);
                    startActivity(intent);

                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);
            builder.setIcon(getResources().getDrawable(R.drawable.icon));
            builder.setTitle("ChatDev");
            builder.setCancelable(false);
            builder.setMessage("Bạn muốn thoát khỏi ứng dụng ???");
            //Thiết lập các button
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            //Nạp giao diện
            dialog = builder.create();
            //Hiển thị dialog lên
            dialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
