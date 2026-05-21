package com.muhdila.accurateuserapp.core.domain.analytics

interface IAnalyticsTracker {
    fun logEvent(name: String, params: Map<String, Any> = emptyMap())
}
