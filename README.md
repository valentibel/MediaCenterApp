# MediaCenterApp
**MediaCenterApp** is a native Android application for displaying media data.

<img src="MediaCenterApp.png" width="50%">

## Features

The app reads media data from JSON file and shows it in hiarerchical order. Root item as the tabs and child items as a list or a grid. The history of the tab navigaition is preserved while switching between the tabs. Clicking on the selected tab returns to the root content.

## Architecture

* **MVVM Architecture**: The application follows the MVVM (Model-View-ViewModel) design pattern.
* **User interface**: The UI is build using Jetpack Compose toolkit.
* **Modularisation**: The data source is exposed as an Android library module *mediaLibrary*
* **Dependency Injection**: The app utilizes Hilt for DI.
* **SOLID**: Development was strictly based on SOLID principles.
* **Error Handling**: Comprehensive error and exception handling is in place.

## Testing

Unit Testing: Repository and view model are covered by unit tests using the JUnit4, Mockito and kotlinx-coroutines-test packages.
