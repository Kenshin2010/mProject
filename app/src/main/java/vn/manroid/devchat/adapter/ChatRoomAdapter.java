package vn.manroid.devchat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import vn.manroid.devchat.R;
import vn.manroid.devchat.data.LoadImageChatRoom;
import vn.manroid.devchat.model.ChatRoomModel;

/**
 * Created by manro on 20/06/2017.
 */

public class ChatRoomAdapter extends BaseAdapter {

    public ArrayList<ChatRoomModel> getListData() {
        return listData;
    }

    public void setListData(ArrayList<ChatRoomModel> listData) {
        this.listData = listData;
    }

    private ArrayList<ChatRoomModel> listData;
    private LayoutInflater inflater;

    public ChatRoomAdapter(ArrayList<ChatRoomModel> listData, LayoutInflater inflater) {
        this.listData = listData;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public ChatRoomModel getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = inflater.inflate(R.layout.chat_room_item, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imgAvatarChattingRoom);

        TextView textView = (TextView) view.findViewById(R.id.txtTextChatRoom);

        ChatRoomModel md = getItem(position);

        LoadImageChatRoom room = new LoadImageChatRoom();
        room.execute(md.getPhotoUserChatRoom());

        imageView.setImageBitmap(room.getAvatarChatRoom(position));

        if (md.getIdTenGuiRoom() == null)
            textView.setText("Người lạ" + " : \n" + md.getThongDiepRoom());
        else
            textView.setText(md.getIdTenGuiRoom() + " : \n" + md.getThongDiepRoom());

        Random r = new Random();
        int num = r.nextInt(10);

        switch (num) {
            case 0:
                textView.setTextColor(view.getResources().getColor(R.color.xanhdatroi));
                break;
            case 1:
                textView.setTextColor(view.getResources().getColor(R.color.mautim));
                break;
            case 2:
                textView.setTextColor(view.getResources().getColor(R.color.maudo));
                break;
            case 3:
                textView.setTextColor(view.getResources().getColor(R.color.mauhong));
                break;
            case 4:
                textView.setTextColor(view.getResources().getColor(R.color.maunau));
                break;
            case 5:
                textView.setTextColor(view.getResources().getColor(R.color.vang));
                break;

        }

        return view;
    }

}
