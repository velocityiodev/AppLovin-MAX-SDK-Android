package com.applovin.sdk;

import android.content.Context;

import com.applovin.impl.privacy.ComplianceManager;
import com.applovin.impl.sdk.CoreSdk;
import com.applovin.impl.sdk.Logger;
import com.applovin.impl.sdk.utils.Utils;

import androidx.annotation.Nullable;
import lombok.val;

/**
 * This class contains privacy settings for AppLovin.
 */
public class AppLovinPrivacySettings
{
    private static final String TAG = "AppLovinPrivacySettings";

    /**
     * Set whether or not user has provided consent for information sharing.
     *
     * @param hasUserConsent {@code true} if the user has provided consent for information sharing.
     */
    public static void setHasUserConsent(final boolean hasUserConsent)
    {
        setHasUserConsent( hasUserConsent, CoreSdk.getApplicationContext() );
    }

    /**
     * @deprecated This API is deprecated and will be removed in a future SDK version. Please use {@link #setHasUserConsent(boolean)} instead.
     */
    @Deprecated
    public static void setHasUserConsent(final boolean hasUserConsent, final Context context)
    {
        Logger.userDebug( TAG, "setHasUserConsent()" );

        val isValueChanged = ComplianceManager.setHasUserConsent( hasUserConsent, context );
        if ( isValueChanged )
        {
            AppLovinSdk.getInstance( context ).reinitialize( hasUserConsent, null );
        }
    }

    /**
     * Check if user has provided consent for information sharing.
     *
     * @return {@code true} if user has provided consent for information sharing. {@code false} if the user declined to share information or the consent value has not been set (see {@link #isUserConsentSet()}).
     */
    public static boolean hasUserConsent()
    {
        return hasUserConsent( CoreSdk.getApplicationContext() );
    }

    /**
     * @deprecated This API is deprecated and will be removed in a future SDK version. Please use {@link #hasUserConsent()} instead.
     */
    @Deprecated
    public static boolean hasUserConsent(final Context context)
    {
        val hasUserConsent = ComplianceManager.hasUserConsent().getValue( context );
        if ( hasUserConsent != null )
        {
            return hasUserConsent;
        }
        else
        {
            return false;
        }
    }

    /**
     * Check if user has set consent for information sharing.
     *
     * @return {@code true} if user has set a value of consent for information sharing.
     */
    public static boolean isUserConsentSet()
    {
        return isUserConsentSet( CoreSdk.getApplicationContext() );
    }

    /**
     * @deprecated This API is deprecated and will be removed in a future SDK version. Please use {@link #isUserConsentSet()} instead.
     */
    @Deprecated
    public static boolean isUserConsentSet(final Context context)
    {
        return ComplianceManager.hasUserConsent().getValue( context ) != null;
    }

    /**
     * Set whether or not user has opted out of the sale of their personal information.
     *
     * @param doNotSell {@code true} if the user has opted out of the sale of their personal information.
     */
    public static void setDoNotSell(final boolean doNotSell)
    {
        setDoNotSell( doNotSell, CoreSdk.getApplicationContext() );
    }

    /**
     * @deprecated This API is deprecated and will be removed in a future SDK version. Please use {@link #setDoNotSell(boolean)} instead.
     */
    @Deprecated
    public static void setDoNotSell(final boolean doNotSell, final Context context)
    {
        Logger.userDebug( TAG, "setDoNotSell()" );

        val isValueChanged = ComplianceManager.setDoNotSell( doNotSell, context );
        if ( isValueChanged )
        {
            AppLovinSdk.getInstance( context ).reinitialize( null, doNotSell );
        }
    }

    /**
     * Check if the user has opted out of the sale of their personal information.
     *
     * @return {@code true} if user has opted out of the sale of their personal information. {@code false} if the user opted in to the sale of their personal information or the value has not been set (see {@link #isDoNotSellSet()}).
     */
    public static boolean isDoNotSell()
    {
        return isDoNotSell( CoreSdk.getApplicationContext() );
    }

    /**
     * @deprecated This API is deprecated and will be removed in a future SDK version. Please use {@link #isDoNotSell()} instead.
     */
    @Deprecated
    public static boolean isDoNotSell(final Context context)
    {
        val isDoNotSell = ComplianceManager.doNotSell().getValue( context );
        if ( isDoNotSell != null )
        {
            return isDoNotSell;
        }
        else
        {
            return false;
        }
    }

