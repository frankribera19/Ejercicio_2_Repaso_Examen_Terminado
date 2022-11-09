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

    private ActivityCrearProductoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_producto);

        binding = ActivityCrearProductoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnCancelarCrearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        calcularTotal();
        binding.btnCrearCrearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Producto producto = new Producto();

                if ((producto = crearProducto()) != null){
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("PRODUCTO",producto);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    private void calcularTotal() {
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
                    int c = Integer.parseInt(binding.txtCantidadCrearProducto.getText().toString());
                    float p = Float.parseFloat(binding.txtPrecioCrearProducto.getText().toString());
                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
                    binding.lblTotalCrearProducto.setText(numberFormat.format(p*c));
                }catch (NumberFormatException ignored){}
            }
        };
        binding.txtCantidadCrearProducto.addTextChangedListener(textWatcher);
        binding.txtPrecioCrearProducto.addTextChangedListener(textWatcher);
    }

    private Producto crearProducto() {
        Producto producto;
        if (binding.txtNombreCrearProducto.getText().toString().isEmpty() &&
            binding.txtCantidadCrearProducto.getText().toString().isEmpty() &&
            binding.txtPrecioCrearProducto.getText().toString().isEmpty()){
            Toast.makeText(this, "tienes que rellenar los datos", Toast.LENGTH_SHORT).show();
            return null;
        }else{
            String n;
            int c;
            float p;

            n = binding.txtNombreCrearProducto.getText().toString();
            c = Integer.parseInt(binding.txtCantidadCrearProducto.getText().toString());
            p = Float.parseFloat(binding.txtPrecioCrearProducto.getText().toString());

            producto = new Producto(n,p,c);
            return producto;
        }

    }
}