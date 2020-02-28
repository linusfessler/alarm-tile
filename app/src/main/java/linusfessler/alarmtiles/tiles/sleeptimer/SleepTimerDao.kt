package linusfessler.alarmtiles.tiles.sleeptimer

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Observable

@Dao
interface SleepTimerDao {
    @Query("SELECT * FROM SleepTimer")
    fun select(): Observable<SleepTimer>

    @Insert
    fun insert(sleepTimer: SleepTimer)

    @Update
    fun update(sleepTimer: SleepTimer)

    @Query("SELECT COUNT(*) FROM SleepTimer")
    fun count(): Int
}