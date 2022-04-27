package com.example.bocailoapp.Utils;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bocailoapp.Clases.Plato;
import com.example.bocailoapp.R;

import java.util.ArrayList;

public class Adaptador extends ArrayAdapter<Plato>
{
    private Context micontexto = null;
    private ArrayList<Plato> miArray;


    public Adaptador(Context micontexto, ArrayList<Plato> miArray)
    {
        super(micontexto, R.layout.listview_item, miArray);
        this.micontexto = micontexto;
        this.miArray = miArray;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        Plato plato = (Plato) getItem(i);

        if(view == null){
            view= LayoutInflater.from(view.getContext()).inflate(R.layout.listview_item, null);
        }

        TextView tvNombrePlato = view.findViewById(R.id.tvNombrePlato);
        TextView tvPrecio =view.findViewById(R.id.tvPvp);
        ImageView ivPlato = view.findViewById(R.id.ivPlato);

        tvNombrePlato.setText(plato.getNombre());
        tvPrecio.setText((int)miArray.get(i).getPrecio());
        ivPlato.setImageURI(Uri.parse(miArray.get(i).getImagen()));

        return view;
    }
}
