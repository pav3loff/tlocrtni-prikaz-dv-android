package com.example.tlocrtniprikazdalekovodamapsforgeandroiddemo;

import org.json.JSONException;
import org.json.JSONObject;

public class Vodic {

    private int idVodica;
    private String oznakaFaze;
    private String materijal;
    private int idDalekovoda;
    private int naponDalekovoda;

    public Vodic(JSONObject vodicJson) throws JSONException {
        if(vodicJson.has("idVodica")) {
            this.idVodica = vodicJson.getInt("idVodica");
        }

        if(vodicJson.has("oznakaFaze")) {
            this.oznakaFaze = vodicJson.getString("oznakaFaze");
        }

        if(vodicJson.has("materijal")) {
            this.materijal = vodicJson.getString("materijal");
        }

        if(vodicJson.has("idDalekovoda")) {
            this.idDalekovoda = vodicJson.getInt("idDalekovoda");
        }

        if(vodicJson.has("naponDalekovoda")) {
            this.naponDalekovoda = vodicJson.getInt("naponDalekovoda");
        }
    }

    public int getIdVodica() {
        return idVodica;
    }

    public void setIdVodica(int idVodica) {
        this.idVodica = idVodica;
    }

    public String getOznakaFaze() {
        return oznakaFaze;
    }

    public void setOznakaFaze(String oznakaFaze) {
        this.oznakaFaze = oznakaFaze;
    }

    public String getMaterijal() {
        return materijal;
    }

    public void setMaterijal(String materijal) {
        this.materijal = materijal;
    }

    public int getIdDalekovoda() {
        return idDalekovoda;
    }

    public void setIdDalekovoda(int idDalekovoda) {
        this.idDalekovoda = idDalekovoda;
    }

    public int getNaponDalekovoda() {
        return naponDalekovoda;
    }

    public void setNaponDalekovoda(int naponDalekovoda) {
        this.naponDalekovoda = naponDalekovoda;
    }
}
