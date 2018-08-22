package muzimuzi.jejuhackerton.com.muzimuzi;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

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

public class LogActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ItemRecyclerAdapter itemRecyclerAdapter;
    public List<Transaction> li;
    private TextView ntc;
    private TextView money;
    public static String CURRENT_NTC = "currentNTC";

    private boolean checked[] = new boolean[3];
    private RelativeLayout btns[]= new RelativeLayout[3];
    private TextView btn_contents[] = new TextView[3];

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

        ImageButton back= (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btns[0] = (RelativeLayout) findViewById(R.id.log_all_layout);
        btns[1] = (RelativeLayout) findViewById(R.id.log_receive_layout);
        btns[2] = (RelativeLayout) findViewById(R.id.log_sent_layout);

        btn_contents[0] = (TextView) findViewById(R.id.log_all_text);
        btn_contents[1] = (TextView) findViewById(R.id.log_receive_text);
        btn_contents[2] = (TextView) findViewById(R.id.log_sent_text);

        checked[0] = true;

        for(int i =0;i<3;++i){
            btns[i].setOnClickListener(this);
        }

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
                                Util.sum = 100;
                                Chain chain = response.body();
                                List<ChainObject> chainObjects = chain.getChain();
                                for (int i = 0; i < chainObjects.size(); i++) {
                                    List<Transaction> transactions = chainObjects.get(i).getTransactions();
                                    for (int j = 0; j < transactions.size(); j++) {
                                        if (transactions.get(j).getSender().equals(Util.getMacAddress2Hash())) {
                                            itemRecyclerAdapter.add(transactions.get(j));
                                            Log.d("sibal", "got");

                                            Util.sum -= transactions.get(j).getAmount();

                                        } else if (transactions.get(j).getRecipient().equals(Util.getMacAddress2Hash())) {
                                            itemRecyclerAdapter.add(transactions.get(j));
                                            Log.d("sibal", "got");

                                            Util.sum += transactions.get(j).getAmount();

                                        }
                                    }
                                }
                                SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager();
                                sharedPreferencesManager.putFloat(CURRENT_NTC, Util.sum, getApplicationContext());
                                itemRecyclerAdapter.notifyDataSetChanged();
                                ntc.setText("current NTC : " + String.valueOf(Util.sum));
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

    @Override
    public void onClick(View v) {
        for(int i =0;i<3;++i){
            if(checked[i]){
                btns[i].setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.transaction_tab_off));
                btn_contents[i].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.tabOff));
            }
            checked[i]=false;
            if(v==btns[i]){
                checked[i]=true;
                btns[i].setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.transaction_tab_on));
                btn_contents[i].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.tabOn));
                if(i == 0)
                    itemRecyclerAdapter.showALL();
                else if(i==1)
                    itemRecyclerAdapter.showRECEIVE();
                else if(i==2)
                    itemRecyclerAdapter.showSENT();

            }
        }
    }
}
