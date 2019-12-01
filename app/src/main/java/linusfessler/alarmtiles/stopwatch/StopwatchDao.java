package linusfessler.alarmtiles.stopwatch;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface StopwatchDao {

    @Query("SELECT * FROM Stopwatch")
    LiveData<Stopwatch> select();

    @Insert
    void insert(Stopwatch stopwatch);

    @Update
    void update(Stopwatch stopwatch);

    @Query("SELECT COUNT(*) FROM Stopwatch")
    int count();
}
