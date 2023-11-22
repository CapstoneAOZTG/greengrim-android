package com.aoztg.greengrim.presentation.ui.info.mapper


import com.aoztg.greengrim.data.model.response.MyCertificationListResponse
import com.aoztg.greengrim.presentation.ui.info.model.MyCertification
import com.aoztg.greengrim.presentation.ui.info.model.MyCertificationListData
import com.aoztg.greengrim.presentation.util.toCategoryText


internal fun MyCertificationListResponse.toMyCertificationListData(onItemClickListener: (Int) -> Unit): MyCertificationListData{
    return MyCertificationListData(
        hasNext = this.hasNext,
        page = this.page,
        result = this.result.map{
            MyCertification(
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