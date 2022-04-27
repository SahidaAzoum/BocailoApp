package com.example.bocailoapp.FragmentsCompartidos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.bocailoapp.Clases.Plato;
import com.example.bocailoapp.R;
import com.example.bocailoapp.Utils.Adaptador;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class Entrantes extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ArrayAdapter<Plato> adaptador;

    private ListView list;
    ArrayList<Plato> platos = new ArrayList<>();

    public Entrantes() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entrantes, container, false);

        list = view.findViewById(R.id.idListViewEntrantes);
        platos = new ArrayList<Plato>();


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference("plato");

        adaptador= new ArrayAdapter<Plato>(getActivity(), android.R.layout.simple_list_item_1, platos);
        list.setAdapter(adaptador);

        databaseReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {

                Plato plato = snapshot.getValue(Plato.class);
                System.out.println(plato);
                //plato.setNombre();
                //plato.setDescripcion(snapshot.child("descripcion").toString());
                //plato.setPrecio(snapshot.child("precio").toString());
                platos.add(plato);
                adaptador.notifyDataSetChanged();
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
}