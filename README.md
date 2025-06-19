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
