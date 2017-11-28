package com.example.android.capstone_project;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class live extends Fragment {


    public live() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live, container, false);
        Button login = (Button) view.findViewById(R.id.backBtn);

        login.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content, new home_Fragment()).commit();
            }


        });

        return view;

    }
}
