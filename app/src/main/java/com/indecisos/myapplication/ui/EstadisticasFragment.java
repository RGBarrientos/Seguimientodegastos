package com.indecisos.myapplication.ui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.indecisos.myapplication.ui.ListEstCategorias;
import com.indecisos.myapplication.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class EstadisticasFragment extends Fragment {
    // Graficos
    private PieChart GraficoPastel;


    private String [] categoriasArr= new String[]{"Transporte","Estudios","Hogar","Facturas","Decoracion"};
    private Float [] gastosArr= new Float[]{520.f,450.f,400.f,500.f,350.f};
    //para las cards
    ArrayList<ListEstCategorias> elements;
    RecyclerView recyclerVest;

    private int []colores=new int[3];
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estadisticas, container, false);
        colores=new int[]{ContextCompat.getColor(getContext(), R.color.color5),ContextCompat.getColor(getContext(), R.color.color3),ContextCompat.getColor(getContext(), R.color.color4)};
        //GraficoBarras=(BarChart) view.findViewById(R.id.graficoBarras);
        GraficoPastel=(PieChart) view.findViewById(R.id.graficoPie);
        Modificarleyenda(GraficoPastel);
        CrearGrafica();
        //recycker view
        recyclerVest=view.findViewById(R.id.listEstRecyclerV);
        recyclerVest.setLayoutManager(new LinearLayoutManager(getContext()));
        elements=new ArrayList<>();
        initList();
        ListAdapterEst adapterEst= new ListAdapterEst(elements,getContext());
        recyclerVest.setAdapter(adapterEst);

        /*Button semanalEstBtn=view.findViewById(R.id.semanalEstBtn);
        semanalEstBtn.setOnClickListener(oyenteSemanaEstBtn);*/ //Button estSemanal

        return view;
    }

    public void initList() {
        elements.add(new ListEstCategorias(categoriasArr[1],gastosArr[1],90,R.drawable.ejemplo ));
        elements.add(new ListEstCategorias(categoriasArr[2],gastosArr[2],80,R.drawable.ejemplo ));
        elements.add(new ListEstCategorias(categoriasArr[3],gastosArr[3],50,R.drawable.ejemplo ));
        elements.add(new ListEstCategorias(categoriasArr[4],gastosArr[4],20,R.drawable.ejemplo ));
        elements.add(new ListEstCategorias(categoriasArr[0],gastosArr[0],10,R.drawable.ejemplo ));
        elements.add(new ListEstCategorias(categoriasArr[1],gastosArr[1],90,R.drawable.ejemplo ));
        elements.add(new ListEstCategorias(categoriasArr[2],gastosArr[2],80,R.drawable.ejemplo ));
        elements.add(new ListEstCategorias(categoriasArr[3],gastosArr[3],50,R.drawable.ejemplo ));

    }

    //button para cambiar de fragment
    private final View.OnClickListener oyenteSemanaEstBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contenedorFragments, new EstSemanalFragment());
            fragmentTransaction.commit();
        }
    };

    // chart o grafica, descripcion color texto color fondo tiempo de animacion
    private Chart getSameChart(Chart chart, String description, int textColor,int backgroung,int animateY){
        chart.getDescription().setText(description);
        chart.getDescription().setTextSize(15);
        chart.getDescription().setTextColor(textColor); //
        chart.setBackgroundColor(backgroung);
        chart.animateY(animateY);
        //Modificarleyenda(chart);
        return chart;
    }

    //DATOS QUE SE MOSTRARAN EN LA LEYENDA DE LOS GRAFICOS
    private void Modificarleyenda(Chart chart){
        Legend leyenda=chart.getLegend();
        leyenda.setEnabled(false);
        /*
        leyenda.setForm(Legend.LegendForm.CIRCLE); //forma de la leyenda
        leyenda.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        // llenar datos
        ArrayList<LegendEntry>entradasLeyenda=new ArrayList<>();
        for (int i = 0; i < categoriasArr.length; i++) {
            LegendEntry entrada=new LegendEntry();
            //colores
            //entrada.formColor=colores[i];
            //texto categorias
            entrada.label=categoriasArr[i];
            //agregar a lista
            entradasLeyenda.add(entrada);
        }
        //para agregar los datos a la leyenda
        leyenda.setCustom(entradasLeyenda); */
    }// modificar leyenda

    //Ingresar Datos a la grafica

    private ArrayList<PieEntry>ingresarDatosGPastel(){
        ArrayList<PieEntry> datosGrafica= new ArrayList<>();
        for (int i = 0; i < gastosArr.length; i++) {
            datosGrafica.add(new PieEntry(gastosArr[i],categoriasArr[i]));
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

        GraficoPastel=(PieChart) getSameChart(GraficoPastel,"", Color.BLACK,Color.WHITE,2000);
        GraficoPastel.setHoleRadius(50); //circulo del medio
        GraficoPastel.setTransparentCircleRadius(20); //radio del circulo
        //GraficoPastel.setDrawHoleEnabled(false);
        GraficoPastel.setData(getPieData());
        GraficoPastel.invalidate();


    }// crear grafica

    // meter datos a la grafica
    private DataSet getData(DataSet dataSet){
        dataSet.setColors(colores);
        dataSet.setValueTextSize(10);
        dataSet.setValueTextColor(Color.WHITE);
        return dataSet;
    }

    private PieData getPieData(){
        PieDataSet pieDataSet=(PieDataSet) getData(new PieDataSet(ingresarDatosGPastel(),""));
        pieDataSet.setSliceSpace(1.5f); //tamaño del espacio de la linea
        pieDataSet.setValueFormatter(new PercentFormatter());
        return new PieData(pieDataSet);// diferente al otro controlar
    }

    

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}