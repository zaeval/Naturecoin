package muzimuzi.jejuhackerton.com.superbin;

import android.content.Context;
import android.graphics.PointF;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import muzimuzi.jejuhackerton.com.superbin.retrofit_objects.Bin;
import muzimuzi.jejuhackerton.com.superbin.retrofit_services.BlockChainService;
import muzimuzi.jejuhackerton.com.superbin.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static muzimuzi.jejuhackerton.com.superbin.util.RetrofitSettings.DEFAULT_PARAM;

public class MainActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {
    private QRCodeReaderView qrCodeReaderView;
    private MaterialDialog loadingDialog;
    private MaterialDialog resultDialog;
    boolean loadingCheck;
    public static final int SEND_INFORMATION = 0;
    public static final int SEND_STOP = 1;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);

        loadingCheck = true;

        qrCodeReaderView.setOnQRCodeReadListener(this);

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

    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        if (loadingCheck) {
            loadingDialog = new MaterialDialog.Builder(this)
                    .title("처리중입니다")
                    .content("조금만 기다려 주십시오.")
                    .progress(true, 0)
                    .show();

            resultDialog = new MaterialDialog.Builder(this)
                    .content("처리가 완료되었습니다.")
                    .show();
            resultDialog.hide();
            BlockChainService blockChainService = BlockChainService.retrofit.create(BlockChainService.class);

            WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            String address = info.getMacAddress();
            Log.d("sibal", Util.sha256(address));
//            Call<Bin> call = blockChainService.newTransactions((new BlockChainService.Transaction(text,Util.sha256(address),1.0f).getJSON()));
            Call<Bin> call = blockChainService.newTransactions(text, Util.sha256(address), 1.0f);
            call.enqueue(new Callback<Bin>() {
                @Override
                public void onResponse(Call<Bin> call,
                                       Response<Bin> response) {
                    if (response.code() == 201) {
                        loadingDialog.hide();
                        Log.d("success", response.message());
                        resultDialog.show();
                        thread = new Thread();
                        thread.start();

                        loadingCheck = true;
                    } else {
                        Log.d("sibal", response.message());
                        resultDialog.setContent(response.message());
                        resultDialog.show();
                        thread = new Thread();
                        thread.start();
                    }
                }

                @Override
                public void onFailure(Call<Bin> call, Throwable t) {
                    Log.d("sibal", t.getMessage());

                    t.printStackTrace();
                }
            });
            loadingCheck = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEND_INFORMATION:

                    break;
                case SEND_STOP:
                    Log.d("sibal","stop");
                    thread.stopThread();
                    resultDialog.hide();
                    break;
                default:
                    break;
            }
        }
    };

    class Thread extends java.lang.Thread {
        boolean stopped = false;
        int i = 0;

        public Thread() {
            stopped = false;
        }

        public void stopThread() {
            stopped = true;
        }

        @Override
        public void run() {
            super.run();
            while (stopped == false) {

                try { // 1초 씩 딜레이 부여
                    sleep(1000);
                    stopThread();
                    Message message = handler.obtainMessage(); // 메시지 ID 설정
                    message.what = SEND_STOP; // 메시지 내용 설정 (int)
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
