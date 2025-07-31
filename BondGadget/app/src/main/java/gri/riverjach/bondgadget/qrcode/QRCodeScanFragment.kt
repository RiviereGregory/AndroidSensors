package gri.riverjach.bondgadget.qrcode

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.zxing.Result
import gri.riverjach.bondgadget.BuildConfig
import gri.riverjach.bondgadget.databinding.FragmentQrcodeScanBinding
import me.dm7.barcodescanner.zxing.ZXingScannerView
import timber.log.Timber

class QRCodeScanFragment : Fragment(), ZXingScannerView.ResultHandler {
    // pour pouvoir utiliser sans faire les findById avec inflate
    private var _binding: FragmentQrcodeScanBinding? = null
    private val binding get() = _binding!!

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Timber.i("permission granted")
                startCamera()
            } else {
                Timber.i("permission denied")
                findNavController().popBackStack()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQrcodeScanBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.qrCodeView.setResultHandler(this)
        if (BuildConfig.QRCODE_SIMULATOR_ENALED) {
            notifyScan("http://qrCode")
        }
    }

    override fun onResume() {
        super.onResume()
        if (!hasCameraPermission()) {
            requestPermission.launch(Manifest.permission.CAMERA)
        } else {
            startCamera()
        }
    }

    override fun onPause() {
        super.onPause()
        stopCamera()
    }

    private fun stopCamera() {
        binding.qrCodeView.stopCamera()
    }

    private fun startCamera() {
        binding.qrCodeView.startCamera()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hasCameraPermission() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    override fun handleResult(rawResult: Result) {
        Timber.i("QRCode ${rawResult.text}")
        notifyScan(rawResult.text)
    }

    fun notifyScan(text: String) {
        // TODO: Add Gadget
        Timber.i("QRCode ${text}")
        findNavController().popBackStack()
    }
}