# ĐẠO SƯ BẮT MA — Phác thảo Kịch bản Game (v1.0)

> Thể loại: Game bắt ma sưu tầm kiểu Pokémon — nhưng thay ném bóng bằng **dán bùa**.
> Nền tảng đề xuất: Mobile (Android/iOS). Phong cách: kinh dị hài hước (cute-horror).

---

## 1. Tổng quan

**Câu giới thiệu (high concept):** Pokémon GO gặp phim ma Hồng Kông. Người chơi vào vai đạo sư trẻ của phái Mao Sơn: mở app, giơ camera lên — bóng ma có thể hiện ra ngay trong khung hình thật, rồi dùng ngón tay **vẽ bùa theo vòng tròn mẫu** — vẽ càng khớp, bùa càng linh, ma càng dễ bị thu phục. Không bản đồ, không cần đi đâu xa — ma ở ngay quanh bạn.

**Điểm khác biệt cốt lõi:**
- Không ném bóng, không đánh theo lượt — kỹ năng của người chơi nằm ở **độ khéo tay vẽ bùa**.
- Ma quỷ lấy từ **kho tàng dân gian Việt Nam & Á Đông** (ma lai, ma da, cương thi, hồ ly…) — gần gũi, dễ viral.
- Càng bắt được ma hiếm, **công lực đạo sư** càng tăng — leo cấp bậc từ Đạo Đồng lên Đạo Tổ.

**Đối tượng:** 12+, fan truyện ma, fan game sưu tầm, chơi giải trí ngắn mỗi ngày.

---

## 2. Cốt truyện nền

Tháng 7 âm lịch, Quỷ Môn Quan mở. Chấn động lớn làm vỡ phong ấn **Trấn Ma Đài** — 108 con quỷ dữ thoát ra, trà trộn khắp nhân gian.

Người chơi là **đệ tử cuối cùng của phái Mao Sơn**, được sư phụ trao lại ba bảo vật:
- **Thiên Sư Bút** — vẽ bùa thu quỷ
- **Túi Càn Khôn** — đựng bùa các loại
- **Hồ Lô Thu Quỷ** — nhốt ma đã bắt

Nhiệm vụ: dùng camera khai nhãn ở bất cứ đâu, thu phục đủ 108 ma quỷ, phong ấn lại Trấn Ma Đài trước khi Quỷ Vương giáng thế.

*(Cốt truyện chia theo chương, mỗi chương mở khóa thêm loài ma mới + 1 con boss.)*

---

## 3. Vòng lặp gameplay chính (Core Loop)

```
Mở app → Giơ camera "khai nhãn" → Ma hiện ra (hoặc không) ở vị trí ngẫu nhiên trên màn hình
→ Chọn "DÁN BÙA" → Vẽ theo vòng tròn mẫu → Chấm điểm độ khớp
→ Trúng: bùa bay dán lên trán ma → Thu phục (+công lực)
→ Hụt: ma tan biến ngay lập tức
```

### Chi tiết một màn bắt ma

1. **Khai nhãn:** người chơi mở app, camera bật lên với khung cảnh thật xung quanh. Bấm "Khai Nhãn" (hoặc chờ vài giây tự quét) — hệ thống roll một lần: một bóng ma **có thể hoặc không** hiện ra ở vị trí ngẫu nhiên trên màn hình, kèm tên + bậc hiếm + cấp độ. Không thấy ma ("âm khí yên tĩnh") thì quét lại lượt khác. Tỉ lệ hiện ra và độ hiếm phụ thuộc giờ giấc (xem mục 7).
2. **Chọn bùa:** mở Túi Càn Khôn, chọn loại bùa muốn dùng (xem bảng bùa ở mục 6). Bùa xịn = tỉ lệ bắt cao hơn nhưng số lượng có hạn.
3. **Vẽ bùa:** màn hình hiện **lá bùa + vòng tròn mẫu màu vàng kim** ở giữa. Người chơi dùng ngón tay vẽ **một nét liền** theo vòng tròn mẫu.
   - Ma càng hiếm, vòng tròn càng khó: 2 vòng đồng tâm (vẽ 2 nét), vòng đứt quãng, vòng tròn **xoay chậm** theo thời gian.
