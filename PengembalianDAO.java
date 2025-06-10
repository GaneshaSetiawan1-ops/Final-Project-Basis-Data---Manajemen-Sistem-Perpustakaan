import java.sql.*;
import java.time.LocalDate;

public class PengembalianDAO {

    // 1. Proses pengembalian buku
    public boolean kembalikanBuku(int id_pinjam) throws SQLException {
        Connection conn = null;
        PreparedStatement stmtPengembalian = null;
        PreparedStatement stmtDetail = null;
        PreparedStatement stmtUpdateStok = null;
        PreparedStatement stmtUpdatePinjam = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Mulai transaksi

            // Cek apakah peminjaman ada dan statusnya "Dipinjam"
            String checkSQL = "SELECT * FROM pinjam WHERE id_pinjam = ? AND status = 'Dipinjam'";
            PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
            checkStmt.setInt(1, id_pinjam);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Peminjaman tidak ditemukan atau sudah dikembalikan.");
                return false;
            }

            // 1. Tambahkan ke tabel pengembalian
            String sqlPengembalian = "INSERT INTO pengembalian(id_pinjam, tanggal_kembali, status) VALUES (?, ?, ?)";
            stmtPengembalian = conn.prepareStatement(sqlPengembalian, Statement.RETURN_GENERATED_KEYS);
            stmtPengembalian.setInt(1, id_pinjam);
            stmtPengembalian.setDate(2, Date.valueOf(LocalDate.now()));
            stmtPengembalian.setString(3, "Dikembalikan");
            stmtPengembalian.executeUpdate();

            int id_kembali = -1;
            ResultSet generatedKeys = stmtPengembalian.getGeneratedKeys();
            if (generatedKeys.next()) {
                id_kembali = generatedKeys.getInt(1);
            }

            // 2. Ambil detail peminjaman dari detail_pinjam
            String getDetailSQL = "SELECT id_buku, jumlah FROM detail_pinjam WHERE id_pinjam = ?";
            PreparedStatement getDetailStmt = conn.prepareStatement(getDetailSQL);
            getDetailStmt.setInt(1, id_pinjam);
            ResultSet detailRS = getDetailStmt.executeQuery();

            while (detailRS.next()) {
                int id_buku = detailRS.getInt("id_buku");
                int jumlah = detailRS.getInt("jumlah");

                // 3. Tambahkan entri ke detail_pengembalian
                String sqlDetailPengembalian = "INSERT INTO detail_pengembalian(id_kembali, id_buku, kondisi_buku) VALUES (?, ?, ?)";
                stmtDetail = conn.prepareStatement(sqlDetailPengembalian);
                stmtDetail.setInt(1, id_kembali);
                stmtDetail.setInt(2, id_buku);
                stmtDetail.setString(3, "Baik"); // Default kondisi
                stmtDetail.executeUpdate();

                // 4. Update stok buku
                String updateStockSQL = "UPDATE buku SET jumlah_stok = jumlah_stok + ? WHERE id_buku = ?";
                stmtUpdateStok = conn.prepareStatement(updateStockSQL);
                stmtUpdateStok.setInt(1, jumlah);
                stmtUpdateStok.setInt(2, id_buku);
                stmtUpdateStok.executeUpdate();
            }

            // 5. Update status peminjaman menjadi "Dikembalikan"
            String updatePinjamSQL = "UPDATE pinjam SET status = ? WHERE id_pinjam = ?";
            stmtUpdatePinjam = conn.prepareStatement(updatePinjamSQL);
            stmtUpdatePinjam.setString(1, "Dikembalikan");
            stmtUpdatePinjam.setInt(2, id_pinjam);
            stmtUpdatePinjam.executeUpdate();

            conn.commit(); // Commit transaksi
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Rollback jika terjadi error
            }
            e.printStackTrace();
            return false;
        } finally {
            // Tutup semua resource
            if (stmtPengembalian != null) stmtPengembalian.close();
            if (stmtDetail != null) stmtDetail.close();
            if (stmtUpdateStok != null) stmtUpdateStok.close();
            if (stmtUpdatePinjam != null) stmtUpdatePinjam.close();
            if (conn != null) conn.setAutoCommit(true);
            DBConnection.closeConnection();
        }
    }

    // 2. Hitung denda berdasarkan hari keterlambatan (misalnya 3 hari tenggang waktu)
    public double hitungDenda(int id_pinjam) throws SQLException {
        String sql = "SELECT DATEDIFF(CURDATE(), DATE_ADD(tanggal_pinjam, INTERVAL 7 DAY)) AS keterlambatan " +
                     "FROM pinjam WHERE id_pinjam = ? AND status = 'Dikembalikan'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_pinjam);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int hariTerlambat = Math.max(0, rs.getInt("keterlambat"));
                double denda = hariTerlambat * 500; // Rp 500/hari
                return denda;
            }
        }
        return 0;
    }
}