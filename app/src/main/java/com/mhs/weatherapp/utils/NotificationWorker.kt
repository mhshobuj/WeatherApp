package com.mhs.weatherapp.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.mhs.weatherapp.R
import com.mhs.weatherapp.view.MainActivity
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.roundToInt

class NotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val temp = inputData.getDouble("TEMP", 0.0)
        val icon = inputData.getString("ICON")
        // Convert temperatures from Kelvin to Celsius
        val celsiusTemp: Double = (temp - 273.15)
        val image_url = "https://openweathermap.org/img/wn/$icon@2x.png"

        showNotification(celsiusTemp, image_url)
        return Result.success()
    }


    private fun showNotification(temp: Double, image_url: String) {
        val notificationId = 1
        val channelId = "weather_update_channel"

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Create a RemoteViews object and set it to custom layout
        val remoteViews = RemoteViews(applicationContext.packageName, R.layout.notification_custom_layout)
        remoteViews.setTextViewText(R.id.tvTitle, "WeatherApp")
        remoteViews.setTextViewText(R.id.tvTemp, "Current Temperature: ${temp.roundToInt()}°C")

        // Load the image asynchronously using Coil or any other image loading library
        Glide.with(applicationContext)
            .asBitmap()
            .load(image_url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?) {
                    remoteViews.setImageViewBitmap(R.id.imageView_weather, resource)

                    // Build the notification with custom layout
                    val builder = NotificationCompat.Builder(applicationContext, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("WeatherApp")
                        .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                        .setCustomContentView(remoteViews)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)

                    // Create notification channel for Android O and above
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val name = "Weather Update Channel"
                        val descriptionText = "This channel provides weather updates."
                        val importance = NotificationManager.IMPORTANCE_HIGH
                        val channel = NotificationChannel(channelId, name, importance).apply {
                            descriptionText
                        }
                        val notificationManager: NotificationManager =
                            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.createNotificationChannel(channel)
                    }

                    // Notify
                    with(NotificationManagerCompat.from(applicationContext)) {
                        if (ActivityCompat.checkSelfPermission(
                                applicationContext,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return
                        }
                        notify(notificationId, builder.build())
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle when the image load is cleared
                }
            })
    }
}

