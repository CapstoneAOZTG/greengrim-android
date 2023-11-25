package com.aoztg.greengrim.presentation.ui.chat.mapper

import com.aoztg.greengrim.data.model.response.CertificationListResponse
import com.aoztg.greengrim.presentation.ui.chat.model.CertificationListData
import com.aoztg.greengrim.presentation.ui.chat.model.CertificationListItem

internal fun CertificationListResponse.toCertificationListData(onItemClickListener: (Int) -> Unit) = CertificationListData(
    page = page,
    hasNext = hasNext,
    result = result.map{
        CertificationListItem(
            id = it.certificationInfo.id,
            profileImg = it.memberSimpleInfo.profileImgUrl,
            nick = it.memberSimpleInfo.nickName,
            certificationImg = it.certificationInfo.imgUrl,
            certificationCount = it.certificationInfo.title,
            date = it.certificationInfo.createdAt,
            description = it.certificationInfo.description,
            onItemClickListener = onItemClickListener
        )
    }
)