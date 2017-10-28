package muzimuzi.jejuhackerton.com.muzimuzi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects.Bin;
import muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects.Chain;
import muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects.Mine;
import muzimuzi.jejuhackerton.com.muzimuzi.retrofit_services.BlockChainService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        BlockChainService blockChainService = BlockChainService.retrofit.create(BlockChainService.class);
        Call<Mine> mineCall = blockChainService.mine();
        mineCall.enqueue(new Callback<Mine>() {
            @Override
            public void onResponse(Call<Mine> call,
                                   Response<Mine> response) {
                if (response.code() == 200) {
                    Mine mine = response.body();

                   Log.d("sibal",mine.getIndex()+mine.getMessage());

                } else {
                    Log.d("sibal", response.message());

                }
            }

            @Override
            public void onFailure(Call<Mine> call, Throwable t) {
                Log.d("sibal", t.getMessage());

                t.printStackTrace();
            }
        });
        Call<Chain> chainCall = blockChainService.chain();
        chainCall.enqueue(new Callback<Chain>() {
            @Override
            public void onResponse(Call<Chain> call,
                                   Response<Chain> response) {
                if (response.code() == 200) {
                    Chain chain = response.body();

                    Log.d("sibal",chain.getLength()+chain.getChain().get(0).getPrevious_hash());

                } else {
                    Log.d("sibal", response.message());

                }
            }

            @Override
            public void onFailure(Call<Chain> call, Throwable t) {
                Log.d("sibal", t.getMessage());

                t.printStackTrace();
            }
        });
    }
}
