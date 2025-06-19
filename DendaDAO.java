import java.sql.*;

public class DendaDAO {

    // Menyimpan denda ke dalam tabel denda
    public boolean simpanDenda(int id_kembali, double jumlahDenda) throws SQLException {
        String sql = "INSERT INTO denda(id_kembali, jumlah, status_pembayaran) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_kembali);
            pstmt.setDouble(2, jumlahDenda);
            pstmt.setString(3, "Belum Bayar");
            pstmt.executeUpdate();
            return true;
        }
    }

    // Menampilkan semua riwayat denda
    public void tampilkanRiwayatDenda() throws SQLException {
        String query = "SELECT d.id_denda, p.id_pinjam, u.nama, b.judul, d.jumlah, d.status_pembayaran " +
                       "FROM denda d " +
                       "JOIN pengembalian k ON d.id_kembali = k.id_kembali " +
                       "JOIN pinjam p ON k.id_pinjam = p.id_pinjam " +
                       "JOIN user u ON p.id_user = u.id_user " +
                       "JOIN detail_pengembalian dp ON k.id_kembali = dp.id_kembali " +
                       "JOIN buku b ON dp.id_buku = b.id_buku";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.println("ID Denda: " + rs.getInt("id_denda"));
                System.out.println("ID Pinjam: " + rs.getInt("id_pinjam"));
                System.out.println("Nama Anggota: " + rs.getString("nama"));
                System.out.println("Judul Buku: " + rs.getString("judul"));
                System.out.println("Jumlah Denda: Rp " + rs.getDouble("jumlah"));
                System.out.println("Status Pembayaran: " + rs.getString("status_pembayaran"));
                System.out.println("-----------------------------");
            }
        }
    }

    // Mencari denda berdasarkan ID
    public void cariDendaById(int id_denda) throws SQLException {
        String query = "SELECT d.id_denda, p.id_pinjam, u.nama, b.judul, d.jumlah, d.status_pembayaran " +
                       "FROM denda d " +
                       "JOIN pengembalian k ON d.id_kembali = k.id_kembali " +
                       "JOIN pinjam p ON k.id_pinjam = p.id_pinjam " +
                       "JOIN user u ON p.id_user = u.id_user " +
                       "JOIN detail_pengembalian dp ON k.id_kembali = dp.id_kembali " +
                       "JOIN buku b ON dp.id_buku = b.id_buku " +
                       "WHERE d.id_denda = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id_denda);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("ID Denda: " + rs.getInt("id_denda"));
                System.out.println("ID Pinjam: " + rs.getInt("id_pinjam"));
                System.out.println("Nama Anggota: " + rs.getString("nama"));
                System.out.println("Judul Buku: " + rs.getString("judul"));
                System.out.println("Jumlah Denda: Rp " + rs.getDouble("jumlah"));
                System.out.println("Status Pembayaran: " + rs.getString("status_pembayaran"));
            } else {
                System.out.println("Tidak ada denda dengan ID tersebut.");
            }
        }
    }
}