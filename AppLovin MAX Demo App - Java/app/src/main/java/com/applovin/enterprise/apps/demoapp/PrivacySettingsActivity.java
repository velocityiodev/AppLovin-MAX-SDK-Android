package com.applovin.enterprise.apps.demoapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.applovin.sdk.AppLovinPrivacySettings;

import androidx.appcompat.app.AppCompatActivity;

/**
 * An {@link android.app.Activity} for testing the AppLovin regulatory APIs (GDPR consent and
 * CCPA do-not-sell). Each setting has three states: Not Set, No (false), Yes (true).
 * A "Reset All" button clears the persisted values so the next adapter call receives absent/null
 * for both flags — useful for a clean test session without reinstalling the app.
 * Values are stored by the AppLovin SDK and forwarded to network adapters with the next ad load.
 */
public class PrivacySettingsActivity
        extends AppCompatActivity
{
    // SharedPreferences keys written by the AppLovin SDK for consent persistence.
    // These are internal keys confirmed from the iOS SDK binary (AppLovin uses the same
    // naming scheme cross-platform). They may change in future SDK versions.
    private static final String CONSENT_KEY    = "com.applovin.sdk.compliance.has_user_consent";
    private static final String DO_NOT_SELL_KEY = "com.applovin.sdk.compliance.is_do_not_sell";

    // AppLovin stores its settings in a SharedPreferences file named after the SDK package.
    private static final String PREFS_FILE_NAME = "com.applovin.sdk";

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
        final Button resetButton = findViewById( R.id.resetButton );

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

        resetButton.setOnClickListener( v -> {
            getSharedPreferences( PREFS_FILE_NAME, MODE_PRIVATE )
                    .edit()
                    .remove( CONSENT_KEY )
                    .remove( DO_NOT_SELL_KEY )
                    .apply();
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
        consentGroup.check(
                !consentSet ? R.id.consentNotSet
                : AppLovinPrivacySettings.hasUserConsent() ? R.id.consentYes : R.id.consentNo
        );
        final RadioButton consentNotSetButton = consentGroup.findViewById( R.id.consentNotSet );
        consentNotSetButton.setEnabled( !consentSet );
        updateStatus( consentStatus, consentSet, AppLovinPrivacySettings.hasUserConsent() );

        final boolean doNotSellSet = AppLovinPrivacySettings.isDoNotSellSet();
        doNotSellGroup.check(
                !doNotSellSet ? R.id.doNotSellNotSet
                : AppLovinPrivacySettings.isDoNotSell() ? R.id.doNotSellYes : R.id.doNotSellNo
        );
        final RadioButton doNotSellNotSetButton = doNotSellGroup.findViewById( R.id.doNotSellNotSet );
        doNotSellNotSetButton.setEnabled( !doNotSellSet );
        updateStatus( doNotSellStatus, doNotSellSet, AppLovinPrivacySettings.isDoNotSell() );
    }

    private void updateStatus(final TextView statusTextView, final boolean isSet, final boolean value)
    {
        statusTextView.setText( isSet ? getString( R.string.privacy_status_set, Boolean.toString( value ) ) : getString( R.string.privacy_status_not_set ) );
    }
}
