package ru.dm.githubpositions.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.dm.githubpositions.data.GitHubPositionsService
import ru.dm.githubpositions.fragments.list.ListViewModel
import ru.dm.githubpositions.navigation.NavigationService
import ru.dm.githubpositions.navigation.NavigationServiceImpl
import ru.dm.githubpositions.utils.ActivityUtils

val applicationModule = module(override = true) {
    viewModel { ListViewModel(get(), get()) }
    single { ActivityUtils() }
    single<GitHubPositionsService> { GitHubPositionsService.getService() }
    single<NavigationService> { NavigationServiceImpl() }
}