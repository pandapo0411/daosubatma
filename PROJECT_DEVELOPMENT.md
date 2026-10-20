# Đạo Sư Bắt Ma — Kế hoạch phát triển

> **Phiên bản:** 0.1 (13-10-2026)  
> **Nền tảng phát hành đầu tiên:** Android / Google Play  
> **Định hướng tiếp theo:** iOS, dùng Kotlin Multiplatform (KMP)

## 1. Tóm tắt sản phẩm

**Đạo Sư Bắt Ma** là game sưu tầm ma phong cách kinh dị hài Á Đông. Người chơi mở camera để “khai nhãn”, gặp ma xuất hiện dạng overlay trên khung hình thực, chọn bùa và vẽ chính xác một mẫu vòng tròn để thu phục ma.

**High concept:** Pokémon GO gặp phim ma Hồng Kông, nhưng không cần GPS hay đi lại — kỹ năng cốt lõi là vẽ bùa.

**Đối tượng:** 12+; người chơi thích game sưu tầm, dân gian Việt/Á Đông và các phiên chơi ngắn hằng ngày.

## 2. Đánh giá khả thi và quyết định phạm vi

| Hạng mục | Khả thi | Quyết định |
|---|---|---|
| Camera + ma overlay 2D | Cao | Làm trong MVP; không cần AR framework. |
| Vẽ bùa, so khớp nét, thu phục | Cao | Core gameplay; xây từ đầu và kiểm thử kỹ cảm giác thao tác. |
| Sưu tầm, cấp bậc, bùa, nhiệm vụ | Cao | MVP làm bản tối giản; mở rộng sau. |
| Quảng cáo thưởng / IAP | Cao | Thực hiện sau khi loop chơi ổn định; cần tuân thủ Play/AdMob. |
| AR bám mặt phẳng, che khuất vật thể | Trung bình | Giai đoạn 3; code nền tảng riêng cho ARCore Android và ARKit iOS. |
| PvP, boss đồng bộ giờ, tỉ lệ sở hữu toàn cầu | Trung bình–thấp | Cần backend, chống gian lận và vận hành; không thuộc MVP. |

**Kết luận:** dự án khả thi nếu phát hành Android MVP với camera overlay 2D, không GPS, không backend bắt buộc. Mốc 4–6 tuần chỉ thực tế với nhóm nhỏ khi giới hạn nội dung, dùng placeholder/asset tối giản và chưa làm AR nâng cao, PvP hay live event. Nếu cần artwork dark-fantasy hoàn chỉnh cho toàn bộ 20 ma, nên dành riêng thời gian/nguồn lực art.

## 3. Trụ cột thiết kế

- Không bản đồ, không định vị: người chơi có thể chơi ở bất cứ đâu.
- Mỗi lần gặp ma chỉ có một cơ hội dán bùa; vẽ hụt thì ma biến mất.
- Độ khéo tay vẽ quyết định kết quả, không phải combat theo lượt.
- Ma dựa trên chất liệu dân gian Việt Nam và Á Đông, xử lý theo phong cách dark fantasy trưởng thành nhưng tránh mô tả quá ghê rợn để phù hợp phân loại nội dung.
- Bậc hiếm thể hiện bằng palette màu phủ trên một thiết kế gốc để tiết kiệm chi phí mỹ thuật.

## 4. Cốt truyện và mục tiêu

Tháng 7 âm lịch, phong ấn Trấn Ma Đài vỡ làm 108 quỷ dữ trà trộn vào nhân gian. Người chơi là đệ tử cuối cùng phái Mao Sơn, nhận ba bảo vật:

- **Thiên Sư Bút:** vẽ bùa thu quỷ.
- **Túi Càn Khôn:** chứa bùa và vật phẩm.
- **Hồ Lô Thu Quỷ:** phong ấn ma đã bắt.

Mục tiêu dài hạn là thu phục đủ Bách Quỷ Lục, phong ấn Trấn Ma Đài và ngăn Quỷ Vương giáng thế. Nội dung MVP dùng 20 loài ma; 108 ma là mục tiêu mở rộng theo chương.

## 5. Vòng lặp chơi cốt lõi

