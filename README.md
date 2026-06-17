# PoCSnap

PoCSnap is a lightweight, efficient Burp Suite extension built using the modern **Montoya API**. It provides security analysts with a dedicated workspace to visualize HTTP requests and responses, apply overlay annotations (such as highlighting parameters or interesting text sequences), and capture beautifully formatted screenshots for penetration testing reports and proof-of-concept (PoC) validation documentation.

## Features

- **Dedicated Workspace:** Easily send request/response pairs from anywhere in Burp straight into a clean canvas.
- **Overlay Drawing Canvas:** Instantly switch to "Draw Mode" to map out and sketch highlighting rectangles over crucial payloads.
- **Clean Layout Screenshots:** Automatically strips out interface controls and navigation buttons during image processing to output an export-ready screenshot.

## Prerequisites

- Burp Suite Professional v2023.x or later (Supporting the Montoya API Framework).
- Java Development Kit (JDK) 17 or higher.

## Installation & Build Instructions

### 📥 Direct Download (Easiest)

Don't want to build from source? Download the latest pre-compiled standalone binary directly from our [Releases Page](https://github.com/cyrilshaji38/PoCsnap/releases).

### To clone the workspace and compile the standalone artifact file, run:

```bash
git clone https://github.com/cyrilshaji38/PoCsnap.git
cd POCSnap
./gradlew jar
```

The compiled standalone executable binary will be generated inside the directory matching:
build/libs/POCSnap-all.jar (or POCSnap.jar).

## Loading into Burp Suite

1. Launch Burp Suite and navigate to the Extensions tab.
2. Under the Installed sub-tab, click Add.
3. Set the Extension type to Java.
4. Select the path pointing to your compiled .jar file and hit Next.