    /**
     * Check if the user has set the option to sell their personal information.
     *
     * @return {@code true} if user has chosen an option to sell their personal information.
     */
    public static boolean isDoNotSellSet()
    {
        return isDoNotSellSet( CoreSdk.getApplicationContext() );
    }

    /**
     * @deprecated This API is deprecated and will be removed in a future SDK version. Please use {@link #isDoNotSellSet()} instead.
     */
    @Deprecated
    public static boolean isDoNotSellSet(final Context context)
    {
        return ComplianceManager.doNotSell().getValue( context ) != null;
    }

    /**
     * Parses the IABTCF_VendorConsents string to determine the consent status of the IAB vendor with the provided ID.
     * <p>
     *
     * @param vendorId Vendor ID as defined in the Global Vendor List.
     *
     * @return {@code true} if the vendor has consent, {@code false} if not, or {@code null} if TC data is not available on disk.
     * @see <a href="https://vendor-list.consensu.org/v3/vendor-list.json">Current Version of Global Vendor List</a>
     */
    @Nullable
    public static Boolean getTcfVendorConsentStatus(final int vendorId)
    {
        val instance = Utils.getCoreSdkInstance();
        if ( instance == null )
        {
            Logger.userError( TAG, "Unable to retrieve TCF vendor consent status" );
            return null;
        }
        return instance.getTcfManager().getTcfVendorConsentStatus( vendorId );
    }

    /**
     * Parses the IABTCF_AddtlConsent string to determine the consent status of the advertising entity with the provided Ad Technology Provider (ATP) ID.
     * <p>
     *
     * @param atpId ATP ID of the advertising entity (e.g. 89 for Meta Audience Network).
     *
     * @return {@code true} if the advertising entity has consent, {@code false} if not, or {@code null} if no AC string is available on disk or the ATP network was not listed in the CMP flow.
     * @see <a href="https://support.google.com/admanager/answer/9681920">Google’s Additional Consent Mode technical specification</a>
     * @see <a href="https://storage.googleapis.com/tcfac/additional-consent-providers.csv">List of Google ATPs and their IDs</a>
     */
    @Nullable
    public static Boolean getAdditionalConsentStatus(final int atpId)
    {
        val instance = Utils.getCoreSdkInstance();
        if ( instance == null )
        {
            Logger.userError( TAG, "Unable to retrieve additional consent status" );
            return null;
        }
        return instance.getTcfManager().getAdditionalConsentStatus( atpId );
    }

    /**
     * Parses the IABTCF_PurposeConsents String to determine the consent status of the IAB defined data processing purpose.
     * <p>
     *
     * @param purposeId Purpose ID.
     *
     * @return {@code true} if the purpose has consent, {@code false} if not, or {@code null} if TC data is not available on disk.
     * @see <a href="https://iabeurope.eu/iab-europe-transparency-consent-framework-policies">IAB Europe Transparency & Consent Framework Policies (Appendix A)</a> for purpose definitions.
     */
    @Nullable
    public static Boolean getPurposeConsentStatus(final int purposeId)
    {
        val instance = Utils.getCoreSdkInstance();
        if ( instance == null )
        {
            Logger.userError( TAG, "Unable to retrieve purpose consent status" );
            return null;
        }
        return instance.getTcfManager().getPurposeConsentStatus( purposeId );
    }

    /**
     * Parses the IABTCF_SpecialFeaturesOptIns String to determine the opt-in status of the IAB defined special feature.
     * <p>
     *
     * @param specialFeatureId Special feature ID.
     *
     * @return {@code true} if the user opted in for the special feature, {@code false} if not, or {@code null} if TC data is not available on disk.
     * @see <a href="https://iabeurope.eu/iab-europe-transparency-consent-framework-policies">IAB Europe Transparency & Consent Framework Policies (Appendix A)</a> for special features definitions.
     */
    @Nullable
    public static Boolean getSpecialFeatureOptInStatus(final int specialFeatureId)
    {
        val instance = Utils.getCoreSdkInstance();
        if ( instance == null )
        {
            Logger.userError( TAG, "Unable to retrieve special feature opt in status" );
            return null;
        }
        return instance.getTcfManager().getSpecialFeatureOptInStatus( specialFeatureId );
    }
}
