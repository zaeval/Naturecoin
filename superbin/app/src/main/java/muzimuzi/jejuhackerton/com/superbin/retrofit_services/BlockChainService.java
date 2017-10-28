package muzimuzi.jejuhackerton.com.superbin.retrofit_services;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;

import muzimuzi.jejuhackerton.com.superbin.retrofit_objects.Bin;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

import static muzimuzi.jejuhackerton.com.superbin.util.RetrofitSettings.BASE_URL;


/**
 * Created by hongseung-ui on 2017. 7. 11..
 */

public interface BlockChainService {
    final String USER_SERVICE_URL="transactions/new";
//    public class Transaction{
//        String sender;
//        String recipient;
//        float amount;
//        public Transaction(String sender, String recipient, float amount){
//            this.sender = sender;
//            this.recipient = recipient;
//            this.amount = amount;
//        }
//        public String getJSON() {
//            JSONStringer jsonStringer = null;
//            try {
//                jsonStringer = new JSONStringer().object().key("sender").value(sender)
//                        .key("recipient").value(recipient)
//                        .key("amount").value(1.0)
//                        .endObject();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return String.valueOf(jsonStringer);
//        }
//    }
//    @Headers("Content-Type: application/json")
    @FormUrlEncoded
    @POST(USER_SERVICE_URL)
    Call<Bin> newTransactions(
//        @Body String data
        @Field("sender") String sender,
        @Field("recipient") String recipient,
        @Field("amount") float amount
    );

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


}

