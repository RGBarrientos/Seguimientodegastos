package com.indecisos.myapplication.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.indecisos.myapplication.R;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class NuevoGastoFragment extends Fragment {
    private List<String> categoriasList ;
    private List<String> categoriasPSpinner;
    private EditText editTextMonto;
    private Spinner spinnerMoneda;
    private Spinner spinnerCategoria;
    private EditText editTextDescripcion;
    private DatePicker datePicker;
    private Button buttonAgregar;
    private SharedPreferences sharedPreferences;
    private ArrayAdapter<String> spinerAdapter;
    private TextView textcate;

    //Para agregar a la BDD
    FirebaseFirestore mFirestore;
    private double montoGasto;
    private String categoriaGasto;
    private String descripcionGasto;

    //



    public void GastosFragment() {
        // Constructor vacío requerido
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nuevo_gasto, container, false);

        TextView textViewNuevoGasto = view.findViewById(R.id.text_nuevo_gasto);
        textcate = view.findViewById(R.id.textview_categoria);
        editTextMonto = view.findViewById(R.id.edit_text_monto);
//        spinnerMoneda = view.findViewById(R.id.spinner_moneda);
        Spinner spinnerCategoria = view.findViewById(R.id.spinner_categoria);
        editTextDescripcion = view.findViewById(R.id.edit_text_descripcion);
        datePicker = view.findViewById(R.id.date_picker);

        //FireStore
        //mFirestore = FirebaseFirestore.getInstance();

        buttonAgregar = view.findViewById(R.id.button_agregar);
        buttonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //volver al fragmento original framgmento
                montoGasto = Double.parseDouble(String.valueOf(editTextMonto.getText()));
                categoriaGasto = String.valueOf(textcate.getText());
                categoriaGasto = categoriaGasto.trim();
                descripcionGasto = String.valueOf(editTextDescripcion.getText());
                descripcionGasto = descripcionGasto.trim();
                if(!(montoGasto <= 0) && !(categoriaGasto.isEmpty())){
                    try {
                        agregarGasto(montoGasto, categoriaGasto, descripcionGasto);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        // Obtén la instancia del fragmento CategoriasFragment
//        CategoriasFragment categoriasFragment = (CategoriasFragment) requireActivity().getSupportFragmentManager().findFragmentByTag("CategoriasFragment");
//
//        // Obtén la lista de categorías del fragmento CategoriasFragment
//        List<String> categoriasPSpinner = categoriasFragment.getCategoriasList();

        categoriasPSpinner = new ArrayList<>();
        spinerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, categoriasPSpinner);

        spinerAdapter.add("Comida");
        spinerAdapter.add("Transporte");
        spinerAdapter.add("Deporte");
        spinerAdapter.add("Salida");
        spinerAdapter.add("Salud");
        spinerAdapter.add("Tecnologia");

//        cargarCategorias();

        spinerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(spinerAdapter);
        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoriaSeleccionada = parent.getItemAtPosition(position).toString();
                textcate.setText(categoriaSeleccionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });
//        cargarCategoriasDesdeSharedPreferences();

        return view;
    }
    private String obtenerFechaSeleccionadaDelDatePickerBDD() {
        // Obtener la fecha seleccionada del DatePicker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        // Crear una instancia de Calendar y establecer la fecha seleccionada
        Calendar calendar = Calendar.getInstance();
        calendar.set(day, month, year);

        // Formatear la fecha en el formato deseado
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fecha = day+"/"+(month+1)+"/"+year;
        return fecha;
    }
    private void agregarGasto(double montoGasto, String categoriaGasto, String descripcionGasto) throws ParseException {
        //Aqui se agrega al FireStore
        Date f = new SimpleDateFormat("dd/MM/yyyy").parse(obtenerFechaSeleccionadaDelDatePickerBDD());
        Timestamp fechaGasto = new Timestamp(f);

        Number monto = (Number)montoGasto;

        Map<String, Object> map = new HashMap<>();
        map.put("montoGasto", monto);
        map.put("categoriaGasto", categoriaGasto);
        map.put("fechaGasto", fechaGasto);
        map.put("descripcionGasto", descripcionGasto);

        mFirestore.collection("gastos").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contenedorFragments, new GastosFragment());
                fragmentTransaction.commit();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    ///--------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("GastosSharedPreferences", Context.MODE_PRIVATE);
        mFirestore = FirebaseFirestore.getInstance();

    }

    private void cargarCategoriasDesdeSharedPreferences() {

    }

}