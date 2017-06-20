package vn.manroid.devchat.model;

import java.io.Serializable;

/**
 * Created by manro on 19/06/2017.
 */

public class ChatRoomModel implements Serializable {

    private String idGuiRoom;
    private String idTenGuiRoom;
    private String thongDiepRoom;
    private long ngay;
    private String photoUserChatRoom;

    public ChatRoomModel() {
    }

    public ChatRoomModel(String idGuiRoom, String idTenGuiRoom, String thongDiepRoom, long ngay, String photoUserChatRoom) {
        this.idGuiRoom = idGuiRoom;
        this.idTenGuiRoom = idTenGuiRoom;
        this.thongDiepRoom = thongDiepRoom;
        this.ngay = ngay;
        this.photoUserChatRoom = photoUserChatRoom;
    }

    public String getIdGuiRoom() {
        return idGuiRoom;
    }

    public void setIdGuiRoom(String idGuiRoom) {
        this.idGuiRoom = idGuiRoom;
    }

    public String getIdTenGuiRoom() {
        return idTenGuiRoom;
    }

    public void setIdTenGuiRoom(String idTenGuiRoom) {
        this.idTenGuiRoom = idTenGuiRoom;
    }

    public String getThongDiepRoom() {
        return thongDiepRoom;
    }

    public void setThongDiepRoom(String thongDiepRoom) {
        this.thongDiepRoom = thongDiepRoom;
    }

    public long getNgay() {
        return ngay;
    }

    public void setNgay(long ngay) {
        this.ngay = ngay;
    }

    public String getPhotoUserChatRoom() {
        return photoUserChatRoom;
    }

    public void setPhotoUserChatRoom(String photoUserChatRoom) {
        this.photoUserChatRoom = photoUserChatRoom;
    }
}
