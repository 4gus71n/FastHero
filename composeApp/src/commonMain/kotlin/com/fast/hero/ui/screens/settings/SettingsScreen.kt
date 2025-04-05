@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.fast.hero.ui.screens.settings


import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import com.fast.hero.ui.theme.FastHeroTheme
import com.fast.hero.ui.utils.MockData

data class SettingOptionList(
    val id: String,
    val title: String,
    val description: String,
    val fastingTimeInSeconds: Int
)

@Composable
@Preview
fun SettingsScreen_Preview() {
    SettingsScreen(optionList = MockData.fastingOptions)
}

@Composable
fun SettingsScreen(
    onBackButtonClicked: (() -> Unit) = {},
    onSlideChanged: ((Int, Int, Int) -> Unit) = {_, _, _ -> },
    optionList: List<SettingOptionList>,
    onOptionSelected: ((String) -> Unit) = {}
) {
    val selectedOption = remember { mutableStateOf("") }

    val sliderStateDifficulty = remember { mutableStateOf(0.5f) }
    val sliderStateCompromise = remember { mutableStateOf(0.5f) }
    val sliderStateDoability = remember { mutableStateOf(0.5f) }

    fun Float.toRange() = (this * 9).toInt() + 1

    FastHeroTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Fasting Plan") },
                    navigationIcon = {
                        IconButton(onClick = onBackButtonClicked) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp), 
                verticalArrangement = Arrangement.spacedBy(16.dp), 
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "We create a personalized weekly fasting plan designed around your preferences.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(Modifier.height(16.dp)) 
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Adjust your preferences",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Spacer(Modifier.height(24.dp)) 
                }

                // Slider Preferences
                item {
                    PreferenceOption(
                        legend = "I don't have time for meal prep.",
                        value = sliderStateDifficulty.value,
                        onValueChanged = {
                            sliderStateDifficulty.value = it
                            onSlideChanged(sliderStateDifficulty.value.toRange(), sliderStateCompromise.value.toRange(), sliderStateDoability.value.toRange())
                        }
                    )
                    Spacer(Modifier.height(24.dp))
                    PreferenceOption(
                        legend = "I've no problem sticking to a strict plan.",
                        value = sliderStateCompromise.value,
                        onValueChanged = {
                            sliderStateCompromise.value = it
                            onSlideChanged(sliderStateDifficulty.value.toRange(), sliderStateCompromise.value.toRange(), sliderStateDoability.value.toRange())
                        }
                    )
                    Spacer(Modifier.height(24.dp))
                    PreferenceOption(
                        legend = "I'll be upset if I can't keep up with the fast.",
                        value = sliderStateDoability.value,
                        onValueChanged = {
                            sliderStateDoability.value = it
                            onSlideChanged(sliderStateDifficulty.value.toRange(), sliderStateCompromise.value.toRange(), sliderStateDoability.value.toRange())
                        }
                    )
                }

                item {
                    Spacer(Modifier.height(32.dp)) 
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "We recommend",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Spacer(Modifier.height(24.dp)) 
                }

                // Option List
                items(optionList, key = { it.id }) { option ->
                    SettingTile(
                        title = option.title,
                        description = option.description,
                        isSelected = selectedOption.value == option.id,
                        modifier = Modifier
                            .animateItemPlacement()
                            .clickable {
                                onOptionSelected(option.id)
                                selectedOption.value = option.id
                            }
                    )
                }
                item {
                    Spacer(Modifier.height(16.dp)) 
                }
            }
        }
    }
}


@Composable
fun PreferenceOption(
    legend: String,
    value: Float = 0.5f,
    onValueChanged: ((Float) -> Unit) = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
    ) {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            text = "\"$legend\"",
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(14.dp))
        Slider(
            value = value,
            onValueChange = {
                onValueChanged(it)
            }
        )
        Row(
            modifier = Modifier.wrapContentHeight(),
        ) {
            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f).wrapContentHeight(),
                text = "Disagree",
                style = MaterialTheme.typography.labelSmall,
            )
            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f).wrapContentHeight(),
                text = "Unsure",
                style = MaterialTheme.typography.labelSmall,
            )
            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f).wrapContentHeight(),
                text = "Agree",
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@Composable
@Preview
fun PreferenceOption_Preview() {
    PreferenceOption(
        legend = "I don't have time for meal planning"
    )
}

@Composable
fun SettingTile(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    title: String,
    description: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.background
            )
            .border(
                border = BorderStroke(
                    width = 0.5.dp,
                    color = MaterialTheme.colorScheme.inversePrimary
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
@Preview
fun Tile_Preview() {
    FastHeroTheme {
        Column(Modifier.padding(8.dp)) {
            SettingTile(
                isSelected = true,
                title = "12:12 - Easy/Starter",
                description = "12 hrs fasting, 12 hrs eating. Every day."
            )
            SettingTile(
                isSelected = false,
                title = "12:12 - Easy/Starter",
                description = "12 hrs fasting, 12 hrs eating. Every day."
            )
        }
    }
}