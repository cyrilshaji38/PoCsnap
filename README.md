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

## Usage instructions

Using PoCsnap to capture and highlight your security findings takes only a few simple steps.

Step 1: Send HTTP Traffic to the Workspace
1. Browse through your traffic anywhere in Burp Suite (e.g., Proxy History, Repeater, or Target tabs).

2. Right-click on any interesting HTTP request/response pair.

3. Select Send to PoCsnap from the context menu.

Step 2: Review and Inspect
1. Click on the PoCsnap tab in Burp Suite's top menu bar.

2. Review the captured data inside the split-screen request and response editors.

Step 3: Draw Highlights and Annotations
1. Click the Draw Mode: OFF button at the top left. The button will switch to Draw Mode: ON, and your mouse cursor will turn into a crosshair.
2. Click and drag your mouse over any crucial parameter, payload reflection, or header value to draw a red bounding box around it.
3. Use the control buttons if you make a mistake:

   Undo: Remove the last box you drew.

   Redo: Bring back a removed box.

   Clear Canvas: Wipe all drawings clean to start over.

<img width="959" height="562" alt="PoCsnap_demo" src="https://github.com/user-attachments/assets/8a8139c7-40db-4d8f-8fc8-f31370b95e4e" />


Step 4: Export Your PoC Screenshot
1. Click the Capture PoC Screenshot button.

2. The extension will automatically hide the control buttons and drawing toggles under the hood so your image stays perfectly clean.

3. A file explorer window will open. Choose where you want to save your file (it defaults to saving as poc_screenshot.png) and click Save.

4. Once exported successfully, a confirmation box will appear, and your workspace buttons will return to normal!

<img width="1280" height="649" alt="poc_screenshot" src="https://github.com/user-attachments/assets/5244b110-3192-4985-9b3d-1d208b34346b" />

