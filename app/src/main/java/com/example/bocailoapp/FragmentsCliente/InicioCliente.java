package com.example.bocailoapp.FragmentsCliente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bocailoapp.R;


public class InicioCliente extends Fragment {


    public InicioCliente() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inicio_cliente, container, false);

        return view;
    }
}