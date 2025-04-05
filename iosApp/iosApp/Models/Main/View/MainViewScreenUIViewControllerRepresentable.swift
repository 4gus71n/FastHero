//
//  MainViewScreenUIViewControllerRepresentable.swift
//  iosApp
//
//  Created by Agustin Larghi on 17/03/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import ComposeApp

public struct MainViewScreenRepresentable: UIViewControllerRepresentable {
    @Binding var state: MainViewState
    var toggleTimer: (() -> Void)

    public func makeUIViewController(context: Context) -> UIViewController {
        MainViewScreenUIViewController().make(toggleTimer: toggleTimer)
    }

    public func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        MainViewScreenUIViewController().update(state: state)
    }
}
