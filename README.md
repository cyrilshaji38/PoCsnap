# PoCSnap

PoCSnap is a lightweight, efficient Burp Suite extension built using the modern **Montoya API**. It provides security analysts with a dedicated workspace to visualize HTTP requests and responses, mask sensitive data, apply highlighting annotations, and capture beautifully formatted screenshots for penetration testing reports and proof-of-concept (PoC) validation documentation.

## Features

- **Dedicated Workspace:** Easily send request/response pairs from anywhere in Burp straight into a clean canvas.
- **Precision Highlight & Masking:** Simply select text directly inside the editor layout using your cursor and click to apply red diagnostic borders or privacy masks instantly.
- **Advanced Privacy Obfuscation:** Mask session tokens, cookies, PII, and credentials using a high-density, 4x4 micro-mosaic block texture before exporting.
- **Real-Time Font Scaling:** Adjust the text sizing dynamically with a dedicated font adjustment selector supporting views from 10px to 16px.
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

Step 3: Apply Highlights and Privacy Masks
1. Use the font selector engine (▼ Font: XXpx ▲) to adjust text size seamlessly if a layout adjustment is needed.
2. Drag and highlight a specific word, parameter value, header, or multi-line block directly with your text cursor inside the editor viewport.
3. Click the target action button on the toolbar to apply the overlay configuration:
   + Highlight Selection: Draws a precise red bounding border tracing your selected text elements.
   + Mask Selection: Instantly tiles a pixelated micro-mosaic pattern across the text block to safely redact cookies, tokens, or personal identifiers.
4. Manage your layers using the control array buttons if you make a mistake:
   + Undo: Remove the last modification layer applied.
   + Redo: Restore a removed modification layer.
   + Clear Canvas: Wipe all annotations clean to start over fresh.


<img width="959" height="563" alt="PoCsnap_demo_v1 1" src="https://github.com/user-attachments/assets/e332206d-5f04-4e32-a6c8-f42582f48ca4" />


Step 4: Export Your PoC Screenshot
1. Click the Capture PoC Screenshot button.
2. The extension will automatically hide the control buttons and drawing toggles under the hood so your image stays perfectly clean.
3. A file explorer window will open. Choose where you want to save your file (it defaults to saving as poc_screenshot.png) and click Save.
4. Once exported successfully, a confirmation box will appear, and your workspace buttons will return to normal!


<img width="1280" height="649" alt="poc_screenshot" src="https://github.com/user-attachments/assets/7998e596-5de7-4138-86ca-300fb1ca8cdf" />
