package com.applovin.enterprise.apps.demoapp;

import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.applovin.sdk.AppLovinPrivacySettings;

import androidx.appcompat.app.AppCompatActivity;

/**
 * An {@link android.app.Activity} for testing the AppLovin regulatory APIs (GDPR consent and
 * CCPA do-not-sell). Each setting has two states: No (false) and Yes (true).
 * The AppLovin SDK has no public "unset" API — once a value is set it persists until the app is
 * reinstalled or app data is cleared. To test the "Not Set" state (adapter receives null),
 * reinstall the app.
 */
public class PrivacySettingsActivity
        extends AppCompatActivity
{
    private RadioGroup consentGroup;
    private TextView consentStatus;
    private RadioGroup doNotSellGroup;
    private TextView doNotSellStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_privacy_settings );
        setTitle( R.string.activity_privacy_settings );

        consentGroup = findViewById( R.id.consentRadioGroup );
        consentStatus = findViewById( R.id.consentStatusTextView );
        doNotSellGroup = findViewById( R.id.doNotSellRadioGroup );
        doNotSellStatus = findViewById( R.id.doNotSellStatusTextView );

        consentGroup.setOnCheckedChangeListener( (group, checkedId) -> {
            if ( checkedId == R.id.consentNo )       AppLovinPrivacySettings.setHasUserConsent( false );
            else if ( checkedId == R.id.consentYes ) AppLovinPrivacySettings.setHasUserConsent( true );
            refreshUI();
        } );

        doNotSellGroup.setOnCheckedChangeListener( (group, checkedId) -> {
            if ( checkedId == R.id.doNotSellNo )       AppLovinPrivacySettings.setDoNotSell( false );
            else if ( checkedId == R.id.doNotSellYes ) AppLovinPrivacySettings.setDoNotSell( true );
            refreshUI();
        } );
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        refreshUI();
    }

    private void refreshUI()
    {
        if ( AppLovinPrivacySettings.isUserConsentSet() )
            consentGroup.check( AppLovinPrivacySettings.hasUserConsent() ? R.id.consentYes : R.id.consentNo );
        else
            consentGroup.clearCheck();
        updateStatus( consentStatus, AppLovinPrivacySettings.isUserConsentSet(), AppLovinPrivacySettings.hasUserConsent() );

        if ( AppLovinPrivacySettings.isDoNotSellSet() )
            doNotSellGroup.check( AppLovinPrivacySettings.isDoNotSell() ? R.id.doNotSellYes : R.id.doNotSellNo );
        else
            doNotSellGroup.clearCheck();
        updateStatus( doNotSellStatus, AppLovinPrivacySettings.isDoNotSellSet(), AppLovinPrivacySettings.isDoNotSell() );
    }

    private void updateStatus(final TextView statusTextView, final boolean isSet, final boolean value)
    {
        statusTextView.setText( isSet ? getString( R.string.privacy_status_set, Boolean.toString( value ) ) : getString( R.string.privacy_status_not_set ) );
    }
}