```text
Mở app → Khai nhãn bằng camera → Roll gặp ma hoặc “âm khí yên tĩnh”
→ Chọn bùa → Vẽ mẫu bùa → Chấm độ khớp
→ Thu phục / ma tan biến → Nhận công lực, cập nhật Bách Quỷ Lục
```

### Luật bắt ma

1. Người chơi bấm **Khai Nhãn**; hệ thống có tỉ lệ cơ bản 60% cho một ma xuất hiện ở **vị trí ngẫu nhiên mới** trên camera preview ở mỗi lượt gặp, trong vùng an toàn không che header/điều khiển. Ma có cửa sổ tồn tại **3 phút** từ lúc lộ diện, kể cả khi hộp chọn bùa đang mở; hết giờ ma tan biến và bắt đầu lượt dò mới. Trong lúc chờ chọn bùa, artwork ma trôi chậm qua các điểm đến ngẫu nhiên nhỏ để sinh động nhưng không làm dịch chuyển tên hay nút thao tác.
2. Người chơi chọn bùa, sau đó có đúng **10 giây** để vẽ một nét theo mẫu vòng tròn. Ma hiếm có thể dùng nhiều vòng, vòng đứt hoặc vòng xoay.
3. Đồng hồ đếm ngược phải luôn hiển thị rõ. Nếu hết 10 giây trước khi người chơi hoàn tất nét vẽ, bùa thất bại, ma tan biến và chuyển thẳng tới kết quả thất bại.
4. Người chơi luôn có nút **Quay lại** trên màn hình Dán Bùa. Quay lại hủy lượt dán hiện tại và trở về màn gặp ma khi đồng hồ vẫn còn thời gian; không được làm kẹt người chơi ở canvas.
5. Điểm khớp được tính từ khoảng cách nét người dùng tới đường mẫu, có dung sai theo độ khó.
6. Kết quả:

| Điểm vẽ | Kết quả |
|---|---|
| ≥ 90% | **Bùa Hoàn Hảo**: cộng 10% tỉ lệ bắt. |
| 70–89% | Bùa chuẩn: dùng tỉ lệ nền của ma, bonus bùa và cộng hưởng. |
| < 70% | Vẽ hụt: ma tan biến ngay. |

7. Khi thành công, bùa bay vào trán ma rồi hút ma vào Hồ Lô; màn kết quả hiển thị ma, độ hiếm, công lực và trạng thái trùng/mới.

## 6. Tiến trình và kinh tế game

### Bậc hiếm

| Bậc | Màu | Tỉ lệ bắt cơ bản | Công lực |
|---|---|---:|---:|
| Phổ thông | Xám tro | 80% | 10 |
| Hiếm | Xanh dương | 50% | 60 |
| Sử thi | Tím | 30% | 180 |
| Huyền thoại | Vàng kim | 15% | 480 |

Mỗi ma chỉ có một thiết kế gốc. Palette theo bậc: xám, xanh, tím và vàng kim.

### Cấp đạo sư

| Cấp bậc | Công lực tổng | Mở khóa |
|---|---:|---|
| Đạo Đồng | 0 | Bùa Vàng |
| Pháp Sư | 500 | Bùa Đỏ, túi bùa rộng hơn |
| Thiên Sư | 2.000 | Bùa Tím, ma Sử thi |
| Chân Nhân | 6.000 | Bùa Kim Cang, tăng cơ hội ma hiếm |
| Đạo Tổ | 15.000 | Danh hiệu, skin; PvP chỉ là tính năng tương lai |

### Bùa và vật phẩm

| Bùa | Hiệu quả | Nguồn |
|---|---:|---|
| Bùa Vàng Trấn Tà | +0% | Không giới hạn |
| Bùa Đỏ Lôi Hỏa | +15% | Nhiệm vụ / tiền game |
| Bùa Tím Tử Vi | +30% | Ghép bùa đỏ / boss |
| Bùa Kim Cang | +50% | Sự kiện / thành tựu / IAP |

