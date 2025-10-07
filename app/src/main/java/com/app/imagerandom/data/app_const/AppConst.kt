package com.app.imagerandom.data.app_const

enum class Genres(val id: Int, val displayName: String) {
    ACTION(28, "Hành động"),
    ADVENTURE(12, "Phiêu lưu"),
    ANIMATION(16, "Hoạt hình"),
    COMEDY(35, "Hài"),
    CRIME(80, "Tội phạm"),
    DOCUMENTARY(99, "Tài liệu"),
    DRAMA(18, "Chính kịch"),
    FAMILY(10751, "Gia đình"),
    FANTASY(14, "Giả tưởng"),
    HISTORY(36, "Lịch sử"),
    HORROR(27, "Kinh dị"),
    MUSIC(10402, "Âm nhạc"),
    MYSTERY(9648, "Bí ẩn"),
    ROMANCE(10749, "Tình cảm"),
    SCIENCE_FICTION(878, "Khoa học viễn tưởng"),
    TV_MOVIE(10770, "Phim truyền hình"),
    THRILLER(53, "Giật gân"),
    WAR(10752, "Chiến tranh"),
    WESTERN(37, "Miền viễn tây");

    companion object {
        fun fromId(id: Int): Genres? = entries.find { it.id == id }
    }
}
