package com.exam.exam0.page

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exam.exam0.dao.EntityDao
import com.exam.exam0.data.local.LocalModel
import com.exam.exam0.data.remote.RemoteDto
import com.exam.exam0.database.AppDatabase
import com.exam.exam0.remote.RemoteProvider
import com.exam.exam0.remote.successOr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppViewModel(
    private val entityDao: EntityDao,
    private val remoteProvider: RemoteProvider,
    private val connectivityLiveData: ConnectivityLiveData
) :
    ViewModel() {

    private val loadingLivedata = MutableLiveData<Boolean>().apply { value = true }
    fun getLoadingLiveData() = loadingLivedata as LiveData<Boolean>

    // General
    fun getConnectivityStatus() = connectivityLiveData

    // Basic crud remote

    /*//Owner Section
    private val itemsSection2LiveData = MutableLiveData<List<RemoteDto>>()

    fun getSecondSectionLiveData() = itemsSection2LiveData as LiveData<List<RemoteDto>>
    var student = ""

    fun insertStudentName(studentName: StudentName) {
        GlobalScope.launch {
            entityDao.insertStudentName(studentName)
        }
    }
    //Replace with get med by date
    fun getBooksByStudentNameRemote(errorHandler: (String) -> Unit, studentName: String) {
        GlobalScope.launch {
            val result = remoteProvider.getItemsByStudentNameRemote(studentName)
            Log.e("CALL", result.toString())
            itemsSection2LiveData.postValue(result.successOr(listOf()))
            if (result.succeeded) {
                entityDao.deleteItemsLocal()
                result.successOr(listOf())
                    .forEach { entityDao.insertItemLocal(it.convertToLocalEntity()) }
            } else {
                errorHandler(result.toString())
            }
        }
    }

    fun getStudentNameFromDB() {
        GlobalScope.launch {
            student = entityDao.getStudentName().name
        }
    }*/

    //Get meds local
    fun getMedsLiveData() = entityDao.getItemsLocal()

    suspend fun getNewId() : Int{
        return withContext(Dispatchers.IO) {

            entityDao.getNextId().toInt()
        }
    }

    //Get meds remote
    fun getMedsRemote(context: Context) {
        GlobalScope.launch {
            val result = remoteProvider.getMedsSortedRemote()
            Log.e("CALL", result.toString())
            entityDao.deleteItemsLocal()
            val data = result.successOr(ArrayList<LocalModel>())
            data?.forEach {
                AppDatabase.getInstance(context).scheduleDAO().insertItemLocal((it as RemoteDto).convertToLocalEntity())
            }
            loadingLivedata.postValue(true)
        }
    }

    // Add Med
    fun addMedRemote(orderDto: RemoteDto, errorHandler: (String) -> Unit) {
        viewModelScope.launch {

            val result = remoteProvider.addMedRemote(orderDto)
            Log.e("CALL", result.toString())
            result.successOr(errorHandler(result.toString()))
        }
    }

    //Delete Med
    fun deleteMedRemote(id: Int, errorHandler: (String) -> Unit) {
        viewModelScope.launch {
            val result = remoteProvider.deleteMedRemote(id)
            Log.e("CALL", result.toString())
            result.successOr(errorHandler(result.toString()))
        }
    }

    //Update Med
    fun updateMedRemote(id: Int, orderDto: RemoteDto, errorHandler: (String) -> Unit) {
        viewModelScope.launch {
            val result = remoteProvider.updateMedRemote(id, orderDto)
            Log.e("CALL", result.toString())
            result.successOr(errorHandler(result.toString()))
        }
    }

    // Basic Crud Local
    fun getItemsLocal(): LiveData<List<LocalModel>> {
        Log.e("LOCAL", "Get items!")
        return entityDao.getItemsLocal()
    }

    fun getItemById(itemId: Int): LiveData<LocalModel> {
        Log.e("LOCAL", "Get item by id!")
        return entityDao.getItemByIdLocal(itemId)
    }

    fun insertItemLocal(localModel: LocalModel) {
        GlobalScope.launch {
            Log.e("LOCAL", "Insert item!")
            entityDao.insertItemLocal(localModel)
        }
    }

    fun deleteByIdItemLocal(itemId: Int) {
        GlobalScope.launch {
            Log.e("LOCAL", "Delete item!")
            entityDao.deleteItemByIdLocal(itemId)
        }
    }

    fun updateItemLocal(localModel: LocalModel) {
        GlobalScope.launch {
            Log.e("LOCAL", "Update item!")
            entityDao.updateItemLocal(localModel)
        }
    }
}