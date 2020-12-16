package ru.dm.githubpositions.fragments.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import ru.dm.githubpositions.data.GitHubPositionsService
import ru.dm.githubpositions.data.PositionsDataSource
import ru.dm.githubpositions.data.PositionsDataSourceFactory
import ru.dm.githubpositions.data.models.Position
import ru.dm.githubpositions.data.models.State

class ListViewModel : ViewModel() {
    private val networkService = GitHubPositionsService.getService()

    val positions: LiveData<PagedList<Position>>

    private val positionsDataSourceFactory: PositionsDataSourceFactory

    private val compositeDisposable = CompositeDisposable()

    init {
        positionsDataSourceFactory = PositionsDataSourceFactory(compositeDisposable, networkService)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(50)
            .build()

        positions = LivePagedListBuilder(positionsDataSourceFactory, config).build()
    }

    fun retry() {
        positionsDataSourceFactory.positionsDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return positions.value?.isEmpty() ?: true
    }

    fun getState(): LiveData<State> = Transformations.switchMap(
        positionsDataSourceFactory.positionsDataSourceLiveData,
        PositionsDataSource::state
    )

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}