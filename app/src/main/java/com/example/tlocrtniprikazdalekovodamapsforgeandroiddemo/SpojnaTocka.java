package com.example.tlocrtniprikazdalekovodamapsforgeandroiddemo;

import org.json.JSONException;
import org.json.JSONObject;

public class SpojnaTocka {

    private int idSt;
    private double x;
    private double y;
    private double z;
    private double geoSirina;
    private double geoDuzina;
    private TipSpojneTocke tipSpojneTocke;

    public SpojnaTocka(JSONObject spojnaTockaJson) throws JSONException {
        if(spojnaTockaJson.has("idSti")) {
            this.idSt = spojnaTockaJson.getInt("idSti");
            tipSpojneTocke = TipSpojneTocke.STI;
        }

        if(spojnaTockaJson.has("idStv")) {
            this.idSt = spojnaTockaJson.getInt("idStv");
            tipSpojneTocke = TipSpojneTocke.STV;
        }

        if(spojnaTockaJson.has("idStzu")) {
            this.idSt = spojnaTockaJson.getInt("idStzu");
            tipSpojneTocke = TipSpojneTocke.STZU;
        }

        if(spojnaTockaJson.has("x")) {
            this.x = spojnaTockaJson.getDouble("x");
        }

        if(spojnaTockaJson.has("y")) {
            this.y = spojnaTockaJson.getDouble("y");
        }

        if(spojnaTockaJson.has("z")) {
            this.z = spojnaTockaJson.getDouble("z");
        }
    }

    public int getIdSt() {
        return idSt;
    }

    public void setIdSt(int idSt) {
        this.idSt = idSt;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getGeoSirina() {
        return geoSirina;
    }

    public void setGeoSirina(double geoSirina) {
        this.geoSirina = geoSirina;
    }

    public double getGeoDuzina() {
        return geoDuzina;
    }

    public void setGeoDuzina(double geoDuzina) {
        this.geoDuzina = geoDuzina;
    }

    public TipSpojneTocke getTipSpojneTocke() {
        return tipSpojneTocke;
    }

    public void setTipSpojneTocke(TipSpojneTocke tipSpojneTocke) {
        this.tipSpojneTocke = tipSpojneTocke;
    }
}
