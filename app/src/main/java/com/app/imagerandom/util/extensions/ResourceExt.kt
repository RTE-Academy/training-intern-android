package com.app.imagerandom.util.extensions

import com.app.imagerandom.util.Resource

fun <T> Resource<T>.dataOrNull(): T? {
    return if (this is Resource.Success) this.data else null
}