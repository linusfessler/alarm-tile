package linusfessler.alarmtiles.timer;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TimerDao {

    @Query("SELECT * FROM Timer")
    LiveData<Timer> select();

    @Insert
    void insert(Timer timer);

    @Update
    void update(Timer timer);

    @Query("SELECT COUNT(*) FROM Timer")
    int count();
}
