package com.example.adbank;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ModelTranzactionAdapter extends RecyclerView.Adapter<ModelTranzactionViewHolder> {

    private List<ModelTranzaction> list;
    private Uri mImageUri;
    private String accountAdBank;

    Context context;

    public ModelTranzactionAdapter(String accountAdBank){
        this.accountAdBank=accountAdBank;
        list = new ArrayList<>();
    }

    @Override
    public ModelTranzactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tranzaction, parent, false);
        context = parent.getContext();
        return new ModelTranzactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ModelTranzactionViewHolder holder, int position) {
        holder.bindTo(list.get(position));
        //mImageUri=Uri.parse(list.get(position).getAppIcon().toString());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<ModelTranzaction> listTranzaction) {
        list=listTranzaction;
    }
}
