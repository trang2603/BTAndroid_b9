package com.demo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.demo.databinding.ActivityMainBinding
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@androidx.camera.core.ExperimentalGetImage
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview =
                Preview.Builder().build().also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }
            val barcodeScanner = BarcodeScanning.getClient()
            val imageAnalyzer =
                ImageAnalysis.Builder().build().also {
                    it.setAnalyzer(cameraExecutor, { imageProxy ->
                        processImageProxy(barcodeScanner, imageProxy)
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy,
    ) {
        val mediaImg = imageProxy.image
        if (mediaImg != null) {
            val img = InputImage.fromMediaImage(mediaImg, imageProxy.imageInfo.rotationDegrees)
            barcodeScanner
                .process(img)
                .addOnSuccessListener { barcodes ->
                    // kiem tra xem cos ma vach nao duoc quet khong
                    if (barcodes.isNotEmpty()) {
                        val barcode = barcodes[0]
                        // lay du lieu tu ma vach
                        val barcodeValue = barcode.rawValue

                        // neu ma vach co gia tri, huong den trang gg tim kiem
                        barcodeValue?.let {
                            val searchUrl = "https://www.google.com/search?q=$it"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl))
                            startActivity(intent)
                        }
                    }
                    // dong imgProxy de giai phong tai nguyen
                    imageProxy.close()
                }.addOnFailureListener { e ->
                    // xu ly neu quet khong thanh cong
                    e.printStackTrace()
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
