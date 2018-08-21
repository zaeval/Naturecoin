package muzimuzi.jejuhackerton.com.muzimuzi.Fragment;

import android.Manifest;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import net.glxn.qrgen.android.QRCode;

import org.w3c.dom.Text;

import java.util.ArrayList;

import muzimuzi.jejuhackerton.com.muzimuzi.MainActivity;
import muzimuzi.jejuhackerton.com.muzimuzi.R;
import muzimuzi.jejuhackerton.com.muzimuzi.util.Util;


public class QrcodeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView imageView;
    private TextView walletCodeView;
    public QrcodeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static QrcodeFragment newInstance(String param1, String param2) {
        QrcodeFragment fragment = new QrcodeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_qrcode, container, false);

        imageView = rootView.findViewById(R.id.image_view);
        walletCodeView = (TextView) rootView.findViewById(R.id.wallet_code);

        PermissionListener permissionlistener2 = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Util.myWalletAddress = Util.getMacAddress2Hash();
                Bitmap myBitmap = QRCode.from(Util.myWalletAddress).bitmap();
                imageView.setImageBitmap(myBitmap);


                walletCodeView.setText(Util.myWalletAddress);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getActivity(), "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }

        };
        TedPermission.with(getContext())
                .setPermissionListener(permissionlistener2)
                .setRationaleMessage("qr코드를 생성하는데 필요합니다.")
                .setDeniedMessage("이후, [설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.ACCESS_WIFI_STATE)
                .check();



        return rootView;
    }

}
