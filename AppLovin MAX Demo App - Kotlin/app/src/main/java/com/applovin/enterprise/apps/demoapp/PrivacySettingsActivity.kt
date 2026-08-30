package com.applovin.enterprise.apps.demoapp

import android.os.Bundle
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.applovin.sdk.AppLovinPrivacySettings

/**
 * An [android.app.Activity] for testing the AppLovin regulatory APIs (GDPR consent and CCPA
 * do-not-sell). Each setting has two states: No (false) and Yes (true).
 * The AppLovin SDK has no public "unset" API — once a value is set it persists until the app is
 * reinstalled or app data is cleared. To test the "Not Set" state (adapter receives null),
 * reinstall the app.
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

        refreshUI(consentGroup, consentStatus, doNotSellGroup, doNotSellStatus)

        consentGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId)
            {
                R.id.consentNo  -> AppLovinPrivacySettings.setHasUserConsent(false)
                R.id.consentYes -> AppLovinPrivacySettings.setHasUserConsent(true)
            }
            refreshUI(consentGroup, consentStatus, doNotSellGroup, doNotSellStatus)
        }

        doNotSellGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId)
            {
                R.id.doNotSellNo  -> AppLovinPrivacySettings.setDoNotSell(false)
                R.id.doNotSellYes -> AppLovinPrivacySettings.setDoNotSell(true)
            }
            refreshUI(consentGroup, consentStatus, doNotSellGroup, doNotSellStatus)
        }
    }

    private fun refreshUI(
        consentGroup: RadioGroup,
        consentStatus: TextView,
        doNotSellGroup: RadioGroup,
        doNotSellStatus: TextView
    )
    {
        if (AppLovinPrivacySettings.isUserConsentSet())
            consentGroup.check(if (AppLovinPrivacySettings.hasUserConsent()) R.id.consentYes else R.id.consentNo)
        else
            consentGroup.clearCheck()
        updateStatus(consentStatus, AppLovinPrivacySettings.isUserConsentSet(), AppLovinPrivacySettings.hasUserConsent())

        if (AppLovinPrivacySettings.isDoNotSellSet())
            doNotSellGroup.check(if (AppLovinPrivacySettings.isDoNotSell()) R.id.doNotSellYes else R.id.doNotSellNo)
        else
            doNotSellGroup.clearCheck()
        updateStatus(doNotSellStatus, AppLovinPrivacySettings.isDoNotSellSet(), AppLovinPrivacySettings.isDoNotSell())
    }

    private fun updateStatus(statusTextView: TextView, isSet: Boolean, value: Boolean)
    {
        statusTextView.text = if (isSet) getString(R.string.privacy_status_set, value.toString()) else getString(R.string.privacy_status_not_set)
    }
}
