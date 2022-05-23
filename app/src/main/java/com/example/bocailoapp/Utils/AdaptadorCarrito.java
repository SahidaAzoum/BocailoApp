package com.example.bocailoapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bocailoapp.Activities.Carrito;
import com.example.bocailoapp.Activities.DetallesActivity;
import com.example.bocailoapp.Clases.Plato;
import com.example.bocailoapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorCarrito extends ArrayAdapter<Plato>
{

    private Context micontexto = null;
    private ArrayList<Plato> miArray;
    private LayoutInflater inflater = null;

    static class ViewHolder
    {
        TextView tvNombrePlato;
        TextView tvPrecio;
        TextView tvObservaciones;
        ImageView ivModificar;
        ImageView ivPapelera;
    }

    public AdaptadorCarrito(Context micontexto, ArrayList<Plato> miArray)
    {
        super(micontexto, R.layout.listview_itemcarrito, miArray);
        inflater= LayoutInflater.from(micontexto);
        this.miArray = miArray;
        this.micontexto= micontexto;
    }

    @Override
    public int getCount()
    {
        return miArray.size();
    }

    @Override
    public Plato getItem(int i)
    {
        return miArray.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return miArray.indexOf(i);
    }

    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
    }



    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        ViewHolder holder;


        if(view == null){
            view= inflater.inflate(R.layout.listview_itemcarrito, null);
            holder = new ViewHolder();
            holder.tvNombrePlato = view.findViewById(R.id.tvNombrePlatoCarrito);
            holder.tvPrecio = view.findViewById(R.id.tvPvpCarrito);
            holder.tvObservaciones = view.findViewById(R.id.tvObservacionesCarrito);
            holder.ivModificar = view.findViewById(R.id.ivModificar);
            holder.ivPapelera = view.findViewById(R.id.ivPapelera);
            view.setTag(holder);
        }else
            holder = (ViewHolder) view.getTag();

        Plato plato = (Plato) getItem(i);
        holder.tvNombrePlato.setText(miArray.get(i).getNombre());
        holder.tvPrecio.setText(String.valueOf(miArray.get(i).getPrecio())+"€");
        holder.tvObservaciones.setText(miArray.get(i).getObservaciones());

        holder.ivModificar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent intentDetalle = new Intent(micontexto, DetallesActivity.class);
                intentDetalle.putExtra("nombre", miArray.get(i).getNombre().toString());
                intentDetalle.putExtra("descripcion", miArray.get(i).getDescripcion().toString());
                intentDetalle.putExtra("imagen", miArray.get(i).getImagen().toString());
                intentDetalle.putExtra("precio", miArray.get(i).getPrecio());
                intentDetalle.putExtra("id", miArray.get(i).getId());
                intentDetalle.putExtra("tipo", miArray.get(i).getTipo());
                intentDetalle.putExtra("observaciones", miArray.get(i).getObservaciones());
                intentDetalle.putExtra("modificar", true);
                intentDetalle.putExtra("posicion", miArray.get(i));
                intentDetalle.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                micontexto.startActivity(intentDetalle);

            }
        });

        holder.ivPapelera.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                miArray.remove(i);
                //Carrito.calcularPrecio(miArray);
                //Carrito.saveData(miArray, getContext());
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
