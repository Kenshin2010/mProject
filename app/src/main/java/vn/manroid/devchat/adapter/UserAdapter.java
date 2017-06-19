package vn.manroid.devchat.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.manroid.devchat.R;
import vn.manroid.devchat.data.LoadImageFromFacebook;
import vn.manroid.devchat.model.UserModel;

/**
 * Created by manro on 12/06/2017.
 */

//public class UserAdapter extends ArrayAdapter<UserModel> {
//    public UserAdapter(@NonNull Context context,
//                       @LayoutRes int resource,
//                       @NonNull ArrayList<UserModel> objects) {
//        super(context, resource, objects);
//    }
//}

public class UserAdapter extends BaseAdapter{

    Bitmap bitmap;
    LayoutInflater inflater;
    ArrayList<UserModel> listUser;

    public UserAdapter(LayoutInflater inflater, ArrayList<UserModel> listUser) {
        this.inflater = inflater;
        this.listUser = listUser;
    }

    @Override
    public int getCount() {
        return listUser.size();
    }

    @Override
    public UserModel getItem(int position) {
        return listUser.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null){
            view = inflater.inflate(R.layout.item_user,null);
        }

        ImageView imgAvatarUser = (ImageView) view.findViewById(R.id.imgAvatar);
        ImageView imgStatus = (ImageView) view.findViewById(R.id.imgStatus);
        TextView txtUserName = (TextView) view.findViewById(R.id.txtUserName);

        UserModel model = (UserModel) getItem(position);

        LoadImageFromFacebook facebook = new LoadImageFromFacebook();
        facebook.execute(model.getPhotoURL());

        txtUserName.setText(model.getName());

        model.setCurrentPosition(position);

        imgAvatarUser.setImageBitmap(facebook.getBitmapAvatar(position));

        if (model.isOnline()){
            imgStatus.setImageResource(R.drawable.online);
        }else {
            imgStatus.setImageResource(R.drawable.offline);

        }

        return view;
    }
}