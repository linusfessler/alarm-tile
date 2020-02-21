package linusfessler.alarmtiles

import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import javax.inject.Inject

class FlashlightManager @Inject constructor(private val cameraManager: CameraManager) {
    private data class Camera(val id: String)

    private var camera: Camera? = null

    init {
        findCamera()
    }

    fun turnOn() {
        setTorchMode(true)
    }

    fun turnOff() {
        setTorchMode(false)
    }

    private fun findCamera() = cameraManager.cameraIdList.forEach { id ->
        cameraManager.getCameraCharacteristics(id)
                .get(CameraCharacteristics.FLASH_INFO_AVAILABLE)?.let { flashInfoAvailable ->
                    flashInfoAvailable.let {
                        camera = Camera(id)
                        return@forEach
                    }
                }
    }

    private fun setTorchMode(enabled: Boolean) {
        camera?.let {
            cameraManager.setTorchMode(it.id, enabled)
        }
    }
}