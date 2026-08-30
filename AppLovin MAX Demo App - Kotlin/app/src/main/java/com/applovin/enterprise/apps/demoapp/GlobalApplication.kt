package com.applovin.enterprise.apps.demoapp

import android.app.Application
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.applovin.sdk.AppLovinMediationProvider
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkInitializationConfiguration
import java.util.concurrent.Executors


class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // TODO: Replace with your MAX SDK key from the AppLovin dashboard, and update applicationId in app/build.gradle to match.
        val YOUR_SDK_KEY = "FJz18KTsZXDLh7BWRL6pdsEGJ9EneQnoubC7i0mfGMBoALugoZryZune5cIQMb3knWULUwD7OCVqyiE2xC_bbN"

        val executor = Executors.newSingleThreadExecutor();
        executor.execute {

            val initConfigBuilder = AppLovinSdkInitializationConfiguration.builder(YOUR_SDK_KEY, this)
            initConfigBuilder.mediationProvider = AppLovinMediationProvider.MAX

            // Test mode is intentionally disabled: AppLovin custom SDK networks do not appear in
            // the waterfall when test mode is active. Test on a physical device with test mode off.

            // Initialize the AppLovin SDK
            val sdk = AppLovinSdk.getInstance(this)
            sdk.initialize(initConfigBuilder.build()) {
                // AppLovin SDK is initialized, start loading ads now or later if ad gate is reached

                // Initialize Adjust SDK (v5 handles activity lifecycle internally)
                val config = AdjustConfig(this, "{YourAppToken}", AdjustConfig.ENVIRONMENT_SANDBOX)
                Adjust.initSdk(config)
            }

            executor.shutdown()
        }
    }
}
