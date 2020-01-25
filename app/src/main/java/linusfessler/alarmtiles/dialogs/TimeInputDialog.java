package linusfessler.alarmtiles.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.views.TimeInput;

public class TimeInputDialog extends AlertDialog {

    private TimeInput timeInput;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public TimeInputDialog(@NonNull final Context context) {
        super(context);
        this.init();
    }

    public TimeInputDialog(@NonNull final Context context, final int themeResId) {
        super(context, themeResId);
        this.init();
    }

    public TimeInputDialog(@NonNull final Context context, final boolean cancelable, @Nullable final OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.init();
    }

    @SuppressLint("InflateParams")
    private void init() {
        final View view = this.getLayoutInflater().inflate(R.layout.dialog_time_input, null);
        this.setView(view);
        this.timeInput = view.findViewById(R.id.time_input);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.disposable.add(this.timeInput.getMillisObservable()
                .subscribe(millis -> {
                    final boolean enabled = millis > 0;
                    this.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(enabled);
                }));
    }

    @Override
    protected void onStop() {
        this.disposable.clear();
        super.onStop();
    }

    public long getMillis() {
        return this.timeInput.getMillis();
    }

    public void clear(final TimeUnit timeUnit) {
        this.timeInput.clear(timeUnit);
    }
}
