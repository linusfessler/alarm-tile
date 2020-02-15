package linusfessler.alarmtiles.shared.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.R;

public class TimeInputDialog extends AlertDialog {

    private final TimeInput timeInput;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @SuppressLint("InflateParams")
    public TimeInputDialog(@NonNull final Context context) {
        super(context);

        final View view = getLayoutInflater().inflate(R.layout.dialog_time_input, null);
        setView(view);
        timeInput = view.findViewById(R.id.time_input);
    }

    public TimeInputDialog(@NonNull final Context context, final InputMethodManager inputMethodManager) {
        this(context);
        setOnShowListener(dialog -> timeInput.showKeyboard(inputMethodManager));
    }

    public TimeInput getTimeInput() {
        return timeInput;
    }

    @Override
    protected void onStart() {
        super.onStart();
        disposable.add(timeInput.getTimeObservable()
                .subscribe(time -> {
                    final boolean enabled = time > 0;
                    getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(enabled);
                }));
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposable.clear();
    }
}
