package com.example.profolio.modelfragment;

public class PrestasiModel {
    private String namaPrestasi, jabatanPrestasi, deskripsiPrestasi, tahunPrestasi, sertifikatPrestasi;
    private String key;

    public PrestasiModel() {

    }

    public PrestasiModel(String namaPrestasi, String jabatanPrestasi, String deskripsiPrestasi, String tahunPrestasi, String sertifikatPrestasi) {
        this.namaPrestasi = namaPrestasi;
        this.jabatanPrestasi = jabatanPrestasi;
        this.deskripsiPrestasi = deskripsiPrestasi;
        this.tahunPrestasi = tahunPrestasi;
        this.sertifikatPrestasi = sertifikatPrestasi;
    }

    public String getNamaPrestasi() {
        return namaPrestasi;
    }

    public void setNamaPrestasi(String namaPrestasi) {
        this.namaPrestasi = namaPrestasi;
    }

    public String getJabatanPrestasi() {
        return jabatanPrestasi;
    }

    public void setJabatanPrestasi(String jabatanPrestasi) {
        this.jabatanPrestasi = jabatanPrestasi;
    }

    public String getDeskripsiPrestasi() {
        return deskripsiPrestasi;
    }

    public void setDeskripsiPrestasi(String deskripsiPrestasi) {
        this.deskripsiPrestasi = deskripsiPrestasi;
    }

    public String getTahunPrestasi() {
        return tahunPrestasi;
    }

    public void setTahunPrestasi(String tahunPrestasi) {
        this.tahunPrestasi = tahunPrestasi;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSertifikatPrestasi() {
        return sertifikatPrestasi;
    }

    public void setSertifikatPrestasi(String sertifikatPrestasi) {
        this.sertifikatPrestasi = sertifikatPrestasi;
    }
}
