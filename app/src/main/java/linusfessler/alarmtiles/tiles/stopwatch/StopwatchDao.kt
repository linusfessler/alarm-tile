package linusfessler.alarmtiles.tiles.stopwatch

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Observable

@Dao
interface StopwatchDao {
    @Query("SELECT * FROM Stopwatch")
    fun select(): Observable<Stopwatch>

    @Insert
    fun insert(stopwatch: Stopwatch)

    @Update
    fun update(stopwatch: Stopwatch)

    @Query("SELECT COUNT(*) FROM Stopwatch")
    fun count(): Int
}