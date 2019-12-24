package com.ssas.tpcms.android.ui.home.qr

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.journeyapps.barcodescanner.camera.CameraSettings
import com.ssas.dinarpay.android.network.Status
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseBottomSheetFragment
import com.ssas.tpcms.android.databinding.FragmentQrScannerBinding
import com.ssas.tpcms.android.repo.home.HomeVM
import com.ssas.tpcms.android.utils.Utils


/**
 * Created by Jaspal on 12/12/2019.
 */

private const val ARG_QR_CARD_TYPE = "ARG_QR_CARD_TYPE"

class QrScannerFragment : BaseBottomSheetFragment<FragmentQrScannerBinding, HomeVM>() {

    private lateinit var barcodeScannerView: DecoratedBarcodeView
    private var beepManager: BeepManager? = null
    private var lastText: String? = null
    private var isTorchOn: Boolean = false
    private var qrCardType: String? = ""
    lateinit var listener: QrCallBackListener

    var formats: Collection<BarcodeFormat> = listOf(BarcodeFormat.QR_CODE)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        /* arguments?.let {
             qrCardType = it.getString(ARG_QR_CARD_TYPE)
         }*/
    }


    override val fragmentBinding: FragmentBinding
        get() = FragmentBinding(R.layout.fragment_qr_scanner, HomeVM::class.java)

    override fun onCreateViewFragment(savedInstanceState: Bundle?) {

    }

    override fun subscribeToEvents(vm: HomeVM) {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        barcodeScannerView = view.findViewById(R.id.barcode_scanner)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setListeners()
        enbleFlashButton()
        setUpBarcodeScanner()
        setBipManager()
    }

    private fun setListeners() {
        binding.cancelBt.setOnClickListener {
            dismiss()
        }
    }

    private fun enbleFlashButton() {
        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
            binding.switchFlashlight.setVisibility(View.GONE)
        }
        binding.switchFlashlight.setOnClickListener {
            switchFlashlight()
        }
    }

    private fun setUpBarcodeScanner() {
        barcodeScannerView.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcodeScannerView.barcodeView.cameraSettings = cameraSettings()
        barcodeScannerView.resume()
        barcodeScannerView.decodeSingle(callback)
    }

    private fun cameraSettings(): CameraSettings {
        val s = CameraSettings()
       // s.isAutoFocusEnabled = true
        s.requestedCameraId = 0
       //s.isContinuousFocusEnabled = true
        return s
    }

    private fun setBipManager() {
        beepManager = BeepManager((context as Activity?)!!)
    }

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null || result.text == lastText) { // Prevent duplicate scans
                return
            }
            beepManager!!.playBeepSoundAndVibrate()
            lastText = result.text
            listener.onQRResult(qrCardType.toString(), result.text)
            dismiss()
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private fun hasFlash(): Boolean {
        return context!!.getPackageManager()
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    private fun switchFlashlight() {
        if (isTorchOn) {
            barcodeScannerView.setTorchOff()
            isTorchOn = false
        } else {
            barcodeScannerView.setTorchOn()
            isTorchOn = true
        }
    }

    fun setQRResultListener(owner: QrCallBackListener) {
        if (owner == null) {
            return
        }
        listener = owner
    }

    interface QrCallBackListener {
        fun onQRResult(qrType: String, resultString: String)
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    companion object {
        const val ARG_OFFICE_CARD = "ARG_OFFICE_CARD"
        const val ARG_VEHICLE_CARD = "ARG_VEHICLE_CARD"
        const val ARG_MISSION_CARD = "ARG_MISSION_CARD"

        @JvmStatic
        fun newInstance(cardType: String?) =
            QrScannerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_QR_CARD_TYPE, cardType)
                }
            }
    }

}