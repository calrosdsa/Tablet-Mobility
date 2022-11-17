package com.coppernic.mobility.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.coppernic.mobility.R

@Composable
fun ImageUserComponent(
    model:Any?,
    description:String?,
    modifier: Modifier = Modifier,
//    transform: (AsyncImagePainter.State) -> AsyncImagePainter.State = AsyncImagePainter.DefaultTransform,
//    onState: ((AsyncImagePainter.State) -> Unit)? = null,
    requestBuilder: (ImageRequest.Builder.() -> ImageRequest.Builder)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
//    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality
){
//    if(model as String != ""){
//        Log.d("DEBUG_D",model.toString())
        coil.compose.AsyncImage(
        model = requestBuilder?.let { builder ->
            when (model) {
                is ImageRequest -> model.newBuilder()
                else -> ImageRequest.Builder(LocalContext.current).data(model)
            }.apply { this.builder() }.build()
        } ?: model,
        contentDescription = description?: "Image $model",
        modifier = modifier
            .clip(RoundedCornerShape(10.dp)),
//        transform = transform,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        error = painterResource(R.drawable.profile),
        fallback = painterResource(R.drawable.profile),
//            onError = {
//                it.painter
//            }
//        placeholder = painterResource(R.drawable.profile),
//        onState = onState,
//        filterQuality = filterQuality
    )
//    } else{
//        Log.d("DEBUG_D",model.toString())
//        Image(painter = painterResource(id = R.drawable.profile),
//            contentDescription =description?:"Image $model" ,
//            modifier = modifier,
//            contentScale = contentScale,
//            alpha = alpha,
//            colorFilter = colorFilter,
//            )
//    }

}
