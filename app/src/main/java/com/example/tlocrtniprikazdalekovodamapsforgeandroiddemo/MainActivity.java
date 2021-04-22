package com.example.tlocrtniprikazdalekovodamapsforgeandroiddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.rendertheme.ContentRenderTheme;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.datastore.MultiMapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {
    private static final int SELECT_MAP_FILE = 0;
    private static final int SELECT_DATA_FILE = 1;
    private static final int SELECT_THEME_FILE = 2;

    private MapView mapView;
    private MapDataStore mapDataStore;
    private MapDataStore dvDataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidGraphicFactory.createInstance(getApplication());

        mapView = new MapView(this);

        mapView.getMapScaleBar().setVisible(false);

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
                openMap(uri);
            } else if(requestCode == SELECT_DATA_FILE) {
                openData(uri);
            } else if(requestCode == SELECT_THEME_FILE) {
                openTheme(uri);
            }
        }
    }

    private void openMap(Uri uri) {
        try {
            FileInputStream fis = (FileInputStream) getContentResolver().openInputStream(uri);
            mapDataStore = new MapFile(fis);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void openData(Uri uri) {
        // Read another map file
        try {
            FileInputStream fis = (FileInputStream) getContentResolver().openInputStream(uri);
            dvDataStore = new MapFile(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void openTheme(Uri uri) {
        XmlRenderTheme xmlRenderTheme = new ContentRenderTheme(getContentResolver(), uri);

        TileCache tileCache = AndroidUtil.createTileCache(this, "mapcache",
                mapView.getModel().displayModel.getTileSize(), 1f,
                mapView.getModel().frameBufferModel.getOverdrawFactor());

        MultiMapDataStore multiMapDataStore = new MultiMapDataStore(MultiMapDataStore.DataPolicy.RETURN_ALL);
        multiMapDataStore.addMapDataStore(mapDataStore, true, true);
        multiMapDataStore.addMapDataStore(dvDataStore, false, false);

        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, multiMapDataStore,
                mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE);

        tileRendererLayer.setXmlRenderTheme(xmlRenderTheme);

        mapView.getLayerManager().getLayers().add(tileRendererLayer);

        mapView.setCenter(new LatLong(45.843568, 15.984531));
        mapView.setZoomLevel((byte) 12);
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
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("*/*");

        startActivityForResult(intent, SELECT_THEME_FILE);
    }

}