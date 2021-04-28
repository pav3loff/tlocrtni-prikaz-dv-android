package com.example.tlocrtniprikazdalekovodamapsforgeandroiddemo;

import org.json.JSONException;
import org.json.JSONObject;

public class Izolator {

    private int idIzolatora;
    private int brojClanaka;
    private String izvedba;
    private String materijal;
    private SpojnaTocka sti;
    private SpojnaTocka stv;

    public Izolator(JSONObject izolatorJson) throws JSONException {
        if(izolatorJson.has("idIzolatora")) {
            this.idIzolatora = izolatorJson.getInt("idIzolatora");
        }

        if(izolatorJson.has("brojClanaka")) {
            this.brojClanaka = izolatorJson.getInt("brojClanaka");
        }

        if(izolatorJson.has("izvedba")) {
            this.izvedba = izolatorJson.getString("izvedba");
        }

        if(izolatorJson.has("materijal")) {
            this.materijal = izolatorJson.getString("materijal");
        }

        if(izolatorJson.has("spojnaTockaIzolatora")) {
            JSONObject stiJson = izolatorJson.getJSONObject("spojnaTockaIzolatora");

            this.sti = new SpojnaTocka(stiJson);
        }

        if(izolatorJson.has("spojnaTockaVodica")) {
            JSONObject stvJson = izolatorJson.getJSONObject("spojnaTockaVodica");

            this.stv = new SpojnaTocka(stvJson);
        }
    }

    public int getIdIzolatora() {
        return idIzolatora;
    }

    public void setIdIzolatora(int idIzolatora) {
        this.idIzolatora = idIzolatora;
    }

    public int getBrojClanaka() {
        return brojClanaka;
    }

    public void setBrojClanaka(int brojClanaka) {
        this.brojClanaka = brojClanaka;
    }

    public String getIzvedba() {
        return izvedba;
    }

    public void setIzvedba(String izvedba) {
        this.izvedba = izvedba;
    }

    public String getMaterijal() {
        return materijal;
    }

    public void setMaterijal(String materijal) {
        this.materijal = materijal;
    }

    public SpojnaTocka getSti() {
        return sti;
    }

    public void setSti(SpojnaTocka sti) {
        this.sti = sti;
    }

    public SpojnaTocka getStv() {
        return stv;
    }

    public void setStv(SpojnaTocka stv) {
        this.stv = stv;
    }
}
