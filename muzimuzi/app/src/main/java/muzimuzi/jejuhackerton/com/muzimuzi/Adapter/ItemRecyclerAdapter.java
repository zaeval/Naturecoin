package muzimuzi.jejuhackerton.com.muzimuzi.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import muzimuzi.jejuhackerton.com.muzimuzi.R;
import muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects.Transaction;
import muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects.WrappingTransaction;
import muzimuzi.jejuhackerton.com.muzimuzi.util.Util;


/**
 * Created by 1002461 on 15. 7. 27..
 */
public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder> {

    private List<WrappingTransaction> itemList;
    private List<WrappingTransaction> backUpList;
    Context context;
    public ItemRecyclerAdapter(List<WrappingTransaction> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
        backUpList = new ArrayList<>();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        final WrappingTransaction data = itemList.get(i);
        String sender = data.getSender();
        String recipient = data.getRecipient();

        if(sender.equals(Util.getMacAddress2Hash())){
            holder.hash.setText( recipient);
            holder.status.setText("SENT");
            holder.status.setTextColor(ContextCompat.getColor(context,R.color.sentColor));
            holder.amount.setTextColor(ContextCompat.getColor(context,R.color.sentColor));

        }
        else if(recipient.equals(Util.getMacAddress2Hash())){
            holder.hash.setText( sender);
            holder.status.setText("RECIEVE");
            holder.status.setTextColor(ContextCompat.getColor(context,R.color.receiveColor));
            holder.amount.setTextColor(ContextCompat.getColor(context,R.color.receiveColor));

        }

        holder.amount.setText(String.valueOf(data.getAmount()));
        holder.date.setText(Util.getDate((long)data.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return (null == itemList) ? 0 : itemList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_layout_item, viewGroup, false);
        v.setOnClickListener(new MyOnClickListener());
        return new ViewHolder(v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView hash;
        TextView status;
        TextView amount;
        TextView date;

        public ViewHolder(View v) {
            super(v);
            hash = (TextView) v.findViewById(R.id.log_hash);
            status = (TextView)v.findViewById(R.id.log_status);
            amount = (TextView)v.findViewById(R.id.log_amount);
            date = (TextView)v.findViewById(R.id.log_date);
        }
    }
    public class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
//            int itemPosition = Define.recyclerView.getChildPosition(view);
//            System.out.println("itemitem : " + itemPosition);
//            final Item data=itemList.get(itemPosition);
//            Intent intent=new Intent(context, ProductActivity.class);
//            intent.putExtra("ICON", data.getImg());
//            intent.putExtra("DRAMA",data.getDramaId());
//            intent.putExtra("BRAND",data.getBrand());
//            intent.putExtra("PRICE",data.getPrice());
//            intent.putExtra("ACTOR",data.getActor());
//            intent.putExtra("ID",data.getId());
//            intent.putExtra("EPSID",data.getEpisd());
//            intent.putExtra("NAME", data.getName());
//            intent.putExtra("ITEM", itemPosition);
//            context.startActivity(intent);
        }
    }
    public void add(WrappingTransaction item)
    {
        itemList.add(item);
        backUpList.add(item);
        itemList.sort(new Comparator<WrappingTransaction>() {
            @Override
            public int compare(WrappingTransaction o1, WrappingTransaction o2) {
                return (int)(o2.getTimestamp() - o1.getTimestamp());
            }
        });
    }
    public void removeAll(){
        itemList.removeAll(itemList);
        backUpList.removeAll(backUpList);
        notifyDataSetChanged();
    }
    public void showSENT(){
        itemList.removeAll(itemList);
        for(int i =0;i<backUpList.size();++i){
            WrappingTransaction transaction = backUpList.get(i);
            if(transaction.getSender().equals(Util.getMacAddress2Hash())) {
                itemList.add(transaction);
            }
        }
        itemList.sort(new Comparator<WrappingTransaction>() {
            @Override
            public int compare(WrappingTransaction o1, WrappingTransaction o2) {
                return (int)(o2.getTimestamp() - o1.getTimestamp());
            }
        });
        notifyDataSetChanged();
    }
    public void showALL(){
        itemList.removeAll(itemList);
        itemList.addAll(backUpList);
        itemList.sort(new Comparator<WrappingTransaction>() {
            @Override
            public int compare(WrappingTransaction o1, WrappingTransaction o2) {
                return (int)(o2.getTimestamp() - o1.getTimestamp());
            }
        });
        notifyDataSetChanged();
    }
    public void showRECEIVE(){
        itemList.removeAll(itemList);
        for(int i =0;i<backUpList.size();++i){
            WrappingTransaction transaction = backUpList.get(i);
            if(transaction.getRecipient().equals(Util.getMacAddress2Hash())) {
                itemList.add(transaction);
            }
        }
        itemList.sort(new Comparator<WrappingTransaction>() {
            @Override
            public int compare(WrappingTransaction o1, WrappingTransaction o2) {
                return (int)(o2.getTimestamp() - o1.getTimestamp());
            }
        });
        notifyDataSetChanged();

    }
}
