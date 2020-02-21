package linusfessler.alarmtiles

import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import javax.inject.Inject

class FlashlightManager
@Inject
constructor(private val cameraManager: CameraManager) {

    private data class Camera(val id: String)

    private var camera: Camera? = null

    init {
        findCamera()
    }

    fun turnOn() {
        if (camera != null) {
            cameraManager.setTorchMode(camera!!.id, true)
        }
    }

    fun turnOff() {
        if (camera != null) {
            cameraManager.setTorchMode(camera!!.id, false)
        }
    }

    private fun findCamera() = cameraManager.cameraIdList.forEach {
        if (cameraManager.getCameraCharacteristics(it)
                        .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) != null) {
            camera = Camera(it)
            return@forEach
        }
    }
}