package com.connan.safetap;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.connan.safetap.R;
import com.connan.safetap.activities.HomeActivity;
import com.hypertrack.lib.HyperTrack;
import com.hypertrack.lib.callbacks.HyperTrackCallback;
import com.hypertrack.lib.internal.common.util.HTTextUtils;
import com.hypertrack.lib.models.ErrorResponse;
import com.hypertrack.lib.models.SuccessResponse;
import com.hypertrack.lib.models.User;
import com.hypertrack.lib.models.UserParams;

public class LoginActivity extends AppCompatActivity {

    private EditText nameText, phoneNumberText;
    private Button loginBtnLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // checkForLocationSettings();
        initUIViews();
    }

    // Call this method to initialize UI views and handle listeners for these views
    private void initUIViews() {
        // Initialize UserName Views
        nameText = (EditText) findViewById(R.id.username);

        // Initialize Password Views
        phoneNumberText = (EditText) findViewById(R.id.phone);

        // Initialize Login Btn Loader
        loginBtnLoader = (Button) findViewById(R.id.login);
    }

    public void checkForLocationSettings() {
        // Check for Location permission
        if (!HyperTrack.checkLocationPermission(this)) {
            HyperTrack.requestPermissions(this);
            return;
        }

        // Check for Location settings
        if (!HyperTrack.checkLocationServices(this)) {
            HyperTrack.requestLocationServices(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,
                grantResults);

        if (requestCode == HyperTrack.REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                // Check if Location Settings are enabled to proceed
                checkForLocationSettings();

            } else {
                // Handle Location Permission denied error
                Toast.makeText(this, "Location Permission denied.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HyperTrack.REQUEST_CODE_LOCATION_SERVICES) {
            if (resultCode == Activity.RESULT_OK) {
                // Check if Location Settings are enabled to proceed
                checkForLocationSettings();

            } else {
                // Handle Enable Location Services request denied error
                Toast.makeText(this, R.string.enable_location_settings,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onLoginButtonClick(View view) {
        checkForLocationSettings();

        final String name = nameText.getText().toString();
        final String phoneNumber = phoneNumberText.getText().toString();
        String UUID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        final String uniqueId = HTTextUtils.isEmpty(UUID) ? phoneNumber : UUID;

        UserParams userParams = new UserParams().setName(name).setPhone(phoneNumber).setUniqueId(uniqueId);
        HyperTrack.getOrCreateUser(userParams, new HyperTrackCallback() {
            @Override
            public void onSuccess(@NonNull SuccessResponse successResponse) {
                User user = (User) successResponse.getResponseObject();
                Intent trackActivityIntent=new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(trackActivityIntent);
            }

            @Override
            public void onError(@NonNull ErrorResponse errorResponse) {
                Log.e(LoginActivity.class.getSimpleName(), errorResponse.getErrorMessage());
            }
        });
    }

}
