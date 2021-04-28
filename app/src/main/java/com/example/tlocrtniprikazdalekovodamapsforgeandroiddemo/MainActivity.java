package com.example.tlocrtniprikazdalekovodamapsforgeandroiddemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tag;
import org.mapsforge.core.model.Tile;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.datastore.MapReadResult;
import org.mapsforge.map.datastore.MultiMapDataStore;
import org.mapsforge.map.datastore.PointOfInterest;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.ZipRenderTheme;
import org.mapsforge.map.rendertheme.ZipXmlThemeResourceProvider;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipInputStream;

public class MainActivity extends AppCompatActivity {
    private static final int SELECT_MAP_FILE = 0;
    private static final int SELECT_DATA_FILE = 1;
    private static final int SELECT_DETAILS_FILE = 2;
    private static final int SELECT_THEME_ARCHIVE = 3;
    private static final int TOUCH_RADIUS = 16;
    private static final int ZOOM_THRESHOLD = 21;

    private MapView mapView;
    private TileRendererLayer tileRendererLayer;
    private Uri mapFileUri;
    private Uri dataFileUri;
    private Uri detailsFileUri;
    private Uri themeArchiveUri;
    private List<Stup> stupovi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidGraphicFactory.createInstance(getApplication());

        mapView = new MapView(this);

        mapView.getMapScaleBar().setVisible(true);
        mapView.setBuiltInZoomControls(true);

        setContentView(R.layout.activity_main);
        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
        relativeLayout.addView(mapView, 0);

