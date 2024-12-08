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

import com.example.profolio.modelfragment.OrganisasiModel;
import com.example.profolio.R;
import com.example.profolio.edit.EditOrganisasiActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.List;

public class AdapterOrganisasi extends RecyclerView.Adapter<AdapterOrganisasi.OrganisasiViewHolder> {
    private List<OrganisasiModel> organisasiItems;
    private Context context;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public AdapterOrganisasi(List<OrganisasiModel> organisasiItems, Context context) {
        this.organisasiItems = organisasiItems;
        this.context = context;
    }
    @NonNull
    @Override
    public AdapterOrganisasi.OrganisasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View organisasiView =inflater.inflate(R.layout.organisasi_cardview_item, parent, false);
        return new OrganisasiViewHolder(organisasiView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrganisasi.OrganisasiViewHolder holder, @SuppressLint("RecyclerView") int position) {
        OrganisasiModel organisasiData = organisasiItems.get(position);

        holder.tvTitleOrganisasi.setText(organisasiData.getNamaOrganisasi());
        holder.tvJabatanOrganisas.setText(organisasiData.getJabatanOrganisasi());
        holder.tvDeskripsiOrganisasi.setText(organisasiData.getDeskripsiOrganisasi());
        holder.tvPeriodeOrganisasi.setText(organisasiData.getTahunMulaiOrganisasi() + " - " + organisasiData.getTahunSelesaiOrganisasi());


        holder.itemView.setAlpha(0f);
        holder.itemView.animate()
                .alpha(1f)
                .setDuration(300)
                .setStartDelay(300 * position)
                .start();

        holder.btn_edit_organisasi.setOnClickListener(v -> {
            Intent editForm = new Intent(context, EditOrganisasiActivity.class);
            editForm.putExtra("key", organisasiData.getKey());
            editForm.putExtra("nama", organisasiData.getNamaOrganisasi());
            editForm.putExtra("deskripsi", organisasiData.getDeskripsiOrganisasi());
            editForm.putExtra("jabatan", organisasiData.getJabatanOrganisasi());
            editForm.putExtra("tahunMulai", organisasiData.getTahunMulaiOrganisasi());
            editForm.putExtra("tahunSelesai", organisasiData.getTahunSelesaiOrganisasi());
            editForm.putExtra("sertifikat", organisasiData.getSertifOrganisasi());
            context.startActivity(editForm);
        });

        holder.btn_delete_organisasi.setOnClickListener(v -> {
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
                    database.child("Users").child(userId).child("Organisasi").child(organisasiData.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Delete data succesfully", Toast.LENGTH_SHORT).show();
                            organisasiItems.remove(position);
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
        return organisasiItems.size();
    }

    public class OrganisasiViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitleOrganisasi, tvJabatanOrganisas, tvPeriodeOrganisasi, tvDeskripsiOrganisasi;
        FloatingActionButton btn_delete_organisasi, btn_edit_organisasi;
        public OrganisasiViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitleOrganisasi = itemView.findViewById(R.id.tvTitleOrganisasi);
            tvJabatanOrganisas = itemView.findViewById(R.id.tvJabatanOrganisas);
            tvPeriodeOrganisasi = itemView.findViewById(R.id.tvPeriodeOrganisasi);
            tvDeskripsiOrganisasi = itemView.findViewById(R.id.tvDeskripsiOrganisasi);
            btn_delete_organisasi = itemView.findViewById(R.id.btn_delete_organisasi);
            btn_edit_organisasi = itemView.findViewById(R.id.btn_edit_organisasi);
        }
    }
}
