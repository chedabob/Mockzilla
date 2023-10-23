package com.apadmi.mockzilla.desktop.ui.widgets.monitorlogs.details

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.viewmodel.ActiveDeviceMonitoringViewModel
import kotlinx.coroutines.CoroutineScope

class MonitorLogDetailsViewModel(
    activeDeviceMonitor: ActiveDeviceMonitor,
    scope: CoroutineScope? = null
) : ActiveDeviceMonitoringViewModel(activeDeviceMonitor, scope) {
    override suspend fun reloadData(selectedDevice: Device?) {
        TODO("Implement class")
    }
}
