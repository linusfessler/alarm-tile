package linusfessler.alarmtiles;

import android.app.Application;

import javax.inject.Inject;

import lombok.Getter;

public class App extends Application {

    @Getter
    private final AppComponent appComponent;

    @Inject
    public App() {
        this.appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
