package com.example.profolio.homepage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.profolio.adapterfragment.AdapterPrestasi;
import com.example.profolio.modelfragment.PrestasiModel;
import com.example.profolio.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrestasiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrestasiFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<PrestasiModel> prestasiItems;
    RecyclerView rvPrestasi;
    AdapterPrestasi adapterPrestasi;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PrestasiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrestasiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrestasiFragment newInstance(String param1, String param2) {
        PrestasiFragment fragment = new PrestasiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View prestasiView = inflater.inflate(R.layout.fragment_prestasi, container, false);
        rvPrestasi = prestasiView.findViewById(R.id.rvPrestasi);
        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(getContext());
        rvPrestasi.setLayoutManager(mLayout);
        rvPrestasi.setHasFixedSize(true);
        rvPrestasi.setItemAnimator(new DefaultItemAnimator());

        showData();
        return prestasiView;
    }
    private void showData() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database.child("Users").child(userId).child("Prestasi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prestasiItems = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()) {
                    PrestasiModel prestasi = item.getValue(PrestasiModel.class);
                    prestasi.setKey(item.getKey());
                    prestasiItems.add(prestasi);
                }
                Collections.reverse(prestasiItems);
                adapterPrestasi = new AdapterPrestasi(prestasiItems, getContext());
                rvPrestasi.setAdapter(adapterPrestasi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}