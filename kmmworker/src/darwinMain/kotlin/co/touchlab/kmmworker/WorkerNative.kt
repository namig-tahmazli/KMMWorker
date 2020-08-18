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

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.staticCFunction
import platform.Foundation.NSThread
import platform.darwin.dispatch_async_f
import platform.darwin.dispatch_get_main_queue
import kotlin.native.concurrent.DetachedObjectGraph
import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.attach
import kotlin.native.concurrent.freeze

actual class Worker actual constructor(){
    val worker = kotlin.native.concurrent.Worker.start()
    actual fun <T> runBackground(backJob: () -> T): Future<T> {
        return Future(worker.execute(TransferMode.SAFE, {backJob.freeze()}){
            it()
        })
    }

    actual fun requestTermination() {
        worker.requestTermination().result
    }
}

actual class Future<T>(private val future: kotlin.native.concurrent.Future<T>) {
    actual fun consume():T = future.result
}

actual val mainThread: Boolean
    get() = NSThread.isMainThread

actual fun <B> Worker.backgroundTask(backJob: () -> B, mainJob: (JobResult<B>) -> Unit) {
    val ref = StableRef.create(mainJob).freeze()
    runBackground {
        val result = try {
            Success(backJob())
        } catch (t: Throwable) {
            Error(t)
        }

        val pair = Pair(result, ref).freeze()

        dispatch_async_f(dispatch_get_main_queue(), DetachedObjectGraph {
            pair
        }.asCPointer(), staticCFunction { it: COpaquePointer? ->
            initRuntimeIfNeeded()
            val pair = DetachedObjectGraph<Any>(it).attach() as Pair<JobResult<B>, StableRef<(JobResult<B>) -> Unit>>
            val result = pair.first
            val stableRef = pair.second
            val job = stableRef.get()
            stableRef.dispose()
            job(result)
        })
    }
}
