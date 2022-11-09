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

    //context para poder pasar el contexto que es en que activity estas
    private Context context;

    //Lista de objetos tipo producto
    private ArrayList<Producto> objects;

    //resource para guardar el numero del layout
    private int resource;

    //CONSTRUCTOR
    public ProductosAdapter(Context context, ArrayList<Producto> objects, int resource) {
        this.context = context;
        this.objects = objects;
        this.resource = resource;
    }

    //Creamos un objeto de tipo view para poder guardar los objetos creados
    @NonNull
    @Override
    public ProductoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Objeto de tipo view al cual le pones los parametros de width (Match parent) & height (Wrap content)
        View productoView = LayoutInflater.from(context).inflate(resource,null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        productoView.setLayoutParams(layoutParams);
        return new ProductoVH(productoView);
    }

    //Se crea el producto gracias el view Holder y le pasas la poscion de la lista en la que se va a añadir el prodcuto
    @Override
    public void onBindViewHolder(@NonNull ProductoVH holder, int position) {
        Producto producto = objects.get(position);

        //En los objetos tipo vista se les establece los valores
        holder.lblNombre.setText(producto.getNombre());
        holder.lblCantidad.setText(String.valueOf(producto.getCantidad()));
        holder.lblPrecio.setText(String.valueOf(producto.getPrecio()));

        //En caso de que el usuario pulse en el prodcuto de la lista se pueda modificar un producto
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Funcion la cual a partir de pasarle un producto y la posicion del priducto en la lista
                //Se abre un AlertDialog que te permite modificar un producto
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

    @Override
    public int getItemCount() {
        return objects.size();
    }

    private AlertDialog confirmaEliminar(Producto producto, int position) {

        //Creo un AlertDialog.Builder y le pasas el contexto
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //Le pongo un titulo al AlertDialog y le pongo para que en caso de que se pulse la pantalla no desaparezca
        builder.setTitle("Eliminar");
        builder.setCancelable(false);

        //Creo un elemento de tipo vista y le pongo el texto, tamaño y color,
        TextView mensaje = new TextView(context);
        mensaje.setText("¿Estas seguro de eliminar este producto?");
        mensaje.setTextSize(26);
        mensaje.setTextColor(Color.RED);

        //Creo el boton de CANCELAR del AlertDialog
        builder.setNegativeButton("CANCELAR",null);

        //Creo el boton de ELIMINAR del AlertDialog
        builder.setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Creo una variable MainActivity para poder llamar a la funcion publico previamente creada en el main
                MainActivity main = (MainActivity) context;

                //elimino un producto de la lista
                objects.remove(producto);

                //Calcula el total de todos los productos
                main.calcularTotalProductos();

                //Calcula el total de todos los precios
                main.calcularTotalPrecio();

                //Notificar que un elemento se ha cambiado a partir de la posicion del producto de la lista
                notifyItemRemoved(position);
            }
        });
        return builder.create();
    }

    private AlertDialog modificarProducto(Producto producto, int position) {

        //Creo un AlertDialog.Builder y le pasas el contexto
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //Le pongo un titulo al AlertDialog y le pongo para que en caso de que se pulse la pantalla no desaparezca
        builder.setTitle("Modificar Producto");
        builder.setCancelable(false);

        //Creo un objeto de tipo view
        View productoView = LayoutInflater.from(context).inflate(R.layout.activity_crear_producto,null);

        //Creo los objetos de tipo vista y hago los findviewById
        EditText txtNombre = productoView.findViewById(R.id.txtNombreCrearProducto);
        EditText txtCantidad  = productoView.findViewById(R.id.txtCantidadCrearProducto);
        EditText txtPrecio = productoView.findViewById(R.id.txtPrecioCrearProducto);
        TextView lblTotal = productoView.findViewById(R.id.lblTotalCrearProducto);
        Button btnCrear = productoView.findViewById(R.id.btnCrearCrearProducto);
        Button btnCancelar = productoView.findViewById(R.id.btnCancelarCrearProducto);

        //Le añado al builder el objeto de tipo view
        builder.setView(productoView);

        //En este caso al reutilizar el XML de crear un producto
        //Me toca ocultar los campos que no necesito que se muestren el el AlertDialog
        txtNombre.setVisibility(View.GONE);
        btnCrear.setVisibility(View.GONE);
        btnCancelar.setVisibility(View.GONE);

        //Cojo la cantidad del producto y se la pongo al txt para que se pueda modificar
        txtCantidad.setText(String.valueOf(producto.getCantidad()));

        //Cojo el precio del producto y se la pongo al txt para que se pueda modificar
        txtPrecio.setText(String.valueOf(producto.getPrecio()));

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
                try{

                    //c guarda el resultado de txtCantidad casteado a int
                    int c = Integer.parseInt(txtCantidad.getText().toString());

                    //p guarda el resultadod de txtPrecio casteado a float
                    float p = Float.parseFloat(txtPrecio.getText().toString());

                    //Creo un NumberFormat para que al numero que se ponga se le ponga la moneda dependiendo de la config del móvil
                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

                    //Al label del total del precio le establezco el texto que en este caso es el precio con valor de moneda
                    lblTotal.setText(numberFormat.format(c*p));

                }catch (NumberFormatException ignored){}

            }
        };

        //Por último al txtCantidad y al Precio le pasamos el textWatcher
        txtCantidad.addTextChangedListener(textWatcher);
        txtPrecio.addTextChangedListener(textWatcher);

        //Creo el boton de CANCELAR del AlertDialog
        builder.setNegativeButton("CANCELAR",null);

        //Creo el boton de MODIFICAR del AlertDialog
        builder.setPositiveButton("MODIFICAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //En caso de que los campos esten rellenados se le ponga al producto que se va a modificar
                //La cantidad y el precio de los txt respectivos
                if (!txtCantidad.getText().toString().isEmpty() &&
                        !txtPrecio.getText().toString().isEmpty()){

                    //Creo una variable MainActivity para poder llamar a la funcion publico previamente creada en el main
                    MainActivity main = (MainActivity) context;
                    producto.setCantidad(Integer.parseInt(txtCantidad.getText().toString()));
                    producto.setPrecio(Float.parseFloat(txtPrecio.getText().toString()));
                    producto.calcularTotal();

                    //Calcula el total de todos los precios
                    main.calcularTotalPrecio();

                    //Notificar que un elemento se ha cambiado a partir de la posicion del producto de la lista
                    notifyItemChanged(position);
                }
            }
        });

        //Se crea el AlertDialog
        return builder.create();
    }

    //Clase en la cual creo los objeto de tipo vista y hago los findViewById del layout Model View
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
