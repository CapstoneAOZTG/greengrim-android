package com.aoztg.greengrim.presentation.ui.info.mapper


import com.aoztg.greengrim.data.model.response.MyCertificationListResponse
import com.aoztg.greengrim.presentation.ui.info.model.UiMyCertification
import com.aoztg.greengrim.presentation.ui.info.model.UiMyCertificationList
import com.aoztg.greengrim.presentation.ui.toCategoryText


internal fun MyCertificationListResponse.toUiMyCertificationList(onItemClickListener: (Int) -> Unit): UiMyCertificationList{
    return UiMyCertificationList(
        hasNext = this.hasNext,
        page = this.page,
        result = this.result.map{
            UiMyCertification(
                id = it.certificationInfo.id,
                categoryImg = it.challengeTitleInfo.imgUrl,
                title = it.challengeTitleInfo.title,
                category = it.challengeTitleInfo.category.toCategoryText(),
                certificationImg = it.certificationInfo.imgUrl,
                certificationCount = it.certificationInfo.title,
                date = it.certificationInfo.createdAt,
                description = it.certificationInfo.description,
                onItemClickListener = onItemClickListener
            )
        }
    )
}