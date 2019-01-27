package vn.manroid.devchat.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;

import vn.manroid.devchat.R;
import vn.manroid.devchat.adapter.UserAdapter;
import vn.manroid.devchat.model.UserModel;

public class ListUserActivity extends AppCompatActivity {

    public static ArrayList<UserModel> ds;
    public static ProgressDialog progressDialog;
    public static Activity activity;
    public static UserAdapter adapter;
    private GridView gvUser;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //===============================================//
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fffcd03c")));
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("Danh sách bạn chat");

        ImageButton imageButton = (ImageButton) mCustomView
                .findViewById(R.id.imageView1);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        //===============================================//

        activity = this;

        //===============================================//

        if (!checkNetwork()) {
            Toast.makeText(this, "Vui lòng kiểm tra lại internet", Toast.LENGTH_SHORT).show();
        }


        gvUser = (GridView) findViewById(R.id.gvUser);
        gvUser.setOnItemClickListener(onItemClick);
        database = FirebaseDatabase.getInstance();

        database.getReference("ListUser").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    ds = new ArrayList<UserModel>();
                    Gson gs = new Gson();
                    for (DataSnapshot child :
                            dataSnapshot.getChildren()) {
                        String json = child.getValue().toString();
                        ds.add(gs.fromJson(json, UserModel.class));

                    }

                    adapter = new UserAdapter(getLayoutInflater(), ds);

                    gvUser.setAdapter(adapter);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private AdapterView.OnItemClickListener onItemClick =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent chat = new Intent(ListUserActivity.this, ChatActivity.class);
                    chat.putExtra("ITEM", adapter.getItem(position));
                    startActivity(chat);
                }
            };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkNetwork() {
        boolean available = false;

        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null &&
                info.isAvailable() &&
                info.isConnected()) {

            available = true;
        }
        return available;
    }
}
