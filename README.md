# MP3 Music Player

A simple, console-based music player application built with Java and Maven. This application allows you to manage and organize your local music files into playlists.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/apachemaven-C71A36.svg?style=for-the-badge&logo=apachemaven&logoColor=white)

---

## Contents

* [Features](#-features)
* [Roadmap](#-roadmap)
* [Dependencies](#-dependencies)
* [Getting Started](#-getting-started)
    * [Prerequisites](#prerequisites)
    * [Installation](#installation)
    * [Running the Application](#running-the-application)
* [Configuration](#-configuration)
* [Project Structure](#-project-structure)
* [Contributing](#-contributing)
* [License](#-license)

---

## Features

* **Playlist Management**: Create, view, and manage your playlists.
* **Song Management**: Add songs to your playlists and view all songs within a specific playlist.
* **Console-Based UI**: A simple and intuitive command-line interface for easy navigation and use.
* **Local File System Integration**: Automatically scans your local file system to find and load your music files.
  
---

## Dependencies

This project has no external dependencies listed in the `pom.xml` file.

---

## Getting Started

Follow these instructions to get a copy of the project up and running on your local machine.

### Prerequisites

* **Java Development Kit (JDK) 17 or higher**: The project is configured to use Java 17.
* **Apache Maven**: Used for building and managing the project.

### Installation

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/nrzty/music-player.git](https://github.com/nrzty/music-player.git)
    ```
2.  **Navigate to the project directory:**
    ```bash
    cd music-player
    ```
3.  **Build the project and install dependencies:**
    ```bash mvn clean install ```

### Running the Application

Once the project is built, you can run the application using the following command:

```bash
mvn exec:java
```

## ⚙Configuration

The application loads your music from a directory specified in a `config.properties` file.

1.  Create a `config.properties` file in the `src/main/resources` directory.
2.  Add the following line to the file, replacing `/path/to/your/music` with the actual path to your music folder:

    ```properties
    playlist.base.path=/path/to/your/music
    ```

    You can also use `${user.home}` as a placeholder for your home directory. For example:

    ```properties
    playlist.base.path=${user.home}/Music
    ```

If the `config.properties` file is not found, the application will default to `~/MusicPlayerData/songs/`.

---

## Project Structure

The project is organized into the following packages:

* **`musicPlayer.models`**: Contains the core data models for the application.
    * `Song.java`: Represents a single song with properties like title, artist, album, genre, and duration.
    * `Playlist.java`: Represents a playlist, which is a collection of `Song` objects.
    * `Library.java`: Represents the music library, which is a collection of `Playlist` objects.
* **`musicPlayer.ui`**: Contains the user interface classes.
    * `ConsoleUI.java`: Handles all console input and output, providing a text-based interface for the user.
* **`musicPlayer.utils`**: Contains utility classes that provide helper functions to the rest of the application.
    * `FilesUtils.java`: Handles file system operations, such as creating directories and reading files.
    * `LibraryLoader.java`: Responsible for loading music files from the file system and creating the `Library` object.
    * `UserInputs.java`: A simple utility for capturing user input from the console.


