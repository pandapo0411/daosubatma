package com.vibecode.daosubatma.core.model

import android.content.Context
import androidx.annotation.StringRes
import com.vibecode.daosubatma.R
import kotlin.random.Random
import org.json.JSONArray

data class GhostCardStats(
    val power: Int,
    val level: Int,
    val ownershipPercent: Float,
)

enum class GhostRarity(
    val assetSuffix: String,
    val captureRate: Float,
    @StringRes val labelRes: Int,
    val cardStats: GhostCardStats,
) {
    Common("common", .80f, R.string.rarity_common, GhostCardStats(power = 10, level = 3, ownershipPercent = 68.5f)),
    Rare("rare", .50f, R.string.rarity_rare, GhostCardStats(power = 60, level = 15, ownershipPercent = 27.6f)),
    Epic("epic", .30f, R.string.rarity_epic, GhostCardStats(power = 180, level = 27, ownershipPercent = 9.8f)),
    Legendary("legendary", .15f, R.string.rarity_legendary, GhostCardStats(power = 480, level = 42, ownershipPercent = 3.2f)),
}

data class GhostDefinition(
    val speciesId: String,
    val name: String,
    val rarity: GhostRarity,
    val lore: String,
) {
    val assetName: String get() = "${speciesId}_${rarity.assetSuffix}"
}

/** Resolves the species and rarity with the active Android locale. */
fun GhostDefinition.localizedName(context: Context): String {
    val entry = GhostNames.entries(context)[assetName] ?: return name
    return if (context.resources.configuration.locales[0].language == "vi") entry.vietnamese else entry.english
}

fun GhostDefinition.localizedLore(context: Context): String =
    if (context.resources.configuration.locales[0].language == "vi") lore
    else context.getString(R.string.ghost_lore_generic, localizedName(context))

private data class LocalizedGhostName(val vietnamese: String, val english: String)

/** The supplied 108-name catalogue is the source of truth for display names. */
private object GhostNames {
    @Volatile private var cached: Map<String, LocalizedGhostName>? = null

    fun entries(context: Context): Map<String, LocalizedGhostName> = cached ?: synchronized(this) {
        cached ?: context.resources.openRawResource(R.raw.ten_ma_108)
            .bufferedReader()
            .use { reader ->
                JSONArray(reader.readText()).let { names ->
                    buildMap {
                        for (index in 0 until names.length()) {
                            val ghost = names.getJSONObject(index)
                            put(
                                ghost.getString("id"),
                                LocalizedGhostName(
                                    vietnamese = ghost.getString("vi"),
                                    english = ghost.getString("en"),
                                ),
                            )
                        }
                    }
                }
            }
            .also { cached = it }
    }
}

private data class GhostArchetype(val id: String, val names: List<String>, val lore: String) {
    fun definition(rarity: GhostRarity) = GhostDefinition(id, names[rarity.ordinal], rarity, lore)
}

