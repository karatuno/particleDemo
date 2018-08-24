package com.example.saini.particle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.cloud.exceptions.ParticleCloudException;
import io.particle.android.sdk.devicesetup.ParticleDeviceSetupLibrary;
import io.particle.android.sdk.utils.Async;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParticleDeviceSetupLibrary.init(this);
        //On "button" click call start Photon setup
        findViewById(R.id.button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ParticleDeviceSetupLibrary
                                .startDeviceSetup(MainActivity.this,
                                        MainActivity.class);
                    }
                });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1
                Async.executeAsync(ParticleCloudSDK.getCloud(),
                        new Async.ApiProcedure<ParticleCloud>() {
                            @Override
                            public Void callApi(ParticleCloud particleCloud)
                                    throws ParticleCloudException, IOException {
                                //2
                                List<ParticleDevice> devices = particleCloud.getDevices();
                                //3
                                for (ParticleDevice particleDevice : devices) {
                                    if ("myDevice".equals(particleDevice.getName())) {
                                        try {
                                            //4
                                            int result = particleDevice
                                                    .callFunction("digitalwrite",
                                                            Arrays.asList("D7", "HIGH"));
                                            //5
                                            if (result == 1) {
                                                Toast.makeText(MainActivity.this,
                                                        "Called a function on myDevice",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (ParticleDevice
                                                .FunctionDoesNotExistException e) {
                                            //e.printStackTrace() to see whole stack trace
                                        }
                                    }
                                }
                                return null;
                            }

                            @Override
                            public void onFailure(ParticleCloudException exception) {
                                //e.printStackTrace() to see whole stack trace
                            }
                        });
            }
        });
    }
}
