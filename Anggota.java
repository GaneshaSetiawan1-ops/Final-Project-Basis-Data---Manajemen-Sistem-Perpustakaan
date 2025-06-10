public class Anggota {
    private int id_user;
    private String nama;
    private String npm;
    private String prodi;
    private String email;
    private String no_hp;

    // Constructor
    public Anggota() {}

    public Anggota(int id_user, String nama, String npm, String prodi, String email, String no_hp) {
        this.id_user = id_user;
        this.nama = nama;
        this.npm = npm;
        this.prodi = prodi;
        this.email = email;
        this.no_hp = no_hp;
    }

    // Getters and Setters
    public int getId_user() { return id_user; }
    public void setId_user(int id_user) { this.id_user = id_user; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getNpm() { return npm; }
    public void setNpm(String npm) { this.npm = npm; }

    public String getProdi() { return prodi; }
    public void setProdi(String prodi) { this.prodi = prodi; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNo_hp() { return no_hp; }
    public void setNo_hp(String no_hp) { this.no_hp = no_hp; }
}