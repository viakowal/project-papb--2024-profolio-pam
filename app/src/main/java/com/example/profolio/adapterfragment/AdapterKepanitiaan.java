package com.example.profolio.adapterfragment;

import android.annotation.SuppressLint;
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

import com.example.profolio.modelfragment.KepanitiaanModel;
import com.example.profolio.R;
import com.example.profolio.edit.EditKepanitiaanActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterKepanitiaan extends RecyclerView.Adapter<AdapterKepanitiaan.KepanitiaanViewHolder> {
    private List<KepanitiaanModel> kepanitiaanItems;
    private Context context;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public AdapterKepanitiaan(List<KepanitiaanModel> kepanitiaanItems, Context context) {
        this.kepanitiaanItems = kepanitiaanItems;
        this.context = context;
    }
    @NonNull
    @Override
    public AdapterKepanitiaan.KepanitiaanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View kepanitiaanView = inflater.inflate(R.layout.kepanitiaan_cardview_item, parent, false);
        return new KepanitiaanViewHolder(kepanitiaanView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterKepanitiaan.KepanitiaanViewHolder holder, @SuppressLint("RecyclerView") int position) {
        KepanitiaanModel kepanitiaanData = kepanitiaanItems.get(position);
        holder.tvTitleKepanitiaan.setText(kepanitiaanData.getNamaKepanitiaan());
        holder.tvJabatanKepanitiaan.setText(kepanitiaanData.getJabatanKepanitiaan());
        holder.tvDeskripsiKepanitiaan.setText(kepanitiaanData.getDeskripsiKepanitiaan());
        holder.tvPeriodeKepanitiaan.setText(kepanitiaanData.getTahunKepanitiaan());

        holder.itemView.setAlpha(0f);
        holder.itemView.animate()
                .alpha(1f)
                .setDuration(300)
                .setStartDelay(300 * position)
                .start();

        holder.btn_edit_kepanitiaan.setOnClickListener(v -> {
            Intent editForm = new Intent(context, EditKepanitiaanActivity.class);
            editForm.putExtra("key", kepanitiaanData.getKey());
            editForm.putExtra("nama", kepanitiaanData.getNamaKepanitiaan());
            editForm.putExtra("deskripsi", kepanitiaanData.getDeskripsiKepanitiaan());
            editForm.putExtra("jabatan", kepanitiaanData.getJabatanKepanitiaan());
            editForm.putExtra("tahun", kepanitiaanData.getTahunKepanitiaan());
            editForm.putExtra("sertifikat", kepanitiaanData.getSertifKepanitiaan());

            context.startActivity(editForm);
        });

        holder.btn_delete_kepanitiaan.setOnClickListener(v -> {
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
                    database.child("Users").child(userId).child("Kepanitiaan").child(kepanitiaanData.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Delete data succesfully", Toast.LENGTH_SHORT).show();
                            kepanitiaanItems.remove(position);
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
        return kepanitiaanItems.size();
    }

    public class KepanitiaanViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitleKepanitiaan, tvJabatanKepanitiaan, tvPeriodeKepanitiaan, tvDeskripsiKepanitiaan;
        FloatingActionButton btn_delete_kepanitiaan, btn_edit_kepanitiaan;
        public KepanitiaanViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitleKepanitiaan = itemView.findViewById(R.id.tvTitleKepanitiaan);
            tvJabatanKepanitiaan = itemView.findViewById(R.id.tvJabatanKepanitiaan);
            tvPeriodeKepanitiaan = itemView.findViewById(R.id.tvPeriodeKepanitiaan);
            tvDeskripsiKepanitiaan = itemView.findViewById(R.id.tvDeskripsiKepanitiaan);

            btn_delete_kepanitiaan = itemView.findViewById(R.id.btn_delete_kepanitiaan);
            btn_edit_kepanitiaan = itemView.findViewById(R.id.btn_edit_kepanitiaan);
        }
    }
}
