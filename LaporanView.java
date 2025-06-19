import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LaporanView {

   // 1. View: v_laporan_subquery
    public void tampilkanLaporanSubquery() {
        System.out.println("===== Laporan Subquery: Total Buku Dipinjam per Anggota =====");
        String sql = "SELECT * FROM v_laporan_subquery";

        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            if (!rs.isBeforeFirst()) {
                System.out.println("Data tidak ditemukan.\n");
                return;
            }

            while (rs.next()) {
                System.out.println("Nama Anggota         : " + rs.getString("nama"));
                System.out.println("Total Buku Dipinjam  : " + rs.getInt("total_buku_dipinjam"));
                System.out.println("----------------------------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2. View: v_laporan_cte_fallback
    public void tampilkanLaporanCTE() {
        System.out.println("===== Laporan CTE: Peminjaman Terlambat (>7 hari) =====");
        String sql = "SELECT * FROM v_laporan_cte_fallback";

        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            if (!rs.isBeforeFirst()) {
                System.out.println("Tidak ada peminjaman yang terlambat.\n");
                return;
            }

            while (rs.next()) {
                System.out.println("ID Pinjam        : " + rs.getInt("id_pinjam"));
                System.out.println("Nama             : " + rs.getString("nama"));
                System.out.println("Tanggal Pinjam   : " + rs.getDate("tanggal_pinjam"));
                System.out.println("Lama Pinjam (hr) : " + rs.getInt("lama_pinjam"));
                System.out.println("----------------------------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3. View: v_laporan_crosstab_perpustakaan
    public void tampilkanLaporanCrosstab() {
        System.out.println("===== Laporan Crosstab: Rekap Jumlah Peminjaman Per Bulan Per Kategori =====");
        String sql = "SELECT * FROM v_laporan_crosstab_perpustakaan";

        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            if (!rs.isBeforeFirst()) {
                System.out.println("Belum ada data peminjaman untuk ditampilkan.\n");
                return;
            }

            while (rs.next()) {
                System.out.println("Kategori  : " + rs.getString("kategori"));
                System.out.println("Tahun     : " + rs.getInt("tahun"));
                System.out.println("Jan       : " + rs.getInt("Jan"));
                System.out.println("Feb       : " + rs.getInt("Feb"));
                System.out.println("Mar       : " + rs.getInt("Mar"));
                System.out.println("Apr       : " + rs.getInt("Apr"));
                System.out.println("May       : " + rs.getInt("May"));
                System.out.println("Jun       : " + rs.getInt("Jun"));
                System.out.println("Jul       : " + rs.getInt("Jul"));
                System.out.println("Aug       : " + rs.getInt("Aug"));
                System.out.println("Sep       : " + rs.getInt("Sep"));
                System.out.println("Oct       : " + rs.getInt("Oct"));
                System.out.println("Nov       : " + rs.getInt("Nov"));
                System.out.println("Dec       : " + rs.getInt("Dec"));
                System.out.println("----------------------------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
