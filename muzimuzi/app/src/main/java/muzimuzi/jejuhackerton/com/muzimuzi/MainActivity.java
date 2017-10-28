package muzimuzi.jejuhackerton.com.muzimuzi;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.zxing.qrcode.QRCodeReader;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import muzimuzi.jejuhackerton.com.muzimuzi.Adapter.MainViewPagerAdapter;
import muzimuzi.jejuhackerton.com.muzimuzi.Fragment.ScanFragment;
import muzimuzi.jejuhackerton.com.muzimuzi.View.CustomViewPager;
import muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects.Bin;
import muzimuzi.jejuhackerton.com.muzimuzi.retrofit_services.BlockChainService;
import muzimuzi.jejuhackerton.com.muzimuzi.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.dial;
import static android.R.attr.targetSdkVersion;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private CustomViewPager viewPager;
    private RelativeLayout pointBanner;
    private QRCodeReaderView qrCodeReaderView = null;
    private boolean loadingCheck;
    private int MY_PERMISSIONS_CAMERA = 133;
    private boolean permissionCheck = true;
    private MaterialDialog resultDialog;
    private MaterialDialog resultDialog2;
    private MaterialDialog loadingDialog;
    private MainViewPagerAdapter adapter;
    boolean first_check=true;
    private BottomBar bottomBar;
    @Override
    protected void onPause() {
        super.onPause();
        if (qrCodeReaderView != null)
            qrCodeReaderView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (qrCodeReaderView != null)
            qrCodeReaderView.startCamera();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(false);
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    viewPager.setCurrentItem(0);
                } else if (tabId == R.id.tab_qrcode) {
                    viewPager.setCurrentItem(1);

                } else if (tabId == R.id.tab_scan) {
                    viewPager.setCurrentItem(2);

                } else if (tabId == R.id.tab_send) {
                    viewPager.setCurrentItem(3);

                } else if (tabId == R.id.tab_setting) {
                    viewPager.setCurrentItem(4);

                }
            }
        });

        adapter = new MainViewPagerAdapter(getSupportFragmentManager(), bottomBar.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    ((ImageView) findViewById(R.id.toolbar_search)).setVisibility(View.VISIBLE);
                    ((ImageView) findViewById(R.id.toolbar_title)).setBackgroundDrawable(getResources().getDrawable(R.drawable.title_naturecoin));
                    if (qrCodeReaderView != null) {
                        qrCodeReaderView.stopCamera();
                        qrCodeReaderView.setVisibility(View.INVISIBLE);
                    }
                } else if (position == 1) {
                    ((ImageView) findViewById(R.id.toolbar_search)).setVisibility(View.INVISIBLE);
                    ((ImageView) findViewById(R.id.toolbar_title)).setBackgroundDrawable(getResources().getDrawable(R.drawable.title_receive));
                    if (qrCodeReaderView != null) {

                        qrCodeReaderView.stopCamera();
                        qrCodeReaderView.setVisibility(View.INVISIBLE);
                    }
                } else if (position == 2) {
                    ((ImageView) findViewById(R.id.toolbar_search)).setVisibility(View.INVISIBLE);
                    ((ImageView) findViewById(R.id.toolbar_title)).setBackgroundDrawable(getResources().getDrawable(R.drawable.title_scan));
                    checkingPermission();

                } else if (position == 3) {
                    ((ImageView) findViewById(R.id.toolbar_search)).setVisibility(View.INVISIBLE);
                    ((ImageView) findViewById(R.id.toolbar_title)).setBackgroundDrawable(getResources().getDrawable(R.drawable.title_send));
                    if (qrCodeReaderView != null) {

                        qrCodeReaderView.stopCamera();
                        qrCodeReaderView.setVisibility(View.INVISIBLE);
                    }

                } else if (position == 4) {
                    ((ImageView) findViewById(R.id.toolbar_search)).setVisibility(View.INVISIBLE);
                    ((ImageView) findViewById(R.id.toolbar_title)).setBackgroundDrawable(getResources().getDrawable(R.drawable.title_setting));
                    if (qrCodeReaderView != null) {

                        qrCodeReaderView.stopCamera();
                        qrCodeReaderView.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Util.myWalletAddress = Util.getMacAddress2Hash(getApplicationContext());

        pointBanner = (RelativeLayout) findViewById(R.id.point_banner);


    }

    @TargetApi(Build.VERSION_CODES.M)
    public void checkingPermission() {
        while (permissionCheck) {
            if (selfPermissionGranted(Manifest.permission.CAMERA, this) == false) {
                Log.d("sibal", "not permissioned");
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_CAMERA);
            } else {
                Log.d("sibal", "yes permissioned "+first_check);

                permissionCheck = false;
                if(first_check) {
                    openCamera();
                    first_check=false;
                    Log.d("sibal", "here are start first");

                }
                else{
                    Log.d("sibal", "here are start secondary");

                    qrCodeReaderView.startCamera();
                    qrCodeReaderView.setVisibility(View.VISIBLE);
                }
            }
        }
        permissionCheck=true;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("sibal", "here r");

        if (requestCode == MY_PERMISSIONS_CAMERA) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("sibal", "allowed");
                loadingCheck = true;
                permissionCheck = false;
                openCamera();
            }
        }
    }

    public boolean selfPermissionGranted(String permission, Context context) {
        // For Android < Android M, self permissions are always granted.
        boolean result = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = context.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(context, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }

        return result;
    }

    public void openCamera() {
        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
        qrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
        qrCodeReaderView.setOnQRCodeReadListener(new QRCodeReaderView.OnQRCodeReadListener() {

            @Override
            public void onQRCodeRead(String text, PointF[] points) {
                qrCodeReaderView.stopCamera();
                Util.recipient=text;
                resultDialog = new MaterialDialog.Builder(MainActivity.this)
                        .content("보낼사람 : " + text + " 맞습니까?")
                        .positiveText("예")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if(dialog == resultDialog) {
                                    Message message = handler.obtainMessage(); // 메시지 ID 설정
                                    message.what = 1; // 메시지 내용 설정 (int)
                                    handler.sendMessage(message);
                                }

                            }

                        })
                        .negativeText("아니오")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                // TODO
                                if(dialog == resultDialog) {
                                    Message message = handler.obtainMessage(); // 메시지 ID 설정
                                    message.what = 0; // 메시지 내용 설정 (int)
                                    handler.sendMessage(message);
                                    Util.recipient="";
                                }

                            }
                        })
                        .show();

            }
        });

        qrCodeReaderView.setVisibility(View.VISIBLE);

    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1) {
                resultDialog.dismiss();
                viewPager.setCurrentItem(3);
                bottomBar.selectTabAtPosition(3);

            }
            else if(msg.what == 0) {
                resultDialog.dismiss();
            }
        }
    };

}

