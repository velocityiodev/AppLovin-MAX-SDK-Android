package com.applovin.enterprise.apps.demoapp

import android.os.Bundle
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.applovin.sdk.AppLovinPrivacySettings

/**
 * An [android.app.Activity] for testing the AppLovin regulatory APIs (GDPR consent and CCPA
 * do-not-sell). Each setting has three states: Not Set (initial, read-only once a value is stored),
 * No (false), Yes (true). Values are stored by the AppLovin SDK and forwarded to network adapters
 * with the next ad load.
 */
class PrivacySettingsActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_settings)
        setTitle(R.string.activity_privacy_settings)

        val consentGroup = findViewById<RadioGroup>(R.id.consentRadioGroup)
        val consentStatus = findViewById<TextView>(R.id.consentStatusTextView)
        val doNotSellGroup = findViewById<RadioGroup>(R.id.doNotSellRadioGroup)
        val doNotSellStatus = findViewById<TextView>(R.id.doNotSellStatusTextView)

        // Initialise radio selection to match stored state.
        // "Not Set" button is disabled once a value has been written (the SDK has no public clear API).
        consentGroup.check(
            if (!AppLovinPrivacySettings.isUserConsentSet(this)) R.id.consentNotSet
            else if (AppLovinPrivacySettings.hasUserConsent(this)) R.id.consentYes
            else R.id.consentNo
        )
        consentGroup.findViewById<android.widget.RadioButton>(R.id.consentNotSet)
            .isEnabled = !AppLovinPrivacySettings.isUserConsentSet(this)
        updateStatus(consentStatus, AppLovinPrivacySettings.isUserConsentSet(this), AppLovinPrivacySettings.hasUserConsent(this))

        doNotSellGroup.check(
            if (!AppLovinPrivacySettings.isDoNotSellSet(this)) R.id.doNotSellNotSet
            else if (AppLovinPrivacySettings.isDoNotSell(this)) R.id.doNotSellYes
            else R.id.doNotSellNo
        )
        doNotSellGroup.findViewById<android.widget.RadioButton>(R.id.doNotSellNotSet)
            .isEnabled = !AppLovinPrivacySettings.isDoNotSellSet(this)
        updateStatus(doNotSellStatus, AppLovinPrivacySettings.isDoNotSellSet(this), AppLovinPrivacySettings.isDoNotSell(this))

        consentGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId)
            {
                R.id.consentNo  -> AppLovinPrivacySettings.setHasUserConsent(false, this)
                R.id.consentYes -> AppLovinPrivacySettings.setHasUserConsent(true, this)
                // consentNotSet is disabled once any value is stored — no action needed
            }
            // Disable "Not Set" after first write
            consentGroup.findViewById<android.widget.RadioButton>(R.id.consentNotSet)
                .isEnabled = !AppLovinPrivacySettings.isUserConsentSet(this)
            updateStatus(consentStatus, AppLovinPrivacySettings.isUserConsentSet(this), AppLovinPrivacySettings.hasUserConsent(this))
        }

        doNotSellGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId)
            {
                R.id.doNotSellNo  -> AppLovinPrivacySettings.setDoNotSell(false, this)
                R.id.doNotSellYes -> AppLovinPrivacySettings.setDoNotSell(true, this)
            }
            doNotSellGroup.findViewById<android.widget.RadioButton>(R.id.doNotSellNotSet)
                .isEnabled = !AppLovinPrivacySettings.isDoNotSellSet(this)
            updateStatus(doNotSellStatus, AppLovinPrivacySettings.isDoNotSellSet(this), AppLovinPrivacySettings.isDoNotSell(this))
        }
    }

    private fun updateStatus(statusTextView: TextView, isSet: Boolean, value: Boolean)
    {
        statusTextView.text = if (isSet) getString(R.string.privacy_status_set, value.toString()) else getString(R.string.privacy_status_not_set)
    }
}
