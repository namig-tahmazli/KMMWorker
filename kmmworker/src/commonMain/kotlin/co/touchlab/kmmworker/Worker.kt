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

expect class Worker() {
    fun <T> runBackground(backJob: () -> T): Future<T>
    fun requestTermination()
}

expect class Future<T> {
    fun consume(): T
}

expect val mainThread: Boolean

fun assertMainThread() {
    if (!mainThread)
        throw IllegalStateException("Must be on main thread")
}

fun assertNotMainThread() {
    if (mainThread)
        throw IllegalStateException("Must not be on main thread")
}

sealed class JobResult<out B:Any>
data class Success<out B:Any>(val result: B) : JobResult<B>()
data class Error(val thrown: Throwable) : JobResult<Nothing>()

expect fun <B:Any> Worker.backgroundTask(backJob: () -> B, mainJob: (JobResult<B>) -> Unit)

