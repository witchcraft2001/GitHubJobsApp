package ru.dm.githubpositions.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import ru.dm.githubpositions.data.models.Item
import ru.dm.githubpositions.data.models.PositionItem

class PositionsDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val networkService: GitHubPositionsService
) : DataSource.Factory<Int, Item>() {
    val positionsDataSourceLiveData = MutableLiveData<PositionsDataSource>()

    override fun create(): DataSource<Int, Item> {
        val positionsDataSource = PositionsDataSource(networkService, compositeDisposable)
        positionsDataSourceLiveData.postValue(positionsDataSource)
        return positionsDataSource
    }
}