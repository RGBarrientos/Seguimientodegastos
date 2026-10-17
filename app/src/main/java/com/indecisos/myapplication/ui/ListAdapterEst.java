package com.indecisos.myapplication.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.indecisos.myapplication.ui.ListEstCategorias;
import com.indecisos.myapplication.R;


import java.text.NumberFormat;
import java.util.List;

public class ListAdapterEst extends RecyclerView.Adapter<ListAdapterEst.ViewHolder>{
    private List<ListEstCategorias> mData;
    private LayoutInflater mInflater;
    private Context context;
    private static final NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance();

    public ListAdapterEst(List<ListEstCategorias> itemList, Context context){
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.mData=itemList;
    }

    @NonNull
    @Override
    public ListAdapterEst.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.list_est_categorias,null);
        return new ListAdapterEst.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterEst.ViewHolder holder, int position) {
        holder.bindData(mData.get(position));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public void setItems(List<ListEstCategorias> items){
        mData=items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView categoria, catTotal;
        ProgressBar progress;
        ViewHolder(View itemView){
            super(itemView);
            categoria=itemView.findViewById(R.id.CategoriaTextView);
            catTotal=itemView.findViewById(R.id.GastoPTextView);
            progress=itemView.findViewById(R.id.CategoriaProgressBar);
        }

        void bindData(final ListEstCategorias item) {


            //Drawable estiloPB  = ContextCompat.getDrawable(context, item.progressDrawable);
        progress.setProgressDrawable((ContextCompat.getDrawable(context, item.progressDrawable)));
        progress.setProgress(item.cantProgressB);
        categoria.setText(item.categoria);
        catTotal.setText(formatoMoneda.format(item.catTotal));




        }
    }

}