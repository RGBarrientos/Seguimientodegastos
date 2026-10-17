package com.indecisos.myapplication.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.indecisos.myapplication.R;

public class AgregarSaldodialogFragment extends DialogFragment {
    private EditText editTextSaldo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogfragment_saldo, container, false);

        editTextSaldo = view.findViewById(R.id.edit_text_saldo);
        Button buttonAgregar = view.findViewById(R.id.button_agregar_saldo);

        buttonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String saldo = editTextSaldo.getText().toString();
                // Realiza la validación y guarda el saldo en las SharedPreferences
                if (!saldo.isEmpty()) {
                    guardarSaldo(saldo);
                }

                dismiss(); // Cierra el DialogFragment
            }
        });

        return view;
    }

    private void guardarSaldo(String saldo) {
        // Guardar el nuevo saldo en las SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("mi_saldo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("saldo", saldo);
        editor.apply();

        // Actualizar el TextView "tvSaldo" en el fragmento "Gastos"
        GastosFragment gastosFragment = (GastosFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.contenedorFragments);
        if (gastosFragment != null) {
            TextView tvSaldo = gastosFragment.getView().findViewById(R.id.tv_saldo);
            tvSaldo.setText(saldo);

        }

    }
}
