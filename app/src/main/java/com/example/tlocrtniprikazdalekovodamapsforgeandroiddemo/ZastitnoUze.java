package com.example.tlocrtniprikazdalekovodamapsforgeandroiddemo;

import org.json.JSONException;
import org.json.JSONObject;

public class ZastitnoUze {

    private int idZastitnogUzeta;

    public ZastitnoUze(JSONObject zastitnoUzeJson) throws JSONException {
        if(zastitnoUzeJson.has("idZastitnogUzeta")) {
            this.idZastitnogUzeta = zastitnoUzeJson.getInt("idZastitnogUzeta");
        }
    }

    public int getIdZastitnogUzeta() {
        return idZastitnogUzeta;
    }

    public void setIdZastitnogUzeta(int idZastitnogUzeta) {
        this.idZastitnogUzeta = idZastitnogUzeta;
    }
}
