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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class Adaptador extends ArrayAdapter<Plato>
{
    private Context micontexto = null;
    private ArrayList<Plato> miArray;
    private LayoutInflater inflater = null;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference;


    static class ViewHolder
    {
        TextView tvNombrePlato;
        TextView tvPrecio;
        ImageView ivPlato;
    }


    public Adaptador(Context micontexto, ArrayList<Plato> miArray)
    {
        super(micontexto, R.layout.listview_item, miArray);
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
            view= inflater.inflate(R.layout.listview_item, null);
            holder = new ViewHolder();
            holder.tvNombrePlato = view.findViewById(R.id.tvNombrePlato);
            holder.tvPrecio = view.findViewById(R.id.tvPvp);
            holder.ivPlato = view.findViewById(R.id.ivPlato);
            view.setTag(holder);
        }else
            holder = (ViewHolder) view.getTag();

            Plato plato = (Plato) getItem(i);
            holder.tvNombrePlato.setText(miArray.get(i).getNombre());
            holder.tvPrecio.setText(String.valueOf(miArray.get(i).getPrecio())+"€");
            if(!miArray.get(i).getImagen().isEmpty()){
                storageReference = storage.getReferenceFromUrl("gs://bocailoapp.appspot.com/ImagenesPlatos").child(miArray.get(i).getImagen());
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        Picasso.with(micontexto).load(uri.toString()).into(holder.ivPlato);
                    }
                });

            }


        return view;
    }
}
