package linusfessler.alarmtiles.sleeptimer;

import android.content.Context;
import android.content.DialogInterface;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.shared.views.TimeInputDialog;

public class SleepTimerStartDialog extends TimeInputDialog {

    private final SleepTimerViewModel viewModel;

    private Disposable disposable;

    public SleepTimerStartDialog(@NonNull final Context context, final InputMethodManager inputMethodManager, final SleepTimerViewModel viewModel) {
        super(context, inputMethodManager);
        this.viewModel = viewModel;

        setTitle(R.string.sleep_timer);

        setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.dialog_ok), (dialog, which) -> {
            final double time = getTimeInput().getTime();
            final TimeUnit timeUnit = getTimeInput().getTimeUnit();
            viewModel.dispatch(new SleepTimerEvent.Start(time, timeUnit));
        });

        setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.dialog_cancel), (dialog, which) -> {
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        disposable = viewModel.getSleepTimer()
                .subscribe(sleepTimer -> {
                    getTimeInput().setTime(sleepTimer.getTime());
                    getTimeInput().setTimeUnit(sleepTimer.getTimeUnit());
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposable.dispose();
    }
}
