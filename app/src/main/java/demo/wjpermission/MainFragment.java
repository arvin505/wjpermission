package demo.wjpermission;

import android.Manifest;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wymaster.common.permission.annotations.PermissionsDenied;
import com.wymaster.common.permission.annotations.PermissionsGranted;
import com.wymaster.common.permission.api.WJPermission;

/**
 * Created by xiaoyi on 2017-8-31.
 */

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.phone)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        WJPermission.requestPermission(MainFragment.this,
                                Manifest.permission.READ_PHONE_STATE,3);
                    }
                });
    }

    @PermissionsGranted(3)
    public void readPhoneStateGranted(){
        Toast.makeText(getActivity(),"readphonegranted",0).show();
    }

    @PermissionsDenied(3)
    public void readPhoneStateDenied(){
        Toast.makeText(getActivity(),"readphonedenied  mo",0).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        WJPermission.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }
}
