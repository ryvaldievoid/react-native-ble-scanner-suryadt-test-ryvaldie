package com.atech.android.domain

import io.reactivex.Scheduler

interface PostExecutionThread {
    fun getScheduler(): Scheduler
}