package com.example.ejercicio2repasoexamen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.ejercicio2repasoexamen.adapters.ProductosAdapter;
import com.example.ejercicio2repasoexamen.modelos.Producto;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.View;


import com.example.ejercicio2repasoexamen.databinding.ActivityMainBinding;


import java.text.NumberFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    private ActivityResultLauncher<Intent> crearProducto;
    private ArrayList<Producto> listaDeProductos;
    private ProductosAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        int columnas;
        columnas = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 1 : 2;

        listaDeProductos = new ArrayList<>();
        adapter = new ProductosAdapter(this,listaDeProductos, R.layout.producto_model_view);
        layoutManager = new GridLayoutManager(this,columnas);
        inicializaLaunchers();
        binding.contentMain.contenedor.setAdapter(adapter);
        binding.contentMain.contenedor.setLayoutManager(layoutManager);

        iniciarPrecioYProductos();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearProducto.launch(new Intent(MainActivity.this, CrearProductoActivity.class));
            }
        });
    }

    private void iniciarPrecioYProductos() {
        binding.contentMain.lblCantidadProductos.setText("Hay 0 productos");

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        binding.contentMain.lblTotalPrecio.setText(numberFormat.format(0));
    }

    private void inicializaLaunchers() {
        crearProducto = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK){
                    if (result.getData() != null){
                        Producto producto = (Producto) result.getData().getExtras().getSerializable("PRODUCTO");
                        listaDeProductos.add(producto);
                        calcularTotalProductos();
                        calcularTotalPrecio();
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void calcularTotalPrecio() {
        float total;
        float acumulador = 0;
        Producto producto;
        for (int i = 0; i < listaDeProductos.size(); i++) {
            producto = listaDeProductos.get(i);
            total = producto.getTotal();
            acumulador += total;
        }
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        binding.contentMain.lblTotalPrecio.setText(numberFormat.format(acumulador));
    }

    @SuppressLint("SetTextI18n")
    public void calcularTotalProductos(){
        int numeroDeProductos = listaDeProductos.size();
        binding.contentMain.lblCantidadProductos.setText("Hay " + numeroDeProductos + " productos");
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("PRODUCTOS", listaDeProductos);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<Producto> temporal = (ArrayList<Producto>) savedInstanceState.getSerializable("PRODUCTOS");
        listaDeProductos.addAll(temporal);
        calcularTotalProductos();
        adapter.notifyItemRangeChanged(0, temporal.size());
    }
}