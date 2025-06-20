# 📚 Sistem Manajemen Buku Perpustakaan (Java + MySQL)

Sistem ini adalah aplikasi desktop sederhana berbasis Java yang terhubung ke database MySQL untuk mengelola perpustakaan. Proyek ini mencakup fitur seperti peminjaman buku, pengembalian, pengelolaan denda, laporan, dan riwayat transaksi.

## Nama Anggota Kelompok 3
1. Ganesha Setiawan (24082010092)
2. Putu Pramudya Pratama (24082010113)
3. Sulthan Valeri Osmond Risqullah (24082010119)
4. David Mahlon Sarumaha (24082010126)
---

## 🗄️ Struktur Database

Database terdiri dari tabel utama berikut:

- `role`: Menyimpan jenis pengguna (Admin, Anggota)
- `user`: Data pengguna perpustakaan
- `kategori_buku`, `buku`: Informasi buku dan kategorinya
- `pinjam`, `detail_pinjam`: Data peminjaman buku
- `pengembalian`, `detail_pengembalian`: Data pengembalian buku
- `denda`: Catatan denda
- `log_stok_habis`, `log_pengembalian`: Catatan otomatis (trigger)
- `v_riwayat_peminjaman`, `v_riwayat_pengembalian`: View untuk laporan
- Stored Procedures dan Trigger untuk peminjaman & pengembalian otomatis

---

## 💡 Fitur Utama

✅ Login sebagai Admin atau Anggota  
✅ Manajemen Anggota & Buku  
✅ Peminjaman Buku (dengan pengecekan stok & trigger log stok habis)  
✅ Pengembalian Buku (dengan trigger log pengembalian)  
✅ Hitung Denda Otomatis jika lewat 7 hari  
✅ Laporan:
- Subquery: Total buku dipinjam per anggota
- CTE: Peminjaman terlambat
- Crosstab: Rekap pinjaman per kategori per bulan  

---

