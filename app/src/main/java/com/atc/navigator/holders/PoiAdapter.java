package com.atc.navigator.holders;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atc.navigator.R;
import com.atc.navigator.models.PoiModel;
import com.atc.navigator.utils.CoordinatesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class PoiAdapter extends RecyclerView.Adapter {

    public interface PoiCallback {
        void onPoiChosen(PoiModel newPoi);

        void onNoPoiChosen();
    }

    List<PoiModel> poiList;

    PoiCallback callback;

    int chosenPoi = -1;

    int chosenPoiColor;
    int chosenPoiNameColor;
    int defaultPoiColor;
    int defaultPoiNameColor;

    public PoiAdapter(Context context, PoiCallback callback) {
        this.callback = callback;

        this.chosenPoiColor = ContextCompat.getColor(context, R.color.poiChosen);
        this.chosenPoiNameColor = ContextCompat.getColor(context, R.color.poiChosen);
        this.defaultPoiColor = ContextCompat.getColor(context, R.color.poiDefault);
        this.defaultPoiNameColor = ContextCompat.getColor(context, android.R.color.black);

        this.poiList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_poi, parent, false);
        return new PoiHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PoiHolder
                && poiList != null
                && poiList.size() > position) {
            PoiHolder poiHolder = (PoiHolder) holder;
            poiHolder.setPoiData(position);
        }
    }

    @Override
    public int getItemCount() {
        return poiList != null ? poiList.size() : 0;
    }

    public void setData(List<PoiModel> list) {
        poiList = list;
        notifyDataSetChanged();
    }

    public void addPoi(PoiModel newElement) {
        int position = poiList.size();
        poiList.add(newElement);
        notifyItemChanged(position);
    }

    public void choosePoi(int position) {
        if (chosenPoi != position) {
            if (chosenPoi != -1) {
                int lastChosenPoi = chosenPoi;
                notifyItemChanged(lastChosenPoi);
            }
            chosenPoi = position;
            notifyItemChanged(position);

            if (callback != null) {
                callback.onPoiChosen(poiList.get(position));
            }
        } else {
            int lastChosenPoi = chosenPoi;
            chosenPoi = -1;
            notifyItemChanged(lastChosenPoi);

            if (callback != null) {
                callback.onNoPoiChosen();
            }
        }
    }

    public void removePoi(int position) {
        poiList.remove(position);
        notifyDataSetChanged();
        if (callback != null) {
            if (poiList.size() == 0) {
                callback.onNoPoiChosen();
            }
        }
        chosenPoi = -1;
    }

    public List<PoiModel> getData() {
        return poiList;
    }

    class PoiHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.poi_checkbox)
        ImageView poiCheckbox;
        @BindView(R.id.poi_name)
        TextView poiName;
        @BindView(R.id.poi_coordinates)
        TextView poiCoordinates;
        @BindView(R.id.poi_date)
        TextView poiDate;

        PoiModel poiModel;
        int position;

        PoiHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void setPoiData(int position) {
            this.position = position;
            poiModel = poiList.get(position);

            poiName.setText(poiModel.getPoiName());
            poiDate.setText(poiModel.getPoiDate().toString("dd/MM/yyyy HH:mm:ss"));

            String poiCoordinatesText = CoordinatesUtil.getLatString(poiModel.getPoiCoordinates()) +
                    CoordinatesUtil.getLongString(poiModel.getPoiCoordinates());
            poiCoordinates.setText(poiCoordinatesText);

            if (chosenPoi == position) {
                poiCheckbox.setColorFilter(chosenPoiColor);
                poiName.setTextColor(chosenPoiNameColor);
            } else {
                poiCheckbox.setColorFilter(defaultPoiColor);
                poiName.setTextColor(defaultPoiNameColor);
            }
        }

        @OnClick(R.id.remove_button)
        void poiBinClicked() {
            Timber.d("removePoi %d", this.position);
            removePoi(this.position);
        }

        @OnClick(R.id.poi_container)
        void poiClicked() {
            Timber.d("poiClicked %d", this.position);
            choosePoi(this.position);
        }
    }
}
