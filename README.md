PlutoPlex is a Netflix-inspired Over-The-Top (OTT) streaming application that lets users enjoy a vast collection of free movies and TV shows. The app offers an intuitive and modern interface, high-quality video playback, and seamless navigation.

🚀 Features
- Free Streaming: Watch a wide range of movies and TV shows for free.
- Modern UI: Built using Jetpack Compose, providing a dynamic and user-friendly design.
- Rich Content: Content sourced from The Movie Database (TMDb) API for updated and diverse entertainment options.
- Ad Integration: Monetized using AdMob to support free streaming services.
- Smooth Media Loading: Images and media thumbnails efficiently loaded using Coil.
- Firebase Integration:
- Firebase Realtime Database for user data and app configurations.
- Ensures seamless backend support.
- High-Quality Playback: Enjoy buffer-free streaming with optimized networking via Retrofit.

🛠️ Technologies Used
- Kotlin: Primary programming language.
- Jetpack Compose: For building a responsive and modern UI.
- Retrofit: To handle network requests efficiently.
- Coil: For loading and caching images.
- AdMob: For in-app advertisements to generate revenue.
- Firebase Database: Backend database for user and app data.
- TMDb API: Source for movie and TV show metadata.
  
📱 Screenshots
![clv](https://github.com/user-attachments/assets/1b3b7cd4-1f15-4725-bf93-8a64fbf873e1)

🌐 Setup & Installation

- You can get the updated code on plutoplex branch

Clone the repository:
bash
Copy code
git clone [https://github.com/your-repo/plutoplex.git](https://github.com/LalremLian/PlutoPlex.git)  
Open the project in Android Studio.

Set up your API keys:
TMDb API Key: Replace YOUR_TMDB_API_KEY in Constants.kt.
Firebase Configuration: Add your google-services.json file to the app/ directory.
AdMob Configuration: Replace the AdMob App ID in your project settings.
Build and run the app on an emulator or a physical device.
