package com.atc.navigator.ui;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atc.navigator.R;
import com.atc.navigator.holders.PoiAdapter;
import com.atc.navigator.models.PoiModel;
import com.atc.navigator.utils.CoordinatesUtil;
import com.github.jksiezni.permissive.Permissive;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements PoiAdapter.PoiCallback,
        LocationListener {

    @BindView(R.id.main_activity_container)
    ConstraintLayout mainActivityContainer;

    @BindView(R.id.current_location_lat)
    TextView currentLocationLat;
    @BindView(R.id.current_location_long)
    TextView currentLocationLong;

    @BindView(R.id.arrow)
    ImageView arrow;
    @BindView(R.id.distance)
    TextView distance;
    @BindView(R.id.choose_poi_hint)
    TextView choosePoiHint;

    @BindView(R.id.poi_container)
    CardView poiContainer;
    @BindView(R.id.poi_list)
    RecyclerView poiListRecycler;

    PoiAdapter adapter;

    Snackbar snackbar;

    LocationManager locationManager;

    Location currentLocation;

    PoiModel chosenPoiModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        adapter = new PoiAdapter(this, this);

        poiListRecycler.setAdapter(adapter);
        poiListRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        requestGpsPermission();

        getSavedPois();
    }

    private void requestGpsPermission() {
        new Permissive.Request(Manifest.permission.ACCESS_FINE_LOCATION)
                .whenPermissionsGranted(permissions -> {
                    hideSnackbar();
                    startLocationListener();
                })
                .whenPermissionsRefused(permissions -> showSnackbar(
                        R.string.permission_denied,
                        R.string.grant_permission_action,
                        v -> requestGpsPermission()))
                .execute(this);
    }

    private void showGpsDisabledSnackbar() {
        showSnackbar(
                R.string.gps_disabled,
                R.string.gps_disabled_retry,
                v -> startLocationListener());
    }

    private void showSnackbar(int message, int action, View.OnClickListener listener) {
        snackbar = Snackbar.make(mainActivityContainer,
                message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(action, listener);
        snackbar.show();
    }

    private void hideSnackbar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    @SuppressWarnings("MissingPermission")  // if we got here, we already have the permission
    public void startLocationListener() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Timber.d("startLocationListener PROVIDER DISABLED");
            showGpsDisabledSnackbar();
        } else {
            Timber.d("startLocationListener PROVIDER ENABLED");
            hideSnackbar();

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 2000, 1, this);

            Location lastKnownLocation =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            currentLocation = lastKnownLocation;
            setCurrentLocationLabels(lastKnownLocation);
        }
    }

    @OnClick(R.id.add_poi)
    public void fabClicked() {
        if (currentLocation != null) {
            addPoi(currentLocation);
        }
    }

    private void addPoi(Location location) {
        PoiModel poiModel = new PoiModel();
        String poiName = getString(R.string.poi_default_name) + (adapter.getData().size() + 1);
        poiModel.setPoiName(poiName);
        poiModel.setPoiDate(new DateTime());
        poiModel.setPoiCoordinates(location);
        // TODO: add poi in paper
        adapter.addPoi(poiModel);
    }

    @Override
    public void onPoiChosen(PoiModel newPoi) {
        Timber.d("onPoiChosen %s", newPoi.getPoiName());

        chosenPoiModel = newPoi;

        arrow.setVisibility(View.VISIBLE);
        distance.setVisibility(View.VISIBLE);
        choosePoiHint.setVisibility(View.INVISIBLE);


        setNumberValues();
    }

    private void setNumberValues() {
        if (chosenPoiModel != null) {
            float distanceToPoi = chosenPoiModel.getPoiCoordinates().distanceTo(currentLocation);
            distance.setText(String.valueOf(distanceToPoi));

            float angle = chosenPoiModel.getPoiCoordinates().bearingTo(currentLocation);
            arrow.setRotation(angle);
        } else {
            distance.setText("0");
            arrow.setRotation(0f);
        }
    }

    @Override
    public void onNoPoiChosen() {
        Timber.d("onNoPoiChosen");

        arrow.setVisibility(View.INVISIBLE);
        distance.setVisibility(View.INVISIBLE);
        choosePoiHint.setVisibility(View.VISIBLE);
    }

    private void setCurrentLocationLabels(Location location) {
        if (location != null) {
            currentLocationLat.setText(CoordinatesUtil.getLatString(location));
            currentLocationLong.setText(CoordinatesUtil.getLongString(location));
        } else {
            currentLocationLat.setText("");
            currentLocationLong.setText("");
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        Timber.d("onLocationChanged %f %f", location.getLatitude(), location.getLongitude());
        setCurrentLocationLabels(location);
        currentLocation = location;
        setNumberValues();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        hideSnackbar();
    }

    @Override
    public void onProviderDisabled(String provider) {
        showGpsDisabledSnackbar();
    }

    public void getSavedPois() {
        // TODO: use paper for storage
    }
}
