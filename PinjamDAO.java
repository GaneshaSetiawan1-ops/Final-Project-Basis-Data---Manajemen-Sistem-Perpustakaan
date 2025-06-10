import java.sql.*;
import java.time.LocalDate;

public class PinjamDAO {

    // 1. Method pinjamBuku (sudah ada)
    public boolean pinjamBuku(int id_user, int id_buku, int jumlahPinjam) throws SQLException {
        Connection conn = null;
        PreparedStatement stmtPinjam = null;
        PreparedStatement stmtDetail = null;
        PreparedStatement stmtUpdateStok = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Cek stok buku
            String checkStockSQL = "SELECT jumlah_stok FROM buku WHERE id_buku = ?";
            PreparedStatement checkStockStmt = conn.prepareStatement(checkStockSQL);
            checkStockStmt.setInt(1, id_buku);
            rs = checkStockStmt.executeQuery();

            if (rs.next() && rs.getInt("jumlah_stok") >= jumlahPinjam) {
                // Tambahkan ke tabel pinjam
                String sqlPinjam = "INSERT INTO pinjam(id_user, tanggal_pinjam, status) VALUES (?, ?, ?)";
                stmtPinjam = conn.prepareStatement(sqlPinjam, Statement.RETURN_GENERATED_KEYS);
                stmtPinjam.setInt(1, id_user);
                stmtPinjam.setDate(2, Date.valueOf(LocalDate.now()));
                stmtPinjam.setString(3, "Dipinjam");
                stmtPinjam.executeUpdate();

                // Dapatkan ID pinjam terbaru
                ResultSet generatedKeys = stmtPinjam.getGeneratedKeys();
                int id_pinjam = -1;
                if (generatedKeys.next()) {
                    id_pinjam = generatedKeys.getInt(1);
                }

                // Tambahkan ke detail_pinjam
                String sqlDetail = "INSERT INTO detail_pinjam(id_pinjam, id_buku, jumlah) VALUES (?, ?, ?)";
                stmtDetail = conn.prepareStatement(sqlDetail);
                stmtDetail.setInt(1, id_pinjam);
                stmtDetail.setInt(2, id_buku);
                stmtDetail.setInt(3, jumlahPinjam);
                stmtDetail.executeUpdate();

                // Update stok buku
                String updateStockSQL = "UPDATE buku SET jumlah_stok = jumlah_stok - ? WHERE id_buku = ?";
                stmtUpdateStok = conn.prepareStatement(updateStockSQL);
                stmtUpdateStok.setInt(1, jumlahPinjam);
                stmtUpdateStok.setInt(2, id_buku);
                stmtUpdateStok.executeUpdate();

                conn.commit();
                return true;
            } else {
                System.out.println("Stok tidak mencukupi.");
                return false;
            }
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (rs != null) rs.close();
            if (stmtPinjam != null) stmtPinjam.close();
            if (stmtDetail != null) stmtDetail.close();
            if (stmtUpdateStok != null) stmtUpdateStok.close();
            if (conn != null) conn.setAutoCommit(true);
            DBConnection.closeConnection();
        }
    }

    // 2. Method baru: tampilkanRiwayatPeminjaman()
    public void tampilkanRiwayatPeminjaman() throws SQLException {
        String query = "SELECT p.id_pinjam, u.nama, b.judul, dp.jumlah, p.tanggal_pinjam, p.status " +
                       "FROM pinjam p " +
                       "JOIN user u ON p.id_user = u.id_user " +
                       "JOIN detail_pinjam dp ON p.id_pinjam = dp.id_pinjam " +
                       "JOIN buku b ON dp.id_buku = b.id_buku";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.println("ID Peminjaman: " + rs.getInt("id_pinjam"));
                System.out.println("Nama Anggota: " + rs.getString("nama"));
                System.out.println("Judul Buku: " + rs.getString("judul"));
                System.out.println("Jumlah: " + rs.getInt("jumlah"));
                System.out.println("Tanggal Pinjam: " + rs.getDate("tanggal_pinjam"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Gagal menampilkan riwayat peminjaman.");
            e.printStackTrace();
        }
    }
}