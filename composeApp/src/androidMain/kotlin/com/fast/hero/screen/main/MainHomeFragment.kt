package com.fast.hero.screen.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fast.hero.ui.screens.main.MainScreen
import com.fast.hero.R
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

class MainHomeFragment : Fragment() {
    private val viewModel by activityViewModels<CountdownViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val progress by viewModel.progress.collectAsStateWithLifecycle(initialValue = 0f)
                val time by viewModel.time.collectAsStateWithLifecycle(initialValue = "--:--")
                val isRunning by viewModel.isFastingRunning.collectAsStateWithLifecycle(false)
                val feed by viewModel.feed.collectAsStateWithLifecycle(emptyList())

                MainScreen(
                    onButtonClicked = ::onDispatchAction,
                    progress = progress,
                    time = time,
                    isRunning = isRunning,
                    feed = feed
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch { 
            viewModel.shouldResumeFast
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    viewModel.resumeFast()
                }
        }
    }
    
    private fun onDispatchAction() {
        val selectedFast = viewModel.findSelectedFast()
        if (selectedFast == null) {
            // TODO: Implement safeNavigate
            // TODO: Add safe nav plugin so we generate the directions
            findNavController().navigate(R.id.action_mainHomeFragment_to_onboardinMenuFragment)
        } else {
            viewModel.toggleTimer()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    MainScreen()
}