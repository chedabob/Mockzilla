package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.nativedarwin.localdiscovery.BonjourService
import platform.UIKit.UIDevice

internal actual class DiscoveryService actual constructor(x: String) {
    private val bonjour = BonjourService()
    @Suppress("diktat")
    actual fun makeDiscoverable() {
        val deviceName = UIDevice.currentDevice.name
        bonjour.startWithName(deviceName);
    }
}