4. **Chấm điểm độ khớp** (tính theo % nét vẽ trùng với vòng mẫu, có dung sai):
   - **≥ 90% — "BÙA HOÀN HẢO"**: tỉ lệ bắt +20%, thưởng thêm 50% công lực, hiệu ứng bùa rực sáng.
   - **70–89% — Bùa chuẩn:** bắt theo tỉ lệ của bùa.
   - **< 70% — Vẽ hụt:** bùa tắt ngúm, con ma **tan biến ngay lập tức**.
5. **Hiệu ứng thu phục:** lá bùa phóng to, xoay tít, "VÚT" bay thẳng vào mặt ma — **"BỐP!"** dán ngay giữa trán. Ma co rúm, bị hút xoáy vào lá bùa → chui vào Hồ Lô Thu Quỷ.
6. **Kết quả:** hiện bảng — tên ma, bậc hiếm, **+điểm công lực**, có lọt Bách Quỷ Lục hay trùng (trùng thì quy đổi, xem mục 9).

**Luật một cơ hội duy nhất:** mỗi lần ma hiện ra chỉ có đúng **1 lượt dán bùa**. Vẽ hụt = ma tan biến ngay, muốn bắt tiếp phải khai nhãn lại từ đầu. Căng tay, không được run!

---

## 4. Hệ thống Ma Quỷ — "Bách Quỷ Lục"

4 bậc hiếm (màu sắc hiển thị trong game):

| Bậc | Màu | Tỉ lệ bắt cơ bản | Điểm công lực |
|---|---|---|---|
| Phổ thông | Xám | 80% | 10 |
| Hiếm | Xanh dương | 50% | 40 |
| Sử thi | Tím | 30% | 150 |
| Huyền thoại | Vàng kim | 15% | 500 |

*Vẽ "Bùa Hoàn Hảo" (≥90%) cộng thêm 50% công lực.*

**Biến thể màu theo bậc hiếm (Hùng chốt 2026-10-12):** mỗi loài ma chỉ cần **1 thiết kế gốc** phong cách dark fantasy trưởng thành; bậc hiếm thể hiện bằng palette màu phủ khác nhau — Phổ thông: xám tro, Hiếm: xanh dương, Sử thi: tím, Huyền thoại: vàng kim rực. Tiết kiệm công vẽ, người chơi nhìn màu là biết độ hiếm ngay.

### Danh sách 20 loài ma (bản đầu)

**Phổ thông** — gặp khắp nơi, ban đêm:
1. **Ma Đói (Ngạ Quỷ)** — thân gầy như que củi, bụng phệ; hay xin ăn vặt của người chơi.
2. **Bóng Ma Lang Thang** — hồn vất vưởng mờ ảo, mặt buồn thiu.
3. **Ma Xó** — nhỏ con, chuyên giữ nhà, giữ của.
4. **Tiểu Yêu Tinh Nghịch** — tinh linh tí hon, thích trêu chọc, cười khanh khách.
5. **Vong Nhi** — ma trẻ con, hay chơi ú òa, khóc cười thất thường.

**Hiếm** — về đêm, giờ linh:
6. **Ma Da (Ma Nước)** — da xanh nhớt, tóc rong rêu; chuyên kéo chân người dưới nước.
7. **Bóng Đè** — đen thui, nặng trịch; đè người lúc nửa đêm.
8. **Cương Thi Nhỏ** — nhảy lò cò, đội mũ quan, dán bùa che mặt (trớ trêu: nó cũng bị dán bùa).
9. **Hồ Ly Một Đuôi** — cáo trắng mới tu luyện, mắt láo liên.
10. **Tinh Cây Cổ Thụ** — mặt người trên thân cây, rễ quấn quít.

