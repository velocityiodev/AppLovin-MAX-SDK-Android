package com.applovin.enterprise.apps.demoapp.ads.max.nativead

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applovin.enterprise.apps.demoapp.R
import com.applovin.mediation.MaxAd
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import com.applovin.mediation.nativeAds.adPlacer.MaxAdPlacer
import com.applovin.mediation.nativeAds.adPlacer.MaxAdPlacerSettings
import com.applovin.mediation.nativeAds.adPlacer.MaxRecyclerAdapter

class RecyclerViewNativeAdActivity : AppCompatActivity() {

    private val sampleData = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".chunked(1)
    private lateinit var adAdapter: MaxRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_recycler_view)

        // Create recycler adapter
        val originalAdapter = CustomRecyclerAdapter(this, sampleData)

        // Configure ad adapter
        val settings = MaxAdPlacerSettings("fbca8f772f36695f")
        settings.addFixedPosition(2)
        settings.addFixedPosition(8)
        settings.repeatingInterval = 6

        // If using custom views, you must also set the nativeAdViewBinder on the adapter

        adAdapter = MaxRecyclerAdapter(settings, originalAdapter, this)

        // Velocity Ads is a custom network, so MAX does not deliver pre-rendered template views —
        // the ad placer requires a custom view binder plus an explicit ad size to render ads.
        val binder = MaxNativeAdViewBinder.Builder(R.layout.native_custom_ad_view)
            .setTitleTextViewId(R.id.title_text_view)
            .setBodyTextViewId(R.id.body_text_view)
            .setAdvertiserTextViewId(R.id.advertiser_text_view)
            .setIconImageViewId(R.id.icon_image_view)
            .setMediaContentViewGroupId(R.id.media_view_container)
            .setOptionsContentViewGroupId(R.id.options_view)
            .setStarRatingContentViewGroupId(R.id.star_rating_view)
            .setCallToActionButtonId(R.id.cta_button)
            .build()
        adAdapter.adPlacer.setNativeAdViewBinder(binder)
        // Negative values are used verbatim as LayoutParams (MATCH_PARENT width, WRAP_CONTENT height).
        adAdapter.adPlacer.setAdSize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        adAdapter.setListener(object : MaxAdPlacer.Listener {
            override fun onAdLoaded(position: Int) {}

            override fun onAdRemoved(position: Int) {}

            override fun onAdClicked(ad: MaxAd?) {}

            override fun onAdRevenuePaid(ad: MaxAd?) {}
        })

        // Configure recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        adAdapter.loadAds()
    }

    override fun onDestroy() {
        adAdapter.destroy()
        super.onDestroy()
    }

    class CustomRecyclerAdapter(private val activity: Activity, val data: List<String>) : RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = activity.layoutInflater.inflate(R.layout.activity_text_recycler_view_holder, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textView.text = data[position]
        }

        override fun getItemCount(): Int {
            return data.size
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.text_view)
        }

    }
}
