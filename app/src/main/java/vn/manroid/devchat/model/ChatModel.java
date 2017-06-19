package vn.manroid.devchat.model;

import java.io.Serializable;

/**
 * Created by manro on 12/06/2017.
 */

public class ChatModel implements Serializable {
    private String idGui;
    private String tenGui;
    private String idNhan;
    private String tenNhan;
    private String thongDiep;
    private long ngay;
    private String photoURLCurrentUser;


    public ChatModel(){}

    public ChatModel(String idGui, String tenGui, String idNhan, String tenNhan, String thongDiep, long ngay, String photoURLCurrentUser) {
        this.idGui = idGui;
        this.tenGui = tenGui;
        this.idNhan = idNhan;
        this.tenNhan = tenNhan;
        this.thongDiep = thongDiep;
        this.ngay = ngay;
        this.photoURLCurrentUser = photoURLCurrentUser;
    }

    public String getIdGui() {
        return idGui;
    }

    public void setIdGui(String idGui) {
        this.idGui = idGui;
    }

    public String getTenGui() {
        return tenGui;
    }

    public void setTenGui(String tenGui) {
        this.tenGui = tenGui;
    }

    public String getIdNhan() {
        return idNhan;
    }

    public void setIdNhan(String idNhan) {
        this.idNhan = idNhan;
    }

    public String getTenNhan() {
        return tenNhan;
    }

    public void setTenNhan(String tenNhan) {
        this.tenNhan = tenNhan;
    }

    public String getThongDiep() {
        return thongDiep;
    }

    public void setThongDiep(String thongDiep) {
        this.thongDiep = thongDiep;
    }

    public long getNgay() {
        return ngay;
    }

    public void setNgay(long ngay) {
        this.ngay = ngay;
    }

    public String getPhotoURLCurrentUser() {
        return photoURLCurrentUser;
    }

    public void setPhotoURLCurrentUser(String photoURLCurrentUser) {
        this.photoURLCurrentUser = photoURLCurrentUser;
    }
}
