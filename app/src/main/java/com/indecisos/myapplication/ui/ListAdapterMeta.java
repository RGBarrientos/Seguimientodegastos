package com.indecisos.myapplication.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.indecisos.myapplication.ui.ListMetas;
import com.indecisos.myapplication.R;

import java.text.NumberFormat;
import java.util.List;

public class ListAdapterMeta extends RecyclerView.Adapter<ListAdapterMeta.ViewHolder>{
    private List<ListMetas> mData;
    private LayoutInflater mInflater;
    private Context context;
    private static final NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance();

    public ListAdapterMeta(List<ListMetas> itemList, Context context){
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.mData=itemList;
    }

    @NonNull
    @Override
    public ListAdapterMeta.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.list_metas,null);
        return new ListAdapterMeta.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterMeta.ViewHolder holder, int position) {
        holder.bindData(mData.get(position));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public void setItems(List<ListMetas> items){
        mData=items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tituloMeta, metaTextView;
        EditText fechaInicio, fechaFin;

        ViewHolder(View itemView) {
            super(itemView);
            tituloMeta = itemView.findViewById(R.id.tituloMetaTextView);
            metaTextView = itemView.findViewById(R.id.MetaTextView);
            fechaInicio = itemView.findViewById(R.id.editTextFechaInicioMeta);
            fechaFin = itemView.findViewById(R.id.editTextFechaFin);
        }

        void bindData(final ListMetas item) {

            fechaInicio.setText(item.fechaInicio);
            fechaFin.setText(item.fechaFin);
            tituloMeta.setText(item.tituloMeta);
            metaTextView.setText(formatoMoneda.format(item.metaCant));


        }
    }

}
