public class DetailPengembalian {
    private int id_detail_pengembalian;
    private int id_kembali;
    private int id_buku;
    private String kondisi_buku;

    // Constructor
    public DetailPengembalian() {}

    public DetailPengembalian(int id_detail_pengembalian, int id_kembali, int id_buku, String kondisi_buku) {
        this.id_detail_pengembalian = id_detail_pengembalian;
        this.id_kembali = id_kembali;
        this.id_buku = id_buku;
        this.kondisi_buku = kondisi_buku;
    }

    // Getters and Setters
    public int getId_detail_pengembalian() { return id_detail_pengembalian; }
    public void setId_detail_pengembalian(int id_detail_pengembalian) { this.id_detail_pengembalian = id_detail_pengembalian; }

    public int getId_kembali() { return id_kembali; }
    public void setId_kembali(int id_kembali) { this.id_kembali = id_kembali; }

    public int getId_buku() { return id_buku; }
    public void setId_buku(int id_buku) { this.id_buku = id_buku; }

    public String getKondisi_buku() { return kondisi_buku; }
    public void setKondisi_buku(String kondisi_buku) { this.kondisi_buku = kondisi_buku; }
}