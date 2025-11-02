# 🎮 JavaGame

Welcome to **JavaGame**, a small 2D game built entirely in **Java Swing**.  
This was my **first ever programming project**, created to learn the fundamentals of game development, graphics, and user input handling in Java.  
It was made with occasional help from **AI assistance** when I needed guidance. 🤖

---

## 🕹️ Overview

JavaGame is a simple desktop game that includes:
- A **Start Menu**
- A **Level Selector**
- An **Options Menu**
- Multiple playable **Levels**
- Custom **graphics and settings**

It’s a lightweight, pure Java project — no external libraries or game engines.

---

## ✨ Features

✅ Classic 2D graphics using `JPanel` and `Graphics2D`  
✅ Level and settings system (`settings.properties`)  
✅ Keyboard input and basic animations  
✅ Menu screens for navigation  
✅ Organized assets (images, sounds)

---

## 🧠 What I Learned

This project helped me understand:
- How to structure a Java project
- How rendering loops and frame updates work
- How to handle keyboard input in Swing
- How to organize files and manage game state
- The importance of clean code and modularity

---

## 🚀 How to Run


### 🧩 Running from the Command Line

```bash
# 🧩 1️⃣ Go to the project folder
cd "C:\Users\riley\Desktop\github\my projects\Full Game"

# 🧠 2️⃣ Compile all Java files and build the JAR
cd Java
javac -d ../classes *.java
jar cfm ../finishedjar/Game.jar MANIFEST.MF -C ../classes . -C ../images .
cd ..

# ⚙️ 3️⃣ Copy the finished JAR into the launcher folder for embedding
copy finishedjar\Game.jar Launcher\Game.jar

# 🛠️ 4️⃣ Build the C++ launcher and embed the JAR
cd Launcher
windres resources.rc -o resources.o
g++ launch.cpp resources.o -o ../GameLauncher.exe -mwindows
cd ..

# ▶️ 5️⃣ Run your game!
GameLauncher.exe

```
Then Run the .exe created
