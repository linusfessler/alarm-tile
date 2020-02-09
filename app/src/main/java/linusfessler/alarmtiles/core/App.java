package linusfessler.alarmtiles.core;

import android.app.Application;

import lombok.Getter;

public class App extends Application {

    @Getter
    private final AppComponent appComponent;

    public App() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
