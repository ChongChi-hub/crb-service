# Hướng Dẫn Chạy Local - CRB Backend Service (Sea Turtle Web Service)

Tài liệu hướng dẫn chi tiết cách thiết lập database, cấu hình các file properties và khởi chạy backend **CRB Service** bằng **IntelliJ IDEA** trên môi trường local.

---

## 1. Yêu Cầu Môi Trường & Công Cụ

- **IDE**: [IntelliJ IDEA](https://www.jetbrains.com/idea/) (Community hoặc Ultimate Edition)
- **JDK (Java Development Kit)**: **Java 21** (Temurin / Oracle / Amazon Corretto 21)
  - Dự án yêu cầu Java 21 (`sourceCompatibility = '21'`).
- **Build Tool**: Gradle (đi kèm sẵn qua `gradlew` trong dự án).
- **Cơ Sở Dữ Liệu**: **MySQL Server** phiên bản `8.0+` đang chạy tại cổng `3306`.

---

## 2. Chuẩn Bị Cơ Sở Dữ Liệu (MySQL)

Trước khi khởi động ứng dụng, bạn cần tạo trước database:

Mở MySQL client (DBeaver, MySQL Workbench, DataGrip, hoặc Terminal):
```sql
CREATE DATABASE sea_turtle_web CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

---

## 3. Các File Cần Chỉnh Sửa / Cấu Hình Lại

### 3.1. File `src/main/resources/application.properties`
Chuyển profile hoạt động mặc định từ `prod` sang `local`:

```properties
# Đổi dòng này:
# spring.profiles.active=prod

# Thành:
spring.profiles.active=local
```

---

### 3.2. File `src/main/resources/application-local.properties`
Kiểm tra và cập nhật các thông số cho phù hợp với máy của bạn:

```properties
# 1. Cấu hình Database kết nối MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/sea_turtle_web
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root        # ✏️ Sửa thành username MySQL của bạn
spring.datasource.password=root        # ✏️ Sửa thành password MySQL của bạn

# 2. Cấu hình Flyway Migration (Tự động tạo bảng & import dữ liệu ban đầu)
spring.flyway.url=jdbc:mysql://localhost:3306/sea_turtle_web
spring.flyway.user=root                # ✏️ Username MySQL
spring.flyway.password=root            # ✏️ Password MySQL
spring.flyway.enabled=false            # ⚠️ Đặt false khi DB đã có dữ liệu (chỉ bật true nếu DB mới tinh chưa có bảng)
spring.flyway.baseline-on-migrate=true
spring.flyway.out-of-order=false

# 3. Cấu hình CORS (Cho phép Client gọi API)
app.cors.allowedOrigins=http://localhost:5500/,http://localhost:3000/

# 4. Cấu hình Server Port & SSL
server.port=8443
server.ssl.enabled=false

# 5. Cấu hình SFTP (Upload và hiển thị ảnh)
sftp.url=sftp://root:Crb%402024%23ruaBien@27.71.26.231/wp-content/uploads/
sftp.hostname=conruabien.vn
```

> **Ghi chú về Flyway**:
> - Trong thư mục `src/main/resources/db/migration/` đã có sẵn các script migration từ `V1_00` đến `V1_13` (tạo bảng, phân quyền, dữ liệu mẫu bài viết, danh mục, bảng giá...).
> - Đặt `spring.flyway.enabled=true` sẽ giúp ứng dụng tự động chạy các file script này khi khởi động.

---

## 4. Các Bước Khởi Chạy Bằng IntelliJ IDEA

### Bước 1: Mở dự án trong IntelliJ IDEA
1. Mở IntelliJ IDEA.
2. Chọn **File** ➔ **Open...** ➔ Chọn thư mục `sea-turtle-web-service`.
3. Đợi IntelliJ tải dependencies và đồng bộ dự án với Gradle (quá trình này mất khoảng 1-2 phút lần đầu tiên).

### Bước 2: Thiết lập Java 21 cho dự án
1. Vào menu **File** ➔ **Project Structure...** (hoặc nhấn `Cmd + ;` trên macOS / `Ctrl + Alt + Shift + S` trên Windows).
2. Tại tab **Project**:
   - Mục **SDK**: Chọn **JDK 21** (nếu chưa có, bấm *Add SDK* ➔ *Download JDK* ➔ chọn version 21).
   - Mục **Language level**: Chọn **21 - Sealed classes, pattern matching...**.
3. Tại tab **Settings / Preferences** (`Cmd + ,` hoặc `Ctrl + Alt + S`):
   - Vào **Build, Execution, Deployment** ➔ **Build Tools** ➔ **Gradle**.
   - Mục **Gradle JVM**: Chọn **Java 21**.
4. Bấm **Apply** và **OK**.

### Bước 3: Chạy ứng dụng
1. Mở cây thư mục đến file:
   📂 `src/main/java/com/crb/seaturtlewebservice/SeaTurtleWebServiceApplication.java`
2. Nhấn vào icon **Mũi tên xanh (Run)** ở bên cạnh dòng:
   ```java
   public static void main(String[] args) { ... }
   ```
   hoặc chuột phải vào file và chọn **Run 'SeaTurtleWebServiceApplication'**.

*(Tùy chọn: Bạn cũng có thể mở **Run/Debug Configurations**, tại mục **Active profiles** nhập: `local` thay vì sửa file `application.properties`).*

---

## 5. Kiểm Tra Hoạt Động (Verify)

Khi console IntelliJ hiển thị thông báo Spring Boot đã khởi động thành công (thường kết thúc bằng dòng: `Started SeaTurtleWebServiceApplication in ... seconds`):

Mở trình duyệt hoặc Postman truy cập thử các endpoint API:
- Kiểm tra danh mục trang chủ:
  `http://localhost:8443/api/categories/home`
- Kiểm tra danh sách chi nhánh:
  `http://localhost:8443/api/branches`

Nếu trình duyệt trả về định dạng JSON thì backend đã hoạt động hoàn toàn chính xác và sẵn sàng phục vụ cho Client.

---

## 6. Xử Lý Sự Cố Thường Gặp (Troubleshooting)

| Lỗi | Nguyên nhân & Cách khắc phục |
| :--- | :--- |
| **`Access denied for user 'root'@'localhost'`** | Sai username hoặc password MySQL trong `application-local.properties`. Hãy cập nhật lại đúng thông tin tài khoản MySQL của bạn. |
| **`Unknown database 'sea_turtle_web'`** | Chưa tạo database trong MySQL. Chạy lệnh: `CREATE DATABASE sea_turtle_web;`. |
| **`Java version mismatch` / Lỗi biên dịch class** | IntelliJ đang trỏ nhầm JDK cũ (như Java 8, 11 hoặc 17). Đảm bảo cả **Project SDK** và **Gradle JVM** đều là **Java 21**. |
| **`Address already in use: bind` (Port 8443)** | Cổng 8443 đang bị chiếm dụng. Tắt ứng dụng đang dùng cổng này hoặc đổi `server.port` trong `application-local.properties` (nếu đổi port backend thì cần đổi cả bên `appConstant.js` của Client). |
| **Lỗi Flyway migration checksum** | Đặt `spring.flyway.repair=true` hoặc xóa database tạo lại nếu là môi trường dev local. |
