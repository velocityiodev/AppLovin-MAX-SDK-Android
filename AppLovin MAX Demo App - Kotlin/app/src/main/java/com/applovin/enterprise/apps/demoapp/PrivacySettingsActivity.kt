package com.applovin.enterprise.apps.demoapp

import android.os.Bundle
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.applovin.sdk.AppLovinPrivacySettings

/**
 * An [android.app.Activity] for testing the AppLovin regulatory APIs (GDPR consent and CCPA
 * do-not-sell). Each setting has three states: Not Set, No (false), Yes (true).
 * Tap "Reset All to Not Set" to clear the AppLovin SDK's stored consent values so adapters
 * receive null on the next ad load. Note: the SDK caches these values at launch, so they persist
 * across sessions until explicitly reset here.
 */
class PrivacySettingsActivity : AppCompatActivity()
{
    // SharedPreferences keys written by the AppLovin SDK for consent persistence.
    private val consentKey   = "com.applovin.sdk.compliance.has_user_consent"
    private val doNotSellKey = "com.applovin.sdk.compliance.is_do_not_sell"
    private val prefsFileName = "com.applovin.sdk"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_settings)
        setTitle(R.string.activity_privacy_settings)

        val consentGroup = findViewById<RadioGroup>(R.id.consentRadioGroup)
        val consentStatus = findViewById<TextView>(R.id.consentStatusTextView)
        val doNotSellGroup = findViewById<RadioGroup>(R.id.doNotSellRadioGroup)
        val doNotSellStatus = findViewById<TextView>(R.id.doNotSellStatusTextView)
        val resetButton = findViewById<android.widget.Button>(R.id.resetButton)

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

        resetButton.setOnClickListener {
            getSharedPreferences(prefsFileName, MODE_PRIVATE)
                .edit().remove(consentKey).remove(doNotSellKey).apply()
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
        val consentSet = AppLovinPrivacySettings.isUserConsentSet()
        consentGroup.findViewById<android.widget.RadioButton>(R.id.consentNotSet).isEnabled = !consentSet
        consentGroup.check(
            if (!consentSet) R.id.consentNotSet
            else if (AppLovinPrivacySettings.hasUserConsent()) R.id.consentYes
            else R.id.consentNo
        )
        updateStatus(consentStatus, consentSet, AppLovinPrivacySettings.hasUserConsent())

        val doNotSellSet = AppLovinPrivacySettings.isDoNotSellSet()
        doNotSellGroup.findViewById<android.widget.RadioButton>(R.id.doNotSellNotSet).isEnabled = !doNotSellSet
        doNotSellGroup.check(
            if (!doNotSellSet) R.id.doNotSellNotSet
            else if (AppLovinPrivacySettings.isDoNotSell()) R.id.doNotSellYes
            else R.id.doNotSellNo
        )
        updateStatus(doNotSellStatus, doNotSellSet, AppLovinPrivacySettings.isDoNotSell())
    }

    private fun updateStatus(statusTextView: TextView, isSet: Boolean, value: Boolean)
    {
        statusTextView.text = if (isSet) getString(R.string.privacy_status_set, value.toString()) else getString(R.string.privacy_status_not_set)
    }
}
