package io.bewsys.spmobile.util



sealed class ApiResult<T>(
    val data: T? = null,
    val error: Throwable? = null,
    val errorMsg: String? = null

) {
    class Success<T>(data: T) : ApiResult<T>(data)
    class Loading<T>(data: T? = null) : ApiResult<T>(data)
    class Failure<T>(data: T? = null, errorMsg: String) : ApiResult<T>(data)
    class Exception<T>(throwable: Throwable, data: T? = null) : ApiResult<T>(data, throwable)
}