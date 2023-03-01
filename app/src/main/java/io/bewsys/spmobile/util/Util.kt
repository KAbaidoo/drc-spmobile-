package io.bewsys.spmobile.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

val <T> T.exhaustive: T
    get() = this

class PairMediatorLiveData<F, S>(firstLiveData: LiveData<F>, secondLiveData: LiveData<S>) :
    MediatorLiveData<Pair<F?, S?>>() {
    init {
        addSource(firstLiveData) { firstLiveDataValue: F ->
            value = firstLiveDataValue to secondLiveData.value
        }
        addSource(secondLiveData) { secondLiveDataValue: S ->
            value = firstLiveData.value to secondLiveDataValue
        }
    }
}

class TripleMediatorLiveData<F, S, T>(
    firstLiveData: LiveData<F>,
    secondLiveData: LiveData<S>,
    thirdLiveData: LiveData<T>
) : MediatorLiveData<Triple<F?, S?, T?>>() {
    init {
        addSource(firstLiveData) { firstLiveDataValue: F ->
            value = Triple(firstLiveDataValue,secondLiveData.value , thirdLiveData.value)
        }
        addSource(secondLiveData) { secondLiveDataValue: S ->
            value = Triple(firstLiveData.value ,secondLiveDataValue ,thirdLiveData.value)
        }
        addSource(thirdLiveData) {thirdLiveDataValue: T ->
            value = Triple(firstLiveData.value , secondLiveData.value , thirdLiveDataValue)
        }
    }
}

sealed class Resource<out R> {
    data class Success<out R>(val data: R): Resource<R>()
    data class Failure<out R>(val errorMsg: String): Resource<R>()
    object Loading: Resource<Nothing>()
    data class Exception<R>(val throwable: Throwable, val data: R? = null) : Resource<R>()
}


