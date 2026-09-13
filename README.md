# WindowsBrowser (Windows-style Android Browser)

A Windows-classic-style WebView browser for Android, featuring a toolbar UI,
downloads via the system DownloadManager, page screenshots, MHTML page saving,
a floating toolbar toggle, layout mode settings (desktop/mobile), and a
skeleton VPN service.

## English Instructions

1. Open the project in Android Studio (File > Open, select this folder).
2. Gradle wrapper is not included; create/adjust `build.gradle` files if needed
   (minSdk 21+, targetSdk 33+).
3. Build and run on a device or emulator (API 21+).
4. Features:
   - Address bar with Google search fallback.
   - Back / Forward / Reload / Home buttons.
   - Downloads handled by Android DownloadManager.
   - Screenshot button saves PNGs to app-specific `Screenshots` folder.
   - Save-page button stores MHTML archives in `SavedPages` folder.
   - Toggle button shows/hides the toolbar (floating UI toggle).
   - SettingsActivity: desktop vs mobile layout mode; VPN enable/disable.
5. VPN is a skeleton: it establishes a TUN interface but does not route or
   filter packets yet.

## راهنمای فارسی

۱. پوشه پروژه را در Android Studio باز کنید (File > Open).
۲. Gradle wrapper ضمیمه نشده؛ در صورت نیاز `build.gradle` را اضافه/تنظیم کنید
   (minSdk نسخه ۲۱ و بالاتر).
۳. روی دستگاه یا شبیه‌ساز اجرا کنید.
۴. امکانات:
   - نوار آدرس با جستجوی گوگل در صورت ورود عبارت.
   - دکمه‌های عقب / جلو / بارگذاری مجدد / خانه.
   - دانلود فایل‌ها از طریق DownloadManager اندروید.
   - دکمه اسکرین‌شات: ذخیره PNG در پوشه `Screenshots`.
   - دکمه ذخیره صفحه: آرشیو MHTML در پوشه `SavedPages`.
   - دکمه نمایش/مخفی‌کردن نوار ابزار (رابط شناور).
   - تنظیمات: حالت نمایش دسکتاپ یا موبایل، و فعال/غیرفعال‌سازی VPN.
۵. سرویس VPN اسکلت است: فقط رابط TUN را می‌سازد و مسیریابی/فیلتر بسته‌ها
   در نسخه‌های بعدی پیاده‌سازی می‌شود.
