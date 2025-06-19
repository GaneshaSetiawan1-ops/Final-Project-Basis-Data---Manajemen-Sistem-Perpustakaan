import java.sql.*;

public class UserDAO {
    public User authenticate(String npm, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            String query = "SELECT * FROM user WHERE npm = ? AND password = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, npm);
            stmt.setString(2, password);

            rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId_user(rs.getInt("id_user"));
                user.setId_role(rs.getInt("id_role"));
                user.setNama(rs.getString("nama"));
                user.setPassword(rs.getString("password"));
                user.setNpm(rs.getString("npm"));
                user.setProdi(rs.getString("prodi"));
                user.setEmail(rs.getString("email"));
                user.setNo_hp(rs.getString("no_hp"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                DBConnection.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}