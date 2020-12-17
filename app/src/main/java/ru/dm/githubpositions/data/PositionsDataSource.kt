package ru.dm.githubpositions.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import ru.dm.githubpositions.data.models.Item
import ru.dm.githubpositions.data.models.PositionItem
import ru.dm.githubpositions.data.models.SectionItem
import ru.dm.githubpositions.data.models.State
import ru.dm.githubpositions.data.responses.PositionResponse
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class PositionsDataSource(
    private val networkService: GitHubPositionsService,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Item>() {

    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null
    private val format = SimpleDateFormat("dd/MM/yyyy")
    private val parser = SimpleDateFormat("EEE MMM d HH:mm:ss 'UTC' yyyy")

    private var lastDate = ""
    private val today = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 0) }.time
    private val yesterday = Calendar.getInstance().apply {
        add(Calendar.DATE, -1)
        set(Calendar.HOUR_OF_DAY,0)
    }.time

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Item>
    ) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkService.getPositions(0)
                .map { toListModel(it) }
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

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {}

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkService.getPositions(params.key)
                .map { toListModel(it) }
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

    private fun toListModel(list: List<PositionResponse>): List<Item> {
        val newList = ArrayList<Item>()
        for (item in list) {
            val model = toModel(item)
            if (model.createdAt.equals(lastDate)) {
                newList.add(model)
            } else {
                lastDate = model.createdAt
                newList.add(SectionItem(model.createdAt))
                newList.add(model)
            }
        }
        return newList
    }

    private fun toModel(positionResponse: PositionResponse): PositionItem {
        positionResponse.apply {
            return PositionItem(
                id,
                toInnerDateString(createdAt),
                removeTags(company),
                removeTags(title),
                removeTags(description),
                removeTags(howToApply),
                companyLogo
            )
        }
    }

    private fun removeTags(string: String?): String {
        return string?.replace("<[^>]*>".toRegex(), "") ?: ""
    }

    private fun toInnerDateString(string: String): String {
        try {
            val date = parser.parse(string) ?: return ""
            return when {
                date >= today -> {
                    "Сегодня"
                }
                date >= yesterday && date < today -> {
                    "Вчера"
                }
                else -> {
                    format.format(date)
                }

            }
        } catch (exception: ParseException) {
            return ""
        }
    }
}