**Sử thi** — giờ linh (nửa đêm, rằm), cấp cao:
11. **Ma Lai Rút Ruột** — cái đầu bay lơ lửng, ruột thòng lòng thòng; chỉ xuất hiện sau 12h đêm.
12. **Quỷ Một Chân (Sơn Tiêu)** — rừng núi, nhảy một chân nhanh như chớp.
13. **Bạch Cốt Tinh** — bộ xương trắng mặc áo dài, giả gái đẹp dụ người.
14. **Hồ Ly Chín Đuôi** — tu luyện ngàn năm, chín đuôi rực lửa, đẹp mê hồn.
15. **Thủy Quái Sông Sâu** — thân như trăn khổng lồ, vảy đen sì, chỉ hiện vào đêm mưa.

**Huyền thoại** — boss sự kiện / cuối chương:
16. **Quỷ Vương Dạ Xoa** — mặt xanh nanh vàng, chúa tể các vong hồn vất vưởng.
17. **Cương Thi Vương** — mặc hoàng bào, nhảy một cái rung đất.
18. **Chúa Quỷ Bóng Đêm** — không mặt, nuốt ánh sáng xung quanh.
19. **Long Mạch Yêu Vương** — rồng ma quấn quanh núi, vảy đen sì.
20. **Vô Diện Quỷ Vương** — **boss cuối**, mặt trắng bệch không ngũ quan, là kẻ phá vỡ Trấn Ma Đài.

---

## 5. Hệ thống Đạo Sư — Công lực & Cấp bậc

**Công lực** = tổng điểm từ ma đã thu phục. Càng bắt ma hiếm, lên cấp càng nhanh.

| Cấp bậc | Tổng công lực | Mở khóa |
|---|---|---|
| Đạo Đồng | 0 | Bùa Vàng |
| Pháp Sư | 500 | Bùa Đỏ, túi bùa mở rộng |
| Thiên Sư | 2.000 | Bùa Tím, bắt đầu gặp được ma Sử thi |
| Chân Nhân | 6.000 | Bùa Kim Cang, tỉ lệ gặp ma hiếm tăng |
| Đạo Tổ | 15.000 | Danh hiệu + skin đạo bào vàng, mở đấu pháp PvP |

**Dương khí:** mỗi lần khai nhãn tốn một ít Dương khí; ma hiếm giãy mạnh cũng làm hao tổn. Hết Dương khí → về **Đạo Quán** hồi phục (chờ hồi theo giờ hoặc xem quảng cáo hồi nhanh).

---

## 6. Hệ thống Bùa & Vật phẩm

| Bùa | Tỉ lệ cộng thêm | Dùng cho | Cách có |
|---|---|---|---|
| Bùa Vàng Trấn Tà | +0% | Ma phổ thông | Vô hạn (bùa cơ bản) |
| Bùa Đỏ Lôi Hỏa | +15% | Ma hiếm | Mua bằng tiền game / nhiệm vụ |
| Bùa Tím Tử Vi | +30% | Ma sử thi | Ghép từ bùa đỏ / boss rơi |
| Bùa Kim Cang | +50% | Ma huyền thoại | Sự kiện / IAP / thành tựu lớn |

**Vật phẩm hỗ trợ:**
- **Chuông Dẫn Hồn** — trong 30 phút, tỉ lệ ma hiện ra khi khai nhãn tăng gấp đôi.
- **Gương Bát Quái** — soi ra ma ẩn thân (một số ma hiếm ở dạng tàng hình, phải dùng gương mới thấy để dán bùa).
- **Đèn Lồng Dẫn Lối** — lần khai nhãn kế tiếp tỉ lệ gặp ma Hiếm trở lên tăng mạnh.
- **Cơm Cúng** — lần khai nhãn kế tiếp chắc chắn gặp Ma Đói (ưu tiên loài chưa có trong Bách Quỷ Lục).

