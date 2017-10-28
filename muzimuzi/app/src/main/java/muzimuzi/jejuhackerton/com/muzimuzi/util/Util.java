package muzimuzi.jejuhackerton.com.muzimuzi.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hongseung-ui on 2017. 10. 27..
 */

public class Util {
    public static String myWalletAddress = "";
    public static String sha256(String str){

        String SHA = "";

        try{

            MessageDigest sh = MessageDigest.getInstance("SHA-256");

            sh.update(str.getBytes());

            byte byteData[] = sh.digest();

            StringBuffer sb = new StringBuffer();

            for(int i = 0 ; i < byteData.length ; i++){

                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));

            }

            SHA = sb.toString();



        }catch(NoSuchAlgorithmException e){

            e.printStackTrace();

            SHA = null;

        }

        return SHA;

    }
    public static String getMacAddress2Hash(Context context){
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        return Util.sha256(address);
    }


}