        Intent intent = new Intent(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ?
                Intent.ACTION_OPEN_DOCUMENT : Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        startActivityForResult(intent, SELECT_MAP_FILE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String selectedItem = item.getTitle().toString();

        Intent intent = new Intent(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ?
                Intent.ACTION_OPEN_DOCUMENT : Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        if(selectedItem.equals("Load Data")) {
            startActivityForResult(intent, SELECT_DATA_FILE);
        } else if(selectedItem.equals("Load Details")) {
            startActivityForResult(intent, SELECT_DETAILS_FILE);
        } else if(selectedItem.equals("Load Theme")) {
            if(mapFileUri != null && dataFileUri != null) {
                startActivityForResult(intent, SELECT_THEME_ARCHIVE);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();

            if (requestCode == SELECT_MAP_FILE) {
                mapFileUri = uri;
                openMap();
            } else if(requestCode == SELECT_DATA_FILE) {
                dataFileUri = uri;
            } else if(requestCode == SELECT_DETAILS_FILE) {
                detailsFileUri = uri;
                openDetailsFile();
            } else if(requestCode == SELECT_THEME_ARCHIVE) {
                themeArchiveUri = uri;
                openThemeArchive();
            }
        }
    }

    private void openMap() {
        TileCache tileCache = AndroidUtil.createTileCache(this, "mapcache",
                mapView.getModel().displayModel.getTileSize(), 1f,
                mapView.getModel().frameBufferModel.getOverdrawFactor());

        try {
            FileInputStream fis = (FileInputStream) getContentResolver().openInputStream(mapFileUri);

            MapDataStore mapDataStore = new MapFile(fis);

            tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
                    mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE);

            tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);

            mapView.getLayerManager().getLayers().add(tileRendererLayer);

            mapView.setCenter(new LatLong(45.843568, 15.984531));
            mapView.setZoomLevel((byte) 12);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void openDetailsFile() {
        try {
            FileInputStream fis = (FileInputStream) getContentResolver().openInputStream(detailsFileUri);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();

            String line = br.readLine();
            while(line != null) {
                sb.append(line);

                line = br.readLine();
            }

            JSONObject detailsInfoJson = new JSONObject(sb.toString());
            JSONArray stupoviInfoJson = detailsInfoJson.getJSONArray("stupovi");
            this.stupovi = new LinkedList<>();

            for(int i = 0; i < stupoviInfoJson.length(); i++) {
                this.stupovi.add(new Stup(stupoviInfoJson.getJSONObject(i)));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void openThemeArchive() {
        try {
            List<String> xmlThemes = ZipXmlThemeResourceProvider.scanXmlThemes(
                    new ZipInputStream(new BufferedInputStream(getContentResolver().openInputStream(themeArchiveUri))));

            if(xmlThemes.isEmpty()) {
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Select theme");
            builder.setSingleChoiceItems(xmlThemes.toArray(new String[0]), -1, (dialog, which) -> {
                dialog.dismiss();

                try {
                    XmlRenderTheme xmlRenderTheme = new ZipRenderTheme(xmlThemes.get(which),
                            new ZipXmlThemeResourceProvider(new ZipInputStream(
                                    new BufferedInputStream(getContentResolver().openInputStream(themeArchiveUri)))));

                    renderTheme(xmlRenderTheme);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            builder.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void renderTheme(XmlRenderTheme xmlRenderTheme) {
        mapView.getLayerManager().getLayers().remove(tileRendererLayer);
        tileRendererLayer.onDestroy();
        tileRendererLayer.getTileCache().purge();

        TileCache tileCache = AndroidUtil.createTileCache(this, "mapcache",
                mapView.getModel().displayModel.getTileSize(), 1f,
                mapView.getModel().frameBufferModel.getOverdrawFactor());

        try {
            FileInputStream mapFileFis = (FileInputStream) getContentResolver().openInputStream(mapFileUri);
            MapDataStore mapDataStore = new MapFile(mapFileFis);

            FileInputStream dataFileFis = (FileInputStream) getContentResolver().openInputStream(dataFileUri);
            MapDataStore dvDataStore = new MapFile(dataFileFis);

            MultiMapDataStore multiMapDataStore = new MultiMapDataStore(MultiMapDataStore.DataPolicy.RETURN_ALL);
            multiMapDataStore.addMapDataStore(mapDataStore, true, true);
            multiMapDataStore.addMapDataStore(dvDataStore, false, false);

            tileRendererLayer = new TileRendererLayer(tileCache, multiMapDataStore, mapView.getModel().mapViewPosition,
                    false, true, false, AndroidGraphicFactory.INSTANCE) {
                @Override
                public boolean onLongPress(LatLong tapLatLong, Point layerXY, Point tapXY) {
                    onLongClick(tapLatLong, tapXY, dvDataStore);

                    return true;
                }
            };

            tileRendererLayer.setXmlRenderTheme(xmlRenderTheme);

            mapView.getLayerManager().getLayers().add(tileRendererLayer);
            mapView.getLayerManager().redrawLayers();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void onLongClick(LatLong tapLatLong, Point tapXY, MapDataStore dataStore) {
        if(detailsFileUri == null) {
            return;
        }

        // Read all labeled POI and ways for the area covered by the tiles under touch
        float touchRadius = TOUCH_RADIUS * this.mapView.getModel().displayModel.getScaleFactor();
        long mapSize = MercatorProjection.getMapSize(
                this.mapView.getModel().mapViewPosition.getZoomLevel(), this.mapView.getModel().displayModel.getTileSize());
        double pixelX = MercatorProjection.longitudeToPixelX(tapLatLong.longitude, mapSize);
        double pixelY = MercatorProjection.latitudeToPixelY(tapLatLong.latitude, mapSize);
        int tileXMin = MercatorProjection.pixelXToTileX(pixelX - touchRadius,
                this.mapView.getModel().mapViewPosition.getZoomLevel(), this.mapView.getModel().displayModel.getTileSize());
        int tileXMax = MercatorProjection.pixelXToTileX(pixelX + touchRadius,
                this.mapView.getModel().mapViewPosition.getZoomLevel(), this.mapView.getModel().displayModel.getTileSize());
        int tileYMin = MercatorProjection.pixelYToTileY(pixelY - touchRadius,
                this.mapView.getModel().mapViewPosition.getZoomLevel(), this.mapView.getModel().displayModel.getTileSize());
        int tileYMax = MercatorProjection.pixelYToTileY(pixelY + touchRadius,
                this.mapView.getModel().mapViewPosition.getZoomLevel(), this.mapView.getModel().displayModel.getTileSize());
        Tile upperLeft = new Tile(tileXMin, tileYMin,
                this.mapView.getModel().mapViewPosition.getZoomLevel(), this.mapView.getModel().displayModel.getTileSize());
        Tile lowerRight = new Tile(tileXMax, tileYMax,
                this.mapView.getModel().mapViewPosition.getZoomLevel(), this.mapView.getModel().displayModel.getTileSize());
        MapReadResult mapReadResult = dataStore.readLabels(upperLeft, lowerRight);

        StringBuilder sb = new StringBuilder();
        String foundElementType = null;
        String foundElementId = null;
        String info = null;

        // Nadi elemente unutar klika
        for (PointOfInterest pointOfInterest : mapReadResult.pointOfInterests) {
            Point layerXY = this.mapView.getMapViewProjection().toPixels(pointOfInterest.position);
            if (layerXY.distance(tapXY) <= touchRadius) {
                List<Tag> tags = pointOfInterest.tags;

                if(tags.contains(new Tag("type", "stup"))) { // Ako je kliknuto u blizini stupa uvijek prioritiziraj info stupa
                    for(Tag tag : tags) {
                        if(tag.key.equals("idStupa")) {
                            foundElementType = "STUP";
                            foundElementId = tag.value;
                            info = getStupInfo(Integer.parseInt(tag.value));

                            break;
                        }
                    }
                }

                if(this.mapView.getModel().mapViewPosition.getZoomLevel() >= ZOOM_THRESHOLD) {
                    for(Tag tag : tags) {
                        if(tag.key.equals("idSt")) {
                            foundElementType = "SPOJNA TOCKA";
                            foundElementId = tag.value;
                            info = getStInfo(Integer.parseInt(tag.value));

                            break;
                        }
                    }
                }
            }
        }

        if(info != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(android.R.drawable.ic_menu_search);
            builder.setTitle(foundElementType + " " + foundElementId);
            builder.setMessage(info);
            builder.show();
        }
    }

    @Override
    protected void onDestroy() {
        mapView.destroyAll();
        AndroidGraphicFactory.clearResourceMemoryCache();
        super.onDestroy();
    }

    private String getStupInfo(int stupId) {
        Stup odabraniStup = null;

        for(Stup stup : this.stupovi) {
            if(stup.getIdStupa() == stupId) {
                odabraniStup = stup;
            }
        }

        return Util.generateStupInfo(odabraniStup);
    }

    private String getStInfo(int stId) {
        SpojnaTocka odabranaSt = null;

        for(Stup stup : this.stupovi) {
            boolean isFound = false;

            for(SpojnaTocka st : stup.getSpojneTockeZu()) {
                if(st.getIdSt() == stId) {
                    odabranaSt = st;

                    isFound = true;
                    break;
                }
            }

            if(isFound) {
                break;
            } else {
                for(Izolator izolator : stup.getIzolatori()) {
                    if(izolator.getSti().getIdSt() == stId) {
                        odabranaSt = izolator.getSti();

                        isFound = true;
                        break;
                    }

                    if(izolator.getStv().getIdSt() == stId) {
                        odabranaSt = izolator.getStv();

                        isFound = true;
                        break;
                    }
                }
            }

            if(isFound) {
                break;
            }
        }

        return Util.generateStInfo(odabranaSt);
    }

}