**Nâng cấp bùa:** 3 bùa thường + nguyên liệu (ma trùng luyện hóa) → ghép thành 1 bùa bậc cao hơn.

---

## 7. Cơ chế xuất hiện ma (không bản đồ, không GPS)

- **Không vùng đất, không bản đồ.** Ở bất cứ đâu — trong nhà, ngoài đường, quán cà phê — người chơi mở app, giơ camera lên là có thể gặp ma.
- Mỗi lần **khai nhãn**, hệ thống roll một lần:
  - **Có ma hiện ra** (tỉ lệ cơ bản ~60%) ở **vị trí ngẫu nhiên** trên màn hình, hoặc
  - **Không có gì** ("âm khí yên tĩnh") — quét lại lượt khác.
- Tỉ lệ hiện ra và độ hiếm phụ thuộc **giờ giấc thực**:
  - **Ban ngày:** tỉ lệ thấp, chủ yếu ma Phổ thông.
  - **Ban đêm (18h–5h):** tỉ lệ cao, dễ gặp ma Hiếm.
  - **Đêm rằm (15 âm lịch):** sự kiện tự nhiên mỗi tháng — tỉ lệ ma Sử thi/Huyền thoại tăng mạnh.
  - **Sau 12h đêm:** Ma Lai Rút Ruột có thể xuất hiện.
- **Cấp đạo sư càng cao, càng dễ gặp ma hiếm:** ma Huyền thoại chỉ hiện ra với đạo sư từ cấp Chân Nhân trở lên.
- Ma hiếm có thể ở dạng **tàng hình** — phải dùng Gương Bát Quái mới thấy để dán bùa.

---

## 8. Nhiệm vụ & Sự kiện

- **Nhiệm vụ chương:** theo cốt truyện 108 ma, mỗi chương kết thúc bằng 1 con boss (vòng bùa cực khó — 2-3 vòng đồng tâm xoay).
- **Nhiệm vụ ngày/tuần:** "Bắt 5 Ma Đói", "Vẽ 3 Bùa Hoàn Hảo" — thưởng bùa, tiền game.
- **Boss Trừ Tà (giờ cố định):** ví dụ 20h thứ Bảy, boss huyền thoại hiện ra với **mọi người chơi đang mở app** — mỗi người 1 lượt dán bùa, thưởng theo độ khớp nét vẽ của mình.
- **Sự kiện tháng 10 — "Lễ Hội Ma Quái":** đúng chất tháng kinh dị — spawn ma x2 cả tháng, thêm ma giới hạn **Bí Ngô Tinh** và **Ma Cà Rồng Halloween**, skin đạo sư "Thợ Săn Ma".

---

## 9. Sưu tầm, Thẻ Ma & Khoe bạn bè

- **Bách Quỷ Lục:** sổ tay sưu tầm đủ 108 ma — mỗi con có tranh vẽ, truyền thuyết dân gian, thời điểm bắt được. Sưu tầm đủ bộ theo bậc hiếm → nhận danh hiệu + bùa hiếm.
- **Ma bắt trùng:** không bỏ đi — đem **luyện hóa** thành điểm công lực hoặc nguyên liệu ghép bùa.
- **Thành tựu:** "Tay Vẽ Vàng" (100 Bùa Hoàn Hảo), "Khắc Tinh Ma Lai", "Bắt đủ bộ Sử thi"…

