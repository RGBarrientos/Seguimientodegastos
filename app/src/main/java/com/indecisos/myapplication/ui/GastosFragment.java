package com.indecisos.myapplication.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.indecisos.myapplication.R;

import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GastosFragment extends Fragment {
    private TextView tvSaldo;
    private TextView tvGastosTotales;
    private Button btnNuevoGasto;
    private ArrayAdapter<String> gastosAdapter;
    private ArrayAdapter<String> fechasAdapter;
    private ArrayAdapter<String> descripAdapter;
    private ArrayAdapter<String> categoAdapter;
    private List<String> gastosList;
    private List<String> fechasList;
    private List<String> descripList;
    private List<String> categoList;

    FirebaseFirestore mFirestore;

    private SharedPreferences sharedPreferences;
    public GastosFragment() {
        // Constructor vacío requerido
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gastos, container, false);

        tvSaldo = view.findViewById(R.id.tv_saldo);
        // Recuperar el saldo almacenado en las preferencias compartidas
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("mi_saldo", Context.MODE_PRIVATE);
        String saldo = sharedPreferences.getString("saldo", "0.00");
        tvSaldo.setText(saldo);
        tvSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarSaldodialogFragment dialogFragment = new AgregarSaldodialogFragment();
                dialogFragment.show(getFragmentManager(), "agregar_saldo_dialog");
            }
        });

        tvGastosTotales = view.findViewById(R.id.tv_gastos_totales);
        ListView listViewGastos = view.findViewById(R.id.list_gastos);
        ListView listViewCate = view.findViewById(R.id.list_categoria);
        ListView listViewDescri = view.findViewById(R.id.list_descripcion);
        ListView listViewFecha = view.findViewById(R.id.list_fecha);
        btnNuevoGasto = view.findViewById(R.id.btn_nuevo_gasto);
        btnNuevoGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //abrir otro framgmento
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contenedorFragments, new NuevoGastoFragment());
                fragmentTransaction.commit();
            }
        });
        // Inicializar las lista y los adaptador
        gastosList = new ArrayList<>();
        fechasList = new ArrayList<>();
        categoList = new ArrayList<>();
        descripList = new ArrayList<>();
        // Obtener una referencia al ListView
        cargarDatos();
        gastosAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, gastosList);
        fechasAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, fechasList);
        categoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, categoList);
        descripAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, descripList);
        listViewGastos.setAdapter(gastosAdapter);
        listViewCate.setAdapter(categoAdapter);
        listViewDescri.setAdapter(descripAdapter);
        listViewFecha.setAdapter(fechasAdapter);
        //actualiza gastos totales
        int suma = sumarLista(gastosList);
        tvGastosTotales.setText(String.valueOf(suma +" bs"));
        //actualiza saldo total
        String saldoActual = tvSaldo.getText().toString();
        double saldoRestante = Double.parseDouble(saldoActual) - suma;
        tvSaldo.setText(String.valueOf(saldoRestante +" bs"));
        /*if (!gastosList.isEmpty()) {

        }*/
        return view;
    }
    private void cargarDatos() {
        //Desde la BDD
        mFirestore.collection("gastos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> montos = new ArrayList<>();
                    List<String> categorias = new ArrayList<>();
                    List<String> descripciones = new ArrayList<>();
                    List<String> fechas = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        montos.add(String.valueOf(document.getDouble("montoGasto")));
                        categorias.add(String.valueOf(document.get("categoriaGasto")));
                        descripciones.add(String.valueOf(document.get("descripcionGasto")));
                        Timestamp tStamp = document.getTimestamp("fechaGasto");
                        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                        fechas.add(String.valueOf(formato.format(tStamp.toDate())));
                    }
                    gastosList.clear();
                    gastosList.addAll(montos);
                    gastosAdapter.notifyDataSetChanged();
                    tvGastosTotales.setText(String.valueOf(sumarLista(gastosList) + " Bs"));

                    categoList.clear();
                    categoList.addAll(categorias);
                    categoAdapter.notifyDataSetChanged();

                    descripList.clear();
                    descripList.addAll(descripciones);
                    descripAdapter.notifyDataSetChanged();

                    fechasList.clear();
                    fechasList.addAll(fechas);
                    fechasAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();

        // Inicializar SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("GastosSharedPreferences", Context.MODE_PRIVATE);
    }

    private int sumarLista(List<String> lista) {
        int suma = 200;
        for (String numero : lista) {
            try {
                int valor = Integer.parseInt(numero);
                suma += valor;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // Manejar la conversión fallida de cadena a número si es necesario
            }
        }
        return suma;
    }
}
