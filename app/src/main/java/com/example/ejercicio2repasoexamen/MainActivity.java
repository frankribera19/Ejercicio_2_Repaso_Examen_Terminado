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

    /**
     * VARIABLES GLOBALES
     * DECLARO EL BINDIG PARA PODER PILLAR LOS ID
     * CREO UN LAUNCHER PARA PODER ENVIAR Y DEVOLVER INFORMACIÓN
     * CREO UNA LISTA DE OBJETOS DE TIPO PRODUCTO PARA ALMACENAR PRODUCTOS
     * CREO UNA VARIABLE ADAPTER PARA COGER LA INFO Y PONERLA EN EL RECYCLER
     * CREO UNA VARIABLE LAYOUTMANAGER PODER CREAR LA VISTA EN EL CONTENEDOR (CARD CON INFO DEL PRODUCTO)
     */
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

        //Metodo que inicializa las variables globales
        inicializarListayAdapteryLayout();

        //Metodo para inicializar el launcher
        inicializaLaunchers();

        //Le añado al recycler = contenedor el adapter y el layaout
        binding.contentMain.contenedor.setAdapter(adapter);
        binding.contentMain.contenedor.setLayoutManager(layoutManager);

        //Inicializo el total del precio y de los productos
        iniciarPrecioYProductos();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Uso el launcher para abrir la ACTIVITY de crear productos
                crearProducto.launch(new Intent(MainActivity.this, CrearProductoActivity.class));
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //Creo una lista para uso temporal pasandole el bundle del onSaveInstanceState mientras se hace la transicion de girar el móvil
        ArrayList<Producto> temporal = (ArrayList<Producto>) savedInstanceState.getSerializable("PRODUCTOS");

        //Añado a la lista general la lista de uso temporal
        listaDeProductos.addAll(temporal);

        //Actualizo el total de productos
        calcularTotalProductos();

        //Actualizo el total del precio
        calcularTotalPrecio();

        //Notifico que se ha cambiado el rango de los bjetos dentro de la lista pasandole la posicion
        adapter.notifyItemRangeChanged(0, temporal.size());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        //Guardo en el bundle con la KEY la lista de productos general
        outState.putSerializable("PRODUCTOS", listaDeProductos);
    }

    public void calcularTotalPrecio() {

        //En total guardo el total de un producto usando producto.getTotal();
        float total;

        //En acumulador lo inicializo a 0 y luego voy incrementando el total
        float acumulador = 0;

        //producto me permite hacer listaDeProductos.get(i) para poder coger los datos de producto a producto.
        Producto producto;

        //Bucle para recorrer la lista y poder hacer la logica para ir sumando los totales de los productos
        for (int i = 0; i < listaDeProductos.size(); i++) {

            //Coges un producto
            producto = listaDeProductos.get(i);

            //Gurdas el total
            total = producto.getTotal();

            //Guardas en acumulador la suma del total de todos los productos usando +=
            acumulador += total;
        }
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        binding.contentMain.lblTotalPrecio.setText(numberFormat.format(acumulador));
    }

    @SuppressLint("SetTextI18n")
    public void calcularTotalProductos(){

        //numeroDeProductos guarda listaDeProductos.size() que es el total de objetos que tiene la lista
        int numeroDeProductos = listaDeProductos.size();

        //Al label de total de productos le pongo el texto con el total de productoa que hay.
        binding.contentMain.lblCantidadProductos.setText("Hay " + numeroDeProductos + " productos");
    }

    private void iniciarPrecioYProductos() {
        //Al label del total de productos lo inicializo y le establezco el texto
        binding.contentMain.lblCantidadProductos.setText("Hay 0 productos");

        //Creo un NumberFormat para que al numero que se ponga se le ponga la moneda dependiendo de la config del móvil
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

        //Al label del total de precios lo inicializo y le establezco el texto que en este caso es el precio con valor de moneda
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
                                //Creo un producto pasandole la KEY para saber que viene de la ACTIVITY crear producto
                                Producto producto = (Producto) result.getData().getExtras().getSerializable("PRODUCTO");

                                //Añado a la lista el nuevo producto
                                listaDeProductos.add(producto);

                                //Actualizo el total de productos
                                calcularTotalProductos();

                                //Actualizo el total del precio
                                calcularTotalPrecio();

                                //Notifico que se ha modificado la lista y se ha añadido un objeto mas
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    private void inicializarListayAdapteryLayout() {
        //EN LA VARAIBLE COLUMNAS GUARDAMOS LA ORIENTACION DE LA PANTALLA:
        //PARA COGER LA ORIENTACION USAMOS RESOURCES.CONFIGURATION.ORIENTATION
        //2 TIPOS DE ORIENTACION:
        //PORTRAIT -> Vertical
        //LANDSCAPE -> HORIZONTAL
        int columnas;
        columnas = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 1 : 2;
        listaDeProductos = new ArrayList<>();
        adapter = new ProductosAdapter(this,listaDeProductos, R.layout.producto_model_view);
        layoutManager = new GridLayoutManager(this,columnas);
    }
}