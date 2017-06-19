package vn.manroid.devchat.model;

import java.io.Serializable;


/**
 * Created by manro on 12/06/2017.
 */

public class UserModel implements Serializable {
    private String userID;
    private String name;
    private String email;
    private String photoURL;
    private int currentPosition;

    private boolean isOnline;

    public UserModel() {
    }

    public UserModel(String userID, String name, String email,String photoURL,boolean isOnline) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.photoURL = photoURL;
        this.isOnline = isOnline;
    }

    @Override
    public String toString() {
        return this.name == null ?
                this.email : this.name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}

