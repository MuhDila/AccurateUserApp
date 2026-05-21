package com.muhdila.accurateuserapp.core.data.analytics

import android.util.Log
import com.muhdila.accurateuserapp.core.domain.analytics.IAnalyticsTracker
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConsoleAnalyticsTracker @Inject constructor() : IAnalyticsTracker {
    override fun logEvent(name: String, params: Map<String, Any>) {
        Log.d("AccurateAnalytics", "Event logged -> Name: $name, Params: $params")
    }
}
