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
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
    val database: SleepDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    //permite que eu possa cancelar as coroutines caso elas não sejam mais necessárias
    private var viewModelJob = Job()

    /**
     * É utilizado para garantir que essa thread não será bloqueada pela nossa coroutine
     * irá atualizar a UI de acordo com o resultado da coroutine
     * */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //lembrando que LiveData atualiza automaticamente por conta do Room
    private var tonight = MutableLiveData<SleepNight?>()

    private val nights = database.getAllNights()

    val nightsString = Transformations.map(nights){
        formatNights(it, application.resources)
    }

    val startButtonVisible = Transformations.map(tonight) {
        null == it
    }

    val stopButtonVisible = Transformations.map(tonight) {
        null != it
    }

    val clearButtonVisible = Transformations.map(nights) {
        it?.isNotEmpty()
    }

    private var _showSnackbarEvent = MutableLiveData<Boolean>()
    val showSnackbarEvent : LiveData<Boolean>
        get() = _showSnackbarEvent

    private val _navigationToSleepQuality = MutableLiveData<SleepNight>()
    val navigationToSleepNight : LiveData<SleepNight>
        get() = _navigationToSleepQuality

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        //Usando launch, dizemos que no escopo da UI irá criar uma coroutine sem bloquear a thread utilizada nesse scopo
        uiScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    /**
     * Para criar a coroutine, precisamos dar o contexto withContext passando o Dispatchers que representa a thread
     * na qual queremos utilizar, no caso a Dispatchers.IO
     * */
    private suspend fun getTonightFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO) {
            var night = database.getTonight()
            if (night?.endTimeMilli != night?.startTimeMilli) {
                night = null
            }
            night
        }
    }

    fun onStartTracking() {
        uiScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.insert(night)
        }
    }

    //return@launch, retornaremos o que o launch retorna em vez do valor para a variável
    //isso acontecerá caso a aplicação demore mto na task
    fun onStopTracking() {
        uiScope.launch {
            val oldNight = tonight.value ?: return@launch

            oldNight.endTimeMilli = System.currentTimeMillis()

            update(oldNight)
            _navigationToSleepQuality.value = oldNight
        }
    }

    private suspend fun update(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.update(night)
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()
            tonight.value = null
            _showSnackbarEvent.value = true
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    fun doneNavigating() {
        _navigationToSleepQuality.value = null
    }

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    //cancelaremos todas as coroutines quando o viewModel for destruido
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}

