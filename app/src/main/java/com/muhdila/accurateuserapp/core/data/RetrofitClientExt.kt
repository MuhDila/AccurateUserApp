package com.muhdila.accurateuserapp.core.data

import android.database.sqlite.SQLiteFullException
import com.muhdila.accurateuserapp.core.domain.DataError
import com.muhdila.accurateuserapp.core.domain.Result
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> Response<T>
): Result<T, DataError.Remote> {
    return try {
        val response = apiCall()
        responseToResult(response)
    } catch (_: SocketTimeoutException) {
        Result.Error(DataError.Remote.REQUEST_TIMEOUT)
    } catch (_: IOException) {
        Result.Error(DataError.Remote.NO_INTERNET)
    } catch (e: HttpException) {
        when (e.code()) {
            in 500..599 -> Result.Error(DataError.Remote.SERVER)
            else -> Result.Error(DataError.Remote.UNKNOWN)
        }
    } catch (e: CancellationException) {
        throw e
    } catch (_: Exception) {
        Result.Error(DataError.Remote.UNKNOWN)
    }
}

fun <T> responseToResult(response: Response<T>): Result<T, DataError.Remote> {
    return when {
        response.isSuccessful -> {
            val body = response.body()
            if (body != null) {
                Result.Success(body)
            } else {
                Result.Error(DataError.Remote.SERIALIZATION)
            }
        }
        response.code() == 400 -> Result.Error(DataError.Remote.BAD_REQUEST)
        response.code() == 401 -> Result.Error(DataError.Remote.UNAUTHORIZED)
        response.code() == 403 -> Result.Error(DataError.Remote.FORBIDDEN)
        response.code() == 404 -> Result.Error(DataError.Remote.NOT_FOUND)
        response.code() == 408 -> Result.Error(DataError.Remote.REQUEST_TIMEOUT)
        response.code() == 429 -> Result.Error(DataError.Remote.TOO_MANY_REQUESTS)
        response.code() in 500..599 -> Result.Error(DataError.Remote.SERVER)
        else -> Result.Error(DataError.Remote.UNKNOWN)
    }
}

suspend inline fun <T> safeDbCall(
    crossinline block: suspend () -> T
): Result<T, DataError.Local> {
    return try {
        Result.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (_: SQLiteFullException) {
        Result.Error(DataError.Local.DISK_FULL)
    } catch (_: IOException) {
        Result.Error(DataError.Local.UNKNOWN)
    } catch (_: Exception) {
        Result.Error(DataError.Local.UNKNOWN)
    }
}
