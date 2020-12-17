package ru.dm.githubpositions.fragments.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dm.githubpositions.data.models.PositionItem

class DetailsViewModel: ViewModel() {

    private val _position = MutableLiveData<PositionItem>()
    val position: LiveData<PositionItem> get() = _position

    fun setModel(position: PositionItem) {
        _position.postValue(position)
    }

}