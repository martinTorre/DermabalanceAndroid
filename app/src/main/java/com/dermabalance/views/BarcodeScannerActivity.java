package com.dermabalance.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.dermabalance.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.lang.reflect.Field;

public class BarcodeScannerActivity extends Activity implements SurfaceHolder.Callback, Detector.Processor<Barcode> {

    /**Request code.*/
    public static final int REQUEST_CODE = 100;

    /**Custom result, handle when user accept permission.*/
    public static final int RESULT_ACCEPTED_PERMISSION = 103;

    /**EXTRA_KEY.*/
    public static final String EXTRA_KEY = "barcode";

    /**Camera view.*/
    private SurfaceView surfaceView;

    /**Camera source.*/
    private CameraSource cameraSource;

    /**Code detector.*/
    private BarcodeDetector barcodeDetector;

    /**Flash button.*/
    private ImageView imageViewFlash;

    /**Flag to handle flash light.*/
    private boolean isFlashOn;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);
        surfaceView = findViewById(R.id.surfaceView);
        imageViewFlash = findViewById(R.id.imageView_flash);

        checkPermission();
    }

    /**
     * Check and request camera permission.
     * @return true if already accepted
     */
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
            return false;
        }
        configureScannerBarcode();
        return true;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           final String permissions[], final int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setResult(RESULT_ACCEPTED_PERMISSION, new Intent());
            finish();
        }
    }

    /**
     * Init scanner.
     */
    public void configureScannerBarcode() {
        surfaceView.requestFocus();
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setAutoFocusEnabled(true).build();
        surfaceView.getHolder().addCallback(this);
        barcodeDetector.setProcessor(this);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void surfaceCreated(final SurfaceHolder holder) {
        try {
            cameraSource.start(surfaceView.getHolder());
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(final SurfaceHolder holder, final int format, final int width, final int height) {

    }

    @Override
    public void surfaceDestroyed(final SurfaceHolder holder) {
        cameraSource.stop();
    }

    @Override
    public void release() {

    }

    @Override
    public void receiveDetections(final Detector.Detections<Barcode> detections) {
        final SparseArray<Barcode> barcodes = detections.getDetectedItems();
        if (barcodes.size() != 0) {
            final Barcode barcode = barcodes.valueAt(0);
            final Intent intentResult = new Intent();
            intentResult.putExtra(EXTRA_KEY, barcode.rawValue);
            setResult(RESULT_OK, intentResult);
            finish();

        }
    }

    /**
     * Turn on/off flash light.
     * @param view clicked
     */
    public void handleFlash(final View view) {
        try {
            final Camera camera = getCamera(cameraSource);
            final Camera.Parameters parameters = camera.getParameters();
            if (isFlashOn) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                imageViewFlash.setImageResource(R.drawable.ic_flash);
                isFlashOn = false;
            } else {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
                isFlashOn = true;
                imageViewFlash.setImageResource(R.drawable.ic_flash_off);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get Camera.
     * @param cameraSource to get it
     * @return camera
     */
    private static Camera getCamera(final @NonNull CameraSource cameraSource) {
        final Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (final Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    final Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        return camera;
                    }
                    return null;
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return null;
    }
}
