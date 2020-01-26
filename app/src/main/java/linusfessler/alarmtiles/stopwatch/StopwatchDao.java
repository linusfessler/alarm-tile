package linusfessler.alarmtiles.stopwatch;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import io.reactivex.Observable;

@Dao
public interface StopwatchDao {

    @Query("SELECT * FROM Stopwatch")
    Observable<Stopwatch> select();

    @Insert
    void insert(Stopwatch stopwatch);

    @Update
    void update(Stopwatch stopwatch);

    @Query("SELECT COUNT(*) FROM Stopwatch")
    int count();
}
