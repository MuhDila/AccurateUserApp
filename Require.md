# CPS SOFT - [Android] Mobile Development Test

Anda akan diberikan sebuah API Endpoint untuk CRUD sebuah resource data user di Accurate. Anda diminta untuk membuat Android App yang dapat memberikan fitur:

1. Melihat daftar user
2. Mencari user yang diinginkan
3. Mengurutkan user berdasarkan nama
4. Memfilter user berdasarkan kota
5. Menambahkan user baru

## 🎯 Goals

1. User experience yang baik dan mudah digunakan saat melihat daftar user, mencari user, filter user dan menambahkan user baru.
2. Tidak ada masalah saat mengakses aplikasi dalam kondisi offline.
3. Berikan penjelasan pada `README.md` (di *repository* hasil pengerjaan), yaitu:
   - a. Cara penggunaan aplikasi
   - b. Teknologi yang digunakan
   - c. Kenapa tampilan/interaksi seperti itu.
4. Commit message yang relevan dan history, yang incremental dengan syntax [Karma Git Commit Msg](http://karma-runner.github.io/4.0/dev/git-commit-msg.html).

## ✨ Nice to Have

- [x] Implementasi SOLID principle
- [x] Implementasi Clean architecture
- [x] Implementasi Adaptive layout
- [x] Implementasi Android Hilt
- [x] Implementasi Jetpack Compose, atau view binding dan data binding.
- [x] Implementasi Reactive Programming (RxAndroid atau Coroutine Flow).
- [x] Implementasi Moshi sebagai Json Converter
- [x] Implementasi Room Database
- [x] Implementasi Work Manager
- [x] Implementasi Firebase Event

---

## 🔗 Resources

### 1. API User
**Endpoint:** `https://661f555f16358961cd940b83.mockapi.io/api/v2/accurate/user`

#### Get List
- **Method:** `GET`
- **DTO:**
  ```json
  [
    {
      "name": "Tiko",
      "address": "Tangerang",
      "email": "tiko@gmail.com",
      "phoneNumber": "081398302869",
      "city": "Tangerang",
      "gender": 0,
      "id": "1"
    }
  ]