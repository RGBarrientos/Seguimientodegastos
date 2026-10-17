package com.indecisos.myapplication.ui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.indecisos.myapplication.R;

import java.util.ArrayList;

public class EstSemanalFragment extends Fragment {

    private BarChart GraficoBarras;
    private String [] categoriasArr= new String[]{"Transporte","Estudios","Hogar"};
    private Float [] gastosArr= new Float[]{520.f,450.f,400.f};
    //private int [] colores=new int[]{getResources().getColor(R.color.color3),getResources().getColor(R.color.color2),getResources().getColor(R.color.color4)};
    //private int [] colores=new int[]{Color.GREEN,Color.MAGENTA,Color.RED};
    // private int []colores=new int[]{ContextCompat.getColor(getContext(), R.color.color5),ContextCompat.getColor(getContext(), R.color.color3),ContextCompat.getColor(getContext(), R.color.color4)};
    private int []colores=new int[3];
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_est_semanal, container, false);
        colores=new int[]{ContextCompat.getColor(getContext(), R.color.color5),ContextCompat.getColor(getContext(), R.color.color3),ContextCompat.getColor(getContext(), R.color.color4)};
        GraficoBarras=(BarChart) view.findViewById(R.id.graficoBarras);
        //btnNuevoGasto = view.findViewById(R.id.btn_nuevo_gasto);
        Button gastosEstBtn=view.findViewById(R.id.gastosEstBtn);
        gastosEstBtn.setOnClickListener(oyenteGastoEstBtn);

        //oyente button


        //Modificarleyenda(GraficoBarras);
        CrearGrafica();


        return view;
    }

    private final View.OnClickListener oyenteGastoEstBtn=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contenedorFragments, new EstadisticasFragment());
            fragmentTransaction.commit();
        }
    };

    // chart o grafica, descripcion color texto color fondo tiempo de animacion
    private Chart getSameChart(Chart chart, String description, int textColor, int backgroung, int animateY){
        chart.getDescription().setText(description);
        chart.getDescription().setTextSize(15);
        chart.getDescription().setTextColor(textColor); //
        chart.setBackgroundColor(backgroung);
        chart.animateY(animateY);
        Modificarleyenda(chart);
        return chart;
    }

    //INGRESAR DATOS QUE SE MOSTRARAN EN LA LEYENDA DE LOS GRAFICOS
    private void Modificarleyenda(Chart chart){
        Legend leyenda=chart.getLegend();
        leyenda.setForm(Legend.LegendForm.CIRCLE); //forma de la leyenda
        leyenda.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        // llenar datos
        ArrayList<LegendEntry> entradasLeyenda=new ArrayList<>();
        for (int i = 0; i < categoriasArr.length; i++) {
            LegendEntry entrada=new LegendEntry();
            //colores
            entrada.formColor=colores[i];
            //texto categorias
            entrada.label=categoriasArr[i];
            //agregar a lista
            entradasLeyenda.add(entrada);
        }
        //para agregar los datos a la leyenda
        leyenda.setCustom(entradasLeyenda);
    }// modificar leyenda

    //Ingresar Datos a la grafica
    private ArrayList<BarEntry>ingresarDatos(){
        ArrayList<BarEntry> datosGrafica= new ArrayList<>();
        for (int i = 0; i < gastosArr.length; i++) {
            datosGrafica.add(new BarEntry(i,gastosArr[i]));
        }
        return datosGrafica;
    }

    // eje x recibe el eje X
    private void ejeX(XAxis axis){
        axis.setGranularityEnabled(true);// Granulidad de cuanto en cuanto se muestran las graficas
        axis.setPosition(XAxis.XAxisPosition.BOTTOM); // titulos se muestran solo abajo
        axis.setValueFormatter(new IndexAxisValueFormatter(categoriasArr)); // formato (?
        axis.setEnabled(false);
    }
    private void ejeY(XAxis axis){
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(categoriasArr));
    }
    private void leyendaDerecha(YAxis axis){
        axis.setEnabled(false);
    }

    //create chars
    private void CrearGrafica(){
        GraficoBarras=(BarChart) getSameChart(GraficoBarras,"", Color.BLACK,Color.WHITE,2000);
        GraficoBarras.setDrawGridBackground(true); // grid en la grafica
        GraficoBarras.setData(getBarData()); //
        GraficoBarras.invalidate();
        ejeX(GraficoBarras.getXAxis());
        leyendaDerecha(GraficoBarras.getAxisRight());




    }// crear grafica

    // meter datos a la grafica
    private DataSet getData(DataSet dataSet){
        dataSet.setColors(colores);
        dataSet.setValueTextSize(10);
        dataSet.setValueTextColor(Color.WHITE);
        return dataSet;
    }
    private BarData getBarData(){
        BarDataSet barDataSet=(BarDataSet) getData(new BarDataSet(ingresarDatos(),""));
        barDataSet.setColors(colores);
        BarData barData=new BarData(barDataSet);
        barData.setBarWidth(0.45f);
        return barData;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}