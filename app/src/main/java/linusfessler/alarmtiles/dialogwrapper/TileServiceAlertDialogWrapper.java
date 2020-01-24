package linusfessler.alarmtiles.dialogwrapper;

import android.service.quicksettings.TileService;

import androidx.appcompat.app.AlertDialog;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TileServiceAlertDialogWrapper implements AlertDialogWrapper {

    private final TileService tileService;
    private final AlertDialog dialog;

    @Override
    public AlertDialog getDialog() {
        return this.dialog;
    }

    @Override
    public void showDialog() {
        this.tileService.showDialog(this.dialog);
    }
}
