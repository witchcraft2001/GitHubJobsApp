package ru.dm.githubpositions.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.dm.githubpositions.fragments.list.ListViewModel

val applicationModule = module(override = true) {
    viewModel { ListViewModel() }
}