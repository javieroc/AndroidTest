package com.connan.safetap.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.connan.safetap.R;
import com.hypertrack.lib.HyperTrack;
import com.hypertrack.lib.HyperTrackMapAdapter;
import com.hypertrack.lib.callbacks.HyperTrackCallback;
import com.hypertrack.lib.models.Action;
import com.hypertrack.lib.models.ActionParamsBuilder;
import com.hypertrack.lib.models.ErrorResponse;
import com.hypertrack.lib.models.GeoJSONLocation;
import com.hypertrack.lib.models.Place;
import com.hypertrack.lib.models.SuccessResponse;
import com.hypertrack.lib.tracking.MapProvider.HyperTrackMapFragment;
import com.hypertrack.lib.tracking.MapProvider.MapFragmentView;

import java.util.ArrayList;
import java.util.List;

import com.connan.safetap.base.MyHyperTrackMapAdapter;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private MyHyperTrackMapAdapter myMapAdapter;
    HyperTrackMapFragment hyperTrackMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupHypertrackMapFragment();
        createAction();
    }

    public void setupHypertrackMapFragment() {
        hyperTrackMapFragment = (HyperTrackMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        myMapAdapter = new MyHyperTrackMapAdapter(this);
        //hyperTrackMapFragment.setMapAdapter(new HyperTrackMapAdapter(this));
        // hyperTrackMapFragment.setMapCallback(new MapCallback());
        hyperTrackMapFragment.setMapAdapter(myMapAdapter);
    }


    public void createAction() {
        Place expectedPlace = new Place();
        expectedPlace.setLocation(new GeoJSONLocation(-34.570642, -58.424640));

        ActionParamsBuilder actionParamsBuilder = new ActionParamsBuilder();
        actionParamsBuilder.setType(Action.TYPE_DELIVERY);
        actionParamsBuilder.setExpectedPlace(expectedPlace);

        HyperTrack.createAction(actionParamsBuilder.build(), new HyperTrackCallback() {
            @Override
            public void onSuccess(@NonNull SuccessResponse response) {
                Action action = (Action) response.getResponseObject();
                saveDeliveryAction(action);
            }

            @Override
            public void onError(@NonNull ErrorResponse errorResponse) {
            }
        });
    }

    private void saveDeliveryAction(final Action action) {
        List<String> ids = new ArrayList<String>();
        ids.add(action.getId());
        HyperTrack.trackAction(ids, new HyperTrackCallback() {
            @Override
            public void onSuccess(@NonNull SuccessResponse response) {
                hyperTrackMapFragment.setUseCaseType(MapFragmentView.Type.ORDER_TRACKING);
            }

            @Override
            public void onError(@NonNull ErrorResponse errorResponse) {
                Log.e(TAG, "onError: " + errorResponse.getErrorMessage());
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        HyperTrack.removeActions(null);
    }
}
