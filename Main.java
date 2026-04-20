import java.util.ArrayList;
import java.util.Scanner;

class Buku {
    private String judul;
    private boolean tersedia;

    public Buku(String judul) {
        this.judul    = judul;
        this.tersedia = true;
    }

    public String  getJudul()                    { return judul; }
    public boolean isTersedia()                  { return tersedia; }
    public void    setTersedia(boolean tersedia) { this.tersedia = tersedia; }

    public void tampil() {
        System.out.println(judul + " (" + (tersedia ? "Tersedia" : "Dipinjam") + ")");
    }
}

class User {
    protected String nama;
    protected String nim;

    public User(String nama, String nim) {
        this.nama = nama;
        this.nim  = nim;
    }
}

class Peminjam extends User {
    private int             jumlahPinjam;
    private int             maxPinjam;
    private ArrayList<Buku> bukuDipinjam;

    public Peminjam(String nama, String nim) {
        super(nama, nim);
        jumlahPinjam = 0;
        bukuDipinjam = new ArrayList<>();
        maxPinjam    = hitungMaxPinjam(nim);

        System.out.println("\n=========================================");
        System.out.printf( " Nama                       : %s\n", nama);
        System.out.printf( " NIM                        : %s\n", nim);
        System.out.printf( " Batas Max Peminjaman Buku  : %d buku\n", maxPinjam);
        System.out.println("=========================================");
    }

    private int hitungMaxPinjam(String nim) {
        int angkaTerakhir = nim.charAt(nim.length() - 1) - '0';
        if (angkaTerakhir <= 3) return 2;
        if (angkaTerakhir <= 6) return 3;
        return 5;
    }

    public ArrayList<Buku> getBukuDipinjam() {
        return bukuDipinjam;
    }

    // Pinjam 1 buku
    public void pinjamBuku(Buku b) {
        if (!b.isTersedia()) {
            System.out.println(" Gagal: \"" + b.getJudul() + "\" sedang dipinjam!");
        } else if (jumlahPinjam >= maxPinjam) {
            System.out.println(" Gagal: batas pinjam tercapai (" + maxPinjam + " buku)!");
        } else {
            b.setTersedia(false);
            bukuDipinjam.add(b);
            jumlahPinjam++;
            System.out.println(" Berhasil meminjam : " + b.getJudul());
        }
    }

    public void pinjamBuku(int[] daftarIndex, Perpustakaan perpus) {
        System.out.println("\n-----------------------------------------");
        for (int idx : daftarIndex) {
            Buku b = perpus.pilihBuku(idx);
            if (b != null) pinjamBuku(b);
            else System.out.println(" Nomor " + (idx + 1) + " tidak valid, dilewati.");
        }
        System.out.println("-----------------------------------------");
        System.out.println(" Sisa Kuota Peminjaman " + (maxPinjam - jumlahPinjam) + " buku lagi");
        System.out.println("-----------------------------------------");
    }

    public void kembaliBuku(Buku b) {
        if (bukuDipinjam.contains(b)) {
            b.setTersedia(true);
            bukuDipinjam.remove(b);
            jumlahPinjam--;
            System.out.println(" Berhasil dikembalikan: " + b.getJudul());
        } else {
            b.setTersedia(false);
            bukuDipinjam.add(b);
            jumlahPinjam++;
            System.out.println(" Gagal: buku tidak ada di pinjaman kamu!");
        }
    }

    // kembalikan banyak buku sekaligus
    public void kembaliBuku(int[] daftarIndex) {
        System.out.println("\n-----------------------------------------");
        for (int idx : daftarIndex) {
            if (idx >= 0 && idx < bukuDipinjam.size()) {
                kembaliBuku(bukuDipinjam.get(idx));
            } else {
                System.out.println(" Nomor " + (idx + 1) + " tidak valid, dilewati.");
            }
        }

        System.out.println("-----------------------------------------");
        System.out.println(" Sisa Kuota Peminjaman " + (maxPinjam - jumlahPinjam) + " buku lagi");
        System.out.println("-----------------------------------------");
    }

    public void lihatPinjaman() {
        System.out.println("\n=========================================");
        System.out.println("           DAFTAR PINJAMAN               ");
        System.out.println("=========================================");
        if (bukuDipinjam.isEmpty()) {
            System.out.println(" Tidak ada buku dipinjam.");
        } else {
            for (int i = 0; i < bukuDipinjam.size(); i++) {
                System.out.printf(" %d. %s\n", i + 1, bukuDipinjam.get(i).getJudul());
            }
        }
        System.out.printf(" Kuota Peminjaman: %d/%d buku\n", jumlahPinjam, maxPinjam);
        System.out.println("=========================================");
    }

    public void info() {
        System.out.println("\n=========================================");
        System.out.println("             INFO PEMINJAM               ");
        System.out.println("=========================================");
        System.out.printf( " Nama                      : %s\n", nama);
        System.out.printf( " NIM                       : %s\n", nim);
        System.out.printf( " Buku yang Dipinjam        : %d/%d buku\n", jumlahPinjam, maxPinjam);
        System.out.printf( " Batas Max Peminjaman Buku : %d buku\n", maxPinjam);
        System.out.println("=========================================");
    }
}

