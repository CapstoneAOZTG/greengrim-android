package com.aoztg.greengrim.presentation.ui.editgrim

object CompleteGrim {
    var grimState: GrimState = GrimState.NONE
    var grimId: Int? = -1
    var grimImgUrl: String? = ""
}

enum class GrimState{
    NONE,
    GRIM_DRAWED,
    GRIM_TITLED
}