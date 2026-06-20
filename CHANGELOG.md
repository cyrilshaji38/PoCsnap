# Changelog

All notable changes to this project will be documented in this file.

## [1.1.0] - 2026-06-20

### Added
- **Privacy Masking Tool:** Introduced the new "Privacy Mask" mode featuring a high-density, 4x4 micro-mosaic block texture to securely obfuscate sensitive tokens, cookies, and credentials before exporting.
- **UI Font Scaling Engine:** Integrated a real-time font adjustment selector (`▼ Font: XXpx ▲`) supporting sizes from 10px to 16px.

### Changed
- **Toolbar Workflow Optimization:** Re-ordered the global header menu layout to position the screenshot button to the end so that it follows the workflow order.

### Fixed
- **Draw Mode Scroll Hijack:** Fixed a critical bug where toggling "Draw Mode: ON" would reset the editor viewports and scroll the user back to the top of the request/response. Added automatic asynchronous scroll position caching and restoration handlers.

---

## [1.0.0] - 2026-05-15

### Added
- **Initial Baseline Release:** Rolled out core context menu payload piping handlers ("Send to PoCsnap").
- **Overlay Canvas Subsystem:** Introduced live transparent vector layering mechanics with traditional diagnostic red highlighting wireframe boxes.
- **History Rollback Tracking:** Added standalone vector management support via standard Undo, Redo, and Canvas Clear tracking arrays.
- **Snapshot Layout Exporter:** Added automated toolbar visibility stripping and automated layout capture pipeline rendering directly to external PNG file structures.