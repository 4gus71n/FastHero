//
//  MainViewScreenUIViewControllerRepresentable.swift
//  iosApp
//
//  Created by Agustin Larghi on 17/03/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import ComposeApp

public struct SettingsViewScreenRepresentable: UIViewControllerRepresentable {
    @Binding var state: SettingsViewState
    var onSlideChanged: ((KotlinInt, KotlinInt, KotlinInt) -> Void)
    var onOptionSelected: ((String) -> Void)
    var onBackBtnClicked: (() -> Void)

    public func makeUIViewController(context: Context) -> UIViewController {
        SettingsViewScreenUIViewController().make(
            onSlideChanged: onSlideChanged,
            onOptionSelected: onOptionSelected,
            onBackButtonClicked: onBackBtnClicked
        )
    }

    public func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        SettingsViewScreenUIViewController().update(state: state)
    }
}
