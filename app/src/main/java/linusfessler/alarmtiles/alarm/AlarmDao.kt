package linusfessler.alarmtiles.alarm

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AlarmDao {
    @Query("SELECT * FROM Alarm")
    fun select(): LiveData<Alarm>

    @Insert
    fun insert(alarm: Alarm)

    @Update
    fun update(alarm: Alarm)

    @Query("SELECT COUNT(*) FROM Alarm")
    fun count(): Int
}