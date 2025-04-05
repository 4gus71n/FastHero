//
//  SettingsView.swift
//  iosApp
//
//  Created by Agustin Larghi on 23/03/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import ComposeApp

struct SettingsView: View {
    @EnvironmentObject var countdownViewModel: MainViewModel
    @StateObject private var viewModel = SettingsViewModel()

    @ObservedObject var coordinator: AppCoordinator

    @State private var state = SettingsViewState(optionList: [])

    var body: some View {
        VStack {
            SettingsViewScreenRepresentable(
                state: $state,
                onSlideChanged: { difficulty, compromise, doability in
                    viewModel.sortRecommendationList(
                        difficulty: Int(difficulty),
                        compromise: Int(compromise),
                        doability: Int(doability)
                    )
                },
                onOptionSelected: { fastId in
                    countdownViewModel.setSelectedFast(fastId: fastId)
                    coordinator.goBack()
                },
                onBackBtnClicked: {
                    coordinator.goBack()
                }
            )
            .onReceive(viewModel.$fastingOptions) { optionList in
                updateState(optionList: optionList)
            }
        }
        .navigationBarBackButtonHidden(true)
    }

    private func updateState(optionList: [SettingOptionList]? = nil) {
            state = SettingsViewState(
                optionList: optionList ?? state.optionList
            )
        }
}
