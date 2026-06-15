package com.pdmcourse2026.basictemplate

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.pdmcourse2026.basictemplate.screens.home.HomeScreen
import com.pdmcourse2026.basictemplate.screens.home.OptionsScreen
import com.pdmcourse2026.basictemplate.screens.home.QuestionsScreen
import com.pdmcourse2026.basictemplate.screens.home.ResultadosScreen
import com.pdmcourse2026.basictemplate.viewmodel.HomeViewModel
import com.pdmcourse2026.basictemplate.viewmodel.ResultsViewModel

@Composable
fun RankeUCA_App() {
    val backStack = rememberNavBackStack(Routes.Questions)

    val context = androidx.compose.ui.platform.LocalContext.current
    val appProvider = (context.applicationContext as RankeUcaApplication).appProvider
    val placeRepository = appProvider.providePlaceRepository()

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {

            entry<Routes.Home> {
                val viewModel: HomeViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer { HomeViewModel(placeRepository) }
                    }
                )
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToResults = { backStack.add(Routes.Resultados) },
                    onNavigateToOptions = { backStack.add(Routes.Questions) }
                )
            }

            entry<Routes.Resultados> {
                val viewModel: ResultsViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer { ResultsViewModel(placeRepository) }
                    }
                )
                ResultadosScreen(
                    viewModel = viewModel,
                    onNavigateToHome = {
                        while (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    }
                )
            }

            entry<Routes.Questions> {
                QuestionsScreen(
                    onQuestionClick = { questionId ->
                        backStack.add(Routes.Options(questionId))
                    }
                )
            }

            entry<Routes.Options> {
                OptionsScreen(questionId = it.questionId)
            }
        }
    )
}