/*
 * Copyright (c) 2020 Touchlab
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package co.touchlab.kmmworker

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

actual class Worker actual constructor(){
    private val executor = Executors.newSingleThreadExecutor()
    actual fun <T> runBackground(backJob: () -> T): Future<T> {
        return Future(executor.submit(backJob) as java.util.concurrent.Future<T>)
    }

    actual fun requestTermination() {
        executor.shutdown()
        executor.awaitTermination(30, TimeUnit.SECONDS)
    }
}

actual class Future<T>(private val future: java.util.concurrent.Future<T>) {
    actual fun consume():T = future.get()
}

actual val mainThread: Boolean
    get() = Looper.getMainLooper() === Looper.myLooper()

actual fun <B> Worker.backgroundTask(
    backJob: () -> B,
    mainJob: (JobResult<B>) -> Unit
) {
    val result = try {
        Success(backJob())
    } catch (t: Throwable) {
        Error(t)
    } as JobResult<B>

    Handler(Looper.getMainLooper()).post {
        mainJob(result)
    }
}
