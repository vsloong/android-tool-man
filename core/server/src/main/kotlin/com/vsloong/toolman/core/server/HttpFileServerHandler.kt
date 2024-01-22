package com.vsloong.toolman.core.server

import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.*
import io.netty.handler.stream.ChunkedFile
import io.netty.util.CharsetUtil
import java.io.File
import java.io.RandomAccessFile
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.nio.file.Path


class HttpFileServerHandler(
    private val localDirPath: Path
) : SimpleChannelInboundHandler<FullHttpRequest>() {
    override fun channelRead0(ctx: ChannelHandlerContext, request: FullHttpRequest) {
        val uri = request.uri()
        val path = sanitizeUri(uri)

        println("文件地址：$path")

        if (path == null) {
            sendError(ctx, HttpResponseStatus.FORBIDDEN)
            return
        }

        val file = File(path)
        if (file.isFile) {
            val response = DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, getContentType(file))
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length())
            response.headers().set(HttpHeaderNames.CONTENT_DISPOSITION, getContentDisposition(file))

            if (HttpUtil.isKeepAlive(request)) {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
            }

            //支持图片预览
//            val region = DefaultFileRegion(file, 0, file.length())

            // 支持文件下载
            val region = ChunkedFile(RandomAccessFile(file, "r"))
            ctx.write(response)
            ctx.writeAndFlush(region).addListener(ChannelFutureListener.CLOSE)

        } else {
            sendError(ctx, HttpResponseStatus.NOT_FOUND)
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        if (ctx.channel().isActive) {
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR)
        }
    }

    private fun sendError(ctx: ChannelHandlerContext, status: HttpResponseStatus) {
        val response = DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1,
            status,
            Unpooled.copiedBuffer("Failure: $status\r\n", CharsetUtil.UTF_8)
        )
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8")
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
    }

    private fun sanitizeUri(uri: String): String? {
        try {
            val decodedUri = URLDecoder.decode(uri, "UTF-8")
            if (decodedUri.isEmpty() || decodedUri[0] != '/') {
                return null
            }

            // Convert file separators.
            val separator = File.separatorChar
            return "$localDirPath$decodedUri"
        } catch (e: UnsupportedEncodingException) {
            throw Error(e)
        }
    }

    private fun getContentType(file: File): String {
        val fileName = file.name.lowercase()
        return when {
            fileName.endsWith(".png") -> "image/png"
            fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") -> "image/jpeg"
            fileName.endsWith(".gif") -> "image/gif"
            else -> "application/octet-stream"
        }
    }

    private fun getContentDisposition(file: File): String {
        return "attachment; filename=${URLEncoder.encode(file.name, StandardCharsets.UTF_8.toString())}"
    }
}