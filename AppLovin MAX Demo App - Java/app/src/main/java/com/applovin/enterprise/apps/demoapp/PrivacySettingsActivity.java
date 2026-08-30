package com.applovin.enterprise.apps.demoapp;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.applovin.sdk.AppLovinPrivacySettings;

import androidx.appcompat.app.AppCompatActivity;

/**
 * An {@link android.app.Activity} for testing the AppLovin regulatory APIs (GDPR consent and
 * CCPA do-not-sell). Each setting has three states: Not Set, No (false), Yes (true).
 * Privacy flags are cleared automatically on every app launch; values set here persist for the
 * current session only and are forwarded to network adapters with the next ad load.
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
        final boolean consentSet = AppLovinPrivacySettings.isUserConsentSet();
        final RadioButton consentNotSetButton = consentGroup.findViewById( R.id.consentNotSet );
        consentNotSetButton.setEnabled( !consentSet );
        consentGroup.check(
                !consentSet ? R.id.consentNotSet
                : AppLovinPrivacySettings.hasUserConsent() ? R.id.consentYes : R.id.consentNo
        );
        updateStatus( consentStatus, consentSet, AppLovinPrivacySettings.hasUserConsent() );

        final boolean doNotSellSet = AppLovinPrivacySettings.isDoNotSellSet();
        final RadioButton doNotSellNotSetButton = doNotSellGroup.findViewById( R.id.doNotSellNotSet );
        doNotSellNotSetButton.setEnabled( !doNotSellSet );
        doNotSellGroup.check(
                !doNotSellSet ? R.id.doNotSellNotSet
                : AppLovinPrivacySettings.isDoNotSell() ? R.id.doNotSellYes : R.id.doNotSellNo
        );
        updateStatus( doNotSellStatus, doNotSellSet, AppLovinPrivacySettings.isDoNotSell() );
    }

    private void updateStatus(final TextView statusTextView, final boolean isSet, final boolean value)
    {
        statusTextView.setText( isSet ? getString( R.string.privacy_status_set, Boolean.toString( value ) ) : getString( R.string.privacy_status_not_set ) );
    }
}
