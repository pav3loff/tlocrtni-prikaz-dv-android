package com.example.tlocrtniprikazdalekovodamapsforgeandroiddemo;

public class Util {

    public static String generateStupInfo(Stup stup) {
        StringBuilder sb = new StringBuilder();
        if(stup != null) {
            sb.append("Oblik glave: " + stup.getOblikGlaveStupa());
            sb.append("\n");
            sb.append("Zatezni: " + stup.isZatezni());
            sb.append("\n");
            sb.append("Visina: " + stup.getVisina());
            sb.append("\n");
            sb.append("Tezina: " + stup.getTezina());
            sb.append("\n");
            sb.append("Orijentacija: " + stup.getOrijentacija());
            sb.append("\n");
            sb.append("Tip stupa: " + stup.getTipStupa());
            sb.append("\n");
            sb.append("Proizvodac: " + stup.getProizvodac());
            sb.append("\n");
            sb.append("Vrsta zastite: " + stup.getVrstaZastite());
            sb.append("\n");
            sb.append("Oznaka uzemljenja: " + stup.getOznakaUzemljenja());
            sb.append("\n");
            sb.append("Geo. sirina: " + stup.getGeoSirina());
            sb.append("\n");
            sb.append("Geo. duzina: " + stup.getGeoDuzina());
            sb.append("\n\n");
            sb.append("IZOLATORI:");
            sb.append("\n");
            for(Izolator izolator : stup.getIzolatori()) {
                sb.append("\tId izolatora: " + izolator.getIdIzolatora());
                sb.append("\n");
                sb.append("\tIzvedba: " + izolator.getIzvedba());
                sb.append("\n");
                sb.append("\tMaterijal: " + izolator.getMaterijal());
                sb.append("\n");
                sb.append("\tBroj clanaka: " + izolator.getBrojClanaka());
                sb.append("\n");
                sb.append("\tSTI: ");
                sb.append("\n");
                SpojnaTocka sti = izolator.getSti();
                sb.append("\t\tId spojne tocke: " + sti.getIdSt());
                sb.append("\n");
                sb.append("\t\tX: " + sti.getX());
                sb.append("\n");
                sb.append("\t\tY: " + sti.getY());
                sb.append("\n");
                sb.append("\t\tZ: " + sti.getZ());
                sb.append("\n");
                sb.append("\tSTV: ");
                sb.append("\n");
                SpojnaTocka stv = izolator.getStv();
                sb.append("\t\tId spojne tocke: " + stv.getIdSt());
                sb.append("\n");
                sb.append("\t\tX: " + stv.getX());
                sb.append("\n");
                sb.append("\t\tY: " + stv.getY());
                sb.append("\n");
                sb.append("\t\tZ: " + stv.getZ());
                sb.append("\n\n");
            }
            sb.append("OVJESENI VODICI:");
            sb.append("\n");
            for(Vodic vodic : stup.getOvjeseniVodici()) {
                sb.append("\tId vodica: " + vodic.getIdVodica());
                sb.append("\n");
                sb.append("\tOznaka faze: " + vodic.getOznakaFaze());
                sb.append("\n");
                sb.append("\tMaterijal: " + vodic.getMaterijal());
                sb.append("\n");
                sb.append("\tId dalekovoda: " + vodic.getIdDalekovoda());
                sb.append("\n");
                sb.append("\tNapon: " + vodic.getNaponDalekovoda());
                sb.append("\n\n");
            }
            sb.append("OVJESENA ZASTITNA UZAD:");
            sb.append("\n");
            for(ZastitnoUze zastitnoUze : stup.getOvjesenaZastitnaUzad()) {
                sb.append("\tId zast. uzeta: " + zastitnoUze.getIdZastitnogUzeta());
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    public static String generateStInfo(SpojnaTocka spojnaTocka) {
        StringBuilder sb = new StringBuilder();
        if(spojnaTocka != null) {
            sb.append("Tip: " + spojnaTocka.getTipSpojneTocke());
            sb.append("\n");
            sb.append("X: " + spojnaTocka.getX());
            sb.append("\n");
            sb.append("Y: " + spojnaTocka.getY());
            sb.append("\n");
            sb.append("Z: " + spojnaTocka.getZ());
        }

        return sb.toString();
    }

}