## 🔧 Stored Procedure
Contoh penerapan Stored Procedure yaitu menambahkan data ke tabel pinjam, menambahkan data ke tabel detail pinjam, dan mengurangi stok buku secara otomatis
```sql
-- Stored Procedure Pinjam Buku
CREATE DEFINER=`root`@`localhost` PROCEDURE manajemen_perpustakaan.pinjam_buku(
   IN p_id_user INT,
   IN p_jumlah INT,
   IN p_id_buku INT,
   IN p_tanggal DATE,
   OUT p_status VARCHAR(50)
)


BEGIN
   DECLARE pinjam_id INT;
   DECLARE stok_buku INT;


   -- Cek stok buku terlebih dahulu
   SELECT jumlah_stok INTO stok_buku FROM buku WHERE id_buku = p_id_buku;
   IF stok_buku IS NULL THEN
       SET p_status = 'Gagal: Buku tidak ditemukan.';
  
   ELSEIF stok_buku < p_jumlah THEN
       SET p_status = 'Gagal: Stok buku tidak mencukupi.';
  
   ELSE
       -- 1. Insert ke tabel pinjam
       INSERT INTO pinjam (id_user, tanggal_pinjam, status) VALUES (p_id_user, p_tanggal, 'Dipinjam');


       -- 2. Ambil ID pinjam terakhir
       SET pinjam_id = LAST_INSERT_ID();


       -- 3. Insert ke detail_pinjam
       INSERT INTO detail_pinjam (id_pinjam, id_buku, jumlah) VALUES (pinjam_id, p_id_buku, p_jumlah);


       -- 4. Update stok buku
       UPDATE buku  SET jumlah_stok = jumlah_stok - p_jumlah
       WHERE id_buku = p_id_buku;
       SET p_status = CONCAT('Berhasil meminjam buku. ID Pinjam: ', pinjam_id);
      
   END IF;
END;
```
## 🔧 Trigger
```sql
-- Membuat tabel log
CREATE TABLE IF NOT EXISTS log_stok_habis (
   id_log INT AUTO_INCREMENT PRIMARY KEY,
   id_buku INT,
   waktu TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   pesan VARCHAR(255)
);


-- Trigger setelah update di tabel buku
CREATE TRIGGER trg_stok_habis
AFTER UPDATE ON buku
FOR EACH ROW


BEGIN
   IF NEW.jumlah_stok = 0 AND OLD.jumlah_stok > 0 THEN
       INSERT INTO log_stok_habis (id_buku, pesan)
       VALUES (NEW.id_buku, CONCAT('Stok buku dengan ID ', NEW.id_buku, ' telah habis.'));
   END IF;
END;
```
## 🔧 Report(CrossTab, CTEm Sub Query)
```sql
-- Membuat tabel log
CREATE TABLE IF NOT EXISTS log_stok_habis (
   id_log INT AUTO_INCREMENT PRIMARY KEY,
   id_buku INT,
   waktu TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   pesan VARCHAR(255)
);


-- Trigger setelah update di tabel buku
CREATE TRIGGER trg_stok_habis
AFTER UPDATE ON buku
FOR EACH ROW


BEGIN
   IF NEW.jumlah_stok = 0 AND OLD.jumlah_stok > 0 THEN
       INSERT INTO log_stok_habis (id_buku, pesan)
       VALUES (NEW.id_buku, CONCAT('Stok buku dengan ID ', NEW.id_buku, ' telah habis.'));
   END IF;
END;
```
## 🔧  Script View query mysql dan sebagai java class(LaporanView.java)=
```sql
-- Laporan Subquery untuk menampilkan nama anggota dan total buku yang dipinjam
CREATE OR REPLACE VIEW v_laporan_subquery AS
SELECT 
    u.nama,
    (SELECT SUM(dp.jumlah)
     FROM pinjam p
     JOIN detail_pinjam dp ON p.id_pinjam = dp.id_pinjam
     WHERE p.id_user = u.id_user) AS total_buku_dipinjam
FROM user u
WHERE u.id_role = 2; -- Id role anggota

-- Laporan CTE untuk mencatat peminjaman yang terlambat (lebih dari 7 hari)
CREATE OR REPLACE VIEW v_laporan_cte_fallback AS
SELECT 
    p.id_pinjam,
    u.nama,
    p.tanggal_pinjam,
    DATEDIFF(pg.tanggal_kembali, p.tanggal_pinjam) AS lama_pinjam
FROM pinjam p
JOIN pengembalian pg ON pg.id_pinjam = p.id_pinjam
JOIN user u ON p.id_user = u.id_user
WHERE DATEDIFF(pg.tanggal_kembali, p.tanggal_pinjam) > 7;
```
LaporanView.java =
```java
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

```
## ▶️ Cara Menjalankan Aplikasi

> Pastikan sudah install **MySQL**, **Java**, dan sudah import skrip SQL ke database.

### 1. Masuk ke folder `src`:
```bash
cd src
```

### 2. Compile semua file Java:
```bash
javac -cp "lib/mysql-connector-j-9.3.0.jar" *.java
```

### 3. Jalankan aplikasi:
```bash
java -cp ".;lib/mysql-connector-j-9.3.0.jar" Main
```

> ⚠️ **Note untuk Linux/MacOS:** Ganti `;` menjadi `:`  
> Contoh: `java -cp ".:lib/mysql-connector-j-9.3.0.jar" Main`

---

## 🔧 Konfigurasi Koneksi Database

Edit file `DBConnection.java` untuk menyesuaikan koneksi ke database:

```java
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/nama_database";
    private static final String USER = "root";
    private static final String PASS = "password_mysqlmu";
    ...
}
```

---

## 📂 Contoh Data Awal

Proyek ini sudah menyertakan data dummy:
- 1 Admin (Putu)
- 1 Anggota (Ganesha)
- 4 Buku dari 4 kategori
- 1 riwayat peminjaman dan pengembalian

---

## 📜 Lisensi

Proyek ini open-source dan bebas digunakan untuk pembelajaran.

---

## 🙋‍♂️ Kontribusi

Pull request dan feedback sangat diterima. Kamu juga bisa forking dan membuat pengembangan lebih lanjut seperti:
- Fitur pencarian buku
- Peminjaman multi-buku sekaligus
- Export laporan ke PDF
