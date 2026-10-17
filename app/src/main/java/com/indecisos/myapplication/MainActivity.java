package com.indecisos.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.indecisos.myapplication.databinding.ActivityMainBinding;
import com.indecisos.myapplication.ui.CategoriasFragment;
import com.indecisos.myapplication.ui.EstadisticasFragment;
import com.indecisos.myapplication.ui.GastosFragment;
import com.indecisos.myapplication.ui.MetasFragment;
import com.indecisos.myapplication.ui.dashboard.DashboardFragment;
import com.indecisos.myapplication.ui.home.HomeFragment;
import com.indecisos.myapplication.ui.notifications.NotificationsFragment;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    BottomNavigationView navView;

    private static Context context;

    //Login - Firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();
        mostrarFragmento(new GastosFragment());
        /*binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());*/

        mAuth = FirebaseAuth.getInstance();

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                /*if(item.getItemId() == R.id.navigation_home){
                    mostrarFragmento(new HomeFragment());
                }*/
                if(item.getItemId() == R.id.navigation_est){
                    mostrarFragmento(new EstadisticasFragment());
                }
                if(item.getItemId() == R.id.navigation_gastos){
                    mostrarFragmento(new GastosFragment());
                }
                if(item.getItemId() == R.id.navigation_metas){
                    mostrarFragmento(new MetasFragment());
                }
                if(item.getItemId() == R.id.navigation_categorias){
                    mostrarFragmento(new CategoriasFragment());
                }
                return true;
            }
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);*/
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opciones_main_menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cerrarSesion:
                mAuth.signOut();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(i);
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }


    private void mostrarFragmento(Fragment fragmento){
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragments, fragmento)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

    }

    public static Context getAppContext(){
        return MainActivity.context;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser sesion = mAuth.getCurrentUser();
        if(sesion == null){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

}