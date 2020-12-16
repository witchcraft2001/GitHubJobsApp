package ru.dm.githubpositions.fragments.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dm.githubpositions.data.models.Position

class DetailsViewModel: ViewModel() {

    private val _position = MutableLiveData<Position>()
    val position: LiveData<Position> get() = _position

    fun setModel(position: Position) {
        _position.postValue(position)
    }

}