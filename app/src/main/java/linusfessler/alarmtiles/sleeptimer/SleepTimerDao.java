package linusfessler.alarmtiles.sleeptimer;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import io.reactivex.Observable;
import linusfessler.alarmtiles.sleeptimer.model.SleepTimer;

@Dao
public interface SleepTimerDao {

    @Query("SELECT * FROM SleepTimer")
    Observable<SleepTimer> select();

    @Insert
    void insert(SleepTimer sleepTimer);

    @Update
    void update(SleepTimer sleepTimer);

    @Query("SELECT COUNT(*) FROM SleepTimer")
    int count();
}
