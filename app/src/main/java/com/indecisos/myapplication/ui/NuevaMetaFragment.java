package com.indecisos.myapplication.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.indecisos.myapplication.R;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NuevaMetaFragment extends Fragment {

    private String nombreMeta;
    private String inicioMeta;
    private String finMeta;
    private String objetivoMeta;
    private int dia,mes,anio;
    EditText nombreMetaEditText;
    EditText inicioMetaEditText;
    EditText finMetaEditText;
    EditText objetivoMetaEditText;
    float objetivo;

    //Para agregar a la BDD
    FirebaseFirestore mFirestore;
    private String nombreAddMeta;
    private double objetivoAddMeta;
    private Date fechaInicioMeta;
    private Date fechaFinalMeta;

    private static final NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nueva_meta, container, false);
        nombreMetaEditText = view.findViewById(R.id.nombreMetaEditText);
        inicioMetaEditText = view.findViewById(R.id.inicioMetaEditText);
        finMetaEditText= view.findViewById(R.id.finMetaEditText);
        objetivoMetaEditText = view.findViewById(R.id.objetivoMetaEditText);
        inicioMetaEditText.setOnClickListener(oyenteFechaInicio);
        finMetaEditText.setOnClickListener(oyenteFechaFin);
        Button crearMeta = (Button) view.findViewById(R.id.crearMetaButton);
        crearMeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nombreMeta = String.valueOf(nombreMetaEditText.getText());
                inicioMeta = String.valueOf(inicioMetaEditText.getText());
                try {
                    fechaInicioMeta = new SimpleDateFormat("dd/MM/yyyy").parse(inicioMeta);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                finMeta = String.valueOf(finMetaEditText.getText());
                try {
                    fechaFinalMeta = new SimpleDateFormat("dd/MM/yyyy").parse(finMeta);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                objetivoMeta = String.valueOf(objetivoMetaEditText.getText());
                objetivoAddMeta = Double.parseDouble(String.valueOf(objetivoMetaEditText.getText()));
                if(!(nombreMeta.isEmpty()) && !(inicioMeta.isEmpty()) && !(finMeta.isEmpty()) && !(objetivoMeta.isEmpty())){
                    agregarMeta(nombreMeta, fechaInicioMeta, fechaFinalMeta, objetivoAddMeta);
                }

            }
        });
        return view;
    }

    private final View.OnClickListener oyenteFechaInicio = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Calendar c = Calendar.getInstance();
            anio = c.get(Calendar.YEAR);
            mes = c.get(Calendar.MONTH);
            dia = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    inicioMetaEditText.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                }
            }, anio,mes,dia);
            datePickerDialog.show();
        }
    };
    private final View.OnClickListener oyenteFechaFin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Calendar c = Calendar.getInstance();
            anio = c.get(Calendar.YEAR);
            mes = c.get(Calendar.MONTH);
            dia = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    finMetaEditText.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                }
            }, anio,mes,dia);
            datePickerDialog.show();
        }
    };

    private void agregarMeta(String nombreAddMeta, Date fechaInicioMeta, Date fechaFinalMeta, Double objetivoAddMeta) {
        //Aqui se agrega al FireStore
        /*Timestamp fechaInicio = new Timestamp(fechaInicioMeta);
        Timestamp fechaFin = new Timestamp(fechaFinalMeta);
        Number monto = (Number)objetivoAddMeta;*/

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String fechaInicio = formato.format(fechaInicioMeta);
        String fechaFin = formato.format(fechaFinalMeta);


        Map<String, Object> map = new HashMap<>();
        map.put("nombreMeta", nombreAddMeta);
        map.put("fechaInicioMeta", fechaInicio);
        map.put("fechaFinMeta", fechaFin);
        map.put("objetivoMeta", String.valueOf(objetivoAddMeta));

        mFirestore.collection("metas").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contenedorFragments, new MetasFragment());
                fragmentTransaction.commit();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirestore = FirebaseFirestore.getInstance();
    }
}