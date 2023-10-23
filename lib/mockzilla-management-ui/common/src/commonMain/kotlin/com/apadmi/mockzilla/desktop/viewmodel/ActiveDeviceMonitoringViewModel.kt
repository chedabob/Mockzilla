package com.apadmi.mockzilla.desktop.viewmodel

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.Device
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

abstract class ActiveDeviceMonitoringViewModel(
    private val activeDeviceMonitor: ActiveDeviceMonitor,
    scope: CoroutineScope? = null
) : ViewModel(scope) {
    init {
        activeDeviceMonitor.selectedDevice.onEach { reloadData(it?.device) }.launchIn(viewModelScope)
    }

    internal suspend fun <T> activeDeviceOrDefault(
        default: T,
        block: suspend (Device) -> T
    ): T = activeDeviceMonitor.selectedDevice.value?.device?.let { block(it) } ?: default

    abstract suspend fun reloadData(selectedDevice: Device?)
}
