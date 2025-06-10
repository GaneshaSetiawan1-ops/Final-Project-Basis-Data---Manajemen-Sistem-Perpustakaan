public class Buku {
    private int id_buku;
    private String judul;
    private String pengarang;
    private String penerbit;
    private int tahun_terbit;
    private int jumlah_stok;
    private int id_kategori;

    // Constructor
    public Buku() {}

    public Buku(int id_buku, String judul, String pengarang, String penerbit, int tahun_terbit, int jumlah_stok, int id_kategori) {
        this.id_buku = id_buku;
        this.judul = judul;
        this.pengarang = pengarang;
        this.penerbit = penerbit;
        this.tahun_terbit = tahun_terbit;
        this.jumlah_stok = jumlah_stok;
        this.id_kategori = id_kategori;
    }

    // Getters and Setters
    public int getId_buku() { return id_buku; }
    public void setId_buku(int id_buku) { this.id_buku = id_buku; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getPengarang() { return pengarang; }
    public void setPengarang(String pengarang) { this.pengarang = pengarang; }

    public String getPenerbit() { return penerbit; }
    public void setPenerbit(String penerbit) { this.penerbit = penerbit; }

    public int getTahun_terbit() { return tahun_terbit; }
    public void setTahun_terbit(int tahun_terbit) { this.tahun_terbit = tahun_terbit; }

    public int getJumlah_stok() { return jumlah_stok; }
    public void setJumlah_stok(int jumlah_stok) { this.jumlah_stok = jumlah_stok; }

    public int getId_kategori() { return id_kategori; }
    public void setId_kategori(int id_kategori) { this.id_kategori = id_kategori; }

    @Override
    public String toString() {
        return "ID: " + id_buku + 
            ", Judul: " + judul + 
            ", Pengarang: " + pengarang + 
            ", Penerbit: " + penerbit + 
            ", Tahun: " + tahun_terbit + 
            ", Stok: " + jumlah_stok + 
            ", ID Kategori: " + id_kategori;
    }
}