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
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_privacy_settings );
        setTitle( R.string.activity_privacy_settings );

        final RadioGroup consentGroup = findViewById( R.id.consentRadioGroup );
        final TextView consentStatus = findViewById( R.id.consentStatusTextView );
        final RadioGroup doNotSellGroup = findViewById( R.id.doNotSellRadioGroup );
        final TextView doNotSellStatus = findViewById( R.id.doNotSellStatusTextView );

        refreshUI( consentGroup, consentStatus, doNotSellGroup, doNotSellStatus );

        consentGroup.setOnCheckedChangeListener( (group, checkedId) -> {
            if ( checkedId == R.id.consentNo )       AppLovinPrivacySettings.setHasUserConsent( false );
            else if ( checkedId == R.id.consentYes ) AppLovinPrivacySettings.setHasUserConsent( true );
            refreshUI( consentGroup, consentStatus, doNotSellGroup, doNotSellStatus );
        } );

        doNotSellGroup.setOnCheckedChangeListener( (group, checkedId) -> {
            if ( checkedId == R.id.doNotSellNo )       AppLovinPrivacySettings.setDoNotSell( false );
            else if ( checkedId == R.id.doNotSellYes ) AppLovinPrivacySettings.setDoNotSell( true );
            refreshUI( consentGroup, consentStatus, doNotSellGroup, doNotSellStatus );
        } );
    }

    private void refreshUI(
            final RadioGroup consentGroup,
            final TextView consentStatus,
            final RadioGroup doNotSellGroup,
            final TextView doNotSellStatus)
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
