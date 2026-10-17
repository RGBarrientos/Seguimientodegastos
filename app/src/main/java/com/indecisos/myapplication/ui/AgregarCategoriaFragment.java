package com.indecisos.myapplication.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AgregarCategoriaFragment extends Fragment {
    private EditText editTextNombre;
    private Spinner spinnerModificarColor;
    private Button buttonGuardar;
    private View colorView;

    private SharedPreferences sharedPreferences;
    private List<String> categoriasList;
    private ArrayAdapter<String> categoriasAdapter;
    private ArrayAdapter<String> spinerAdapter;

    //Para agregar a la BDD
    FirebaseFirestore mFirestore;
    private String nombreCategoria;
    private int colorCategoria = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_categoria, container, false);

        colorView = view.findViewById(R.id.colorView);
        // Enlazar con el ImageView y el botón guardar
//        imageViewIcono.setColorFilter(colorSeleccionado);

        TextView textViewAgregarCategoria = view.findViewById(R.id.text_agregar_categoria);
        editTextNombre = view.findViewById(R.id.edit_text_nombre);


        sharedPreferences = requireContext().getSharedPreferences("Categorias", Context.MODE_PRIVATE);

        // Inicializar categoriasList
        categoriasList = new ArrayList<>();

        // Inicializar categoriasAdapter
        categoriasAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, categoriasList);

        // Obtener referencia al EditText
        editTextNombre = view.findViewById(R.id.edit_text_nombre);

        buttonGuardar = view.findViewById(R.id.button_guardar);
//        buttonGuardar.setBackgroundColor(colorSeleccionado);

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreCategoria = String.valueOf(editTextNombre.getText());
                if(!(nombreCategoria.isEmpty()) && (colorCategoria != -1)){
                    agregarCategoria(nombreCategoria, colorCategoria);
                }
            }
        });
//        buttonModificarColor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                colorSeleccionado = colorSeleccionadoNuevo;
//                imageViewIcono.setColorFilter(colorSeleccionado);
//                buttonGuardar.setBackgroundColor(colorSeleccionado);
//            }
//        });
        // Obtener referencia a la ListView en el fragmento "Categorías"
        //ListView listViewCategorias = requireActivity().findViewById(R.id.list_categorias);

        // Asignar el adaptador a la ListView en el fragmento "Categorías"
        //listViewCategorias.setAdapter(categoriasAdapter);

        // Inicializar SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("CategoriasSharedPreferences", Context.MODE_PRIVATE);


        Spinner spinnerModificarColor = view.findViewById(R.id.spinner_modificar_color);

        spinerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, categoriasList);

        spinerAdapter.add("Naranja");
        spinerAdapter.add("Azul");
        spinerAdapter.add("Amarillo");
        spinerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModificarColor.setAdapter(spinerAdapter);
        spinnerModificarColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String categoriaSeleccionada = parent.getItemAtPosition(position).toString();
//                textcate.setText(categoriaSeleccionada);
                String color = parent.getItemAtPosition(position).toString();
                String colorfinal = null;
                if(color == "Naranja"){
                    colorfinal = String.valueOf(0xFFFB8500);
                }
                if(color == "Azul"){
                    colorfinal = String.valueOf(0xFF023047);
                }
                if(color == "Amarillo"){
                    colorfinal = String.valueOf((Color.YELLOW));
                }
                colorView.setBackgroundColor(Integer.parseInt(colorfinal));
                colorCategoria = Integer.parseInt(colorfinal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });
        return view;
    }//onCreateView()


    private void agregarCategoria(String nombreCategoria, int colorCategoria) {
        //Aqui se agrega al FireStore
        Map<String, Object> map = new HashMap<>();
        map.put("nombreCategoria", nombreCategoria);
        map.put("colorCategoria", colorCategoria);

        mFirestore.collection("categorias").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contenedorFragments, new CategoriasFragment());
                fragmentTransaction.commit();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void guardarCategoria() {
        String nombreCategoria = editTextNombre.getText().toString().trim();
//        String colorfondo = String.valueOf(colorView.getBackground());

        // Validar que el nombre de la categoría no esté vacío
        if (!nombreCategoria.isEmpty()) {
            // Generar una clave única para la categoría
            String claveCategoria = UUID.randomUUID().toString();

            // Guardar la categoría en SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(claveCategoria, nombreCategoria);
            editor.apply();

            // Agregar la categoría a la lista y notificar al adaptador
            categoriasList.add(nombreCategoria);
            categoriasAdapter.notifyDataSetChanged();

            // Limpiar el campo de nombre
            editTextNombre.setText("");
        } else {
            Toast.makeText(requireContext(), "Ingrese un nombre de categoría válido", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();

        // Inicializar SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("CategoriasSharedPreferences", Context.MODE_PRIVATE);
    }
}
