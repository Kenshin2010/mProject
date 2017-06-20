package vn.manroid.devchat.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import vn.manroid.devchat.EmojiconEditText;
import vn.manroid.devchat.EmojiconGridFragment;
import vn.manroid.devchat.EmojiconsFragment;
import vn.manroid.devchat.R;
import vn.manroid.devchat.adapter.ChatBaseAdapter;
import vn.manroid.devchat.emoji.Emojicon;
import vn.manroid.devchat.model.ChatModel;
import vn.manroid.devchat.model.UserModel;

import static vn.manroid.devchat.R.id.edtMessage;

public class ChatActivity extends AppCompatActivity
        implements EmojiconGridFragment.OnEmojiconClickedListener
        ,EmojiconsFragment.OnEmojiconBackspaceClickedListener {

    private EmojiconEditText mEditEmojicon;
    private int count = 0;

    private FrameLayout frameLayout;
    private String stringUri;

    private UserModel friend;
    private FirebaseUser me;
    private ChatBaseAdapter adapter;
    private ListView lvChat;
    private Button btnSend, btnShowEmoji;
    public static ChatModel md;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        friend = (UserModel) getIntent().getSerializableExtra("ITEM");

        //===============================================//

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fffcd03c")));
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("Chat với : " + friend.getName());

        ImageButton imageButton = (ImageButton) mCustomView
                .findViewById(R.id.imageView1);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        //===============================================//

        me = FirebaseAuth.getInstance().getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        lvChat = (ListView) findViewById(R.id.lvChat);
        //Cho listview rỗng lên luôn
        adapter = new ChatBaseAdapter(new ArrayList<ChatModel>(), getLayoutInflater(), friend);
        lvChat.setAdapter(adapter);

        mEditEmojicon = (EmojiconEditText) findViewById(edtMessage);

        frameLayout = (FrameLayout) findViewById(R.id.emojicons);
        btnShowEmoji = (Button) findViewById(R.id.btnEmoji);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(onChatting);

        getSupportActionBar().setTitle("Chat với: " + friend.getName());

        //Truy cập đến nút đó luôn
        reference = firebaseDatabase.getReference("ListChatNEW");

        //Lắng nghe xem việc thêm dữ liệu vào nút ListChat đã OK chưa
        reference.orderByChild("ngay").addChildEventListener(onChildChanged);

        //emoji ..............
        btnShowEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if (count==1){
                    frameLayout.setVisibility(View.VISIBLE);
                    setEmojiconFragment(false);
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }else {
                    frameLayout.setVisibility(View.GONE);
                    count=0;
                }
            }
        });

        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    private ChildEventListener onChildChanged = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

//            Log.i("onChildChanged", "Hello");

            try {
                if (dataSnapshot != null) {
                    //Lọc dữ liệu

                    FirebaseUser me = FirebaseAuth.getInstance().getCurrentUser();
                    Uri uri = me.getPhotoUrl();
                    if (uri==null){
                        stringUri="https://www.drupal.org/files/styles/drupalorg_user_picture/public/default-avatar.png?itok=qMUyWcaa";
                    }else {
                        stringUri = uri.toString();
                    }

                    HashMap<String, String> maps = (HashMap<String, String>) dataSnapshot.getValue();

                    //long ngay = Long.valueOf(maps.get("ngay").toString()).longValue();

                    md = new ChatModel(maps.get("idGui"), maps.get("tenGui"), maps.get("idNhan"),
                            maps.get("tenNhan"), maps.get("thongDiep"), 0, stringUri);


                    if ((friend.getUserID().equals(md.getIdGui())
                            && me.getUid().equals(md.getIdNhan())) ||
                            (me.getUid().equals(md.getIdGui())
                                    && friend.getUserID().equals(md.getIdNhan()))) {
                        //Tôi đang chat vơi ông Friend được chọn
                        //Nếu id friend bằng với id gủi của bản ghi nhận về và tôi là người nhận
                        //hoặc ngược lại
                        Log.i("Ngay", md.getTenGui() + " - " + md.getNgay() + " - " + md.getThongDiep());
                        adapter.getData().add(md);
                        adapter.notifyDataSetChanged();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private View.OnClickListener onChatting = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                String strMsg = " ";
                strMsg += mEditEmojicon.getText().toString();
                if (TextUtils.isEmpty(strMsg)) {
                    mEditEmojicon.setError("Nhập văn bản bạn muốn gửi!");
                    return;
                }

                //Add từng bản ghi chat
                FirebaseUser me = FirebaseAuth.getInstance().getCurrentUser();
                //Sinh key tự động sử dụng sinh Guid (SQL Server), thử tìm firebase auto increament id
                UUID uuid = UUID.randomUUID();
                String key = uuid.toString();

                //======================================================//
                Uri uri = me.getPhotoUrl();
                if (uri==null){
                    stringUri="https://www.drupal.org/files/styles/drupalorg_user_picture/public/default-avatar.png?itok=qMUyWcaa";
                }else {
                    stringUri = uri.toString();
                }

                //======================================================//

                ChatModel md = new ChatModel(me.getUid(), me.getDisplayName(),
                        friend.getUserID(), friend.getName(), mEditEmojicon.getText().toString(),
                        Calendar.getInstance().getTimeInMillis(), stringUri);
                reference.child(key).setValue(md);
                //Thêm lên Server rồi
                mEditEmojicon.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }

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

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(mEditEmojicon, emojicon);

    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(mEditEmojicon);
    }

    private void setEmojiconFragment(boolean useSystemDefault) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }

}

