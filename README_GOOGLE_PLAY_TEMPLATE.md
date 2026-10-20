# Mẫu README: Kotlin Multiplatform và phát hành Google Play

> Sao chép file này sang mỗi dự án mới, rồi thay toàn bộ nội dung trong dấu `<...>` bằng thông tin thực tế của app.

## 1. Khởi tạo ứng dụng

- Dùng **Kotlin Multiplatform** và **Compose Multiplatform** để chia sẻ logic, dữ liệu và UI giữa Android/iOS.
- Đặt mã dùng chung trong `composeApp/src/commonMain`; mã riêng nền tảng nằm ở `androidMain` và `iosMain`.
- Chọn package name/application ID duy nhất ngay từ đầu, ví dụ `com.<developer>.<app>`. Không đổi package name sau khi app đã phát hành.

## 2. Android và versioning

- Đặt `compileSdk = 36` và `targetSdk = 36` trong module Android. Đây là mức API tối thiểu Play Console hiện yêu cầu cho app mới.
- Kiểm tra `minSdk`, AndroidManifest, icon adaptive, splash screen và các quyền cần thiết.
- Tăng `versionCode` cho **mỗi** lần upload AAB. `versionCode` không được trùng hoặc thấp hơn bản đã upload.
- Cập nhật `versionName` theo phiên bản hiển thị, ví dụ `1.0.0`, `1.0.1`.

## 3. Ký release và tạo Android App Bundle

Tạo keystore release và file không được commit, ví dụ `keystore.properties`:

```properties
storeFile=<ten-keystore>.jks
storePassword=<mat-khau>
keyAlias=<alias>
keyPassword=<mat-khau>
```

- Với bản thử nghiệm cục bộ, có thể đặt mật khẩu `123456` theo quy ước nội bộ. Không dùng mật khẩu này cho app/thông tin thật.
- Với production, dùng mật khẩu mạnh; lưu keystore và mật khẩu trong password manager hoặc CI secrets.
- Thêm `*.jks`, `*.keystore`, `keystore.properties` và file secrets vào `.gitignore`.
- Tạo AAB release:

```sh
./gradlew :composeApp:bundleRelease
```

- Upload file `.aab` trong thư mục `build/outputs/bundle/release/` lên Play Console.

## 4. AdMob: Banner và Interstitial

1. Tạo app trong AdMob, sau đó tạo một Banner ad unit và một Interstitial ad unit.
2. Thêm Google Mobile Ads SDK; khai báo AdMob App ID trong AndroidManifest.
3. Dùng **Google test ad unit IDs** khi chạy debug/dev; chỉ dùng ad unit ID thật cho release.
4. Tạo một cờ chọn ID theo build type, ví dụ `useTestAds = BuildConfig.DEBUG`.
5. Tạo thêm một cờ toàn cục `isAdsEnabled` để có thể tắt mọi quảng cáo mà không xóa SDK.
6. Chỉ đặt banner ở vị trí không gây nhầm lẫn hay cản trở thao tác; chỉ hiện interstitial ở điểm ngắt tự nhiên sau một hành động hoàn tất.
7. Không nhấp quảng cáo thật khi thử nghiệm.

Ví dụ logic chọn ID:

```kotlin
val bannerAdUnitId = if (useTestAds) TEST_BANNER_ID else LIVE_BANNER_ID
val interstitialAdUnitId = if (useTestAds) TEST_INTERSTITIAL_ID else LIVE_INTERSTITIAL_ID
```

Nếu dùng AdMob, khai báo **Advertising ID = Yes** trong Play Console. Cập nhật phần Data safety và Privacy Policy theo đúng cách SDK xử lý dữ liệu.

## 5. Privacy Policy và GitHub Pages

- Tạo một trang HTML tĩnh, public qua HTTPS và không yêu cầu đăng nhập.
- Privacy Policy phải nêu chính xác: tên app, package name, tên developer/cá nhân/pháp nhân chịu trách nhiệm và email liên hệ trùng với Play Console.
- Mô tả dữ liệu app lưu/thu thập, SDK bên thứ ba (như AdMob), mục đích xử lý, chia sẻ dữ liệu, thời gian lưu và cách xóa dữ liệu.
- Đưa source app lên repository **private**. Không commit keystore, passwords hoặc API keys.
- Tạo repository **public riêng** chỉ chứa `index.html` Privacy Policy và file công khai cần thiết.
- Bật GitHub Pages: **Settings → Pages → Deploy from a branch → `main` → `/(root)`**.
- Dán URL GitHub Pages vào mục Privacy policy của Play Console.
- Nếu dùng AdMob, thêm file `app-ads.txt` ở root domain và khai báo developer website/domain trùng khớp trên Google Play.

Nội dung `app-ads.txt` do AdMob cung cấp, ví dụ:

```text
google.com, pub-<publisher-id>, DIRECT, f08c47fec0942fa0
```

## 6. Store listing

Chuẩn bị:

- Tên ứng dụng.
- Short description (tối đa 80 ký tự): một câu nêu giá trị chính của app.
- Full description: giới thiệu, tính năng chính, cách app xử lý dữ liệu/quảng cáo và liên kết Privacy Policy nếu cần.
- App icon, feature graphic, 2–8 phone screenshots đúng kích thước/yêu cầu Play Console.
- Email liên hệ nhà phát triển, category, content rating và website (nếu có).

Mẫu mô tả tổng quát:

```text
Short description
<Một câu ngắn mô tả giá trị chính của ứng dụng.>

Full description
<Tên app> giúp bạn <lợi ích chính>.

Tính năng chính:
- <Tính năng 1>
- <Tính năng 2>
- <Tính năng 3>

<Mô tả rõ dữ liệu người dùng được lưu/xử lý ở đâu và quảng cáo nếu có.>
```

Chỉ nêu tính năng đã có trong bản phát hành; không đưa cam kết không thể kiểm chứng hoặc mô tả sai cách app xử lý dữ liệu.

## 7. Trước khi phát hành

- Cài AAB qua Internal testing hoặc Closed testing và kiểm tra install, startup, navigation, crash, privacy policy, icon/splash, ad test/live behavior.
- Hoàn tất App content, Data safety, Advertising ID, content rating, thông tin liên hệ và store listing.
- Kiểm tra privacy policy/app-ads.txt có thể mở công khai bằng trình duyệt.
- Tạo Production release, chỉ giữ artifact có version code cần phát hành, rồi gửi Google xét duyệt.
