package muzimuzi.jejuhackerton.com.muzimuzi.retrofit_services;

import muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects.Bin;
import muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects.Chain;
import muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects.Mine;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import static muzimuzi.jejuhackerton.com.muzimuzi.retrofit_services.BlockChainService.MINE_SERVICE_URL;
import static muzimuzi.jejuhackerton.com.muzimuzi.util.RetrofitSettings.BASE_URL;


/**
 * Created by hongseung-ui on 2017. 7. 11..
 */

public interface BlockChainService {
    final String NEW_TRANSACTION_SERVICE_URL="transactions/new";
    final String MINE_SERVICE_URL="mine";
    final String CHAIN_SERVICE_URL="chain";

    @FormUrlEncoded
    @POST(NEW_TRANSACTION_SERVICE_URL)
    Call<Bin> newTransactions(
            @Field("sender") String sender,
            @Field("recipient") String recipient,
            @Field("amount") float amount
    );

    @GET(MINE_SERVICE_URL)
    Call<Mine> mine();

    @GET(CHAIN_SERVICE_URL)
    Call<Chain> chain();

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


}

