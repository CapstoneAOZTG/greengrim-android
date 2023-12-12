package com.aoztg.greengrim.presentation.ui.challenge.model

data class UiChallengeCategory(
    val icon: Int,
    val category: CategoryName,
    val point: String,
)

enum class CategoryName(
    val text: String,
    val value: String
){
    BLANK("",""),
    ECO_PRODUCT("에코 제품 사용","ECO_PRODUCT"),
    COOL_AND_HOT_LOOKING("쿨맵시 & 온맵시","COOL_AND_HOT_LOOKING"),
    PICK_UP_KING("줍킹","PICK_UP_KING"),
    GROWING_PLANT("식물 키우기","GROWING_PLANT"),
    DAILY("일상","DAILY"),
    ELECTRIC_CAR("전기차","ELECTRIC_CAR"),
    PUBLIC_TRANSPORTATION("대중교통 이용","PUBLIC_TRANSPORTATION"),
    MAINTAINING_TEMPERATURE("적정 온도 유지","MAINTAINING_TEMPERATURE"),
    RECYCLING("분리수거 라벨 제거","RECYCLING")
}
