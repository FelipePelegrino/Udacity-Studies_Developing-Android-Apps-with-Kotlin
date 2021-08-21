/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _response = MutableLiveData<String>()

    // The external immutable LiveData for the request status String
    val response: LiveData<String>
        get() = _response

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties()
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     * Agora mudamos a callback para coroutine em Kotlin.
     * A coroutine é chamada através da classe Deferred, ela contém um método chamado await()
     * O await() irá fazer a requisição e como é um non-blocking, aguardará a requisição até obter um resultado
     * Quando esse resultado for objetido, caso seja um erro, lançara uma exception, caso for bem sucedido, teremos
     * nossos dados obtidos e podemos vincular a variável do mesmo tipo que criamos o Deferred
     * Infelizmente biblioteca foi marcada como deprecated, recomendação é migrar para retrofit 2.6.0 e usar
     * seus built-in suspend support
     */
    private fun getMarsRealEstateProperties() {
        coroutineScope.launch {
            var getPropertiesDeferred = MarsApi.retrofitService.getProperties()
            try {
                var listResult = getPropertiesDeferred.await()
                _response.value = "Succes: ${listResult?.size} Mars properties retrieved \n ${listResult?.toString()}"
            } catch (t:Throwable) {
                _response.value = "Failure: ${t.message}"
            }
        }
        _response.value = "Set the Mars API Response here!"
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}