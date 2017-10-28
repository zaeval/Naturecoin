package muzimuzi.jejuhackerton.com.muzimuzi.Fragment;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.glxn.qrgen.android.QRCode;

import org.w3c.dom.Text;

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
        Bitmap myBitmap = QRCode.from(Util.myWalletAddress).bitmap();
        imageView.setImageBitmap(myBitmap);

        walletCodeView = (TextView) rootView.findViewById(R.id.wallet_code);
        walletCodeView.setText(Util.myWalletAddress);


        return rootView;
    }

}
