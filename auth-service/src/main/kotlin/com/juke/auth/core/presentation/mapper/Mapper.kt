package com.juke.auth.core.presentation.mapper

interface Mapper<Request, Params> {

    fun convert(request: Request): Params
}