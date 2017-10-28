package muzimuzi.jejuhackerton.com.muzimuzi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.zxing.qrcode.QRCodeReader;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import muzimuzi.jejuhackerton.com.muzimuzi.Adapter.MainViewPagerAdapter;
import muzimuzi.jejuhackerton.com.muzimuzi.Fragment.ScanFragment;
import muzimuzi.jejuhackerton.com.muzimuzi.View.CustomViewPager;
import muzimuzi.jejuhackerton.com.muzimuzi.util.Util;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private CustomViewPager viewPager;
    private RelativeLayout pointBanner;
    private QRCodeReaderView qrCodeReaderView;
    private boolean loadingCheck;
    private int MY_PERMISSIONS_CAMERA = 133;
    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkCameraPermissions();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);

        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(false);
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        qrCodeReaderView.setOnQRCodeReadListener(new QRCodeReaderView.OnQRCodeReadListener(){

            @Override
            public void onQRCodeRead(String text, PointF[] points) {
                loadingCheck = false;

            }
        });

        qrCodeReaderView.setVisibility(View.INVISIBLE);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    viewPager.setCurrentItem(0);
                }
                else if(tabId == R.id.tab_qrcode){
                    viewPager.setCurrentItem(1);

                }
                else if(tabId == R.id.tab_scan){
                    viewPager.setCurrentItem(2);

                }
                else if(tabId == R.id.tab_send){
                    viewPager.setCurrentItem(3);

                }
                else if(tabId == R.id.tab_setting){
                    viewPager.setCurrentItem(4);

                }
            }
        });

        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager(), bottomBar.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    ((ImageView)findViewById(R.id.toolbar_search)).setVisibility(View.VISIBLE);
                    ((ImageView)findViewById(R.id.toolbar_title)).setBackgroundDrawable(getResources().getDrawable(R.drawable.title_naturecoin));
                    qrCodeReaderView.stopCamera();
                    qrCodeReaderView.setVisibility(View.INVISIBLE);
                }
                else if(position == 1){
                    ((ImageView)findViewById(R.id.toolbar_search)).setVisibility(View.INVISIBLE);
                    ((ImageView)findViewById(R.id.toolbar_title)).setBackgroundDrawable(getResources().getDrawable(R.drawable.title_receive));
                    qrCodeReaderView.stopCamera();
                    qrCodeReaderView.setVisibility(View.INVISIBLE);
                }
                else if(position == 2){
                    ((ImageView)findViewById(R.id.toolbar_search)).setVisibility(View.INVISIBLE);
                    ((ImageView)findViewById(R.id.toolbar_title)).setBackgroundDrawable(getResources().getDrawable(R.drawable.title_scan));
                    loadingCheck = true;
                    checkCameraPermissions();






                }
                else if(position == 3){
                    ((ImageView)findViewById(R.id.toolbar_search)).setVisibility(View.INVISIBLE);
                    ((ImageView)findViewById(R.id.toolbar_title)).setBackgroundDrawable(getResources().getDrawable(R.drawable.title_send));
                    qrCodeReaderView.stopCamera();
                    qrCodeReaderView.setVisibility(View.INVISIBLE);
                }
                else if(position == 4){
                    ((ImageView)findViewById(R.id.toolbar_search)).setVisibility(View.INVISIBLE);
                    ((ImageView)findViewById(R.id.toolbar_title)).setBackgroundDrawable(getResources().getDrawable(R.drawable.title_setting));
                    qrCodeReaderView.stopCamera();
                    qrCodeReaderView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Util.myWalletAddress = Util.getMacAddress2Hash(getApplicationContext());

        pointBanner = (RelativeLayout) findViewById(R.id.point_banner);


    }

    @Override
    public void onClick(View view) {

    }
    public void checkCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.d("sibal","here");
            openCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_CAMERA);
        }
    }
    public void openCamera(){
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
        qrCodeReaderView.setVisibility(View.VISIBLE);


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
        }
    }
}
