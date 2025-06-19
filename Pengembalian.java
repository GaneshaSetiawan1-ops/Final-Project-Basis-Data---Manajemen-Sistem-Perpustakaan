import java.sql.Date;

public class Pengembalian {
    private int id_kembali;
    private int id_pinjam;
    private Date tanggal_kembali;
    private String status;

    // Constructor
    public Pengembalian() {}

    public Pengembalian(int id_kembali, int id_pinjam, Date tanggal_kembali, String status) {
        this.id_kembali = id_kembali;
        this.id_pinjam = id_pinjam;
        this.tanggal_kembali = tanggal_kembali;
        this.status = status;
    }

    // Getters and Setters
    public int getId_kembali() {
        return id_kembali;
    }

    public void setId_kembali(int id_kembali) {
        this.id_kembali = id_kembali;
    }

    public int getId_pinjam() {
        return id_pinjam;
    }

    public void setId_pinjam(int id_pinjam) {
        this.id_pinjam = id_pinjam;
    }

    public Date getTanggal_kembali() {
        return tanggal_kembali;
    }

    public void setTanggal_kembali(Date tanggal_kembali) {
        this.tanggal_kembali = tanggal_kembali;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}