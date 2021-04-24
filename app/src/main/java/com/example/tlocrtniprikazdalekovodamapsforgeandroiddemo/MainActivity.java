package com.example.tlocrtniprikazdalekovodamapsforgeandroiddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.datastore.MultiMapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.ZipRenderTheme;
import org.mapsforge.map.rendertheme.ZipXmlThemeResourceProvider;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipInputStream;

public class MainActivity extends AppCompatActivity {
    private static final int SELECT_MAP_FILE = 0;
    private static final int SELECT_DATA_FILE = 1;
    private static final int SELECT_THEME_ARCHIVE = 2;

    private MapView mapView;
    private TileRendererLayer tileRendererLayer;
    private Uri mapFileUri;
    private Uri dataFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidGraphicFactory.createInstance(getApplication());

        mapView = new MapView(this);

        mapView.getMapScaleBar().setVisible(false);
        mapView.setBuiltInZoomControls(false);

        setContentView(R.layout.activity_main);
        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
        relativeLayout.addView(mapView, 0);

        Intent intent = new Intent(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? Intent.ACTION_OPEN_DOCUMENT : Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        startActivityForResult(intent, SELECT_MAP_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();

            if (requestCode == SELECT_MAP_FILE) {
                mapFileUri = uri;
                openMap(uri);
            } else if(requestCode == SELECT_DATA_FILE) {
                dataFileUri = uri;
            } else if(requestCode == SELECT_THEME_ARCHIVE) {
                openThemeArchive(uri);
            }
        }
    }

    private void openMap(Uri uri) {
        TileCache tileCache = AndroidUtil.createTileCache(this, "mapcache",
                mapView.getModel().displayModel.getTileSize(), 1f,
                mapView.getModel().frameBufferModel.getOverdrawFactor());

        try {
            FileInputStream fis = (FileInputStream) getContentResolver().openInputStream(uri);

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

    private void openThemeArchive(Uri uri) {
        try {
            List<String> xmlThemes = ZipXmlThemeResourceProvider.scanXmlThemes(new ZipInputStream(new BufferedInputStream(getContentResolver().openInputStream(uri))));

            if(xmlThemes.isEmpty()) {
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Select theme");
            builder.setSingleChoiceItems(xmlThemes.toArray(new String[0]), -1, (dialog, which) -> {
                dialog.dismiss();

                try {
                    XmlRenderTheme xmlRenderTheme = new ZipRenderTheme(xmlThemes.get(which),
                            new ZipXmlThemeResourceProvider(new ZipInputStream(new BufferedInputStream(getContentResolver().openInputStream(uri)))));

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

            tileRendererLayer = new TileRendererLayer(tileCache, multiMapDataStore,
                    mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE);
            tileRendererLayer.setXmlRenderTheme(xmlRenderTheme);

            mapView.getLayerManager().getLayers().add(tileRendererLayer);
            mapView.getLayerManager().redrawLayers();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        mapView.destroyAll();
        AndroidGraphicFactory.clearResourceMemoryCache();
        super.onDestroy();
    }

    public void loadDataOnClick(View view) {
        Intent intent = new Intent(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? Intent.ACTION_OPEN_DOCUMENT : Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        startActivityForResult(intent, SELECT_DATA_FILE);
    }

    public void loadThemeOnClick(View view) {
        Intent intent = new Intent(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? Intent.ACTION_OPEN_DOCUMENT : Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        startActivityForResult(intent, SELECT_THEME_ARCHIVE);
    }

}