package ru.dm.githubpositions.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import ru.dm.githubpositions.data.models.Position
import ru.dm.githubpositions.data.models.State

class PositionsDataSource(
    private val networkService: GitHubPositionsService,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Position>() {

    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Position>
    ) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkService.getPositions(0)
                .subscribe(
                    { response ->
                        updateState(State.DONE)
                        callback.onResult(response, null, 1)
                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action { loadInitial(params, callback) })
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Position>) {}

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Position>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkService.getPositions(params.key)
                .subscribe(
                    { response ->
                        updateState(State.DONE)
                        callback.onResult(response, params.key + 1)
                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action { loadAfter(params, callback) })
                    }
                )
        )
    }

    fun retry() {
        retryCompletable?.let {
            compositeDisposable.add(
                it.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }
}