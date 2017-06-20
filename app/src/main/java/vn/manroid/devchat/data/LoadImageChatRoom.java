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
import vn.manroid.devchat.activity.ChatRoomActivity;
import vn.manroid.devchat.model.ChatRoomModel;

/**
 * Created by manro on 20/06/2017.
 */

public class LoadImageChatRoom extends AsyncTask<String,Void,Bitmap> {
    private ChatRoomModel model;
    private Bitmap bitmap;
    private int position;

    public Bitmap getAvatarChatRoom(int position){
        model = ChatRoomActivity.adapter.getItem(position);
        return doInBackground(model.getPhotoUserChatRoom());
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
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
}

