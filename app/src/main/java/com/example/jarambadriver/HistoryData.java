package com.example.jarambadriver;

public class HistoryData {
    String trayek, plat, tanggal, waktu, comment, key, rate_status;
    float rating;

    public HistoryData() {

    }

    public HistoryData(float rating, String comment, String trayek, String plat, String tanggal, String waktu, String rate_status) {
        this.rating = rating;
        this.comment = comment;
        this.trayek = trayek;
        this.plat = plat;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.rate_status = rate_status;
    }

    public String getTrayek() {
        return trayek;
    }

    public void setTrayek(String trayek) {
        this.trayek = trayek;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRate_status() {
        return rate_status;
    }

    public void setRate_status(String rate_status) {
        this.rate_status = rate_status;
    }
}
