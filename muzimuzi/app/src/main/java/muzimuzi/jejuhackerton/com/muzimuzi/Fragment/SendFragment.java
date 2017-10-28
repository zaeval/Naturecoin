package muzimuzi.jejuhackerton.com.muzimuzi.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import muzimuzi.jejuhackerton.com.muzimuzi.R;
import muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects.Bin;
import muzimuzi.jejuhackerton.com.muzimuzi.retrofit_services.BlockChainService;
import muzimuzi.jejuhackerton.com.muzimuzi.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SendFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public EditText senderET;
    public static EditText recipientET;
    public EditText amountET;
    public Button sendBtn;
    MaterialDialog loadingDialog;
    MaterialDialog resultDialog;
    public SendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanFragment newInstance(String param1, String param2) {
        ScanFragment fragment = new ScanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_send, container, false);
        senderET = (EditText) rootView.findViewById(R.id.sender);
        senderET.setText(Util.getMacAddress2Hash(getContext()));
        senderET.setEnabled(false);
        recipientET = (EditText) rootView.findViewById(R.id.receipient);
        recipientET.setText("");
        amountET = (EditText)rootView.findViewById(R.id.amount);
        sendBtn = (Button) rootView.findViewById(R.id.send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlockChainService blockChainService = BlockChainService.retrofit.create(BlockChainService.class);

//            Call<Bin> call = blockChainService.newTransactions((new BlockChainService.Transaction(text,Util.sha256(address),1.0f).getJSON()));
                loadingDialog = new MaterialDialog.Builder(getContext())
                        .title("처리중입니다")
                        .content("조금만 기다려 주십시오.")
                        .progress(true, 0)
                        .show();

                resultDialog = new MaterialDialog.Builder(getContext())
                        .content("처리가 완료되었습니다.")
                        .positiveText("확인")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                resultDialog.dismiss();
                            }
                        })
                        .show();
                resultDialog.hide();
                Call<Bin> call = blockChainService.newTransactions(senderET.getText().toString(),recipientET.getText().toString(),Float.valueOf(amountET.getText().toString()));
                call.enqueue(new Callback<Bin>() {
                    @Override
                    public void onResponse(Call<Bin> call,
                                           Response<Bin> response) {
                        if (response.code() == 201) {
                            loadingDialog.dismiss();
                            resultDialog.show();

                        } else {
                            Log.d("sibal", response.message());
                            resultDialog.setContent(response.message());
                            resultDialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Bin> call, Throwable t) {
                        Log.d("sibal", t.getMessage());

                        t.printStackTrace();
                    }
                });
            }
        });
        return rootView;
    }
    public void setText(String text){
        View view = getView();
        EditText textView = (EditText) view.findViewById(R.id.receipient);
        textView.setText(text);
    }

}
