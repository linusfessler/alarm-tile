package linusfessler.alarmtiles;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;

public class Flashlight {

    private CameraManager cameraManager;
    private String id;
    private boolean available;

    public Flashlight(Context context) {
        try {
            cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            if (cameraManager == null) {
                return;
            }

            for (String id : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(id);
                if (cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) != null) {
                    this.id = id;
                    available = true;
                    return;
                }
            }

            available = false;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void turnOn() {
        try {
            if (cameraManager != null && available) {
                cameraManager.setTorchMode(id, true);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void turnOff() {
        try {
            if (cameraManager != null && available) {
                cameraManager.setTorchMode(id, false);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