- Khi ma lộ diện, người chơi phải chọn một lá bùa từ túi trước khi vào màn vẽ. UI hiển thị artwork bùa, số lượng còn lại, bonus và đối tượng ma phù hợp; Bùa Vàng luôn hiển thị vô hạn.
- Bùa Đỏ/Tím/Kim Cang bị tiêu hao sau mọi lượt phong ấn đã hoàn tất, bất kể thành công hay thất bại; Bùa Vàng không giới hạn. Tỉ lệ bằng tỉ lệ nền của ma + bonus bùa + 10% nếu đúng bùa phù hợp + 10% khi Bùa Hoàn Hảo, giới hạn ở 98%.
- Lá bùa đang chọn hiển thị bán trong suốt trên camera trong màn Dán Bùa, cùng vòng pháp trận; không dùng nền đặc che camera.
- **Dương khí:** giới hạn lượt Khai Nhãn; hồi theo thời gian hoặc qua phần thưởng quảng cáo. Cần cân chỉnh để không tạo cảm giác ép xem quảng cáo.
- Chỉ **thẻ trùng cùng loài và bậc hiếm** mới được luyện hóa trong Bách Quỷ Lục; luôn giữ lại ít nhất một thẻ. Phổ thông/Hiếm/Sử thi/Huyền thoại đổi lần lượt 1/4/15/60 Linh Phách; Huyền thoại còn cho 1 Mảnh Kim Cang. 12 Linh Phách đổi Bùa Đỏ; 38 Linh Phách + 1 Bùa Đỏ đổi Bùa Tím. Bùa Kim Cang chỉ đổi trong sự kiện/thành tựu, yêu cầu 5 Mảnh Kim Cang.
- Vật phẩm mở rộng sau MVP: Chuông Dẫn Hồn, Gương Bát Quái, Đèn Lồng Dẫn Lối và Cơm Cúng.

## 7. Nội dung MVP

Ra mắt với 20 ma; phân phối độ hiếm theo dữ liệu thiết kế hiện có:

- **Phổ thông:** Ma Đói, Bóng Ma Lang Thang, Ma Xó, Tiểu Yêu Tinh Nghịch, Vong Nhi.
- **Hiếm:** Ma Da, Bóng Đè, Cương Thi Nhỏ, Hồ Ly Một Đuôi, Tinh Cây Cổ Thụ.
- **Sử thi:** Ma Lai Rút Ruột, Quỷ Một Chân, Bạch Cốt Tinh, Hồ Ly Chín Đuôi, Thủy Quái Sông Sâu.
- **Huyền thoại:** Quỷ Vương Dạ Xoa, Cương Thi Vương, Chúa Quỷ Bóng Đêm, Long Mạch Yêu Vương, Vô Diện Quỷ Vương.

**Asset catalogue 108:** `assets108/danh-sach-108-ma.md` xác định 27 archetype × 4 bậc hiếm. Toàn bộ PNG nền trong suốt được đóng gói trong `app/src/main/res/drawable/` theo tên `{id}_{common|rare|epic|legendary}.png`. Runtime catalogue dùng đúng ID/bậc hiếm để chọn artwork khi ma xuất hiện và khi hiển thị Thẻ Bắt Ma.

**Bách Quỷ Lục** là sổ tay hướng tới đủ 108 loài ma; mỗi loài có artwork, truyền thuyết dân gian và thông tin bắt. Mỗi lần bắt thành công phải tạo một **Thẻ Bắt Ma độc lập** được lưu cục bộ, kể cả khi trùng loài. Thẻ có ID duy nhất, loài ma, bậc hiếm, độ khớp nét vẽ và thời điểm bắt. Danh sách ưu tiên Huyền thoại → Sử thi → Hiếm → Phổ thông, rồi mới đến thời điểm bắt; dùng lưới adaptive: 2 thẻ trên điện thoại dọc và 3 thẻ khi chiều rộng đủ. Mỗi thẻ có mô tả dân gian ngắn và nền quầng linh khí theo độ hiếm để artwork PNG trong suốt vẫn nổi rõ. Chạm một thẻ mở trang chi tiết toàn màn hình, nơi người chơi có thể dùng Android Share Sheet để gửi lời khoe thành tích; MVP chỉ chia sẻ văn bản, chưa xuất ảnh thẻ và không cần tích hợp SDK Facebook/Zalo/TikTok. Sưu tầm đủ bộ theo bậc hiếm sẽ mở danh hiệu và bùa hiếm ở giai đoạn sau.

