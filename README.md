# WeatherApp
# Insert your Google Maps API key in the meta_data section of the manifest file.
# Configure the notification settings in the MainActivity class to receive notifications at your preferred time.

Used stack of project

#MVVM: The MVVM (Model-View-ViewModel) architecture is a design pattern widely used in Android app development for organizing code in a structured manner. This separation of concerns allows for better code organization, testability, and maintenance.

#RETROFIT: Retrofit is a crucial Android library that simplifies network requests and API handling. It provides an intuitive interface to define endpoints and request/response types, integrates with serialization libraries for data conversion, and supports asynchronous calls, error handling, and customization. With its capabilities, Retrofit streamlines networking tasks, enabling developers to create efficient Android apps effortlessly.

#ROOM DATABASE: Room is an Android Jetpack library that simplifies working with SQLite databases. It offers an abstraction layer over SQLite, allowing developers to define database tables through annotated entities and access data using DAO (Data Access Object) interfaces or classes. Room streamlines database operations, eliminating the need for raw SQL queries and enhancing data persistence in Android applications.

#KOTLIN COROUTINES: Streamline asynchronous operations in Kotlin with a sequential, readable approach, providing lightweight threads and simplified handling of concurrent tasks for improved code organization and execution. Overall, they streamline asynchronous programming in Kotlin by offering a more straightforward and organized approach to handling asynchronous tasks.

#LIVE DATA: LiveData is an observable data holder class in Android's Jetpack library, designed to handle data changes in a lifecycle-aware manner. It's used to hold and observe data, ensuring that UI components only update when the data changes and only when the relevant lifecycle is active.

#VIEW MODEL: ViewModel is an Android architecture component that stores and manages UI-related data. It survives configuration changes like screen rotations and retains data during the lifecycle of the associated UI component (like an Activity or Fragment). ViewModel separates UI logic from the UI controllers and helps maintain a clean and lifecycle-aware design in Android apps.

Required Libary list for project

    /**Material Design **/
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")

    /** Scalable Size Units(dimens) **/
    implementation ("com.intuit.sdp:sdp-android:1.0.6")
    implementation ("com.intuit.ssp:ssp-android:1.0.6")

    /** coroutines **/
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    /** viewModel lifecycle **/
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ("androidx.activity:activity-ktx:1.8.0")

    /** dagger hilt **/
    implementation ("com.google.dagger:hilt-android:2.47")
    kapt ("com.google.dagger:hilt-compiler:2.47")
    kapt ("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.5.0")

    /** Gson converter **/
    implementation ("com.google.code.gson:gson:2.9.1")

    /** Retrofit & OkHttp **/
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.1")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.1")

    /** Google map **/
    implementation ("com.google.android.gms:play-services-maps:18.0.2")
    implementation ("com.google.android.gms:play-services-location:19.0.1")
    implementation ("com.google.maps.android:android-maps-utils:0.5")

    /**work manager **/
    implementation ("androidx.work:work-runtime-ktx:2.8.1")

    /** image download **/
    implementation ("io.coil-kt:coil:2.2.2")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
