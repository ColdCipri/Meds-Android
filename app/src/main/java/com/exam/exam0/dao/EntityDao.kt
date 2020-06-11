package com.exam.exam0.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.exam.exam0.data.local.LocalModel

@Dao
interface EntityDao {

    //Basic Crud

    @Query("SELECT * from meds")
    fun getItemsLocal(): LiveData<List<LocalModel>>

    @Query("SELECT MAX(id) + 1 as ID from meds")
    fun getNextId():Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItemLocal(localModel: LocalModel)

    @Query("DELETE from meds")
    fun deleteItemsLocal()

    @Query("DELETE FROM meds WHERE id == :itemId")
    fun deleteItemByIdLocal(itemId: Int)

    @Transaction
    fun updateItemLocal(localModel: LocalModel) {
        deleteItemByIdLocal(localModel.id)
        insertItemLocal(localModel)
    }

    @Query("SELECT * from meds where id == :classId")
    fun getItemByIdLocal(classId: Int): LiveData<LocalModel>
}