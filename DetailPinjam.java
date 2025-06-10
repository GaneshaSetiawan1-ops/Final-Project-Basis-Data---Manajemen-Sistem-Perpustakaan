public class DetailPinjam {
    private int id_detail_pinjam;
    private int id_pinjam;
    private int id_buku;
    private int jumlah;

    // Constructor
    public DetailPinjam() {}

    public DetailPinjam(int id_detail_pinjam, int id_pinjam, int id_buku, int jumlah) {
        this.id_detail_pinjam = id_detail_pinjam;
        this.id_pinjam = id_pinjam;
        this.id_buku = id_buku;
        this.jumlah = jumlah;
    }

    // Getters and Setters
    public int getId_detail_pinjam() { return id_detail_pinjam; }
    public void setId_detail_pinjam(int id_detail_pinjam) { this.id_detail_pinjam = id_detail_pinjam; }

    public int getId_pinjam() { return id_pinjam; }
    public void setId_pinjam(int id_pinjam) { this.id_pinjam = id_pinjam; }

    public int getId_buku() { return id_buku; }
    public void setId_buku(int id_buku) { this.id_buku = id_buku; }

    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
}