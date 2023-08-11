package com.juke.notification.core.presentation.mapper

interface Mapper<Request, Params> {

    fun convert(request: Request): Params
}