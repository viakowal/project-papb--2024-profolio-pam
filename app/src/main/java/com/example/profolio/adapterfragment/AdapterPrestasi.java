package com.example.profolio.adapterfragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profolio.modelfragment.PrestasiModel;
import com.example.profolio.R;
import com.example.profolio.edit.EditPrestasiActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterPrestasi extends RecyclerView.Adapter<AdapterPrestasi.PrestasiViewHolder> {
    private List<PrestasiModel> prestasiItems;
    private Context context;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public AdapterPrestasi(List<PrestasiModel> prestasiItems, Context context) {
        this.prestasiItems = prestasiItems;
        this.context = context;
    }
    @NonNull
    @Override
    public AdapterPrestasi.PrestasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View prestasiView = inflater.inflate(R.layout.prestasi_cardview_item, parent, false);
        return new PrestasiViewHolder(prestasiView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPrestasi.PrestasiViewHolder holder, int position) {
        PrestasiModel prestasiData = prestasiItems.get(position);
        holder.tvTitlePrestasi.setText(prestasiData.getNamaPrestasi());
        holder.tvJabatanPrestasi.setText(prestasiData.getJabatanPrestasi());
        holder.tvDeskripsiPrestasi.setText(prestasiData.getDeskripsiPrestasi());
        holder.tvPeriodePrestasi.setText(prestasiData.getTahunPrestasi());

        holder.itemView.setAlpha(0f);
        holder.itemView.animate()
                .alpha(1f)
                .setDuration(300)
                .setStartDelay(300 * position)
                .start();

        holder.btn_edit_prestasi.setOnClickListener(v -> {
            Intent editForm = new Intent(context, EditPrestasiActivity.class);
            editForm.putExtra("key", prestasiData.getKey());
            editForm.putExtra("nama", prestasiData.getNamaPrestasi());
            editForm.putExtra("deskripsi", prestasiData.getDeskripsiPrestasi());
            editForm.putExtra("jabatan", prestasiData.getJabatanPrestasi());
            editForm.putExtra("tahun", prestasiData.getTahunPrestasi());
            editForm.putExtra("sertifikat", prestasiData.getSertifikatPrestasi());

            context.startActivity(editForm);
        });

        holder.btn_delete_prestasi.setOnClickListener(v -> {
            Dialog popUp = new Dialog(context);
            popUp.setContentView(R.layout.popup_1_delete);
            Window window = popUp.getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

            AppCompatButton cancel = popUp.findViewById(R.id.btnDeleteCancel);
            AppCompatButton confirm = popUp.findViewById(R.id.btnDeleteConfirm);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popUp.dismiss();
                }
            });

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    database.child("Users").child(userId).child("Prestasi").child(prestasiData.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Delete data succesfully", Toast.LENGTH_SHORT).show();
                            prestasiItems.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Delete data failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    popUp.dismiss();
                }
            });
            popUp.show();
        });
    }

    @Override
    public int getItemCount() {
        return prestasiItems.size();
    }

    public class PrestasiViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitlePrestasi, tvJabatanPrestasi, tvPeriodePrestasi, tvDeskripsiPrestasi;
        FloatingActionButton btn_delete_prestasi, btn_edit_prestasi;
        public PrestasiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitlePrestasi = itemView.findViewById(R.id.tvTitlePrestasi);
            tvJabatanPrestasi = itemView.findViewById(R.id.tvJabatanPrestasi);
            tvPeriodePrestasi = itemView.findViewById(R.id.tvPeriodePrestasi);
            tvDeskripsiPrestasi = itemView.findViewById(R.id.tvDeskripsiPrestasi);

            btn_delete_prestasi = itemView.findViewById(R.id.btn_delete_prestasi);
            btn_edit_prestasi = itemView.findViewById(R.id.btn_edit_prestasi);
        }
    }
}
