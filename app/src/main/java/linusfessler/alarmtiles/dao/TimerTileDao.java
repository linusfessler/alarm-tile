package linusfessler.alarmtiles.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import linusfessler.alarmtiles.model.TimerTile;

@Dao
public interface TimerTileDao {

    @Query("SELECT * FROM TimerTile")
    LiveData<TimerTile> select();

    @Insert
    void insert(TimerTile timerTile);

    @Update
    void update(TimerTile timerTile);

    @Query("SELECT COUNT(*) FROM TimerTile")
    int count();
}
