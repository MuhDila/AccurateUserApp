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
        Result.Error(DataError.Remote.RequestTimeout)
    } catch (_: IOException) {
        Result.Error(DataError.Remote.NoInternet)
    } catch (e: HttpException) {
        when (e.code()) {
            in 500..599 -> Result.Error(DataError.Remote.Server)
            else -> Result.Error(DataError.Remote.Unknown)
        }
    } catch (e: CancellationException) {
        throw e
    } catch (_: Exception) {
        Result.Error(DataError.Remote.Unknown)
    }
}

fun <T> responseToResult(response: Response<T>): Result<T, DataError.Remote> {
    return when {
        response.isSuccessful -> {
            val body = response.body()
            if (body != null) {
                Result.Success(body)
            } else {
                Result.Error(DataError.Remote.Serialization)
            }
        }
        response.code() == 400 -> {
            val errorMsg = response.errorBody()?.string()?.trim('"', ' ', '\n', '\r')
            Result.Error(DataError.Remote.BadRequest(errorMsg))
        }
        response.code() == 401 -> Result.Error(DataError.Remote.Unauthorized)
        response.code() == 403 -> Result.Error(DataError.Remote.Forbidden)
        response.code() == 404 -> Result.Error(DataError.Remote.NotFound)
        response.code() == 408 -> Result.Error(DataError.Remote.RequestTimeout)
        response.code() == 429 -> Result.Error(DataError.Remote.TooManyRequests)
        response.code() in 500..599 -> Result.Error(DataError.Remote.Server)
        else -> Result.Error(DataError.Remote.Unknown)
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
