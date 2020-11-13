package com.example.appbank;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.viewHolder> {
    ArrayList<Transaction> transactionList;

    public TransactionAdapter(ArrayList<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction,null,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.viewHolder holder, int position) {
        holder.transactionnumber.setText(transactionList.get(position).getTransactionNumber().toString());
        holder.originaccount.setText(transactionList.get(position).getOriginAccount().toString());
        holder.targetaccount.setText(transactionList.get(position).getTargetAccount().toString());
        holder.date.setText(transactionList.get(position).getDate().toString());
        holder.amount.setText(transactionList.get(position).getAmount().toString());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView transactionnumber, originaccount, targetaccount, date, amount;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            transactionnumber = itemView.findViewById(R.id.tvtransactionnumberl);
            originaccount = itemView.findViewById(R.id.tvoriginaccountl);
            targetaccount = itemView.findViewById(R.id.tvtargetaccountl);
            date = itemView.findViewById(R.id.tvdatel);
            amount = itemView.findViewById(R.id.tvamountl);
        }
    }
}
