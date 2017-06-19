package vn.manroid.devchat.data;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import vn.manroid.devchat.R;
import vn.manroid.devchat.activity.ChatActivity;
import vn.manroid.devchat.activity.ListUserActivity;
import vn.manroid.devchat.model.ChatModel;
import vn.manroid.devchat.model.UserModel;

/**
 * Created by manro on 13/06/2017.
 */

public class LoadImageFromFacebook extends AsyncTask<String,Void,Bitmap> {
    private UserModel model;
    private ChatModel chatModel;
    private Bitmap bitmap;
    private int position;
    private ProgressDialog dialog = ListUserActivity.progressDialog;

    public Bitmap getBitmapAvatar(int position){
        model = ListUserActivity.adapter.getItem(position);
        return doInBackground(model.getPhotoURL());
    }

    public Bitmap getBitMapChatModel(){
        return doInBackground(ChatActivity.md.getPhotoURLCurrentUser());
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        try {

            URL url = new URL(params[position]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(60000);
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;

    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        dialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(ListUserActivity.activity);
        dialog.setTitle("DevChat");
        dialog.setIcon(R.drawable.icon);
        dialog.setMessage("Đang tải dữ liệu ... !!!");
        dialog.setCancelable(false);
        dialog.setIndeterminate(false);
        dialog.show();
    }
}