Hướng dẫn vòng lặp luyện hóa nằm trong màn **Lò Luyện Hóa** và có cặp thẻ minh họa trùng nhau, không đưa dữ liệu mẫu vào Bách Quỷ Lục, không ảnh hưởng tiến trình sưu tầm hay phần thưởng.

Các chỉ số hiển thị trên Thẻ Bắt Ma hiện là giá trị thiết kế tĩnh theo bậc hiếm (không phải số liệu live-ops):

| Bậc hiếm | Công lực | Cấp độ | Độ hiếm thật / tỷ lệ đạo sĩ sở hữu |
|---|---:|---:|---:|
| Phổ thông | +10 | Lv.3 | 68,5% |
| Hiếm | +60 | Lv.15 | 27,6% |
| Sử thi | +180 | Lv.27 | 9,8% |
| Huyền thoại | +480 | Lv.42 | 3,2% |

Độ khớp nét vẽ và thời điểm bắt vẫn là dữ liệu của từng lần thu phục, nên có thể khác nhau giữa hai thẻ cùng một loài ma.

## 8. Luật xuất hiện

- Ban ngày: cơ hội thấp hơn, thiên về Phổ thông.
- 18:00–05:00: tăng cơ hội xuất hiện và ma Hiếm.
- Sau 00:00: Ma Lai Rút Ruột có thể xuất hiện.
- Ma Huyền thoại yêu cầu tối thiểu cấp Chân Nhân.
- Rằm âm lịch, boss toàn cầu và ma tàng hình là tính năng live-ops tương lai. MVP có thể giả lập lịch dương đơn giản hoặc tạm chưa bật để tránh phụ thuộc backend/lịch âm.

## 9. Kỹ thuật và kiến trúc

### Quy tắc tổ chức mã nguồn (bắt buộc)

- **Không đưa toàn bộ app vào một file.** Tách trách nhiệm rõ ràng để mỗi file/component có phạm vi nhỏ, dễ kiểm thử và thay thế.
- Tổ chức theo feature, ví dụ `feature/splash`, `feature/menu`, `feature/hunt`, `feature/talisman`, `feature/result`, `feature/collection`; phần dùng chung đặt ở `core/ui`, `core/model`, `core/data` và `core/localization`.
- Mỗi màn hình gồm `Route` (quản lý state/navigation) và các composable UI nhỏ. Ví dụ màn Dán Bùa tách tối thiểu: `TalismanScreen`, `TalismanTimer`, `TalismanCanvas`, `TalismanScorer` và `TalismanResult`.
- Camera và cảm biến là adapter nền tảng tách khỏi UI; luật bắt ma, timer, chấm bùa và dữ liệu ma là logic thuần Kotlin có unit test.
- State dùng model/view-model theo feature; không truyền state mutable xuyên nhiều màn hình hoặc đặt business logic trong composable hiển thị.

### Localization (Việt / Anh trước)

- Mọi chuỗi hiển thị phải dùng resource/key localization, **không hard-code văn bản trong composable**.
- Ra mắt với `vi` là ngôn ngữ mặc định và `en` là ngôn ngữ thứ hai. MVP tôn trọng ngôn ngữ hệ thống; lựa chọn đổi ngôn ngữ trong Settings được thêm khi có màn Settings.
- Tên ma, bậc hiếm, mô tả, truyền thuyết, thông báo, store metadata và nội dung accessibility đều cần bản dịch Việt/Anh.
- Dùng format string/plural resource cho số lượng và thời gian. Không ghép chuỗi thủ công vì thứ tự từ khác nhau giữa hai ngôn ngữ.
- Thêm kiểm tra UI cho text dài tiếng Anh để không vỡ layout và content-description bản địa hóa cho mọi nút/đối tượng quan trọng.

### Stack

