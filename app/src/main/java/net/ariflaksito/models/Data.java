package net.ariflaksito.models;

import java.sql.Timestamp;

/**
 * Created by ariflaksito on 10/11/17.
 */

public abstract class Data {

    private int _id;
    private int aid;
    private String name;
    private String img;
    private Timestamp postdate;
    private double tinggi;
    private int banjir;
    private String type;
    private String desc;

    public Data(){}


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Timestamp getPostDate() {
        return postdate;
    }

    public void setPostDate(Timestamp postdate) {
        this.postdate = postdate;
    }

    public double getTinggi() {
        return tinggi;
    }

    public void setTinggi(double tinggi) {
        this.tinggi = tinggi;
    }

    public int getBanjir() {
        return banjir;
    }

    public void setBanjir(int banjir) {
        this.banjir = banjir;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
