package muzimuzi.jejuhackerton.com.muzimuzi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import muzimuzi.jejuhackerton.com.muzimuzi.Adapter.ItemRecyclerAdapter;
import muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects.Bin;
import muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects.Chain;
import muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects.ChainObject;
import muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects.Mine;
import muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects.Transaction;
import muzimuzi.jejuhackerton.com.muzimuzi.retrofit_services.BlockChainService;
import muzimuzi.jejuhackerton.com.muzimuzi.util.SharedPreferencesManager;
import muzimuzi.jejuhackerton.com.muzimuzi.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ItemRecyclerAdapter itemRecyclerAdapter;
    public List<Transaction> li;
    private TextView ntc;
    private TextView money;
    public static String CURRENT_NTC="currentNTC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        li = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        itemRecyclerAdapter = new ItemRecyclerAdapter(li, getApplicationContext());

        ntc = (TextView) findViewById(R.id.current_ntc);
        money = (TextView) findViewById(R.id.current_money);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(itemRecyclerAdapter);

        BlockChainService blockChainService = BlockChainService.retrofit.create(BlockChainService.class);
        Call<Mine> mineCall = blockChainService.mine();
        mineCall.enqueue(new Callback<Mine>() {
            @Override
            public void onResponse(Call<Mine> call,
                                   Response<Mine> response) {
                if (response.code() == 200) {
                    Mine mine = response.body();
                    BlockChainService blockChainService2 = BlockChainService.retrofit.create(BlockChainService.class);

                    Log.d("sibal", mine.getIndex() + mine.getMessage());
                    Call<Chain> chainCall = blockChainService2.chain();
                    chainCall.enqueue(new Callback<Chain>() {
                        @Override
                        public void onResponse(Call<Chain> call,
                                               Response<Chain> response) {
                            if (response.code() == 200) {
                                Util.sum=100;
                                Chain chain = response.body();
                                List<ChainObject> chainObjects = chain.getChain();
                                for (int i = 0; i < chainObjects.size(); i++) {
                                    List<Transaction> transactions = chainObjects.get(i).getTransactions();
                                    for (int j = 0; j < transactions.size(); j++) {
                                        if (transactions.get(j).getSender().equals(Util.getMacAddress2Hash())) {
                                            itemRecyclerAdapter.add(transactions.get(j));
                                            Log.d("sibal", "got");

                                            Util.sum-=transactions.get(j).getAmount();

                                        }
                                        else if (transactions.get(j).getRecipient().equals(Util.getMacAddress2Hash())) {
                                            itemRecyclerAdapter.add(transactions.get(j));
                                            Log.d("sibal", "got");

                                            Util.sum+=transactions.get(j).getAmount();

                                        }
                                    }
                                }
                                SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager();
                                sharedPreferencesManager.putFloat(CURRENT_NTC,Util.sum,getApplicationContext());
                                itemRecyclerAdapter.notifyDataSetChanged();
                                ntc.setText("current NTC : "+ String.valueOf(Util.sum));
                                Log.d("sibal", li.size() + "");
                            } else {

                            }
                        }

                        @Override
                        public void onFailure(Call<Chain> call, Throwable t) {
                            Log.d("sibal", t.getMessage());

                            t.printStackTrace();
                        }
                    });

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

    }
}
