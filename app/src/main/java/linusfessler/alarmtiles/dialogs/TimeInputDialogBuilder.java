package linusfessler.alarmtiles.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import io.reactivex.disposables.Disposable;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.views.TimeInput;

public class TimeInputDialogBuilder extends AlertDialog.Builder {

    public TimeInputDialogBuilder(@NonNull final Context context) {
        super(context);
    }

    @NonNull
    @Override
    @SuppressLint("InflateParams")
    public AlertDialog create() {
        final AlertDialog alertDialog = super.create();

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, this.getContext().getString(R.string.dialog_cancel), (dialog, which) -> {
        });

        final View view = alertDialog.getLayoutInflater().inflate(R.layout.dialog_time_input, null);
        alertDialog.setView(view);

        final TimeInput timeInput = view.findViewById(R.id.time_input);
        final Disposable timeInputDisposable = timeInput.getMillis().subscribe(millis -> {
            final boolean enabled = millis > 0;
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(enabled);
        });

        this.setOnDismissListener(dialog -> timeInputDisposable.dispose());
        this.setOnCancelListener(dialog -> timeInputDisposable.dispose());

        return alertDialog;
    }
}
