# KMM (Kotlin Mobile Multiplatform) Worker

Basic background process worker for Android and iOS.

> Check out [KaMP Kit](https://github.com/touchlab/KaMPKit) to get started developing for Kotlin Multiplatform

## What is this?

There are a number of ways in which you can execute work on multiple threads, but they're all a bit complex to set up 
or have a few caveats you need to keep in mind. While writing a doc on how to write concurrent code for KMM, we 
realized there wasn't just a basic background queue library. This is a basic background queue library.

## Plans?

We're not sure yet that we'll be making any serious updates or additions to this over time. We're currently using 
the multithreaded coroutines branch for our multithreaded code. Other great options include [CoroutineWorker](https://github.com/Autodesk/coroutineworker)
and [Reaktive](https://github.com/badoo/Reaktive). However, we do use some of this functionality with [TestHelp](https://github.com/touchlab/TestHelp), 
and may more formally support a version of this library in the future.

## Should you use it?

You can, but there are more complete options out there.

License
=======

    Copyright 2020 Touchlab, Inc.
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
