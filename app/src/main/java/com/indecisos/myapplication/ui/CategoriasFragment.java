package com.indecisos.myapplication.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.indecisos.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CategoriasFragment extends Fragment {
    private ListView listViewCategorias;
    public List<String> categoriasList;
    private ArrayAdapter<String> categoriasAdapter;
    private SharedPreferences sharedPreferences;

    FirebaseFirestore mFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categorias, container, false);

        TextView textViewCategorias = view.findViewById(R.id.text_categorias);
        listViewCategorias = view.findViewById(R.id.list_categorias);
        FloatingActionButton fabAgregarCategoria = view.findViewById(R.id.fab_agregar_categoria);

        fabAgregarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contenedorFragments, new AgregarCategoriaFragment());
                fragmentTransaction.commit();
            }
        });
        // Inicializar la lista de categorías y el adaptador
        categoriasList = new ArrayList<>();
        cargarDatos();
        categoriasAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, categoriasList);

        // Asignar el adaptador al ListView
        listViewCategorias.setAdapter(categoriasAdapter);

        // Registrar el listener para el click largo en los elementos de la lista
        listViewCategorias.setOnItemLongClickListener((adapterView, view1, position, id) -> {
            // Obtener la categoría seleccionada
            String categoria = categoriasList.get(position);

            // Mostrar un diálogo de confirmación para eliminar la categoría
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Eliminar categoría");
            builder.setMessage("¿Está seguro de que desea eliminar esta categoría?");
            builder.setPositiveButton("Sí", (dialogInterface, i) -> eliminarCategoria(position));
            builder.setNegativeButton("No", null);
            builder.show();

            return true;
        });

        // Cargar las categorías guardadas en las SharedPreferences
        //cargarCategoriasDesdeSharedPreferences();

        return view;
    }

    private void cargarDatos() {
        //Desde la BDD
        mFirestore.collection("categorias").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> categorias = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        categorias.add(String.valueOf(document.get("nombreCategoria")));
                    }
                    categoriasList.clear();
                    categoriasList.addAll(categorias);
                    categoriasAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cargarCategoriasDesdeSharedPreferences() {
        // Obtener todas las claves de las categorías guardadas
        Map<String, ?> categorias = sharedPreferences.getAll();

        // Limpiar la lista de categorías antes de cargarlas nuevamente
        categoriasList.clear();

        // Iterar sobre las claves y obtener los valores correspondientes
        for (String claveCategoria : categorias.keySet()) {
            String nombreCategoria = sharedPreferences.getString(claveCategoria, null);
            if (nombreCategoria != null) {
                // Agregar la categoría a la lista
                categoriasList.add(nombreCategoria);
            }
        }

        // Notificar al adaptador del cambio en la lista de categorías
        categoriasAdapter.notifyDataSetChanged();
    }

    private void eliminarCategoria(int position) {
        // Obtener la categoría seleccionada
        String categoria = categoriasList.get(position);

        // Eliminar la categoría de las SharedPreferences
        for (String claveCategoria : sharedPreferences.getAll().keySet()) {
            String nombreCategoria = sharedPreferences.getString(claveCategoria, null);
            if (nombreCategoria != null && nombreCategoria.equals(categoria)) {
                sharedPreferences.edit().remove(claveCategoria).apply();
                break;
            }
        }

        // Actualizar la lista de categorías
        categoriasList.remove(position);
        categoriasAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();

        // Inicializar SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("CategoriasSharedPreferences", Context.MODE_PRIVATE);
    }
    public List<String> getCategoriasList() {
        return categoriasList;
    }


}
