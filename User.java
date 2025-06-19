public class User {
    private int id_user;
    private int id_role; // 1 = Admin, 2 = Anggota
    private String nama;
    private String password;
    private String npm;
    private String prodi;
    private String email;
    private String no_hp;

    // Constructor
    public User() {}

    public User(int id_user, int id_role, String nama, String password, String npm, String prodi, String email, String no_hp) {
        this.id_user = id_user;
        this.id_role = id_role;
        this.nama = nama;
        this.password = password;
        this.npm = npm;
        this.prodi = prodi;
        this.email = email;
        this.no_hp = no_hp;
    }

    // Getters and Setters
    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_role() {
        return id_role;
    }

    public void setId_role(int id_role) {
        this.id_role = id_role;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNpm() {
        return npm;
    }

    public void setNpm(String npm) {
        this.npm = npm;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }
}