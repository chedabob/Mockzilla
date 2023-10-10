//
//  AppDelegate.swift
//  iosApp
//
//  Created by Sam Da Costa on 29/10/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import UIKit
import shared
import Network

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    private let listener: nw_listener_t = {
        let params = nw_parameters_create()
        let tcpOptions = nw_tcp_create_options()
        nw_tcp_options_set_enable_keepalive(tcpOptions, true)
        nw_tcp_options_set_connection_timeout(tcpOptions, 60)

        let stack = nw_parameters_copy_default_protocol_stack(params)
        nw_protocol_stack_set_transport_protocol(stack, tcpOptions)
        
        let l = nw_listener_create(params)
        
        return l!
    }()
    
    static var repository: Repository!
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        let params = MockServerKt.startMockServer()
        AppDelegate.repository = Repository(baseUrl: params.mockBaseUrl)
//        makeDiscoverable()
        return true
    }
    
    func makeDiscoverable() {
        let lkssa = nw_advertise_descriptor_create_bonjour_service(nil, "_mockzilla._tcp.", nil)
        let x = nw_txt_record_create_dictionary()

        nw_txt_record_set_key(x, "host", "192.168.88.30", 10)
        nw_advertise_descriptor_set_txt_record_object(lkssa!, x)
        nw_listener_set_advertise_descriptor(listener, lkssa)
        nw_listener_set_new_connection_handler(listener) {_ in 
            // Do nothing
        }

        nw_listener_set_state_changed_handler(listener) { state, obj in
            switch state {
            case nw_listener_state_ready:
                debugPrint("ready")
            case nw_listener_state_failed:
                debugPrint("failed", obj)
            case nw_listener_state_invalid:
                debugPrint("invalid")
            case nw_listener_state_waiting:
                debugPrint("waiting")
            case nw_listener_state_cancelled:
                debugPrint("cancelled")
            default:
                debugPrint(state)

            }
        }

        nw_listener_set_queue(listener, DispatchQueue.main)
        nw_listener_start(listener)

    }
    
    func application(_: UIApplication,
                     configurationForConnecting connectingSceneSession: UISceneSession,
                     options _: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }
    
    func applicationWillTerminate(_ application: UIApplication) {
        MockServerKt.stopMockServer()
    }
}
