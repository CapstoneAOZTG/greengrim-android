package com.aoztg.greengrim.presentation.ui.home.model

data class HomeUiModel(
    val imgUrl : String?="",
    val title : String?="",
    val chipList : List<ChipModel>?=null,
    val profileImg : String?="",
    val userName : String?="",
    val carbon : String?="",
    val textDetail : String?="",
    val point : String?=""
)

data class ChipModel(
    val text : String,
    val color : String
)
