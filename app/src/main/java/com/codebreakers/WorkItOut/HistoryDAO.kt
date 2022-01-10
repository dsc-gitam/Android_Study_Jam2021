package com.codebreakers.WorkItOut

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDAO {
    @Insert
    suspend fun insert(historyEntity: HistoryEntity)

    @Query("Select * from `history-table`")
    fun fetchALlDates(): Flow<List<HistoryEntity>>
}