- **Kotlin Multiplatform + Compose Multiplatform.** Logic game, dữ liệu, UI dùng chung nằm ở `composeApp/src/commonMain`; tích hợp camera, quyền, thanh toán, quảng cáo và AR nằm ở `androidMain` / `iosMain`.
- Android phát hành trước. Cấu hình `compileSdk = 37`, `targetSdk = 36`; chọn `minSdk` phù hợp thư viện camera (đề xuất kiểm tra khi khởi tạo dự án).
- Camera MVP dùng preview nền tảng và overlay Compose/Canvas 2D. Không lưu ảnh/video camera mặc định.
- Lưu cục bộ: tiến trình, Bách Quỷ Lục, inventory, cooldown. Thiết kế repository để có thể đồng bộ backend sau này.

### Các màn hình MVP

1. Splash/onboarding và xin quyền camera, giải thích rõ quyền dùng để hiển thị nền khi chơi.
2. Màn Khai Nhãn: camera preview, trạng thái Dương khí, nút Khai Nhãn.
3. Màn gặp ma: overlay ma và chọn bùa.
4. Màn Dán Bùa: canvas vẽ, đồng hồ đếm ngược 10 giây, nút Quay lại, tính điểm và hiệu ứng kết quả/hết giờ.
5. Bách Quỷ Lục/kho ma: thẻ bắt ma, bậc hiếm, số lần bắt, độ khớp cao nhất và thời gian bắt gần nhất.
6. Túi bùa / vật phẩm và phần cài đặt (âm thanh, privacy policy, tắt quảng cáo nếu có entitlement).

### Rủi ro cần xử lý sớm

- Camera permission bị từ chối: cung cấp màn hướng dẫn mở Settings; không “giả” camera khi chưa được người dùng đồng ý.
- Chấm nét vẽ: xác định bộ test cho ngón tay nhanh/chậm, màn hình nhỏ/lớn và hỗ trợ accessibility; tránh điểm số cảm tính.
- Timer và navigation: chuyển màn/hủy lượt phải dừng coroutine đếm ngược; không để timer cũ tự kết thúc sau khi người chơi đã quay lại hoặc sang kết quả.
- Localization: thiết kế key và resource ngay từ màn đầu tiên; không để chuỗi tiếng Việt hard-code rồi mới dịch khi gần phát hành.
- Gỡ cài đặt hoặc đổi máy làm mất dữ liệu khi MVP chỉ lưu local: thông báo rõ, hoặc bổ sung đăng nhập/cloud save ở giai đoạn sau.
- Không dùng GPS giúp giảm quyền riêng tư, nhưng camera và AdMob vẫn phải được khai báo đúng trên Google Play.

## 10. Lộ trình

| Giai đoạn | Phạm vi | Kết quả |
|---|---|---|
| 0 — Pre-production | Prototype vẽ bùa, chốt UX, 20 concept, quyết định chỉ Android | Xác nhận core loop vui và hiệu năng camera |
| 1 — MVP (4–6 tuần) | Camera overlay 2D, bắt ma, 20 ma, 4 bùa, Bách Quỷ Lục local | Bản Android internal test |
| 2 | Chương truyện, daily quest, luyện hóa, boss local, thẻ chia sẻ | Closed test |
| 3 | Backend/live event, cloud save, AR bám vật thể, iOS, PvP nếu cần | Live operations |

## 11. Kiếm tiền và nguyên tắc công bằng

- Game miễn phí tải.
- Quảng cáo thưởng: hồi Dương khí hoặc nhận bùa, hoàn toàn theo lựa chọn của người chơi.
- IAP: gói bùa, vật phẩm tiện lợi, skin và battle pass tương lai.
- Bùa tốt nhất phải có đường kiếm được trong game; trả tiền giúp nhanh hơn nhưng không được chặn tiến trình bắt buộc.
- Chỉ đặt interstitial ở điểm ngắt tự nhiên sau khi một hành động hoàn tất; banner không được che điều khiển hay gây nhầm lẫn.

## 12. Google Play: yêu cầu thực hiện

### Cấu hình phát hành

