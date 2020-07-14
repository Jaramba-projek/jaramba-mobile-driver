package com.example.jarambadriver;

public class HistoryData {
    String trayek, plate_number, tanggal, start_time, end_time, comment, key, rate_status, id_key, id_driver;
    float rating;

    public HistoryData() {

    }

    public HistoryData(float rating, String comment, String trayek, String plat, String tanggal, String start_time, String end_time, String rate_status, String id_key, String id_driver) {
        this.rating = rating;
        this.comment = comment;
        this.trayek = trayek;
        this.plate_number = plat;
        this.tanggal = tanggal;
        this.start_time = start_time;
        this.end_time = end_time;
        this.rate_status = rate_status;
        this.id_key = id_key;
        this.id_driver = id_driver;
    }

    public String getId_driver() {return id_driver;}

    public void setId_driver(String id_driver) {
        this.id_driver = id_driver;
    }

    public String getStart_time() {return start_time;}

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getId_key() {return id_key;}

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getEnd_time() {return end_time;}

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getTrayek() {
        return trayek;
    }

    public void setTrayek(String trayek) {
        this.trayek = trayek;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
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
