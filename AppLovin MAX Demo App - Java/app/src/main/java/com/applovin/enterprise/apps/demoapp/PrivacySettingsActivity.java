package com.applovin.enterprise.apps.demoapp;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.applovin.sdk.AppLovinPrivacySettings;

import androidx.appcompat.app.AppCompatActivity;

/**
 * An {@link android.app.Activity} for testing the AppLovin regulatory APIs (GDPR consent and
 * CCPA do-not-sell). Each setting has three states: Not Set (initial, read-only once a value is
 * stored), No (false), Yes (true). Values are stored by the AppLovin SDK and forwarded to network
 * adapters with the next ad load.
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

        // Initialise radio selection to match stored state.
        // "Not Set" button is disabled once a value has been written (the SDK has no public clear API).
        consentGroup.check(
                !AppLovinPrivacySettings.isUserConsentSet( this ) ? R.id.consentNotSet
                : AppLovinPrivacySettings.hasUserConsent( this ) ? R.id.consentYes : R.id.consentNo
        );
        final RadioButton consentNotSetButton = consentGroup.findViewById( R.id.consentNotSet );
        consentNotSetButton.setEnabled( !AppLovinPrivacySettings.isUserConsentSet( this ) );
        updateStatus( consentStatus, AppLovinPrivacySettings.isUserConsentSet( this ), AppLovinPrivacySettings.hasUserConsent( this ) );

        doNotSellGroup.check(
                !AppLovinPrivacySettings.isDoNotSellSet( this ) ? R.id.doNotSellNotSet
                : AppLovinPrivacySettings.isDoNotSell( this ) ? R.id.doNotSellYes : R.id.doNotSellNo
        );
        final RadioButton doNotSellNotSetButton = doNotSellGroup.findViewById( R.id.doNotSellNotSet );
        doNotSellNotSetButton.setEnabled( !AppLovinPrivacySettings.isDoNotSellSet( this ) );
        updateStatus( doNotSellStatus, AppLovinPrivacySettings.isDoNotSellSet( this ), AppLovinPrivacySettings.isDoNotSell( this ) );

        consentGroup.setOnCheckedChangeListener( (group, checkedId) -> {
            if ( checkedId == R.id.consentNo )       AppLovinPrivacySettings.setHasUserConsent( false, this );
            else if ( checkedId == R.id.consentYes ) AppLovinPrivacySettings.setHasUserConsent( true, this );
            consentNotSetButton.setEnabled( !AppLovinPrivacySettings.isUserConsentSet( this ) );
            updateStatus( consentStatus, AppLovinPrivacySettings.isUserConsentSet( this ), AppLovinPrivacySettings.hasUserConsent( this ) );
        } );

        doNotSellGroup.setOnCheckedChangeListener( (group, checkedId) -> {
            if ( checkedId == R.id.doNotSellNo )       AppLovinPrivacySettings.setDoNotSell( false, this );
            else if ( checkedId == R.id.doNotSellYes ) AppLovinPrivacySettings.setDoNotSell( true, this );
            doNotSellNotSetButton.setEnabled( !AppLovinPrivacySettings.isDoNotSellSet( this ) );
            updateStatus( doNotSellStatus, AppLovinPrivacySettings.isDoNotSellSet( this ), AppLovinPrivacySettings.isDoNotSell( this ) );
        } );
    }

    private void updateStatus(final TextView statusTextView, final boolean isSet, final boolean value)
    {
        statusTextView.setText( isSet ? getString( R.string.privacy_status_set, Boolean.toString( value ) ) : getString( R.string.privacy_status_not_set ) );
    }
}