- Application ID/package name chính thức: `com.vibecode.daosubatma`. Không đổi sau khi app đã phát hành.
- Kiểm tra `minSdk`, AndroidManifest, adaptive icon, splash screen và quyền camera trước closed test.
- Tăng `versionCode` với mọi AAB upload; cập nhật `versionName` theo bản hiển thị.
- Ký release bằng keystore riêng. Không commit `*.jks`, `*.keystore`, `keystore.properties`, mật khẩu hay API key.
- Tạo bundle bằng `./gradlew :composeApp:bundleRelease` và lấy AAB ở `build/outputs/bundle/release/`.

### Quảng cáo, dữ liệu và quyền riêng tư

- Dùng Google Mobile Ads SDK và test ad unit IDs ở debug; chỉ dùng ID thật ở release. Banner cố định ở cạnh dưới các màn Compose. Interstitial được preload khi lượt bắt kết thúc và chỉ hiển thị khi người chơi bấm sang lượt Khai Nhãn tiếp theo, không chen vào thao tác vẽ bùa.
- Nếu dùng AdMob, khai báo Advertising ID trong Play Console và điền Data safety đúng theo SDK/dữ liệu thực tế.
- Privacy Policy public qua HTTPS, ghi đúng tên app, package name, developer, email liên hệ, camera permission, dữ liệu lưu/thu thập, SDK bên thứ ba, mục đích, chia sẻ, lưu giữ và cách xóa dữ liệu.
- Dùng repository private cho source app; một repository public riêng có thể chứa trang Privacy Policy và `app-ads.txt` (khi AdMob yêu cầu), phát hành qua GitHub Pages.
- Phân loại nội dung phải dựa trên bản game thực tế và bảng câu hỏi của Play Console, không tự khẳng định rating cố định.

### Store listing cần chuẩn bị

- Tên ứng dụng, email developer, category, content rating và website.
- Short description tối đa 80 ký tự; full description chỉ mô tả các tính năng đã phát hành.
- App icon, feature graphic và 2–8 screenshots điện thoại đúng chuẩn Play Console.
- Privacy Policy URL mở công khai; kiểm tra trên thiết bị thật trước khi gửi duyệt.

## 13. Tiêu chí hoàn thành MVP

- Cài được AAB từ Internal testing, mở app và xin camera permission đúng luồng.
- Khai Nhãn hoạt động khi cấp quyền; ma spawn/không spawn theo tỉ lệ cấu hình.
- Vẽ bùa được chấm ổn định, có cả ba ngưỡng kết quả, một lượt bắt duy nhất, nút Quay lại hoạt động và timeout đúng 10 giây khiến ma tan biến.
- Mỗi lượt ma xuất hiện ở vị trí ngẫu nhiên an toàn trên màn hình; kết quả bắt thành công được lưu và hiển thị lại trong Bách Quỷ Lục sau khi mở lại app.
- Toàn bộ UI MVP có bản dịch Việt/Anh từ resource; đổi ngôn ngữ không làm vỡ layout hoặc reset tiến trình game.
- Mã nguồn tuân thủ tổ chức theo feature/component; không có file UI đơn lẻ chứa toàn bộ flow ứng dụng hay business logic của nhiều feature.
- 20 ma, 4 bậc hiếm, bùa, công lực/cấp bậc và Bách Quỷ Lục local hoạt động xuyên qua lần mở app tiếp theo.
- Không crash với camera; âm thanh có thể tắt; các luồng không có quảng cáo vẫn chơi được.
- Đã kiểm tra privacy policy, icon/splash, versioning và toàn bộ khai báo Play Console trước closed test.

## 14. Các quyết định còn cần chốt

1. Tên store chính thức: giữ **Đạo Sư Bắt Ma** hay đổi sang tên khác?
2. Dương khí: giới hạn lượt Khai Nhãn như thiết kế hiện tại hay cho quét miễn phí trong MVP?
3. Nội dung ma dân gian: giữ nguyên danh sách hay thêm ma sáng tạo hoàn toàn?
4. Boss Trừ Tà: chỉ vẽ bùa hay thêm một cơ chế chiến đấu riêng ở giai đoạn 2?
5. Có thực sự đầu tư PvP và AR che khuất vật thể sau khi MVP chứng minh được retention không?
