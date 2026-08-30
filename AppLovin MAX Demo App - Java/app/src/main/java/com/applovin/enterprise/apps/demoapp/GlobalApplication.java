package com.applovin.enterprise.apps.demoapp;

import android.app.Application;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkInitializationConfiguration;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GlobalApplication
        extends Application
{
    // If you want to test your own AppLovin SDK key, change the value here and update the package name in the build.gradle
    private static final String YOUR_SDK_KEY = "FJz18KTsZXDLh7BWRL6pdsEGJ9EneQnoubC7i0mfGMBoALugoZryZune5cIQMb3knWULUwD7OCVqyiE2xC_bbN";

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Reset persisted privacy flags at session start so the AppLovin SDK initialises without
        // stale consent state and adapters receive null for both flags on every fresh launch.
        // Use the Privacy Settings screen during the session to test specific consent states.
        getSharedPreferences( "com.applovin.sdk", MODE_PRIVATE ).edit()
                .remove( "com.applovin.sdk.compliance.has_user_consent" )
                .remove( "com.applovin.sdk.compliance.is_do_not_sell" )
                .apply();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute( () -> {

            AppLovinSdkInitializationConfiguration.Builder initConfigBuilder = AppLovinSdkInitializationConfiguration.builder( YOUR_SDK_KEY, this );
            initConfigBuilder.setMediationProvider( AppLovinMediationProvider.MAX );

            try
            {
                // Enable test mode by default for the current device. Cannot be run on the main thread.
                String currentGaid = AdvertisingIdClient.getAdvertisingIdInfo( this ).getId();
                if ( currentGaid != null )
                {
                    initConfigBuilder.setTestDeviceAdvertisingIds( Collections.singletonList( currentGaid ) );
                }
            }
            catch ( Throwable ignored ) { }

            // Initialize the AppLovin SDK
            AppLovinSdk.getInstance( this ).initialize( initConfigBuilder.build(), appLovinSdkConfiguration -> {
                // AppLovin SDK is initialized, start loading ads now or later if ad gate is reached

                // Initialize Adjust SDK
                AdjustConfig adjustConfig = new AdjustConfig( getApplicationContext(), "{YourAppToken}", AdjustConfig.ENVIRONMENT_SANDBOX );
                Adjust.initSdk( adjustConfig );
            } );

            executor.shutdown();
        } );
    }
}
