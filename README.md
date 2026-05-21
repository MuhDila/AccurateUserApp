# AccurateUserApp

Android application developed in Kotlin using Clean Architecture, MVI (Model-View-Intent), and Jetpack Compose. This application serves as a demonstration for managing and synchronizing user data with an offline-first capability.

---

## Cara Penggunaan Aplikasi

1. **Melihat Daftar User**: Aplikasi otomatis memuat daftar pengguna saat dibuka. Jika perangkat terhubung ke internet, data terbaru akan diunduh dan disimpan di database lokal.
2. **Pencarian User**: Masukkan nama, email, atau alamat pada kolom pencarian di bagian atas untuk menyaring daftar pengguna secara instan.
3. **Pengurutan (Sorting)**: Tekan ikon urutkan untuk menyusun daftar pengguna secara alfabetis (A-Z, Z-A, atau kembali ke urutan default).
4. **Filter Kota**: Tekan ikon filter untuk memfilter daftar pengguna berdasarkan kota tempat mereka tinggal.
5. **Menambahkan User**: Tekan tombol tambah di sudut kanan bawah, isi formulir (Nama, Email, dan Alamat), lalu simpan.
6. **Fungsionalitas Offline**: 
   - Bila tidak ada koneksi internet, Anda tetap dapat melihat daftar pengguna dan menambahkan pengguna baru.
   - Pengguna baru yang ditambahkan dalam kondisi offline akan disimpan di database lokal terlebih dahulu dan ditandai dengan badge "Pending".
   - Aplikasi akan mendeteksi ketika perangkat kembali online dan otomatis mengunggah data pending ke server di latar belakang menggunakan WorkManager.

---

## Teknologi yang Digunakan

*   **Clean Architecture & SOLID Principles**: Pemisahan kode menjadi modular layer (domain, data, dan presentation).
*   **MVI (Model-View-Intent)**: State management satu arah yang terpusat menggunakan StateFlow dan UserEvent.
*   **Jetpack Compose**: Pembangunan UI deklaratif secara modern dan responsif.
*   **Android Hilt**: Dependency Injection untuk manajemen dependensi yang bersih.
*   **Room Database**: Local caching dan persistence storage sebagai Single Source of Truth (SSoT).
*   **Retrofit & Moshi**: Library network API dan JSON serialization parser.
*   **WorkManager**: Penjadwal tugas latar belakang untuk sinkronisasi data offline secara andal.

---

## Struktur Proyek & Alasan Pemilihannya

Proyek ini terbagi menjadi struktur direktori yang terorganisir dengan rapi berdasarkan arsitektur Clean Architecture dan pendekatan **Feature-First**:

```text
com.muhdila.accurateuserapp/
│
├── core/                  # Utilitas global dan konfigurasi dasar aplikasi
│   ├── data/              # Konfigurasi Database Room dan Retrofit global
│   ├── domain/            # Error handling global dan fungsionalitas umum
│   └── presentation/      # Tema UI murni Material 3, Font, dan utility UI
│
├── di/                    # Modul Dependency Injection (Hilt) untuk seluruh modul
│
└── user/                  # Modul fitur User (Fitur Utama)
    ├── data/              # API Service, Room DAO, Entity, Mapper, dan SyncWorker
    ├── domain/            # Model data domain, Interface Repository, dan Use Cases
    └── presentation/      # MVI State, Events/Intents, ViewModel, dan UI Composables
```

### Mengapa Memilih Struktur Ini?
1. **Pemisahan Kepentingan (Separation of Concerns)**: Dengan membagi kode ke dalam lapisan `domain`, `data`, dan `presentation`, logika bisnis utama (`domain`) terbebas dari dependensi platform Android atau library pihak ketiga. Hal ini membuat aplikasi lebih mudah dipelihara dan diuji.
2. **Modular dan Terukur (Scalable)**: Struktur *feature-first* pada folder `user` mengisolasi semua kelas yang berkaitan dengan fitur tersebut. Jika di kemudian hari ditambahkan fitur baru (misal: `product`, `transaction`), kita cukup membuat folder baru setingkat dengan `user` tanpa mengganggu kode yang sudah ada.
3. **MVI State Predictability**: MVI memastikan alur data satu arah (unidirectional data flow) yang sangat cocok digabungkan dengan Jetpack Compose. UI hanya merender `UserUiState` yang dikirim oleh ViewModel, dan semua interaksi pengguna dikirim kembali ke ViewModel sebagai `UserEvent`.

---

## Desain dan Tampilan Antarmuka

Tampilan dan interaksi aplikasi didesain dengan prinsip kesederhanaan dan minimalisme:
*   **Material 3 Default Style**: Aplikasi ini menggunakan palet warna default murni dari sistem Material 3. Mengingat gaya default Material 3 sudah dirancang dengan sangat matang dan standar industri tinggi, keputusan ini membuat antarmuka terasa sangat bersih, fungsional, serta konsisten dengan sistem operasi Android modern.
*   **Desain Minimalis**: Elemen visual dibuat sesederhana mungkin untuk memprioritaskan kemudahan membaca informasi (readability) tanpa adanya ornamen dekoratif yang tidak diperlukan.
*   **Tata Letak Adaptif**: Menggunakan grid responsif yang menyesuaikan ukuran layar perangkat secara dinamis baik pada handphone maupun tablet.

---

## Cara Menjalankan Aplikasi

### Langkah-langkah
1. Buka proyek ini di Android Studio (direkomendasikan versi JDK 17).
2. Jalankan Gradle sync.
3. Jalankan aplikasi pada Emulator atau Perangkat Fisik.

---

## Download Release APK

Anda dapat mengunduh build rilis (APK) yang sudah dikompilasi langsung pada halaman rilis resmi GitHub:
*   [Download app-release.apk (GitHub Release v1.0.0)](https://github.com/MuhDila/AccurateUserApp/releases/tag/v1.0.0)
