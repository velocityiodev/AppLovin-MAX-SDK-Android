package com.applovin.enterprise.apps.demoapp

import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.applovin.sdk.AppLovinPrivacySettings

/**
 * An [android.app.Activity] for testing the AppLovin regulatory APIs (GDPR consent and CCPA
 * do-not-sell). Each setting has three states: Not Set, No (false), Yes (true).
 * A "Reset All" button clears the persisted values so the next adapter call receives absent/null
 * for both flags — useful for a clean test session without reinstalling the app.
 * Values are stored by the AppLovin SDK and forwarded to network adapters with the next ad load.
 */
class PrivacySettingsActivity : AppCompatActivity()
{
    // SharedPreferences keys written by the AppLovin SDK for consent persistence.
    // These are internal keys confirmed from the iOS SDK binary (AppLovin uses the same
    // naming scheme cross-platform). They may change in future SDK versions.
    private val consentKey   = "com.applovin.sdk.compliance.has_user_consent"
    private val doNotSellKey = "com.applovin.sdk.compliance.is_do_not_sell"

    // AppLovin stores its settings in a SharedPreferences file named after the SDK package.
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
        val resetButton = findViewById<Button>(R.id.resetButton)

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
            val prefs = getSharedPreferences(prefsFileName, MODE_PRIVATE)
            prefs.edit().remove(consentKey).remove(doNotSellKey).apply()
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
        consentGroup.check(
            if (!consentSet) R.id.consentNotSet
            else if (AppLovinPrivacySettings.hasUserConsent()) R.id.consentYes
            else R.id.consentNo
        )
        consentGroup.findViewById<android.widget.RadioButton>(R.id.consentNotSet).isEnabled = !consentSet
        updateStatus(consentStatus, consentSet, AppLovinPrivacySettings.hasUserConsent())

        val doNotSellSet = AppLovinPrivacySettings.isDoNotSellSet()
        doNotSellGroup.check(
            if (!doNotSellSet) R.id.doNotSellNotSet
            else if (AppLovinPrivacySettings.isDoNotSell()) R.id.doNotSellYes
            else R.id.doNotSellNo
        )
        doNotSellGroup.findViewById<android.widget.RadioButton>(R.id.doNotSellNotSet).isEnabled = !doNotSellSet
        updateStatus(doNotSellStatus, doNotSellSet, AppLovinPrivacySettings.isDoNotSell())
    }

    private fun updateStatus(statusTextView: TextView, isSet: Boolean, value: Boolean)
    {
        statusTextView.text = if (isSet) getString(R.string.privacy_status_set, value.toString()) else getString(R.string.privacy_status_not_set)
    }
}
