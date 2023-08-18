package com.juke.profileservice.core.presentation.mapper

interface Mapper<Request, Params> {

    fun convert(request: Request): Params
}