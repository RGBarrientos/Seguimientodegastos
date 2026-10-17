package com.indecisos.myapplication.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.indecisos.myapplication.ui.ListEstCategorias;
import com.indecisos.myapplication.ui.ListMetas;
import com.indecisos.myapplication.LoginActivity;
import com.indecisos.myapplication.MainActivity;
import com.indecisos.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class MetasFragment extends Fragment{

    //View view;
    LinearLayout listaLayout;
    public ArrayList<ListMetas> elements=new ArrayList<>();
    public ListAdapterMeta adapterMeta;
    RecyclerView recyclerVmetas;
    FirebaseFirestore mFirestore;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_metas, container, false);
        //
        recyclerVmetas=view.findViewById(R.id.metasRecyclerView);
        recyclerVmetas.setLayoutManager(new LinearLayoutManager(getContext()));

        initList();
        //agregarMeta();
        adapterMeta= new ListAdapterMeta(elements,getContext());
        recyclerVmetas.setAdapter(adapterMeta);

        Button nuevaMeta = (Button) view.findViewById(R.id.agregarMetaButton);
        nuevaMeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contenedorFragments, new NuevaMetaFragment());
                fragmentTransaction.commit();
            }
        });
        return view;
    }
    private void initList(){
        mFirestore.collection("metas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> aux = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        elements.add(new ListMetas(document.getString("nombreMeta"), document.getString("fechaInicioMeta"), document.getString("fechaFinMeta"), Float.parseFloat(document.getString("objetivoMeta"))));
                        //aux.add(String.valueOf(document.get("nombreMeta")));
                    }
                    adapterMeta= new ListAdapterMeta(elements,getContext());
                    recyclerVmetas.setAdapter(adapterMeta);
                } else {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void agregarMeta(String tituloMeta, String fechaInicio, String fechaFin, float metaCant){
        elements.add(new ListMetas(tituloMeta,fechaInicio,fechaFin,metaCant));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void cargarDatos() {
        //Desde la BDD
        mFirestore.collection("metas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        elements.add(new ListMetas(document.getString("nombreMeta"), document.getString("fechaInicioMeta"), document.getString("fechaFinMeta"), Float.parseFloat(document.getString("objetivoMeta"))));
                    }
                } else {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirestore = FirebaseFirestore.getInstance();
    }
}