class Perpustakaan {
    private ArrayList<Buku> daftarBuku;

    public Perpustakaan() {
        daftarBuku = new ArrayList<>();
    }

    public void tambahBuku(Buku b) { daftarBuku.add(b); }

    public void tampilkanBuku() {
        System.out.println("\n=========================================");
        System.out.println("              DAFTAR BUKU                ");
        System.out.println("=========================================");
        for (int i = 0; i < daftarBuku.size(); i++) {
            Buku b = daftarBuku.get(i);
            System.out.printf(" %d. %-25s [%s]\n",
                i + 1,
                b.getJudul(),
                b.isTersedia() ? "Tersedia" : "Dipinjam"
            );
        }
        System.out.println("=========================================");
    }

    public Buku pilihBuku(int index) {
        if (index >= 0 && index < daftarBuku.size())
            return daftarBuku.get(index);
        return null;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        Perpustakaan perpus = new Perpustakaan();
        perpus.tambahBuku(new Buku("Algoritma Pemrograman"));
        perpus.tambahBuku(new Buku("Basis Data"));
        perpus.tambahBuku(new Buku("Pemrograman Berorientasi Objek"));
        perpus.tambahBuku(new Buku("Jaringan Komputer"));
        perpus.tambahBuku(new Buku("Sistem Operasi"));

        System.out.println("\n=========================================");
        System.out.println("         SISTEM PERPUSTAKAAN MINI          ");
        System.out.println("=========================================");
        System.out.println(" Masukkan identitas anda!");
        System.out.print(" Nama : "); String nama = input.nextLine();
        System.out.print(" NIM  : "); String nim  = input.nextLine();

        Peminjam mhs = new Peminjam(nama, nim);

        int pilih = -1;
        do {
            System.out.println("\n=========================================");
            System.out.println("           MENU PERPUSTAKAAN             ");
            System.out.println("=========================================");
            System.out.println(" 1. Lihat Daftar Buku");
            System.out.println(" 2. Pinjam Buku");
            System.out.println(" 3. Kembalikan Buku");
            System.out.println(" 4. Lihat Buku yang Dipinjam");
            System.out.println(" 5. Info User");
            System.out.println(" 0. Keluar");
            System.out.println("=========================================");
            System.out.print(" Pilih: ");

            pilih = input.nextInt();

            switch (pilih) {
                case 1:
                    perpus.tampilkanBuku();
                    break;

                case 2:
                    perpus.tampilkanBuku();
                    System.out.println(" Masukkan nomor buku yang ingin dipinjam.");
                    System.out.println(" Pisahkan dengan spasi jika lebih dari satu.");
                    System.out.println(" Contoh: 1 3 5");
                    System.out.print(" Pilih: ");
                    input.nextLine(); // bersihkan buffer
                    String[] inputNomor = input.nextLine().trim().split("\\s+");

                    int[] daftarIndex = new int[inputNomor.length];
                    for (int i = 0; i < inputNomor.length; i++) {
                        daftarIndex[i] = Integer.parseInt(inputNomor[i]) - 1;
                    }
                    mhs.pinjamBuku(daftarIndex, perpus);
                    break;

                case 3:
                    if (mhs.getBukuDipinjam().isEmpty()) {
                        System.out.println("\n Kamu sedang tidak meminjam buku apapun.");
                    } else {
                        System.out.println("\n=========================================");
                        System.out.println("        BUKU YANG SEDANG DIPINJAM         ");
                        System.out.println("=========================================");
                        for (int i = 0; i < mhs.getBukuDipinjam().size(); i++) {
                            System.out.printf(" %d. %s\n", i + 1, mhs.getBukuDipinjam().get(i).getJudul());
                        }
                        System.out.println("=========================================");
                        System.out.println(" Masukkan nomor buku yang ingin dikembalikan.");
                        System.out.println(" Pisahkan dengan spasi jika lebih dari satu.");
                        System.out.println(" Contoh: 1 2");
                        System.out.print(" Pilih: ");
                        input.nextLine(); // bersihkan buffer
                        String[] inputnomor = input.nextLine().trim().split("\\s+");

                        int[] daftarindex = new int[inputnomor.length];
                        for (int i = 0; i < inputnomor.length; i++) {
                            daftarindex[i] = Integer.parseInt(inputnomor[i]) - 1;
                        }
                        mhs.kembaliBuku(daftarindex);
                    }
                    break;

                case 4:
                    mhs.lihatPinjaman();
                    break;

                case 5:
                    mhs.info();
                    break;

                case 0:
                    System.out.println("\n=========================================");
                    System.out.println("             Terima kasih, " + nama + "!");
                    System.out.println("=========================================");
                    break;

                default:
                    System.out.println(" Pilihan tidak tersedia!");
            }

        } while (pilih != 0);

        input.close();
    }
}