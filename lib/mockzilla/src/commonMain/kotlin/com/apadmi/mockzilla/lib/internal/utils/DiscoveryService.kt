package com.apadmi.mockzilla.lib.internal.utils

/**
 * @property x
 */
internal expect class DiscoveryService(x: String) {
    fun makeDiscoverable()
}
