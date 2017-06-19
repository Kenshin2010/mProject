package vn.manroid.devchat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import vn.manroid.devchat.R;
import vn.manroid.devchat.model.UserModel;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSignIn,btnSignUp;
    private EditText edtAcc,edtPass;
    private static Uri uri;
    private LoginButton btnLoginFacebook;
    private CallbackManager callbackManager;
    private static FirebaseAuth mAuthen;
    private TextView txtFont;
    private static FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Moi nguoi sua lai facebook
        FacebookSdk.setApplicationId("479285745752653");
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_log_in);

        btnLoginFacebook = (LoginButton) findViewById(R.id.btnFacebook);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        edtAcc = (EditText) findViewById(R.id.edtAcc);
        edtPass = (EditText) findViewById(R.id.edtPass);

        txtFont = (TextView) findViewById(R.id.txtFontLogIn);
        txtFont.setTypeface(Typeface.createFromAsset(getAssets(),
                "myfont.ttf"));

        if (!checkNetwork()) {
            Toast.makeText(this, "Vui lòng kiểm tra lại internet", Toast.LENGTH_SHORT).show();
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnLoginFacebook.setReadPermissions("email", "public_profile");

        btnLoginFacebook.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        //Đối tượng này hỗ trợ cho việc khởi tọa login trên face
        callbackManager = CallbackManager.Factory.create();

        mAuthen = FirebaseAuth.getInstance();

        //Lắng nghe trạng thái login
        mAuthen.addAuthStateListener(onAuthenStated);

        firebaseDatabase = FirebaseDatabase.getInstance();


    }

    private FirebaseAuth.AuthStateListener onAuthenStated =
            new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if (firebaseAuth.getCurrentUser() != null) {
                        //Tạo user vào firebase thành công
                        Intent main = new Intent(LogInActivity.this, MainActivity.class);
                        startActivity(main);

                        finish();
                    }

                }
            };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnFacebook:
                btnLoginFacebook.registerCallback(callbackManager,
                        login_CallBack);
                break;

            case R.id.btnSignIn:
                try {
                    if (edtAcc.getText().toString().trim().length() ==0
                            && edtPass.getText().toString().length()==0){
                        Toast.makeText(this, "Yêu cầu nhập thông tin !!!", Toast.LENGTH_SHORT).show();
                    }else {
                        new LoginTask().execute(edtAcc.getText().toString().trim()
                                ,edtPass.getText().toString());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btnSignUp:
                try {
                    if (edtAcc.getText().toString().trim().length() ==0
                            && edtPass.getText().toString().length()==0
                            && edtPass.getText().toString().length() < 6){
                        Toast.makeText(this, "Yêu cầu nhập thông tin !!!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "Mật khẩu phải 6 ký tự trở lên", Toast.LENGTH_LONG).show();

                    }else {
                        new RegisterTask().execute(edtAcc.getText().toString().trim()
                        ,edtPass.getText().toString());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    public static void sendOjectLoginSuccessfull(Boolean status){
        try {
            DatabaseReference reference =
                    firebaseDatabase.getReference("ListUser");

            UserModel user = new UserModel();
            FirebaseUser us = mAuthen.getCurrentUser();

            //user.setPhotoURL(us.getPhotoUrl());


            user.setPhotoURL("https://www.drupal.org/files/styles/drupalorg_user_picture/public/default-avatar.png?itok=qMUyWcaa");
            user.setUserID(us.getUid());
            user.setName("Người lạ");
            user.setEmail(us.getEmail());
            user.setOnline(status);

            Gson gs = new Gson();
            reference.child(us.getUid()).setValue(gs.toJson(user));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendObjectFacebookSuccessfull(Boolean status){
        DatabaseReference reference =
                firebaseDatabase.getReference("ListUser");

        UserModel user = new UserModel();
        FirebaseUser us = mAuthen.getCurrentUser();

        // user.setPhotoURL(us.getPhotoUrl());
        if (us!=null){
            String stringUri;
            uri = us.getPhotoUrl();

            if (uri != null){
                stringUri = uri.toString();

                user.setPhotoURL(stringUri);
                user.setUserID(us.getUid());
                user.setName(us.getDisplayName());
                user.setEmail(us.getEmail());
                user.setOnline(status);

                Gson gs = new Gson();
                reference.child(us.getUid()).setValue(gs.toJson(user));
            }

        }
    }

    private FacebookCallback<LoginResult> login_CallBack
            = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            //Login vào face của bạn thành công
            //Như tạo user lên firebase
            AuthCredential credential =
                    FacebookAuthProvider.getCredential(
                            loginResult.getAccessToken().getToken());


            //Tạo User
            mAuthen.signInWithCredential(credential)
                    .addOnCompleteListener(LogInActivity.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    try {
                                        if (task.isSuccessful()) {
                                            //Tạo user vào firebase thành công
                                            sendObjectFacebookSuccessfull(true);
                                            Intent main = new Intent(LogInActivity.this, MainActivity.class);
                                            startActivity(main);

                                            finish();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });


        }

        @Override
        public void onCancel() {

            //Hủy việc login
        }

        @Override
        public void onError(FacebookException error) {

            //Login có lỗi xảy ra
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private ProgressDialog dialog;

    class RegisterTask extends AsyncTask<String,Void,Boolean>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LogInActivity.this);
            dialog.setMessage("Hệ thống đang xử lý ...!!!");
            dialog.setCancelable(false);
            dialog.setTitle("Quản lý tài khoản");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            mAuthen.createUserWithEmailAndPassword(params[0],params[1])
                    .addOnCompleteListener(onRegister_Complete);
            return null;
        }
    }

    class LoginTask extends AsyncTask<String,Void,Boolean>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LogInActivity.this);
            dialog.setMessage("Hệ thống đang xử lý ...!!!");
            dialog.setCancelable(false);
            dialog.setTitle("Quản lý tài khoản");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            mAuthen.signInWithEmailAndPassword(params[0],params[1])
                    .addOnCompleteListener(onLogin_Complete);
            return null;
        }
    }

    private OnCompleteListener onRegister_Complete = new OnCompleteListener() {
        @Override
        public void onComplete(@NonNull Task task) {
            try {
                dialog.dismiss();
                if (task.isComplete() && task.isSuccessful()){
                    sendOjectLoginSuccessfull(true);
                    Intent intent = new Intent(LogInActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(LogInActivity.this, "Tài khoản đã tồn tại !!!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private OnCompleteListener onLogin_Complete = new OnCompleteListener() {
        @Override
        public void onComplete(@NonNull Task task) {
            try {
                FirebaseUser user = mAuthen.getCurrentUser();
                if (user !=null){
                    sendOjectLoginSuccessfull(true);
                    Intent intent = new Intent(LogInActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(LogInActivity.this, "Tài khoản hoặc mật khẩu không đúng!!!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private boolean checkNetwork() {
        boolean available = false;

        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null &&
                info.isAvailable() &&
                info.isConnected()) {
            //Chỉ biết là có tín hiệu mạng
            available = true;
        }
        return available;
    }
}

