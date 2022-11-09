package com.example.ejercicio2repasoexamen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.ejercicio2repasoexamen.databinding.ActivityCrearProductoBinding;
import com.example.ejercicio2repasoexamen.modelos.Producto;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class CrearProductoActivity extends AppCompatActivity {

    //Creamos la variable binding para que sea mas facil hacer los findViewById
    private ActivityCrearProductoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_producto);

        //Inicializamos el binding
        binding = ActivityCrearProductoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Boton cancelar que devuelve el resultado cancelado
        binding.btnCancelarCrearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        //Funcion que calcula el total para mostrarlo en un label a mode de informacion para el cliente
        calcularTotal();

        //Boton crear que devuelve al contenedor un producto
        binding.btnCrearCrearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Producto producto;

                if ((producto = crearProducto()) != null){

                    //Creas el intent y el bundle
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();

                    //en el bundle le pasas la KEY y el producto
                    bundle.putSerializable("PRODUCTO",producto);

                    //En el intent le pones en el putExtras el bundle
                    intent.putExtras(bundle);

                    //Devuelves el resultado a OK y le pasas el intent
                    setResult(RESULT_OK,intent);
                    finish();
                }else{
                    Toast.makeText(CrearProductoActivity.this, "Error al crear un producto", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void calcularTotal() {
        //Creamos un textWatcher para que en caso de que la cantidad y el precio se modifiquen aparezca el total en el label
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {

                    //c guarda el resultado de txtCantidad casteado a int
                    int c = Integer.parseInt(binding.txtCantidadCrearProducto.getText().toString());

                    //p guarda el resultadod de txtPrecio casteado a float
                    float p = Float.parseFloat(binding.txtPrecioCrearProducto.getText().toString());

                    //Creo un NumberFormat para que al numero que se ponga se le ponga la moneda dependiendo de la config del móvil
                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

                    //Al label del total de precios le establezco el texto que en este caso es el precio con valor de moneda
                    binding.lblTotalCrearProducto.setText(numberFormat.format(p*c));
                }catch (NumberFormatException ignored){}
            }
        };

        //Por último al txtCantidad y al Precio le pasamos el textWatcher
        binding.txtCantidadCrearProducto.addTextChangedListener(textWatcher);
        binding.txtPrecioCrearProducto.addTextChangedListener(textWatcher);
    }

    private Producto crearProducto() {

        //Inicializo el producto
        Producto producto;

        //Condicion para que en caso de que todos los campos esten rellenados se cree un producto
        if (binding.txtNombreCrearProducto.getText().toString().isEmpty() &&
            binding.txtCantidadCrearProducto.getText().toString().isEmpty() &&
            binding.txtPrecioCrearProducto.getText().toString().isEmpty()){
            Toast.makeText(this, "tienes que rellenar los datos", Toast.LENGTH_SHORT).show();
            return null;
        }else{
            String n;
            int c;
            float p;

            //Cogemos el resultado de txtNombre
            n = binding.txtNombreCrearProducto.getText().toString();

            //Cogemos el resultado de txtCantidad
            c = Integer.parseInt(binding.txtCantidadCrearProducto.getText().toString());

            //Cogemos el resultado de txtPrecio
            p = Float.parseFloat(binding.txtPrecioCrearProducto.getText().toString());

            //Creamos un producto
            producto = new Producto(n,p,c);

            //Devolvemos un producto
            return producto;
        }
    }
}