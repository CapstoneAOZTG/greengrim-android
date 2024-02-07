package com.aoztg.greengrim.presentation.ui.chat.mapper

import com.aoztg.greengrim.data.model.response.CertificationListResponse
import com.aoztg.greengrim.presentation.ui.chat.model.UiCertificationList
import com.aoztg.greengrim.presentation.ui.chat.model.UiCertificationItem

internal fun CertificationListResponse.toUiCertificationList(onItemClickListener: (Long) -> Unit) = UiCertificationList(
    page = page,
    hasNext = hasNext,
    result = result.map{
        UiCertificationItem(
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