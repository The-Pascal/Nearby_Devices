package com.solvabit.myapplication

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

private const val TAG = "BlueWorker"

class BlueWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        Log.d(TAG, "onCreate: CREATED")

        val timer = object: CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d(TAG, "onTick: SERVICE RUNNING")
            }

            override fun onFinish() {
                Log.d(TAG, "onFinish: FINISHED TIMER")
            }
        }
        timer.start()

        return Result.success()

    }
}