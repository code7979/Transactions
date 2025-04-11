package com.abhiket.transactions.ui.activities;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhiket.transactions.data.remote.model.Transaction;
import com.abhiket.transactions.databinding.ItemTansactionBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransitionAdapter extends RecyclerView.Adapter<TransitionAdapter.TransitionViewHolder> {
    private List<Transaction> transactions = new ArrayList<>();

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    public Transaction getTransaction(int position) {
        return transactions.get(position);
    }

    @NonNull
    @Override
    public TransitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemTansactionBinding binding = ItemTansactionBinding.inflate(inflater, parent, false);
        return new TransitionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransitionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.bind(transaction);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }


    public static class TransitionViewHolder extends RecyclerView.ViewHolder {
        private final ItemTansactionBinding binding;

        public TransitionViewHolder(@NonNull ItemTansactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Transaction transaction) {
            binding.tvAmount.setText(String.format(Locale.getDefault(), "₹ %d", transaction.getAmount()));
            binding.tvDate.setText(transaction.getDate());
            binding.tvDescription.setText(transaction.getDescription());
            binding.tvCategory.setText(transaction.getCategory());
        }
    }
}

