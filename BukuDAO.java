import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BukuDAO {
    public List<Buku> getAllBuku() {
        String sql = "SELECT * FROM buku";
        List<Buku> list = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Buku b = new Buku();
                b.setId_buku(rs.getInt("id_buku"));
                b.setJudul(rs.getString("judul"));
                b.setPengarang(rs.getString("pengarang"));
                b.setPenerbit(rs.getString("penerbit"));
                b.setTahun_terbit(rs.getInt("tahun_terbit"));
                b.setJumlah_stok(rs.getInt("jumlah_stok"));
                b.setId_kategori(rs.getInt("id_kategori"));
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addBuku(Buku b) {
        String sql = "INSERT INTO buku(judul, pengarang, penerbit, tahun_terbit, jumlah_stok, id_kategori) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, b.getJudul());
            pstmt.setString(2, b.getPengarang());
            pstmt.setString(3, b.getPenerbit());
            pstmt.setInt(4, b.getTahun_terbit());
            pstmt.setInt(5, b.getJumlah_stok());
            pstmt.setInt(6, b.getId_kategori());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateBuku(Buku b) {
        String sql = "UPDATE buku SET judul=?, pengarang=?, penerbit=?, tahun_terbit=?, jumlah_stok=?, id_kategori=? WHERE id_buku=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, b.getJudul());
            pstmt.setString(2, b.getPengarang());
            pstmt.setString(3, b.getPenerbit());
            pstmt.setInt(4, b.getTahun_terbit());
            pstmt.setInt(5, b.getJumlah_stok());
            pstmt.setInt(6, b.getId_kategori());
            pstmt.setInt(7, b.getId_buku());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBuku(int id_buku) {
        String sql = "DELETE FROM buku WHERE id_buku=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_buku);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Buku searchByJudul(String judul) {
        String sql = "SELECT * FROM buku WHERE judul LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + judul + "%");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Buku b = new Buku();
                b.setId_buku(rs.getInt("id_buku"));
                b.setJudul(rs.getString("judul"));
                b.setPengarang(rs.getString("pengarang"));
                b.setPenerbit(rs.getString("penerbit"));
                b.setTahun_terbit(rs.getInt("tahun_terbit"));
                b.setJumlah_stok(rs.getInt("jumlah_stok"));
                b.setId_kategori(rs.getInt("id_kategori"));
                return b;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}