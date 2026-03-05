# 🛒 FlashCart - Quick Grocery Delivery App

Welcome to **FlashCart**! This is a modern Android application built to simulate a fast grocery and daily essentials delivery service. Users can log in, browse various categories (like fruits, vegetables, munchies, and pet supplies), add items to their cart, and check out seamlessly.

## 📱 Screenshots

*(Note: These represent the visual flow of the app based on your uploaded screenshots)*


<img width="144" height="312" alt="Screenshot_20260301_220919" src="https://github.com/user-attachments/assets/9c210ea0-714b-4d9e-8f77-2810a0db421f" />
<img width="144" height="312" alt="Screenshot_20260301_221644" src="https://github.com/user-attachments/assets/c0986eb0-e6c1-417e-af08-be4470d716f3" />
<img width="144" height="312" alt="Screenshot_20260301_221720" src="https://github.com/user-attachments/assets/76ce2f6d-4072-4d08-b759-0a891262147d" />
<img width="144" height="312" alt="Screenshot_20260301_221739" src="https://github.com/user-attachments/assets/4f9181a8-c276-4507-8742-5f35b0d72625" />
<img width="144" height="312" alt="Screenshot_20260301_221817" src="https://github.com/user-attachments/assets/fda71680-b209-4162-be2a-f448eafbff94" />
<img width="144" height="312" alt="Screenshot_20260301_221843" src="https://github.com/user-attachments/assets/05cee008-6003-4e12-b8bc-32d6aec90e85" />
<img width="144" height="312" alt="Screenshot_20260301_221942" src="https://github.com/user-attachments/assets/37f2ec51-ba0a-43d3-8e80-863aa137d9f3" />
<img width="144" height="312" alt="Screenshot_20260301_222000" src="https://github.com/user-attachments/assets/c258b7f5-000d-4c84-b0e2-c242a9d2cfd9" />

| Login / OTP | Home / Categories | Items List | Cart & Checkout |

## ✨ Key Features

* **Secure Authentication:** Users can log in using their mobile number and securely verify it using an OTP (One-Time Password) system.
* **Dynamic Product Catalog:** Products are organized into neat categories (Fruits, Kitchen, Beverages, etc.) fetched from a remote source.
* **Smart Cart Management:** Users can add items, increase or decrease quantities, and instantly see the total price update.
* **Special Offers:** A dedicated screen to showcase the latest deals and promotional banners.
* **Smooth Navigation:** Seamless transitions between screens for a high-quality user experience.

## 🛠️ Tech Stack & Tools

* **Language:** Kotlin
* **UI Framework:** Jetpack Compose (Modern declarative UI)
* **Architecture:** MVVM (Model-View-ViewModel) for clean, maintainable code.
* **Networking:** Retrofit / API integration (to fetch items from the internet).
* **Build System:** Gradle (Kotlin DSL).

## 📂 Code & Architecture Breakdown

The project follows a clean architecture model, separating the user interface, data, and logic. Here is a detailed analysis of your codebase:

### 1. The Core UI (Jetpack Compose)

* **`FlashApp.kt`**: This is the main entry point for your UI. It acts as the navigation hub, routing the user between the login screen, home screen, and cart.
* **`StartScreen.kt`**: The homepage. It displays the beautiful category grids and banners so users can find what they want quickly.
* **`ItemScreen.kt`**: Once a category is clicked, this screen displays the specific `InternetItem` objects (like an Apple or Pepsi) available for purchase.
* **`CartScreen.kt`**: Handles the checkout process. It reads the cart data, calculates totals, and handles empty cart states (using the `cart_empty.png` drawable).
* **`OfferScreen.kt`**: Displays active discounts using promotional graphics.
* **Authentication Screens (`NumberScreen.kt`, `OtpScreen.kt`, `LoginUi.kt`)**: Together, these handle the user onboarding flow, collecting the phone number and verifying the security code.

### 2. State Management & Logic (ViewModel)

* **`FlashViewModel.kt`**: The "brain" of your app. It holds all the business logic. It fetches data from the API, updates the cart, tracks item quantities, and manages what the user sees.
* **`FlashUiState.kt`**: Represents the current state of the app (e.g., is the app loading? did the network fail? what items are in the cart?). The UI listens to this state and updates automatically.

### 3. Data Models

* **`Categories.kt`**: Defines the data structure for product groupings.
* **`InternetItem.kt`**: The blueprint for a single product fetched from your API (contains name, price, image URL, etc.).
* **`InternetItemWithQuantity.kt`**: A special data wrapper that pairs an `InternetItem` with the specific quantity the user wants to buy. This is what powers the `CartScreen`.
* **`DataSource.kt`**: Acts as a repository or provider for mock/local data when the internet isn't available.

### 4. Networking

* **`FlashApiService.kt`**: Located in the `network` folder. This file contains the API endpoints and network calls required to fetch live grocery data from the backend servers.


## 🚀 How to Run the Project

1. **Clone the Repository:** *(Use your preferred method or another example terminal command to pull the code to your local machine)*
```bash
git clone https://github.com/arjunparmar-dev/flashcart.git
```

2. **Open in Android Studio:** Open Android Studio and select `File > Open`, then navigate to the cloned `flashcart` folder.
3. **Sync Gradle:** Wait for Android Studio to download dependencies and sync the Gradle files (`build.gradle.kts`).
4. **Run the App:** Connect an Android device or start up an emulator, then click the green **Play** button at the top of Android Studio.

## 🎨 Design Assets

The project makes heavy use of custom drawables (located in `app/src/main/res/drawable/`) to keep the UI engaging. This includes category icons (`fruits.png`, `munchies.png`), specific products (`banana.png`, `pepsi.png`), and UI states (`error.png`, `loading.png`, `placeholder.png`).

*Developed with ❤️ as part of an Internshala training program / independent development.*
