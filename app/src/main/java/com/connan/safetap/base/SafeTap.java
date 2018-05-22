package com.connan.safetap.base;

import android.app.Application;

import com.connan.safetap.R;
import com.hypertrack.lib.HyperTrack;

public class SafeTap extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize HyperTrack SDK with your Publishable Key here.
        // Sign up to get your keys https://www.hypertrack.com/signup
        // Get your keys from https://dashboard.hypertrack.com/settings
        HyperTrack.initialize(this, getString(R.string.hypertrack_key));
    }
}
