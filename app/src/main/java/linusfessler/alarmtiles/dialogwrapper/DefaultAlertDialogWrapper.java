package linusfessler.alarmtiles.dialogwrapper;

import androidx.appcompat.app.AlertDialog;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultAlertDialogWrapper implements AlertDialogWrapper {

    private final AlertDialog dialog;

    @Override
    public AlertDialog getDialog() {
        return this.dialog;
    }

    @Override
    public void showDialog() {
        this.dialog.show();
    }
}
