# تحدّي الأبطال — Android source

مشروع Android أصلي بسيط بلغة Java، بواجهة عربية وأسئلة مخزنة محليًا داخل `app/src/main/assets/questions.json`، لذلك لا يحتاج اتصالًا أثناء اللعب.

## بناء APK
المشروع يستخدم Android Gradle Plugin 8.7.3 و Gradle 8.9 و Java 17.
يمكن البناء عبر:
`gradle assembleDebug`

أو رفع المشروع إلى GitHub ثم اختيار Actions → Build Android APK → Run workflow.
بعد نجاح البناء، نزّل artifact باسم `ChampionsQuiz-debug-apk` وستجد داخله `app-debug.apk`.

## ملاحظة
هذا إصدار أولي غير موقّع للنشر في المتاجر (debug APK). اختبره على الهاتف قبل توزيعه.
