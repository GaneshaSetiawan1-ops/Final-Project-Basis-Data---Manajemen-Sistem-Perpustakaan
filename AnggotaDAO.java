import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnggotaDAO {
    public List<Anggota> getAllAnggota() {
        List<Anggota> anggotaList = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM user WHERE id_role = 2");

            while (rs.next()) {
                Anggota a = new Anggota();
                a.setId_user(rs.getInt("id_user"));
                a.setNama(rs.getString("nama"));
                a.setNpm(rs.getString("npm"));
                a.setProdi(rs.getString("prodi"));
                a.setEmail(rs.getString("email"));
                a.setNo_hp(rs.getString("no_hp"));
                anggotaList.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return anggotaList;
    }

    public boolean addAnggota(Anggota a) {
        String sql = "INSERT INTO user(nama, npm, prodi, email, no_hp, id_role, password) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, a.getNama());
            pstmt.setString(2, a.getNpm());
            pstmt.setString(3, a.getProdi());
            pstmt.setString(4, a.getEmail());
            pstmt.setString(5, a.getNo_hp());
            pstmt.setInt(6, 2); // role: anggota
            pstmt.setString(7, "default"); // password default
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Mengupdate Anggota
    public boolean updateAnggota(Anggota a) {
        String sql = "UPDATE user SET nama = ?, npm = ?, prodi = ?, email = ?, no_hp = ? WHERE id_user = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, a.getNama());
            pstmt.setString(2, a.getNpm());
            pstmt.setString(3, a.getProdi());
            pstmt.setString(4, a.getEmail());
            pstmt.setString(5, a.getNo_hp());
            pstmt.setInt(6, a.getId_user());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Menghapus anggota
    public boolean deleteAnggota(int id_user) {
        String sql = "DELETE FROM user WHERE id_user=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_user);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Mencari user berdasarkan npm 
    public Anggota searchByNPM(String npm) {
        String sql = "SELECT * FROM user WHERE npm = ? AND id_role = 2";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, npm);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Anggota a = new Anggota();
                a.setId_user(rs.getInt("id_user"));
                a.setNama(rs.getString("nama"));
                a.setNpm(rs.getString("npm"));
                a.setProdi(rs.getString("prodi"));
                a.setEmail(rs.getString("email"));
                a.setNo_hp(rs.getString("no_hp"));
                return a;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}