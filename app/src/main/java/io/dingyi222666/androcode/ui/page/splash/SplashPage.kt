package io.dingyi222666.androcode.ui.page.splash

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import io.dingyi222666.androcode.R
import io.dingyi222666.androcode.ui.resource.theme.AndroCodeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashPage() {

    val iconBitmap = ResourcesCompat.getDrawable(
        LocalContext.current.resources,
        R.mipmap.ic_launcher_round, LocalContext.current.theme
    )?.let { drawable ->
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)


        bitmap
    } ?: ImageBitmap.imageResource(R.mipmap.ic_launcher_round).asAndroidBitmap()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        contentColor = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(160.dp)
                    .width(160.dp)
                    .clip(RoundedCornerShape(84.dp))
                    .align(Alignment.CenterHorizontally),
                bitmap = iconBitmap.asImageBitmap(),
                contentDescription = "icon"
            )

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .offset(
                        y = 70.dp,
                    /*    bottom = 0.dp,
                        start = 0.dp,
                        end = 0.dp*/
                    )
                    .align(Alignment.CenterHorizontally),

                )


            Text(
                text = "Loading...",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .offset(y = 160.dp)
                    .align(Alignment.CenterHorizontally)
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPagePreview() {
    AndroCodeTheme {
        SplashPage()
    }
}