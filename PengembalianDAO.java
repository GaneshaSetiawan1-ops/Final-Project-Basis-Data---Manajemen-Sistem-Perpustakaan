import java.sql.*;
import java.time.LocalDate;

public class PengembalianDAO {

    // 1. Memanggil Stored Procedure untuk proses pengembalian buku
    public boolean kembalikanBuku(int id_pinjam) throws SQLException {
        String callSP = "{CALL pengembalian_buku(?, ?, ?)}";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(callSP)) {

            // Set parameter IN
            cs.setInt(1, id_pinjam);
            cs.setDate(2, Date.valueOf(LocalDate.now()));

            // Register parameter OUT
            cs.registerOutParameter(3, Types.VARCHAR);

            // Eksekusi
            cs.execute();

            String hasil = cs.getString(3);
            System.out.println(hasil);

            return hasil.startsWith("Berhasil");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Hitung denda keterlambatan (langsung dari query SQL)
    public double hitungDenda(int id_pinjam) throws SQLException {
        String sql = "SELECT DATEDIFF(CURDATE(), DATE_ADD(tanggal_pinjam, INTERVAL 7 DAY)) AS keterlambatan " +
                     "FROM pinjam WHERE id_pinjam = ? AND status = 'Dikembalikan'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_pinjam);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int keterlambat = Math.max(0, rs.getInt("keterlambatan"));
                return keterlambat * 500; // Denda Rp 500/hari
            }
        }
        return 0;
    }

    public void tampilkanRiwayatPengembalian() throws SQLException {
        String sql = "SELECT * FROM v_riwayat_pengembalian ORDER BY tanggal_kembali DESC";

        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            if (!rs.isBeforeFirst()) {
                System.out.println("Tidak ada riwayat pengembalian.");
                return;
            }

            System.out.printf("%-5s %-5s %-15s %-25s %-12s %-15s%n", 
                              "ID", "Pinj", "Nama", "Judul Buku", "Kondisi", "Tanggal");
            System.out.println("--------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-5d %-5d %-15s %-25s %-12s %-15s%n",
                        rs.getInt("id_kembali"),
                        rs.getInt("id_pinjam"),
                        rs.getString("nama"),
                        rs.getString("judul"),
                        rs.getString("kondisi_buku"),
                        rs.getDate("tanggal_kembali"));
            }

        } catch (SQLException e) {
            System.out.println("Gagal menampilkan riwayat pengembalian dari VIEW.");
            e.printStackTrace();
        }
    }
}
