package com.example.ejercicio2repasoexamen.adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ejercicio2repasoexamen.MainActivity;
import com.example.ejercicio2repasoexamen.R;
import com.example.ejercicio2repasoexamen.modelos.Producto;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductoVH> {

    private Context context;
    private ArrayList<Producto> objects;
    private int resource;

    public ProductosAdapter(Context context, ArrayList<Producto> objects, int resource) {
        this.context = context;
        this.objects = objects;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ProductoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productoView = LayoutInflater.from(context).inflate(resource,null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        productoView.setLayoutParams(layoutParams);
        return new ProductoVH(productoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoVH holder, int position) {
        Producto producto = objects.get(position);

        holder.lblNombre.setText(producto.getNombre());
        holder.lblCantidad.setText(String.valueOf(producto.getCantidad()));
        holder.lblPrecio.setText(String.valueOf(producto.getPrecio()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarProducto(producto, holder.getAdapterPosition()).show();
            }
        });
        holder.btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmaEliminar(producto, holder.getAdapterPosition()).show();
            }
        });
    }

    private AlertDialog modificarProducto(Producto producto, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Modificar Producto");
        builder.setCancelable(false);

        View productoView = LayoutInflater.from(context).inflate(R.layout.activity_crear_producto,null);
        EditText txtNombre = productoView.findViewById(R.id.txtNombreCrearProducto);
        EditText txtCantidad  = productoView.findViewById(R.id.txtCantidadCrearProducto);
        EditText txtPrecio = productoView.findViewById(R.id.txtPrecioCrearProducto);
        TextView lblTotal = productoView.findViewById(R.id.lblTotalCrearProducto);
        Button btnCrear = productoView.findViewById(R.id.btnCrearCrearProducto);
        Button btnCancelar = productoView.findViewById(R.id.btnCancelarCrearProducto);
        builder.setView(productoView);

        txtNombre.setVisibility(View.GONE);
        btnCrear.setVisibility(View.GONE);
        btnCancelar.setVisibility(View.GONE);
        txtCantidad.setText(String.valueOf(producto.getCantidad()));
        txtPrecio.setText(String.valueOf(producto.getPrecio()));

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try{
                    int c = Integer.parseInt(txtCantidad.getText().toString());
                    float p = Float.parseFloat(txtPrecio.getText().toString());
                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
                    lblTotal.setText(numberFormat.format(c*p));
                }catch (NumberFormatException ignored){}

            }
        };
        txtCantidad.addTextChangedListener(textWatcher);
        txtPrecio.addTextChangedListener(textWatcher);

        builder.setNegativeButton("CANCELAR",null);
        builder.setPositiveButton("MODIFICAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!txtCantidad.getText().toString().isEmpty() &&
                    !txtPrecio.getText().toString().isEmpty()){
                    MainActivity main = (MainActivity) context;
                    producto.setCantidad(Integer.parseInt(txtCantidad.getText().toString()));
                    producto.setPrecio(Float.parseFloat(txtPrecio.getText().toString()));
                    producto.calcularTotal();
                    main.calcularTotalPrecio();
                    notifyItemChanged(position);
                }
            }
        });
        return builder.create();
    }

    private AlertDialog confirmaEliminar(Producto producto, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Eliminar");
        builder.setCancelable(false);

        TextView mensaje = new TextView(context);
        mensaje.setText("¿Estas seguro de eliminar este producto?");
        mensaje.setTextSize(26);
        mensaje.setTextColor(Color.RED);

        builder.setNegativeButton("CANCELAR",null);
        builder.setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity main = (MainActivity) context;
                objects.remove(producto);
                main.calcularTotalProductos();
                main.calcularTotalPrecio();
                notifyItemRemoved(position);
            }
        });
        return builder.create();
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ProductoVH extends RecyclerView.ViewHolder{

        TextView lblNombre;
        TextView lblCantidad;
        TextView lblPrecio;
        Button btnBorrar;

        public ProductoVH(@NonNull View itemView) {
            super(itemView);
            lblNombre = itemView.findViewById(R.id.lblNombreProductoModelView);
            lblCantidad = itemView.findViewById(R.id.lblCantidadProductoModelView);
            lblPrecio = itemView.findViewById(R.id.lblPrecioProductoModelView);
            btnBorrar = itemView.findViewById(R.id.btnBorrarProductoModelView);
        }
    }
}
