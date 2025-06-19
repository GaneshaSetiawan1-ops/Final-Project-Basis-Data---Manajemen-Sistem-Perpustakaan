import java.sql.*;
import java.time.LocalDate;

public class PinjamDAO {

    // 1. Method pinjamBuku (sudah ada)
   public boolean pinjamBuku(int id_user, int id_buku, int jumlahPinjam) throws SQLException {
    String callSP = "{CALL pinjam_buku(?, ?, ?, ?, ?)}";
    
    try (Connection conn = DBConnection.getConnection();
         CallableStatement cs = conn.prepareCall(callSP)) {
        
        // Set parameter IN
        cs.setInt(1, id_user);
        cs.setInt(2, jumlahPinjam);
        cs.setInt(3, id_buku);
        cs.setDate(4, Date.valueOf(LocalDate.now()));

        // Register parameter OUT
        cs.registerOutParameter(5, Types.VARCHAR);

        // Eksekusi prosedur
        cs.execute();

        // Ambil hasil status dari prosedur
        String hasil = cs.getString(5);
        System.out.println(hasil);

        // Cek apakah berhasil
        return hasil.startsWith("Berhasil");

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    // 2. Menampilkan riwayat peminjaman seluruh user
    public void tampilkanRiwayatPeminjaman() throws SQLException {
        String query = "SELECT * FROM v_riwayat_peminjaman ORDER BY tanggal_pinjam DESC";

        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            if (!rs.isBeforeFirst()) {
                System.out.println("Tidak ada data peminjaman.");
                return;
            }

            System.out.printf("%-5s %-15s %-25s %-7s %-15s %-10s%n", 
                             "ID", "Nama", "Judul Buku", "Jumlah", "Tanggal", "Status");
            System.out.println("-------------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-5d %-15s %-25s %-7d %-15s %-10s%n",
                        rs.getInt("id_pinjam"),
                        rs.getString("nama_anggota"),
                        rs.getString("judul_buku"),
                        rs.getInt("jumlah"),
                        rs.getDate("tanggal_pinjam"),
                        rs.getString("status"));
            }

        } catch (SQLException e) {
            System.out.println("Gagal menampilkan riwayat peminjaman dari VIEW.");
            e.printStackTrace();
        }
    }
}