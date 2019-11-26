# kotlin-currency-practice

## Outline
- By integrate the api endpoints from [CurrencyLayer](https://currencylayer.com/) , this repository has been created as the practice Android project in Kotlin, the following features also prepared in this implementation
  - The grid table for display the different currency rates
  - The dropdown menu for switch the standard currency type
  - The input box for input the target price, which will reflect on the price value of the currency cells in the grid table
  - In every 30-min, the app will automatically refresh the currency data

## Setup
- Make sure you have the latest version of the Android Studio and the Android SDK in your machine

## How to generate the build
- Like the other Android projects, you can generate your build via AndroidStudio IDE
  - For generate the debug build, find [Build] -> [Build Bundle(s) / APK(s)] -> [Build APK(s)] and wait for the popup tells you where you can find the generated build
- If you prefer to use the cmd line to generate the build: 
  - In the console, first execute `cd ${project path}` to move to the project folder
  - In order to generate `debug` build , execute `./gradlew assembleDebug`
  - If success, you can find the bulild in the folder `${project path}/app/build/outputs/apk/debug` , you will the file `app-debug.apk` as the generated build

## 3rd party libraries
- [Koin](https://github.com/InsertKoinIO/koin): Integrate the DI
- [gson](https://github.com/google/gson): Parse the json format we get from the backend api endpoints
- [Retrofit2](https://square.github.io/retrofit/): In order to implement the logic for call the api endpoints and get the response data

## Available builds
- We use AppCenter to distrubute the build for review and test
- You can directly scan the following QRCode via your android device, and choose the build you want to install in your phone
- [Debug build]

| [Download list](https://install.appcenter.ms/orgs/github-jeffchao98/apps/currencytable/distribution_groups/android-debug) |
| --- |
| ![debug list qr](/res/android-debug.png) |