/** Catalogue from assets108/danh-sach-108-ma.md: 27 archetypes × 4 rarities. */
object GhostCatalog {
    private val archetypes = listOf(
        GhostArchetype("ma_doi", listOf("Ma Đói Vất Vưởng", "Ngạ Quỷ Đói Khát", "Quỷ Đói Ngàn Năm", "Thao Thiết Quỷ Vương"), "Vất vưởng nơi chợ đêm, chỉ mong một bữa cơm cúng no lòng."),
        GhostArchetype("ma_da", listOf("Ma Da Kéo Chân", "Thủy Quỷ Rêu Xanh", "Oán Hồn Sông Sâu", "Thủy Mẫu Tinh"), "Ẩn dưới dòng nước tối, chờ tiếng gọi lạc bên bờ."),
        GhostArchetype("cuong_thi", listOf("Cương Thi Lò Cò", "Cương Thi Giáp Sĩ", "Cương Thi Tướng Quân", "Cương Thi Vương"), "Thi thể bị âm khí níu giữ, chỉ biết nhảy theo hơi thở người sống."),
        GhostArchetype("ma_lai", listOf("Ma Lai Rút Ruột", "Dạ Ma Phi Đầu", "Phi Đầu Huyết Ma", "Cửu Âm Ma Hậu"), "Một bóng đêm cổ xưa, thoắt ẩn thoắt hiện phía ngoài ngọn đèn."),
        GhostArchetype("ho_ly", listOf("Hồ Ly Một Đuôi", "Hồ Ly Tam Vĩ", "Hồ Ly Cửu Vĩ", "Cửu Vĩ Thiên Hồ"), "Yêu hồ tinh ranh, giỏi mượn vẻ đẹp để thử lòng người."),
        GhostArchetype("quy_vuong", listOf("Dạ Xoa Tuần Dạ", "Dạ Xoa Chiến Tướng", "Quỷ Vương Dạ Xoa", "Đại Ma Vương Dạ Xoa"), "Kẻ canh gác cõi u minh, mang theo tiếng trống trận trong đêm."),
        GhostArchetype("bong_de", listOf("Bóng Đè Nặng Trịch", "Ám Ma Đè Ngực", "Mộng Yểm Quỷ", "Đại Mộng Ma Quân"), "Nó đến khi nửa tỉnh nửa mơ, phủ bóng nặng lên lồng ngực."),
        GhostArchetype("ma_xo", listOf("Ma Xó Giữ Nhà", "Hộ Gia Tiểu Quỷ", "Trấn Trạch Linh", "Thần Giữ Của"), "Linh thể trú nơi góc nhà, vừa giữ của vừa thích trêu người lạ."),
        GhostArchetype("vong_nhi", listOf("Vong Nhi Khóc Đêm", "Tiểu Quỷ Ú Òa", "Oán Nhi Bạch Y", "Anh Linh Cửu U"), "Tiếng khóc trẻ thơ lạc trong gió, mong được một lời ru yên ổn."),
        GhostArchetype("bach_cot", listOf("Bạch Cốt Lẩn Khuất", "Khô Lâu Dạ Hành", "Bạch Cốt Phu Nhân", "Khô Lâu Ma Vương"), "Bộ xương lang thang qua nghĩa địa, còn nhớ lời hẹn chưa thành."),
        GhostArchetype("son_tieu", listOf("Sơn Tiêu Một Chân", "Sơn Quỷ Nhảy Xa", "Lão Sơn Tinh", "Sơn Thần Hắc Diện"), "Sơn linh một chân chuyên dẫn người đi lạc vào rừng sâu."),
        GhostArchetype("thuy_quai", listOf("Thủy Quái Rình Mồi", "Giao Long Nước Đục", "Hắc Thủy Yêu Vương", "Hải Ma Vô Đáy"), "Dấu vết của nó là mặt nước phẳng lặng bất thường trước cơn sóng."),
        GhostArchetype("tinh_cay", listOf("Tinh Cây Cổ Thụ", "Mộc Linh Ngàn Năm", "Đại Thụ Yêu", "Thần Mộc Cửu Thiên"), "Cây già hấp thụ nguyệt khí, lặng im quan sát trăm năm đổi thay."),
        GhostArchetype("nguu_dau", listOf("Ngưu Đầu Ngục Tốt", "Ngưu Đầu Dẫn Hồn", "Ngưu Ma Chiến Sĩ", "Ngưu Ma Vương"), "Ngục tốt đầu bò dẫn linh hồn đi qua con đường không quay lại."),
        GhostArchetype("ma_dien", listOf("Mã Diện Ngục Tốt", "Mã Diện Truy Hồn", "Mã Diện Tướng Quân", "Thiên Mã Quỷ Vương"), "Ngục tốt mặt ngựa nghe thấy lời thề bị bội ước từ rất xa."),
        GhostArchetype("tieu_yeu", listOf("Tiểu Yêu Tinh Nghịch", "Ranh Ma Phá Phách", "Tiểu Ma Đầu Sỏ", "Tổ Yêu Nghịch Thiên"), "Một tiểu yêu tinh quái, để lại tiếng cười trước khi biến mất."),
        GhostArchetype("bong_ma", listOf("Bóng Ma Vất Vưởng", "Du Hồn Không Nhà", "Oán Hồn Lang Thang", "U Minh Quỷ Đế"), "Du hồn không tìm được lối về, thường lần theo ánh đèn ven đường."),
        GhostArchetype("ma_guong", listOf("Ma Trong Gương", "Kính Ma Phản Chiếu", "Gương Yêu Đoạt Hồn", "Huyền Kính Ma Vương"), "Đừng nhìn quá lâu vào gương cũ khi phía sau bạn không có ai."),
        GhostArchetype("nhap_trang", listOf("Xác Sống Nhập Tràng", "Thi Ma Hoang Dã", "Cương Thi Hoang", "Thi Vương Bất Diệt"), "Thi thể thức dậy khi tiếng mèo đen vọng qua gian nhà vắng."),
        GhostArchetype("tinh_da", listOf("Tinh Đá Núi", "Thạch Linh Trầm Mặc", "Nham Thạch Cự Ma", "Thái Sơn Thạch Vương"), "Tinh linh sinh từ đá núi, chậm rãi nhưng bền bỉ như vách đá."),
        GhostArchetype("hoa_yeu", listOf("Hoa Yêu Dụ Hoặc", "Mỹ Nhân Hoa Độc", "Hoa Ma Ngàn Cánh", "Mẫu Đơn Yêu Hậu"), "Hương hoa quá ngọt trong đêm thường che giấu một lời mời nguy hiểm."),
        GhostArchetype("co_hon", listOf("Cô Hồn Dã Quỷ", "Dã Quỷ Không Nơi Nương Tựa", "Bách Quỷ Dạ Hành", "Quỷ Vương Cô Hồn"), "Cô hồn không nơi nương tựa, mong một nén hương giữa đêm rằm."),
        GhostArchetype("ma_khoi", listOf("Ma Khói Hương Tàn", "Vô Hình Yên Ma", "Hắc Yên Quỷ", "Vô Gian Yên Vương"), "Tàn hương vừa tắt, làn khói có thể mang theo một cái tên đã quên."),
        GhostArchetype("da_xoa", listOf("Dạ Xoa Tuần Tra", "Dạ Xoa Hộ Pháp", "Kim Cang Dạ Xoa", "Đại Dạ Xoa Vương"), "Dạ xoa hộ pháp tuần tra nơi ranh giới giữa người sống và cõi âm."),
        GhostArchetype("ma_den", listOf("Ma Đèn Lồng Dẫn Lối", "Hồn Đèn Ma Trơi", "U Linh Đăng Hỏa", "Vạn Đăng Quỷ Vương"), "Ngọn đèn trôi lơ lửng luôn hứa hẹn một con đường, nhưng hiếm khi là đường về."),
        GhostArchetype("oan_linh", listOf("Oán Linh Tử Sĩ", "Chiến Hồn Bất Khuất", "Huyết Chiến Quỷ Tướng", "Tu La Chiến Vương"), "Chiến hồn còn giữ thanh gươm cũ, chờ ngày minh oan cho lời thề dang dở."),
        GhostArchetype("mi_anh", listOf("Mị Ảnh Sương Mờ", "Vụ Ẩn Yêu", "Mê Vụ Ma Nữ", "Cửu U Vụ Hậu"), "Một làn sương biết gọi tên người đi lạc, rồi nuốt mất lối về."),
    )

    fun randomEncounter(): GhostDefinition = archetypes.random().definition(randomRarity())

    fun find(speciesId: String, rarity: GhostRarity): GhostDefinition =
        (archetypes.firstOrNull { it.id == speciesId }
            ?: archetypes.first { it.id == "bong_ma" })
            .definition(rarity)

    private fun randomRarity(): GhostRarity = when (Random.nextInt(100)) {
        in 0..64 -> GhostRarity.Common
        in 65..88 -> GhostRarity.Rare
        in 89..97 -> GhostRarity.Epic
        else -> GhostRarity.Legendary
    }
}
