# 🎵 MP3 Music Player

A simple MP3 music player made purely in **Java**, developed as a personal project to reinforce knowledge in **Data Structures** and **Object-Oriented Programming (OOP)**.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/apachemaven-C71A36.svg?style=for-the-badge&logo=apachemaven&logoColor=white)

---

## 🚀 Features

- 🎼 **Playlist Management** – Create, view, and manage your playlists.
- ⬇️ **Download Songs** – Automatically download songs via terminal.
- 📁 **Song Management** – Add songs to playlists and view songs in each one.
- 💻 **Console-Based UI** – Simple and intuitive command-line interface.
- 📂 **Local File Integration** – Scans your local file system for music files.

---

## 🖼️ Screenshots

<p float="left">
  <img src="https://github.com/Nrzty/Music-Player/blob/main/images/Imagem%20colada.png" width="45%" />
  <img src="https://github.com/Nrzty/Music-Player/blob/main/images/Imagem%20colada%20(2).png" width="45%" />
  <br/>
  <img src="https://github.com/Nrzty/Music-Player/blob/main/images/Imagem%20colada%20(3).png" width="45%" />
  <img src="https://github.com/Nrzty/Music-Player/blob/main/images/Imagem%20colada%20(4).png" width="45%" />
</p>

---

## 🛠 Getting Started

### 📦 Prerequisites

- **Java JDK 17 or higher** – The project uses Java 17.
- **Apache Maven** – For building and managing dependencies.

### 🔧 Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/nrzty/music-player.git

## ⚙ Configuration

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
