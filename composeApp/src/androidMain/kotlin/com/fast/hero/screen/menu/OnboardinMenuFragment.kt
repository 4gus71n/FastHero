package com.fast.hero.screen.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.fast.hero.screen.main.CountdownViewModel
import com.fast.hero.ui.screens.settings.SettingsScreen
import com.fast.hero.sdk.MockData

class OnboardinMenuFragment : Fragment() {
    private val countdownViewModel by activityViewModels<CountdownViewModel>()
    private val viewModel by activityViewModels<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val optionList by viewModel.fastingOptions.collectAsStateWithLifecycle()
            
                SettingsScreen(
                    optionList = optionList,
                    onSlideChanged = { difficulty, compromise, doability ->
                        viewModel.sortRecommendationList(difficulty, compromise, doability)
                    },
                    onOptionSelected = {
                        countdownViewModel.setSelectedFast(it)
                        findNavController().popBackStack()
                    },
                    onBackButtonClicked = {
                        findNavController().popBackStack()
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    SettingsScreen(optionList = MockData.fastingOptions)
}