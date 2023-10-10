@file:OptIn(ExperimentalForeignApi::class)

package com.apadmi.mockzilla.lib.internal.utils

import platform.Network.nw_advertise_descriptor_create_bonjour_service
import platform.Network.nw_advertise_descriptor_set_txt_record_object
import platform.Network.nw_listener_create_with_port
import platform.Network.nw_listener_set_advertise_descriptor
import platform.Network.nw_listener_set_new_connection_handler
import platform.Network.nw_listener_set_queue
import platform.Network.nw_listener_set_state_changed_handler
import platform.Network.nw_listener_start
import platform.Network.nw_parameters_copy_default_protocol_stack
import platform.Network.nw_parameters_create
import platform.Network.nw_protocol_stack_set_transport_protocol
import platform.Network.nw_tcp_create_options
import platform.Network.nw_tcp_options_set_connection_timeout
import platform.Network.nw_tcp_options_set_enable_keepalive
import platform.Network.nw_txt_record_create_dictionary
import platform.Network.nw_txt_record_set_key
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue
import platform.posix.uint8_tVar

import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned

private const val defaultTimeout = 60

typealias Whatever = Pair<CValuesRef<uint8_tVar>, Int>

internal actual class DiscoveryService actual constructor(x: String) {
    private val listener = nw_listener_create_with_port("8080", nw_parameters_create().apply {

        val tcpOptions = nw_tcp_create_options()
        nw_tcp_options_set_enable_keepalive(tcpOptions, true)
        nw_tcp_options_set_connection_timeout(tcpOptions, defaultTimeout.convert())

        val stack = nw_parameters_copy_default_protocol_stack(this)
        nw_protocol_stack_set_transport_protocol(stack, tcpOptions)
    })
    @Suppress("diktat")
    actual fun makeDiscoverable() {
        val lkssa = nw_advertise_descriptor_create_bonjour_service(null, "_mockzilla._tcp.", null)
        val x = nw_txt_record_create_dictionary()

        val blech = stringToCvaluesRef("192.168.88.30")
        nw_txt_record_set_key(x, "host", blech.first, blech.second.convert())
        nw_advertise_descriptor_set_txt_record_object(lkssa, x)
        nw_listener_set_advertise_descriptor(listener, lkssa)
        nw_listener_set_new_connection_handler(listener) {
            print("New connection")
        }

        nw_listener_set_state_changed_handler(listener) { _: UInt, _: NSObject? ->
            print("State changed")
        }

        nw_listener_set_queue(listener, dispatch_get_main_queue())
        nw_listener_start(listener)
    }

    private fun stringToCvaluesRef(inputString: String): Whatever {
        // Convert the string to a byte array using the UTF-8 encoding
        val byteArray = inputString.encodeToByteArray()

        // Create a CValuesRef<uint8_tVar> that points to the byte array
        val valuesRef = byteArray.usePinned {
            it.addressOf(0).reinterpret<uint8_tVar>()
        }

        return Whatever(valuesRef, byteArray.size)
    }
}
