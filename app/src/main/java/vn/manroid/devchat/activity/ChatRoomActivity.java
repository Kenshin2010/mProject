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
import vn.manroid.devchat.adapter.ChatRoomAdapter;
import vn.manroid.devchat.emoji.Emojicon;
import vn.manroid.devchat.model.ChatRoomModel;

public class ChatRoomActivity extends AppCompatActivity
        implements EmojiconGridFragment.OnEmojiconClickedListener
        , EmojiconsFragment.OnEmojiconBackspaceClickedListener {

    private EmojiconEditText mEditEmojicon;
    private FrameLayout frameLayout;
    private int count = 0;
    private Button btnSend, btnShowEmoji;
    private ListView lvChatRoom;

    private FirebaseUser me;
    public static ChatRoomModel chatRoomModel;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private ChatRoomAdapter adapter;
    private ArrayList<ChatRoomModel> mdRoom;

    private String stringUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        // khóa bàn phím
        //========================================================================//

        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        //========================================================================//
        //fix error load image avatar
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        // show custom actionbar
        //========================================================================//

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fffcd03c")));
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("Phòng chat");

        ImageButton imageButton = (ImageButton) mCustomView
                .findViewById(R.id.imageView1);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        //========================================================================//

        initView();
        //========================================================================//

        btnShowEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if (count == 1) {
                    frameLayout.setVisibility(View.VISIBLE);
                    setEmojiconFragment(false);
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                } else {
                    frameLayout.setVisibility(View.GONE);
                    count = 0;
                }
            }
        });

        me = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("ListChatRoom");
        reference.orderByChild("ngay").addChildEventListener(onChildChanged);

        adapter = new ChatRoomAdapter(new ArrayList<ChatRoomModel>(),getLayoutInflater());

        lvChatRoom.setAdapter(adapter);

        btnSend.setOnClickListener(onChattingRoom);

    }


                // CREATE FIREBASE
    //============================================================================//

    private View.OnClickListener onChattingRoom = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try {
                String str = "";
                str = mEditEmojicon.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    mEditEmojicon.setError("Nhập văn bản bạn muốn gửi!");
                    return;
                }

                FirebaseUser me = FirebaseAuth.getInstance().getCurrentUser();

                UUID uuid = UUID.randomUUID();
                String key = uuid.toString();

                //================================================================================//

                Uri uri = me.getPhotoUrl();
                if (uri==null){
                    stringUri="https://www.drupal.org/files/styles/drupalorg_user_picture/public/default-avatar.png?itok=qMUyWcaa";
                }else {
                    stringUri = uri.toString();
                }

                //======================================================//

                ChatRoomModel md = new ChatRoomModel(me.getUid(),me.getDisplayName()
                ,mEditEmojicon.getText().toString(), Calendar.getInstance().getTimeInMillis(),stringUri);

                reference.child(key).setValue(md);
                mEditEmojicon.setText("");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private ChildEventListener onChildChanged = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            try {

                if (dataSnapshot != null){
                    FirebaseUser me = FirebaseAuth.getInstance().getCurrentUser();
                    Uri uri = me.getPhotoUrl();
                    if (uri==null){
                        stringUri="https://www.drupal.org/files/styles/drupalorg_user_picture/public/default-avatar.png?itok=qMUyWcaa";
                    }else {
                        stringUri = uri.toString();
                    }
                }

                HashMap<String, String> maps = (HashMap<String, String>) dataSnapshot.getValue();

                chatRoomModel = new ChatRoomModel(maps.get("idGuiRoom")
                        ,maps.get("idTenGuiRoom")
                        ,maps.get("thongDiepRoom")
                        ,0
                        ,stringUri);

                Log.d("chatroom","============="+chatRoomModel.getThongDiepRoom());

                adapter.getListData().add(chatRoomModel);
                adapter.notifyDataSetChanged();

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
//============================================================================//

    private void initView() {

        lvChatRoom = (ListView) findViewById(R.id.lvChatRoom);
        mEditEmojicon = (EmojiconEditText) findViewById(R.id.edtMessage);
        btnShowEmoji = (Button) findViewById(R.id.btnEmoji);
        btnSend = (Button) findViewById(R.id.btnSend);
        frameLayout = (FrameLayout) findViewById(R.id.emojicons);

    }

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
