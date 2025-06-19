public class Denda {
    private int id_denda;
    private int id_kembali;
    private double jumlah;
    private String status_pembayaran;

    // Constructor
    public Denda() {}

    public Denda(int id_denda, int id_kembali, double jumlah, String status_pembayaran) {
        this.id_denda = id_denda;
        this.id_kembali = id_kembali;
        this.jumlah = jumlah;
        this.status_pembayaran = status_pembayaran;
    }

    // Getters and Setters
    public int getId_denda() {
        return id_denda;
    }

    public void setId_denda(int id_denda) {
        this.id_denda = id_denda;
    }

    public int getId_kembali() {
        return id_kembali;
    }

    public void setId_kembali(int id_kembali) {
        this.id_kembali = id_kembali;
    }

    public double getJumlah() {
        return jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public String getStatus_pembayaran() {
        return status_pembayaran;
    }

    public void setStatus_pembayaran(String status_pembayaran) {
        this.status_pembayaran = status_pembayaran;
    }
}