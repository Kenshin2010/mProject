package vn.manroid.devchat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.manroid.devchat.R;
import vn.manroid.devchat.data.LoadImageFromFacebook;
import vn.manroid.devchat.model.ChatModel;
import vn.manroid.devchat.model.UserModel;

/**
 * Created by manro on 12/06/2017.
 */

public class ChatBaseAdapter extends BaseAdapter {


    public ArrayList<ChatModel> getData() {
        return data;
    }

    public void setData(ArrayList<ChatModel> data) {
        this.data = data;
    }

    private ArrayList<ChatModel> data;
    private LayoutInflater inflater;
    private UserModel friend;

    public ChatBaseAdapter(ArrayList<ChatModel> data, LayoutInflater inflater, UserModel friend) {
        this.data = data;
        this.inflater = inflater;
        this.friend = friend;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ChatModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ChatModel data = getItem(position);
        //Lọc ra xem data này là của nó hay của mình

        LoadImageFromFacebook facebook = new LoadImageFromFacebook();
        facebook.execute(friend.getPhotoURL());

        if (data.getIdGui().equals(friend.getUserID())) {
            //Người gửi đến mình
            view = inflater.inflate(R.layout.nguoinhan_item, null);
            ((ImageView)view.findViewById(R.id.imgAvatarChattingNguoiNhan)).setImageBitmap(facebook.getBitmapAvatar(friend.getCurrentPosition()));


        } else {
                //Mình gửi đến nó
                view = inflater.inflate(R.layout.nguoigui_item, null);
                LoadImageFromFacebook facebook2 = new LoadImageFromFacebook();
                facebook2.execute(data.getPhotoURLCurrentUser());
                ((ImageView) view.findViewById(R.id.imgAvatarChattingNguoiGui)).setImageBitmap(facebook2.getBitMapChatModel());

        }
        if (data.getTenGui() == null || data.getTenNhan() == null){
            ((TextView) view.findViewById(R.id.txtValue)).
                    setText("Người lạ " + ": " + data.getThongDiep());
        }else {
            ((TextView) view.findViewById(R.id.txtValue)).
                    setText(data.getTenGui() + ": " + data.getThongDiep());
        }


        return view;
    }
}
