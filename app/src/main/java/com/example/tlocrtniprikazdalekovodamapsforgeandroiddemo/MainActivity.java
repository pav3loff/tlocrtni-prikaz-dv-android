package com.example.tlocrtniprikazdalekovodamapsforgeandroiddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tag;
import org.mapsforge.map.android.graphics.AndroidBitmap;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.rule.RenderTheme;
import org.mapsforge.map.rendertheme.rule.RenderThemeBuilder;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    // Request code for selecting a map file
    private static final int SELECT_MAP_FILE = 0;

    private static final LatLong COORDINATE_1 = new LatLong(45.843568, 15.984531);
    private static final LatLong COORDINATE_2 = new LatLong(45.843968, 15.984931);

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidGraphicFactory.createInstance(getApplication());

        mapView = new MapView(this);

        setContentView(mapView);

        Intent intent = new Intent(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? Intent.ACTION_OPEN_DOCUMENT : Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, SELECT_MAP_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_MAP_FILE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                openMap(uri);
            }
        }
    }

    private void openMap(Uri uri) {
        try {
            mapView.getMapScaleBar().setVisible(true);
            mapView.setBuiltInZoomControls(true);

            TileCache tileCache = AndroidUtil.createTileCache(this, "mapcache",
                    mapView.getModel().displayModel.getTileSize(), 1f,
                    mapView.getModel().frameBufferModel.getOverdrawFactor());

            FileInputStream fis = (FileInputStream) getContentResolver().openInputStream(uri);
            MapDataStore mapDataStore = new MapFile(fis);
            TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
                    mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE);

            tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);

            mapView.getLayerManager().getLayers().add(tileRendererLayer);

            mapView.setCenter(new LatLong(45.843568, 15.984531));
            mapView.setZoomLevel((byte) 12);

            Marker marker1 = createTappableMarker(this, COORDINATE_1);
            mapView.getLayerManager().getLayers().add(marker1);

            Marker marker2 = createTappableMarker(this, COORDINATE_2);
            mapView.getLayerManager().getLayers().add(marker2);

            Polyline polyline = createPolyline(this, new LinkedList<>(Arrays.asList(COORDINATE_1, COORDINATE_2)));
            mapView.getLayerManager().getLayers().add(polyline);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        mapView.destroyAll();
        AndroidGraphicFactory.clearResourceMemoryCache();
        super.onDestroy();
    }

    private Marker createTappableMarker(final Context c, LatLong latLong) {
        AndroidBitmap bitmap = new AndroidBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.marker_icon));
        bitmap.scaleTo(20, 20);

        return new Marker(latLong, bitmap, 0, 0) {
            @Override
            public boolean onTap(LatLong geoPoint, Point viewPosition,
                                 Point tapPoint) {
                if (contains(viewPosition, tapPoint)) {
                    Toast.makeText(c, "Kliknut sam!", Toast.LENGTH_SHORT).show();

                    return true;
                }
                return false;
            }
        };
    }

    private Polyline createPolyline(final Context c, List<LatLong> latLongs) {
        Paint paint = AndroidGraphicFactory.INSTANCE.createPaint();
        paint.setColor(AndroidGraphicFactory.INSTANCE.createColor(Color.BLACK));
        paint.setStrokeWidth((int) (2));
        paint.setStyle(Style.STROKE);

        Polyline polyline = new Polyline(paint, AndroidGraphicFactory.INSTANCE);

        polyline.setPoints(latLongs);

        return polyline;
    }
}