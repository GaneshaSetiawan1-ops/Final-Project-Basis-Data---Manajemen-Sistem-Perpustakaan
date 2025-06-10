import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserDAO userDAO = new UserDAO();

        System.out.println("=== LOGIN PENGGUNA ===");
        System.out.print("Masukkan NPM: ");
        String npm = scanner.nextLine();
        System.out.print("Masukkan Password: ");
        String password = scanner.nextLine();

        User user = userDAO.authenticate(npm, password);
        if (user != null) {
            System.out.println("Login berhasil! Selamat datang, " + user.getNama());
            if (user.getId_role() == 1) {
                System.out.println("Anda login sebagai Admin.");
            } else {
                System.out.println("Anda login sebagai Anggota.");
            }

            showMenu(user);
        } else {
            System.out.println("Login gagal. NPM atau password salah.");
        }

        scanner.close();
    }

    private static void showMenu(User user) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== MENU UTAMA ===");
            System.out.println("1. Manajemen Anggota");
            System.out.println("2. Manajemen Buku");
            System.out.println("3. Peminjaman Buku");
            System.out.println("4. Pengembalian Buku");
            System.out.println("5. Riwayat Peminjaman");
            System.out.println("6. Riwayat Denda");
            System.out.println("7. Keluar");
            System.out.print("Pilih menu: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    manageAnggota(scanner);
                    break;
                case 2:
                    manageBuku(scanner);
                    break;
                case 3:
                    borrowBook(user, scanner);
                    break;
                case 4:
                    returnBook(user, scanner);
                    break;
                case 5:
                    showRiwayatPeminjaman();
                    break;
                case 6:
                    showRiwayatDenda(scanner);
                    break;
                case 7:
                    System.out.println("Terima kasih! Aplikasi selesai.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        } while (choice != 7);
    }

    // --- MANAJEMEN ANGGOTA ---
    private static void manageAnggota(Scanner scanner) {
        AnggotaDAO anggotaDAO = new AnggotaDAO();
        System.out.println("1. Tambah Anggota");
        System.out.println("2. Lihat Semua Anggota");
        System.out.println("3. Cari Anggota Berdasarkan NPM");
        System.out.print("Pilih opsi: ");
        int opsi = scanner.nextInt();
        scanner.nextLine();

        switch (opsi) {
            case 1:
                System.out.print("Nama: ");
                String nama = scanner.nextLine();
                System.out.print("NPM: ");
                String npm = scanner.nextLine();
                System.out.print("Prodi: ");
                String prodi = scanner.nextLine();
                System.out.print("Email: ");
                String email = scanner.nextLine();
                System.out.print("No HP: ");
                String no_hp = scanner.nextLine();

                Anggota a = new Anggota(0, nama, npm, prodi, email, no_hp);
                anggotaDAO.addAnggota(a);
                System.out.println("Anggota berhasil ditambahkan.");
                break;
            case 2:
                anggotaDAO.getAllAnggota().forEach(anggota -> System.out.println(anggota.toString()));
                break;
            case 3:
                System.out.print("Masukkan NPM: ");
                String cariNPM = scanner.nextLine();
                Anggota hasil = anggotaDAO.searchByNPM(cariNPM);
                if (hasil != null) {
                    System.out.println(hasil.toString());
                } else {
                    System.out.println("Anggota tidak ditemukan.");
                }
                break;
            default:
                System.out.println("Opsi tidak valid.");
        }
    }

    // --- MANAJEMEN BUKU ---
    private static void manageBuku(Scanner scanner) {
        BukuDAO bukuDAO = new BukuDAO();
        System.out.println("1. Tambah Buku");
        System.out.println("2. Lihat Semua Buku");
        System.out.println("3. Cari Buku Berdasarkan Judul");
        System.out.print("Pilih opsi: ");
        int opsi = scanner.nextInt();
        scanner.nextLine();

        switch (opsi) {
            case 1:
                System.out.print("Judul: ");
                String judul = scanner.nextLine();
                System.out.print("Pengarang: ");
                String pengarang = scanner.nextLine();
                System.out.print("Penerbit: ");
                String penerbit = scanner.nextLine();
                System.out.print("Tahun Terbit: ");
                int tahun = scanner.nextInt();
                System.out.print("Jumlah Stok: ");
                int stok = scanner.nextInt();
                System.out.print("ID Kategori: ");
                int kategori = scanner.nextInt();
                scanner.nextLine();

                Buku b = new Buku(0, judul, pengarang, penerbit, tahun, stok, kategori);
                bukuDAO.addBuku(b);
                System.out.println("Buku berhasil ditambahkan.");
                break;
            case 2:
                List<Buku> daftarBuku = bukuDAO.getAllBuku();
                for(Buku buku : daftarBuku) {
                    System.out.println(buku);
                }
                break;
            case 3:
                System.out.print("Masukkan Judul: ");
                String cariJudul = scanner.nextLine();
                Buku hasil = bukuDAO.searchByJudul(cariJudul);
                if (hasil != null) {
                    System.out.println(hasil.toString());
                } else {
                    System.out.println("Buku tidak ditemukan.");
                }
                break;
            default:
                System.out.println("Opsi tidak valid.");
        }
    }

    // --- PEMINJAMAN BUKU ---
    private static void borrowBook(User user, Scanner scanner) {
        PinjamDAO pinjamDAO = new PinjamDAO();
        System.out.print("Masukkan ID Buku yang ingin dipinjam: ");
        int id_buku = scanner.nextInt();
        System.out.print("Jumlah buku yang ingin dipinjam: ");
        int jumlah = scanner.nextInt();

        try {
            boolean success = pinjamDAO.pinjamBuku(user.getId_user(), id_buku, jumlah);
            if (success) {
                System.out.println("Peminjaman berhasil!");
            } else {
                System.out.println("Peminjaman gagal. Stok mungkin tidak mencukupi.");
            }
        } catch (SQLException e) {
            System.out.println("Gagal meminjam buku: " + e.getMessage());
        }
    }

    // --- PENGEMBALIAN BUKU ---
    private static void returnBook(User user, Scanner scanner) {
        PengembalianDAO pengembalianDAO = new PengembalianDAO();
        System.out.print("Masukkan ID Peminjaman yang ingin dikembalikan: ");
        int id_pinjam = scanner.nextInt();

        try {
            boolean success = pengembalianDAO.kembalikanBuku(id_pinjam);
            if (success) {
                System.out.println("Buku berhasil dikembalikan!");

                double denda = pengembalianDAO.hitungDenda(id_pinjam);
                if (denda > 0) {
                    System.out.println("Anda terkena denda sebesar Rp " + denda);

                    DendaDAO dendaDAO = new DendaDAO();
                    dendaDAO.simpanDenda(1, denda); // Anda bisa ambil id_kembali dari return value
                } else {
                    System.out.println("Tidak ada denda.");
                }
            } else {
                System.out.println("Pengembalian gagal.");
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengembalikan buku: " + e.getMessage());
        }
    }

    // --- RIWAYAT PEMINJAMAN ---
    private static void showRiwayatPeminjaman() {
        PinjamDAO pinjamDAO = new PinjamDAO();
        try {
            pinjamDAO.tampilkanRiwayatPeminjaman();
        } catch (SQLException e) {
            System.out.println("Gagal menampilkan riwayat peminjaman.");
        }
    }

    // --- RIWAYAT DENDA ---
    private static void showRiwayatDenda(Scanner scanner) {
        DendaDAO dendaDAO = new DendaDAO();
        System.out.println("1. Tampilkan Semua Denda");
        System.out.println("2. Cari Denda Berdasarkan ID");
        System.out.print("Pilih opsi: ");
        int opsi = scanner.nextInt();
        scanner.nextLine();

        try {
            switch (opsi) {
                case 1:
                    dendaDAO.tampilkanRiwayatDenda();
                    break;
                case 2:
                    System.out.print("Masukkan ID Denda: ");
                    int id_denda = scanner.nextInt();
                    dendaDAO.cariDendaById(id_denda);
                    break;
                default:
                    System.out.println("Opsi tidak valid.");
            }
        } catch (SQLException e) {
            System.out.println("Gagal menampilkan riwayat denda.");
        }
    }
}