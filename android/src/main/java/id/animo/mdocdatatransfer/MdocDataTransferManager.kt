package id.animo.mdocdatatransfer

import android.annotation.SuppressLint
import android.content.Context
import com.android.identity.crypto.Algorithm
import eu.europa.ec.eudi.iso18013.transfer.TransferManager
import eu.europa.ec.eudi.iso18013.transfer.TransferManagerImpl
import eu.europa.ec.eudi.iso18013.transfer.engagement.BleRetrievalMethod
import eu.europa.ec.eudi.iso18013.transfer.response.DisclosedDocuments
import eu.europa.ec.eudi.iso18013.transfer.response.Request
import eu.europa.ec.eudi.iso18013.transfer.response.RequestedDocuments
import eu.europa.ec.eudi.iso18013.transfer.response.RequestProcessor
import eu.europa.ec.eudi.iso18013.transfer.response.ResponseResult
import eu.europa.ec.eudi.iso18013.transfer.response.device.DeviceRequest

/**
 * A minimal RequestProcessor that creates a pass-through ProcessedRequest.
 * This allows us to receive raw device request bytes without needing
 * a full DocumentManager implementation.
 */
class PassthroughRequestProcessor : RequestProcessor {
    override fun process(request: Request): RequestProcessor.ProcessedRequest {
        // We don't process the request on native side - just pass it to JS
        // Return a Success with empty requested documents
        return object : RequestProcessor.ProcessedRequest.Success(RequestedDocuments(emptyList())) {
            override fun generateResponse(
                disclosedDocuments: DisclosedDocuments,
                signatureAlgorithm: Algorithm?
            ): ResponseResult {
                // This won't be called - we handle response generation on JS side
                return ResponseResult.Failure(UnsupportedOperationException("Response generated on JS side"))
            }
        }
    }
}

@SuppressLint("StaticFieldLeak")
object MdocDataTransferManager {
    @Volatile
    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    val transferManager = lazy {
        TransferManagerImpl(
            context = context,
            requestProcessor = PassthroughRequestProcessor(),
            retrievalMethods = listOf(
                BleRetrievalMethod(
                    peripheralServerMode = true,
                    centralClientMode = true,
                    clearBleCache = true
                )
            )
        )
    }
}
