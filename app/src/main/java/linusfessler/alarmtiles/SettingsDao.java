package linusfessler.alarmtiles;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SettingsDao {

    @Query("SELECT * FROM settings")
    LiveData<Settings> getSettings();

    @Insert
    void insert(Settings settings);

    @Update
    void update(Settings settings);

}
