package com.example.news_compose_c40.screens.settings

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.news_compose_c40.R
import com.example.news_compose_c40.activity.HomeActivity
import com.example.news_compose_c40.ui.theme.Poppins
import com.example.news_compose_c40.ui.theme.gray
import com.example.news_compose_c40.ui.theme.green
import com.example.news_compose_c40.ui.theme.green_50
import com.example.news_compose_c40.widgets.NewsTopAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.Serializable

@Serializable
object SettingsRoute

@Composable
fun SettingsScreen(
    scope: CoroutineScope,
    drawerState: DrawerState,
    vm: SettingViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        NewsTopAppBar(
            titleString = stringResource(R.string.settings),
            shouldDisplayMenuIcon = true,
            shouldDisplaySearchIcon = false,
            drawerState = drawerState,
            scope = scope
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .paint(painterResource(R.drawable.bg_pattern))
                .padding(30.dp)
        ) {

            Text(
                text = stringResource(R.string.language),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                fontFamily = Poppins,
                color = gray
            )

            LanguageDropDownMenu(vm)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageDropDownMenu(vm: SettingViewModel) {

    val languageMap = mapOf("en" to "English", "ar" to "العربية")
    val activity = (LocalContext.current) as HomeActivity

    ExposedDropdownMenuBox(expanded = vm.isExpanded, onExpandedChange = {
        vm.setIsExpanded(it)
    }) {

        OutlinedTextField(
            value = vm.selectedLanguage, onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = green,
                unfocusedBorderColor = gray,
                focusedTextColor = green,
                unfocusedTextColor = green_50,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
                , trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = vm.isExpanded)
            }
        )

        ExposedDropdownMenu(expanded = vm.isExpanded,
            onDismissRequest = { vm.setIsExpanded(false)}) {

            languageMap.forEach{language->

                DropdownMenuItem(text = {
                    Text(text = language.value)
                }, onClick = {
                    vm.setSelectedLanguage(language.value)
                    vm.setIsExpanded(false)
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language.key))
                    activity.recreate()
                }, contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding)
            }

        }

    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen(
        scope = rememberCoroutineScope(),
        drawerState = rememberDrawerState(DrawerValue.Closed)
    )
}
