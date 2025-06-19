import java.sql.Date;

public class Pinjam {
    private int id_pinjam;
    private int id_user;
    private Date tanggal_pinjam;
    private String status;

    // Constructor
    public Pinjam() {}

    public Pinjam(int id_pinjam, int id_user, Date tanggal_pinjam, String status) {
        this.id_pinjam = id_pinjam;
        this.id_user = id_user;
        this.tanggal_pinjam = tanggal_pinjam;
        this.status = status;
    }

    // Getters and Setters
    public int getId_pinjam() {
        return id_pinjam;
    }

    public void setId_pinjam(int id_pinjam) {
        this.id_pinjam = id_pinjam;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public Date getTanggal_pinjam() {
        return tanggal_pinjam;
    }   

    public void setTanggal_pinjam(Date tanggal_pinjam) {
        this.tanggal_pinjam = tanggal_pinjam;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}