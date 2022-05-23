package com.example.bocailoapp.FragmentsCompartidos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bocailoapp.Activities.DetallesActivity;
import com.example.bocailoapp.Clases.Plato;
import com.example.bocailoapp.R;
import com.example.bocailoapp.Utils.Adaptador;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Sandwiches extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Adaptador adaptador;

    ListView list;
    ArrayList<Plato> platos = new ArrayList<>();

    public Sandwiches() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_entrantes, container, false);

        list = view.findViewById(R.id.idListViewEntrantes);
        platos = new ArrayList<Plato>();


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference("plato");

        adaptador= new Adaptador(getActivity(), platos);
        list.setAdapter(adaptador);

        databaseReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                if(snapshot.getValue(Plato.class).getTipo().equals("sandwiches")){
                    Plato plato = snapshot.getValue(Plato.class);
                    System.out.println(plato);
                    //plato.setNombre();
                    //plato.setDescripcion(snapshot.child("descripcion").toString());
                    //plato.setPrecio(snapshot.child("precio").toString());
                    platos.add(plato);
                    adaptador.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)
            {
                platos.remove(snapshot.getValue(Plato.class));
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {

                //Fragment fragment = new fragmentDetalle();
                Intent intentDetalle = new Intent(getActivity(), DetallesActivity.class);
                //Bundle args = new Bundle();
                intentDetalle.putExtra("nombre", platos.get(i).getNombre().toString());
                intentDetalle.putExtra("descripcion", platos.get(i).getDescripcion().toString());
                intentDetalle.putExtra("imagen", platos.get(i).getImagen().toString());
                intentDetalle.putExtra("precio", platos.get(i).getPrecio());
                intentDetalle.putExtra("id", platos.get(i).getId());
                intentDetalle.putExtra("tipo", platos.get(i).getTipo());

                startActivity(intentDetalle);

                /*FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.pagercliente, fragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();*/
            }
        });
    }
}