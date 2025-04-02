package com.example.weatherapp.alertscreen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherapp.R
import com.example.weatherapp.data.local.CityDatabase
import com.example.weatherapp.data.local.CityLocationLocalDataSourceImpl
import com.example.weatherapp.data.models.WeatherAlert
import com.example.weatherapp.data.remote.RetrofitHelper
import com.example.weatherapp.data.remote.WeatherRemoteDataSourceImpl
import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        val repo = WeatherRepositoryImpl.getInstance(
            WeatherRemoteDataSourceImpl(
                RetrofitHelper.service
            ),
            CityLocationLocalDataSourceImpl(
                CityDatabase.getInstance(applicationContext).getCityDao()
            )
        )

        Log.d("TAG", "Work triggered")

         val alertJson = inputData.getString("alertData") ?: "{}"
        val gson = Gson()
        val alert: WeatherAlert = gson.fromJson(alertJson, WeatherAlert::class.java)
            val id = repo.getAlertByTime(alert.startDate,alert.endDate)
        Log.d("TAG", "Deserialized Alert: $alert")
        Log.d("TAG", "Deserialized Alert: $id")


        val notificationId = inputData.getInt("notificationId", System.currentTimeMillis().toInt())
        val channelId = "weather_alert_channel"
        val description = inputData.getString("des") ?: "No Data"
        val alertId = inputData.getInt("id", -1)

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Weather Alerts",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        val cancelIntent = Intent(applicationContext, NotificationReceiver::class.java).apply {
            action = "CANCEL_NOTIFICATION"
            putExtra("alertId",alertId)
            putExtra("notificationId", notificationId)
        }

        val cancelPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            alertId,
            cancelIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )



        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Weather Update for ${alert.cityName}")
            .setContentText("Weather is ${description} for this duration!")
            .setSmallIcon(R.drawable.my_ghaith3)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.my_ghaith3, "Cancel", cancelPendingIntent)
            .build()

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)

        CoroutineScope(Dispatchers.IO).launch {
            repo.deleteAlertById(alertId)
        }

        Log.d("NotificationWorker", "Notification displayed with weather details")

//        if (alert.endDate > 0) {
//            val handler = Handler(Looper.getMainLooper())
//            handler.postDelayed({
//                notificationManager.cancel(notificationId)
//                Log.d("TAG", "Sending alertId: ${alert.id} to notification cancel intent")
//
//                CoroutineScope(Dispatchers.IO).launch {
//                    repo.deleteAlertById(alertId)
//                    Log.i("TAG", "doWork:alert.id ++>${alert.id}")
//                    Log.i("TAG", "doWork: alertId =>${alertId}")
//                }
//                Log.d("TAG", "Received alertId: $alert.id for deletion")
//
//            }, alert.endDate - alert.startDate)
//        }

        return Result.success()
    }
}


class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "CANCEL_NOTIFICATION") {
            val alertId = intent.getIntExtra("alertId", -1)
            val notificationId = intent.getIntExtra("notificationId", -1)
            Log.d("TAG", "Received cancel for Alert ID: $alertId")

//            if (context != null && alertId != -1) {
//                val repo = WeatherRepositoryImpl.getInstance(
//                    WeatherRemoteDataSourceImpl(
//                        RetrofitHelper.service
//                    ),
//                    CityLocationLocalDataSourceImpl(
//                        CityDatabase.getInstance(context).getCityDao()
//                    )
//                )
//
//                CoroutineScope(Dispatchers.IO).launch {
//                 val res = repo.deleteAlertById(alertId)
//
//                    val alert = repo.getAlertByID(alertId)
//                    if (alert == null) {
//                        Log.d("TAG", "No alert found with ID: $alertId")
//                    } else {
//                        Log.d("TAG", "Alert found, deleting...")
//                        val rowsDeleted = repo.deleteAlertById(alertId)
//                        Log.d("TAG", "Deleted $rowsDeleted rows from Room")
//                    }
//
//
//                    Log.d("TAG", "Deleted $alertId rows from Room")
//                    Log.d("TAG", "Deleted $res RESULT")
//
//                }
//            }

             val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId)
        }
    }
}