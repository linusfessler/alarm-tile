package linusfessler.alarmtiles.timer

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TimerDao {
    @Query("SELECT * FROM Timer")
    fun select(): LiveData<Timer>

    @Insert
    fun insert(timer: Timer)

    @Update
    fun update(timer: Timer)

    @Query("SELECT COUNT(*) FROM Timer")
    fun count(): Int
}