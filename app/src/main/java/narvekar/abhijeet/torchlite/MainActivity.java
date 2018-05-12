package narvekar.abhijeet.torchlite;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button buttonEnable;
    private ImageView imgFalshLight;
    private static final  int REQUEST_CAMERA = 50;
    private boolean flashLightStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgFalshLight =  findViewById(R.id.imageView2);
        buttonEnable =  findViewById(R.id.button);

        final boolean deviceHasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        boolean isCameraPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED;

        buttonEnable.setEnabled(!isCameraPermissionGranted);
        imgFalshLight.setEnabled(isCameraPermissionGranted);

        buttonEnable.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA},
                        REQUEST_CAMERA);
            }
        });

        imgFalshLight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deviceHasCameraFlash){
                    if(flashLightStatus){
                        flashLightOFF();
                    }
                    else{
                        flashLightON();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "No FLASH Available on device",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void flashLightON(){
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try{
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
            flashLightStatus = true;
            imgFalshLight.setImageResource(R.drawable.off);

        }
        catch (CameraAccessException e){
            //Failed
        }
    }

    private void flashLightOFF(){
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try{
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
            flashLightStatus = false;
            imgFalshLight.setImageResource(R.drawable.on);

        }
        catch (CameraAccessException e){
            //Failed
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CAMERA :
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    buttonEnable.setEnabled(true);
                    buttonEnable.setText("CAMERA ENABLED");
                    imgFalshLight.setEnabled(true);
                }
                else{
                    Toast.makeText(MainActivity.this, "Permission denied for the Camera", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
