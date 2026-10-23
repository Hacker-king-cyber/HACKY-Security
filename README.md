# HACKY-Security

An open-source Android security learning toolkit focused on privacy awareness and safe diagnostics.

## Included in this starter project

- **Network diagnostics:** reports whether the device currently has network connectivity and shows available network transport information.
- **App privacy checker:** opens Android's app settings so you can review permissions and access. Android restricts a normal app from reading every other app's private data or changing its permissions silently.
- **DNS and connection information:** displays DNS server addresses exposed by Android's active network configuration. This is not a full packet monitor and does not intercept other apps' traffic.
- **Cybersecurity learning lab:** short defensive learning notes for networking, DNS, permission hygiene, and authorized testing.
- **Password strength checker:** estimates strength locally on-device. It does not send passwords anywhere or save them.

## Build

Requirements: Android SDK, JDK 17, and Android Gradle Plugin-compatible Gradle. The GitHub Actions workflow in `.github/workflows/android.yml` builds a debug APK automatically.

To build locally from the project root:

```bash
gradle assembleDebug
```

The APK will normally be at `app/build/outputs/apk/debug/app-debug.apk`.

## Publish to GitHub

1. Create a **public** repository named `HACKY-Security` on GitHub.
2. Upload the contents of this project folder to the repository root (not the ZIP file itself).
3. Open **Actions** and enable workflows if GitHub asks.
4. Run the `Android CI` workflow or push a commit. Download `HACKY-Security-debug-apk` from the workflow run's Artifacts section.
5. For public releases, test the APK on your own device first. A production release should use a properly managed signing key; never commit keystores, passwords, API keys, or signing secrets to the repository.

## Android limitations and privacy

- This app requests network-state access only; it does not request location, contacts, SMS, accessibility, VPN, or root permissions.
- Android may hide some network details depending on OS version, device manufacturer, and network type.
- App permission review is opened through Android Settings rather than attempting to silently inspect or modify other apps.
- The learning lab is for defensive learning and systems you own or have explicit permission to test.
- This starter is not a replacement for Android's built-in security features, and it does not claim to detect every threat.

## License

MIT. See [`LICENSE`](LICENSE).
