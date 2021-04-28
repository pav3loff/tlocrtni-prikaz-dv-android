package com.example.tlocrtniprikazdalekovodamapsforgeandroiddemo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class Stup {

    private int idStupa;
    private String oblikGlaveStupa;
    private boolean isZatezni;
    private double visina;
    private double tezina;
    private double orijentacija;
    private String tipStupa;
    private String proizvodac;
    private String vrstaZastite;
    private String oznakaUzemljenja;
    private double geoSirina;
    private double geoDuzina;
    private List<Izolator> izolatori;
    private List<SpojnaTocka> spojneTockeZu;
    private List<Vodic> ovjeseniVodici;
    private List<ZastitnoUze> ovjesenaZastitnaUzad;

    public Stup(JSONObject stupJson) throws JSONException {
        if(stupJson.has("idStupa")) {
            this.idStupa = stupJson.getInt("idStupa");
        }

        if(stupJson.has("oblikGlaveStupa")) {
            this.oblikGlaveStupa = stupJson.getString("oblikGlaveStupa");
        }

        if(stupJson.has("isZatezni")) {
            this.isZatezni = stupJson.getBoolean("isZatezni");
        }

        if(stupJson.has("visina")) {
            this.visina = stupJson.getDouble("visina");
        }

        if(stupJson.has("tezina")) {
            this.tezina = stupJson.getDouble("tezina");
        }

        if(stupJson.has("orijentacija")) {
            this.orijentacija = stupJson.getDouble("orijentacija");
        }

        if(stupJson.has("tipStupa")) {
            this.tipStupa = stupJson.getString("tipStupa");
        }

        if(stupJson.has("proizvodac")) {
            this.proizvodac = stupJson.getString("proizvodac");
        }

        if(stupJson.has("vrstaZastite")) {
            this.vrstaZastite = stupJson.getString("vrstaZastite");
        }

        if(stupJson.has("oznakaUzemljenja")) {
            this.oznakaUzemljenja = stupJson.getString("oznakaUzemljenja");
        }

        if(stupJson.has("geoSirina")) {
            this.geoSirina = stupJson.getDouble("geoSirina");
        }

        if(stupJson.has("geoDuzina")) {
            this.geoDuzina = stupJson.getDouble("geoDuzina");
        }

        if(stupJson.has("izolatori")) {
            JSONArray izolatoriJson = stupJson.getJSONArray("izolatori");
            this.izolatori = new LinkedList<>();

            for(int i = 0; i < izolatoriJson.length(); i++) {
                this.izolatori.add(new Izolator(izolatoriJson.getJSONObject(i)));
            }
        }

        if(stupJson.has("spojneTockeZastitneUzadi")) {
            JSONArray spojneTockeZuJson = stupJson.getJSONArray("spojneTockeZastitneUzadi");
            this.spojneTockeZu = new LinkedList<>();

            for(int i = 0; i < spojneTockeZuJson.length(); i++) {
                this.spojneTockeZu.add(new SpojnaTocka(spojneTockeZuJson.getJSONObject(i)));
            }
        }

        if(stupJson.has("ovjeseniVodici")) {
            JSONArray vodiciJson = stupJson.getJSONArray("ovjeseniVodici");
            this.ovjeseniVodici = new LinkedList<>();

            for(int i = 0; i < vodiciJson.length(); i++) {
                this.ovjeseniVodici.add(new Vodic(vodiciJson.getJSONObject(i)));
            }
        }

        if(stupJson.has("ovjesenaZastitnaUzad")) {
            JSONArray zastitnaUzadJson = stupJson.getJSONArray("ovjesenaZastitnaUzad");
            this.ovjesenaZastitnaUzad = new LinkedList<>();

            for(int i = 0; i < zastitnaUzadJson.length(); i++) {
                this.ovjesenaZastitnaUzad.add(new ZastitnoUze(zastitnaUzadJson.getJSONObject(i)));
            }
        }
    }

    public int getIdStupa() {
        return idStupa;
    }

    public void setIdStupa(int idStupa) {
        this.idStupa = idStupa;
    }

    public String getOblikGlaveStupa() {
        return oblikGlaveStupa;
    }

    public void setOblikGlaveStupa(String oblikGlaveStupa) {
        this.oblikGlaveStupa = oblikGlaveStupa;
    }

    public boolean isZatezni() {
        return isZatezni;
    }

    public void setZatezni(boolean zatezni) {
        isZatezni = zatezni;
    }

    public double getVisina() {
        return visina;
    }

    public void setVisina(double visina) {
        this.visina = visina;
    }

    public double getTezina() {
        return tezina;
    }

    public void setTezina(double tezina) {
        this.tezina = tezina;
    }

    public double getOrijentacija() {
        return orijentacija;
    }

    public void setOrijentacija(double orijentacija) {
        this.orijentacija = orijentacija;
    }

    public String getTipStupa() {
        return tipStupa;
    }

    public void setTipStupa(String tipStupa) {
        this.tipStupa = tipStupa;
    }

    public String getProizvodac() {
        return proizvodac;
    }

    public void setProizvodac(String proizvodac) {
        this.proizvodac = proizvodac;
    }

    public String getVrstaZastite() {
        return vrstaZastite;
    }

    public void setVrstaZastite(String vrstaZastite) {
        this.vrstaZastite = vrstaZastite;
    }

    public String getOznakaUzemljenja() {
        return oznakaUzemljenja;
    }

    public void setOznakaUzemljenja(String oznakaUzemljenja) {
        this.oznakaUzemljenja = oznakaUzemljenja;
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

    public List<Izolator> getIzolatori() {
        return izolatori;
    }

    public void setIzolatori(List<Izolator> izolatori) {
        this.izolatori = izolatori;
    }

    public List<SpojnaTocka> getSpojneTockeZu() {
        return spojneTockeZu;
    }

    public void setSpojneTockeZu(List<SpojnaTocka> spojneTockeZu) {
        this.spojneTockeZu = spojneTockeZu;
    }

    public List<Vodic> getOvjeseniVodici() {
        return ovjeseniVodici;
    }

    public void setOvjeseniVodici(List<Vodic> ovjeseniVodici) {
        this.ovjeseniVodici = ovjeseniVodici;
    }

    public List<ZastitnoUze> getOvjesenaZastitnaUzad() {
        return ovjesenaZastitnaUzad;
    }

    public void setOvjesenaZastitnaUzad(List<ZastitnoUze> ovjesenaZastitnaUzad) {
        this.ovjesenaZastitnaUzad = ovjesenaZastitnaUzad;
    }
}
