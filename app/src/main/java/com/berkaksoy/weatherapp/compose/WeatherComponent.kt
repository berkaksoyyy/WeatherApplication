package com.berkaksoy.weatherapp.compose

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.berkaksoy.weatherapp.R
import com.berkaksoy.weatherapp.data.WeatherResponse
import com.berkaksoy.weatherapp.events.WeatherUiAction

@Composable
fun WeatherComponent(
    weatherData: WeatherResponse?,
    forecastMap: MutableMap<String, List<Double?>>?,
    gifId: Int?,
    handleUiAction: (WeatherUiAction) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CurrentWeatherView(weatherData, gifId, handleUiAction)
        Divider(thickness = 2.dp, color = Color.Black)
        CurrentWeatherExtraInfoView(weatherData)
        Divider(thickness = 2.dp, color = Color.Black)
        ForecastComponent(forecastMap)
    }
}

@Composable
fun CurrentWeatherView(
    data: WeatherResponse?,
    gifId: Int?,
    handleUiAction: (WeatherUiAction) -> Unit
) {
    Column {
        Box(
            Modifier.background(colorResource(id = R.color.yellow))
        ) {
            IconButton(
                modifier = Modifier
                    .height(30.dp)
                    .padding(top = 5.dp),
                onClick = { handleUiAction(WeatherUiAction.WeatherDataCleared("")) }
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = "")
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                text = "${data?.name}",
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
        Divider(thickness = 2.dp, color = Color.Black)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            gifId?.let { GifImage(modifier = Modifier, drawable = gifId) }

            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    text = " ${String.format("%.1f", data?.main?.temp)}°C",
                    color = Color.Black,
                    textAlign = TextAlign.End
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp),
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    text = "Short Term Forecast:",
                    color = Color.Black,
                    textAlign = TextAlign.End
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp, bottom = 10.dp),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    text = "${String.format("%.1f", data?.main?.tempMax)}/${
                        String.format(
                            "%.1f",
                            data?.main?.tempMin
                        )
                    }",
                    color = Color.Black,
                    textAlign = TextAlign.End
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp, bottom = 10.dp),
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic,
                    text = "${data?.weather?.firstOrNull()?.description}",
                    color = Color.Black,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
fun CurrentWeatherExtraInfoView(
    data: WeatherResponse?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.yellow)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp),
            fontSize = 14.sp,
            text = stringResource(R.string.humidity, String.format("%.1f", data?.main?.humidity)),
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp),
            fontSize = 14.sp,
            text = stringResource(R.string.feels_like, String.format("%.1f", data?.main?.feelsLike)),
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp),
            fontSize = 14.sp,
            text = stringResource(R.string.wind, String.format("%.1f", data?.wind?.speed)),
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    drawable: Int
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        horizontalAlignment = Alignment.Start
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(context).data(data = drawable).build(),
                imageLoader = imageLoader
            ),
            contentDescription = null,
            modifier = modifier
                .width((LocalConfiguration.current.screenWidthDp / 2).dp)
                .aspectRatio(1f),
        )
    }
}

@Composable
fun ForecastComponent(forecastMap: MutableMap<String, List<Double?>>?) {
    LazyColumn {
        forecastMap?.forEach { mapItem ->
            item {
                Column {
                    Card(
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Row(
                                Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 10.dp, top = 5.dp, bottom = 5.dp),
                                    text = mapItem.key,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                )
                                Text(
                                    modifier = Modifier.padding(end = 10.dp, top = 5.dp, bottom = 5.dp),
                                    text = "${String.format("%.1f", mapItem.value[0])}/" +
                                            String.format("%.1f", mapItem.value[1]),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}