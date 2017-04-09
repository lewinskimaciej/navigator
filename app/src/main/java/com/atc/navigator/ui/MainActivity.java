package com.atc.navigator.ui;

import android.os.Bundle;
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

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements PoiAdapter.PoiCallback {

    @BindView(R.id.current_location)
    TextView currentLocation;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        adapter = new PoiAdapter(this, this);

        poiListRecycler.setAdapter(adapter);
        poiListRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        PoiModel test1 = new PoiModel();
        test1.setPoiCoordinates("Test1 coords lul")
                .setPoiDate(new DateTime())
                .setPoiName("Test1 name lul");

        PoiModel test2 = new PoiModel();
        test2.setPoiCoordinates("Test2 coords lul")
                .setPoiDate(new DateTime())
                .setPoiName("Test2 name lul");

        adapter.addPoi(test1);
        adapter.addPoi(test2);
    }


    @Override
    public void onPoiChosen(PoiModel newPoi) {
        Timber.d("onPoiChosen %s", newPoi.getPoiName());

        arrow.setVisibility(View.VISIBLE);
        distance.setVisibility(View.VISIBLE);
        choosePoiHint.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onNoPoiChosen() {
        Timber.d("onNoPoiChosen");

        arrow.setVisibility(View.INVISIBLE);
        distance.setVisibility(View.INVISIBLE);
        choosePoiHint.setVisibility(View.VISIBLE);
    }
}
