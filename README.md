# Bluetooth Connectivity and API demo

Brief description of the project.

## Table of Contents


- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup Instructions](#setup-instructions)
- [Usage](#usage)
- [Screenshots](#screenshots)




## Features

- **MVVM Architecture:** 
  - The app follows the MVVM (Model-View-ViewModel) architecture for clean separation of concerns.

- **Bluetooth Connectivity:**
  - Demonstrates Bluetooth connectivity between two devices.
  - Allows sharing data within the app, resembling a Bluetooth chat application.

- **Data Sharing:**
  - Users can share text messages between connected devices.
  - Audio sharing functionality is partially implemented, with the code designed to be extendable to support audio and image sharing in the future.

- **API Integration:**
  - Utilizes Retrofit to make API calls.
  -  Added Unit test cases to varify api calls.
  - Although the current API provides limited data, the app's architecture is designed to support paging for APIs with larger datasets.
    
- **Testing:**
  - Added Unit test cases to varify api calls.



## Project structure (MVVM)
* di
* models
* network
* repository
* utils
* views
* viewmodel
* MainActivity

 

## Technologies/libraries Used

- Android SDK
- JetPackCompose
- Kotlin
- MVVM architecture
- Bluetooth API
- Retrofit
- hilt
  

## Setup Instructions

1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the project on physical device, as android emulators doesn't support the bluetooth connectivity properly.

## Usage

To use the app, follow these steps:

1. **Permissions:**
   - Allow all the permissions requested by the app for smooth operation.

2. **Start Server:**
   - On one device, start the server by clicking on the "Start Server" button.

3. **Connect to Server:**
   - On the other device, click on the discovered or already paired device from the list.
   - Click on the "Connect" button to establish a connection to the server.

4. **Data Sharing:**
   - Once the devices are connected, you will see a toast message indicating that the devices are connected.
   - Start sharing data between the devices.

## Screenshots

<img src="https://github.com/SantoshInchalakaranji/Drops/blob/master/images/Web%201920%20%E2%80%93%201.png" />