### Thẻ Ma (Ghost Card) — để khoe bạn bè
**Toàn bộ ma bắt được đều có Thẻ Ma riêng:**
- **Mặt trước:** artwork ma + tên + bậc hiếm (khung màu theo bậc: xám cho Phổ thông, xanh/tím/vàng kim cho Hiếm/Sử thi/Huyền thoại) + số thứ tự Bách Quỷ Lục (ví dụ #047/108). Thẻ Phổ thông mẫu đơn giản; thẻ bậc cao khung cầu kỳ hơn, thẻ Huyền thoại thêm hiệu ứng lấp lánh (foil).
- **Chi tiết trong thẻ:** truyền thuyết dân gian 1–2 câu; chỉ số gồm công lực, cấp độ ma, **độ khớp nét vẽ lúc bắt** (ví dụ 96% — kèm huy hiệu "Bùa Hoàn Hảo" nếu ≥90%); thời điểm bắt ("bắt lúc 00:37 đêm rằm"); **độ hiếm thật** — tỉ lệ người chơi sở hữu (ví dụ "Chỉ 3,2% đạo sư bắt được").
- Thẻ Huyền thoại có khung vàng kim + hiệu ứng lấp lánh (foil).
- Nút **"Khoe"**: xuất ảnh thẻ → share thẳng lên Facebook/Zalo/TikTok hoặc lưu về máy. Mỗi ngày khoe lần đầu được thưởng bùa — vừa viral vừa giữ chân người chơi.

---

## 10. Gợi ý kiếm tiền (Monetization)

- Miễn phí tải, **quảng cáo thưởng**: xem QC nhận bùa đỏ, hồi Dương khí.
- **IAP:** gói bùa tím/kim cang, Chuông Dẫn Hồn, skin đạo bào, **Battle Pass "Mùa Săn Ma"** theo tháng.
- Nguyên tắc: bùa xịn nhất vẫn **farm được** qua chơi — nạp tiền chỉ nhanh hơn, không pay-to-win tuyệt đối.

---

## 11. Phong cách hình ảnh & Âm thanh

- **Đồ họa:** dark fantasy trưởng thành — ma rờn rợn, tỉ lệ người lớn, chi tiết (không chibi), hiện lồng vào **khung hình camera thật** (AR overlay), bùa vàng kim nổi bật. Mỗi loài ma 1 thiết kế gốc + biến thể màu theo bậc hiếm (xám/xanh/tím/vàng kim).
- **Nhân vật:** đạo sư trẻ mặc áo vàng, đội mũ, đeo kiếm gỗ đào sau lưng.
- **Âm thanh:** tiếng chuông mõ khi phát hiện ma, tiếng bút "xoẹt" khi vẽ, "VÚT — BỐP!" khi dán bùa trúng, nhạc nền u ám + trống dồn dập lúc bắt boss.

---

## 12. Lộ trình phát triển (đề xuất)

- **Giai đoạn 1 — MVP (4–6 tuần):** camera khai nhãn + core loop vẽ bùa bắt ma + 20 ma + 4 loại bùa + Bách Quỷ Lục cơ bản. (Không cần GPS/bản đồ nên MVP gọn nhẹ.)
- **Giai đoạn 2:** nhiệm vụ chương + boss + luyện hóa + nhiệm vụ ngày.
- **Giai đoạn 3:** AR nâng cao (ma bám theo vật thể thật, ma trốn sau đồ vật phải lia camera tìm), sự kiện, đấu pháp PvP.

---

## 13. Câu hỏi cần Hùng chốt / bổ sung

1. Danh sách 20 loài ma: giữ nguyên ma dân gian, hay muốn sáng tạo thêm ma mới hoàn toàn?
2. Mỗi lần khai nhãn tốn một ít Dương khí (chống spam quét) — Hùng thấy ổn không, hay cho quét thoải mái?
3. Boss "Trừ Tà": vẫn dùng cơ chế vẽ bùa, hay muốn thêm cơ chế đánh riêng?
4. Tên game chốt là **"Đạo Sư Bắt Ma"** luôn, hay cần đặt tên khác cho dễ lên store?
5. Có muốn thêm chế độ **đấu đạo sư (PvP)** — dùng ma đã bắt để đấu với nhau không, và chế độ ma "trốn sau đồ vật" phải lia camera tìm?

---
*Phác thảo v1.4 — 2026-10-12 (ma phong cách dark fantasy trưởng thành; bậc hiếm = biến thể màu). Hùng xem lại và bổ sung.*
