package com.example.diary_vaccination

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import com.example.diary_vaccination.database.DiaryVaccinationDao
import com.example.diary_vaccination.database.Entry
import kotlinx.coroutines.*

class AddNewEntryViewModel(
    val dao: DiaryVaccinationDao,
    application: Application
) : AndroidViewModel(application) {
    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun initNewEntry(patientId: Long, vaccineId: Long, component: Long,
    vaccineDate: String, vaccineTime: String){
        uiScope.launch {
            val newEntry = Entry(patientId = patientId, vaccineId = vaccineId,
            component = component, vaccineDate = vaccineDate, vaccineTime = vaccineTime)
            insertEntry(newEntry)
        }
    }

    private suspend fun insertEntry(newEntry: Entry) {
        withContext(Dispatchers.IO){
            dao.insertEntry(newEntry)
        }
    }

    fun clearAllEntries() {
        uiScope.launch {
            clearAllEntriesQueries()
        }
    }
    private suspend fun clearAllEntriesQueries() {
        withContext(Dispatchers.IO) {
            dao.clearEntry()
        }
    }

    fun clearToEntry(id: String) {
        uiScope.launch {
            clearToEntryQueries(id)
        }
    }
    private suspend fun clearToEntryQueries(id: String) {
        withContext(Dispatchers.IO) {
            dao.clearToEntry(id)
        }
    }

    val entries = dao.getAllEntries()
    val listTasksDB = Transformations.map(entries) { tasks ->
        parselistTasksDB(tasks, application.resources)
    }
}