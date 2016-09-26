package org.charmeck.trailofhistory.ranger;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.charmeck.trailofhistory.core.model.PointOfInterest;

/**
 * @author Antoine Campbell
 */
public class MapActivity extends BaseActivity
    implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

  private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
  public static final String EXTRA_LATITUDE = "latitude";
  public static final String EXTRA_LONGITUDE = "longitude";

  private GoogleMap map;
  private Marker locationMarker;
  private View rootView;

  public static Intent newInstance(Context context, PointOfInterest poi) {
    Intent intent = new Intent(context, MapActivity.class);
    if (poi != null) {
      intent.putExtra(EXTRA_LATITUDE, poi.getLatitude());
      intent.putExtra(EXTRA_LONGITUDE, poi.getLongitude());
    }
    return intent;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_map);
    rootView = findViewById(R.id.rootview);
    SupportMapFragment mapFragment =
        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.map_menu, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_done:
        if (locationMarker != null) {
          Intent intent = new Intent();
          intent.putExtra(EXTRA_LATITUDE, locationMarker.getPosition().latitude);
          intent.putExtra(EXTRA_LONGITUDE, locationMarker.getPosition().longitude);
          setResult(RESULT_OK, intent);
          finish();
        } else {
          Snackbar.make(rootView, "Please choose a location.", Snackbar.LENGTH_SHORT).show();
        }
        break;
      case android.R.id.home:
        // MapActivity has two possible parents so we should just return to the previous activity
        finish();
      default:
        return super.onOptionsItemSelected(item);
    }

    return true;
  }

  @Override public void onMapReady(GoogleMap googleMap) {
    map = googleMap;
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      map.setMyLocationEnabled(true);
    } else {
      ActivityCompat.requestPermissions(this,
          new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
          REQUEST_CODE_LOCATION_PERMISSION);
    }
    map.setOnMapClickListener(this);
    Intent intent = getIntent();
    if (intent != null && intent.hasExtra(EXTRA_LATITUDE) && intent.hasExtra(EXTRA_LONGITUDE)) {
      LatLng latLng = new LatLng(intent.getDoubleExtra(EXTRA_LATITUDE, 0),
          intent.getDoubleExtra(EXTRA_LONGITUDE, 0));
      locationMarker = map.addMarker(new MarkerOptions().position(latLng).title(latLng.toString()));
      map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
          == PackageManager.PERMISSION_GRANTED) {
        map.setMyLocationEnabled(true);
      } else {
        //TODO handle permission denied.
      }
    }
  }

  @Override public void onMapClick(LatLng latLng) {
    if (locationMarker == null) {
      locationMarker = map.addMarker(new MarkerOptions().position(latLng).title(latLng.toString()));
    } else {
      locationMarker.setPosition(latLng);
      locationMarker.setTitle(latLng.toString());
    }
  }
}
