package demo.wjpermission;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wymaster.common.permission.annotations.PermissionsDenied;
import com.wymaster.common.permission.annotations.PermissionsGranted;
import com.wymaster.common.permission.annotations.PermissionsRationale;
import com.wymaster.common.permission.annotations.PermissionsRequestSync;
import com.wymaster.common.permission.api.WJPermission;

@PermissionsRequestSync(value = {1}, permission = Manifest.permission.CAMERA)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction().add(R.id.content,new MainFragment())
                .commit();
    }


    public void test(View view) {
        WJPermission.requestPermission(this, Manifest.permission.CAMERA, 1);
    }

    @PermissionsGranted(1)
    public void cameraGranted() {
        Toast.makeText(this, "camera permission granted", 0).show();
    }

    @PermissionsDenied(1)
    public void cameraDenied() {
        Toast.makeText(this, "camera permission cameraDenied", 0).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        WJPermission.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }
}
