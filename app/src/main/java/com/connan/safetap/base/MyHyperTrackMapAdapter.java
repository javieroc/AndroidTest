package com.connan.safetap.base;

import android.content.Context;

import com.connan.safetap.R;
import com.hypertrack.lib.HyperTrackMapAdapter;
import com.hypertrack.lib.internal.transmitter.models.UserActivity;
import com.hypertrack.lib.tracking.model.InfoBoxModel;

public class MyHyperTrackMapAdapter extends HyperTrackMapAdapter {

    public MyHyperTrackMapAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public int getUserMarkerIconForActionID(Context mContext, InfoBoxModel.Type markerType, UserActivity.ActivityType activityType, String actionID) {
        return R.drawable.marker_small;
    }

    @Override
    public boolean showUserInfo() {
        return false;
    }

    @Override
    public int getPulseColor() {
        return R.color.pulseColor;
    }

}
