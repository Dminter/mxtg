package com.zncm.mxtg.uitls;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.ClipboardManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.zncm.mxtg.BuildConfig;
import com.zncm.mxtg.R;
import com.zncm.mxtg.ui.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import de.cketti.library.changelog.ChangeLog;

/**
 * Created by MX on 2014/8/21.
 */
public class XUtil {

    // 截取字符串
    public static String subStr(String input, int len) {
        if (isEmptyOrNull(input)) {
            return null;
        }
        int inpuLen = input.length();
        if (len >= inpuLen) {
            return input;
        } else {
            return input.substring(0, len);
        }

    }

    public static String subStrDot(String input, int len) {
        if (isEmptyOrNull(input)) {
            return null;
        }
        int inpuLen = input.length();
        if (len >= inpuLen) {
            return input;
        } else {
            return input.substring(0, len) + "...";
        }
    }


    public static boolean isEmptyOrNull(String string) {
        if (string == null || string.trim().length() == 0 || string.equalsIgnoreCase("null")) {
            return true;
        } else {
            return false;
        }
    }

    public static View getEmptyView(Context ctx, String str) {
        if (!notEmptyOrNull(str)) {
            return null;
        }
        LayoutInflater mInflater =
                (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout newEmptyView = (RelativeLayout) mInflater.inflate(R.layout.view_data_null, null);
        TextView textView = (TextView) newEmptyView.findViewById(R.id.tvNull);
        textView.setText(str);
        return newEmptyView;
    }


    /**
     * 获取十六进制的颜色代码.例如  "#6E36B4" , For HTML ,
     *
     * @return String
     */
    public static String getRandColorCode() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();
        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        return r + g + b;
    }


    //year
    public static String getDateYear(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String dt = sdf.format(time);
        return dt;
    }

    //month
    public static String getDateMonth(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("M");
        String dt = sdf.format(time);
        return dt;
    }

    //day
    public static String getDateDay(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String dt = sdf.format(time);
        return dt;
    }

    //week
    public static String getDateWeek(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dt = sdf.format(time);
        return dt;
    }

    //hour
    public static String getDateHM(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String dt = sdf.format(time);
        return dt;
    }

    public static String getDateHMS(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dt = sdf.format(time);
        return dt;
    }

    public static Long installedTime(Context context) {
        Long installed = null;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = null;
            appInfo = pm.getApplicationInfo("com.zncm.mxtg", 0);
            String appFile = appInfo.sourceDir;
            installed = new File(appFile).lastModified();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return installed;
    }

    public static void setTextView(View view, int id, Object text) {
        if (view != null) {
            TextView tv = (TextView) view.findViewById(id);
            if (tv != null && text != null) {
                tv.setText(String.valueOf(text));
            }
        }
    }


    public static void setTextView(Activity activity, int id, String text) {
        if (activity != null) {
            TextView tv = (TextView) activity.findViewById(id);
            if (tv != null && text != null) {
                tv.setText(String.valueOf(text));
            }
        }
    }


    public static void dismissShowDialog(DialogInterface dialog, boolean flag) {
//        try {
//            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
//            field.setAccessible(true);
//            field.set(dialog, flag);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (flag) {
            dialog.dismiss();
        }
    }

    public static void autoKeyBoardShow(final EditText editText) {
        new Timer().schedule(new TimerTask() {
                                 public void run() {
                                     InputMethodManager inputManager =
                                             (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                     inputManager.showSoftInput(editText, 0);
                                 }
                             },
                500);
    }


    public static final WindowManager wm = (WindowManager) MyApplication.getInstance().ctx.getSystemService(Context.WINDOW_SERVICE);

    public static DisplayMetrics getDeviceMetrics() {
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static int getDeviceHeight() {
        return getDeviceMetrics().heightPixels;
    }

    public static int getDeviceWidth() {
        return getDeviceMetrics().widthPixels;
    }


    public static boolean copyAllFiles(String oldPath, String newPath)
            throws IOException {
        if (notEmptyOrNull(oldPath) && notEmptyOrNull(newPath)) {
            File oldFile = new File(oldPath);
            File newFile = new File(newPath);

            if (oldPath != null && newFile != null) {
                return copyFilesTo(oldFile, newFile);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean copyFilesTo(File srcDir, File destDir)
            throws IOException {
        if (srcDir == null || destDir == null) {
            return false;
        }

        if (!srcDir.exists()) {
            return false;
        }
        if (!destDir.exists()) {
            createFolder(destDir.getAbsolutePath());
        }

        if (!srcDir.isDirectory() || !destDir.isDirectory())
            return false;

        File[] srcFiles = srcDir.listFiles();
        for (int i = 0; i < srcFiles.length; i++) {
            if (srcFiles[i].isFile()) {
                File destFile = new File(destDir.getPath() + "//"
                        + srcFiles[i].getName());
                copyFileTo(srcFiles[i], destFile);
            } else if (srcFiles[i].isDirectory()) {
                File theDestDir = new File(destDir.getPath() + "//"
                        + srcFiles[i].getName());
                copyFilesTo(srcFiles[i], theDestDir);
            }
        }
        return true;
    }


    public static boolean copyFileTo(File srcFile, File destFile)
            throws IOException {
        if (srcFile == null || destFile == null) {
            return false;
        }
        if (srcFile.isDirectory() || destFile.isDirectory())
            return false;
        if (!srcFile.exists()) {
            return false;
        }
        if (!destFile.exists()) {
            createFile(destFile.getAbsolutePath());
        }
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        int readLen = 0;
        byte[] buf = new byte[1024];
        while ((readLen = fis.read(buf)) != -1) {
            fos.write(buf, 0, readLen);
        }
        fos.flush();
        fos.close();
        fis.close();
        return true;
    }

    public static boolean copyFileTo(InputStream inputStream, File destFile)
            throws IOException {
        if (inputStream == null || destFile == null) {
            return false;
        }
        if (destFile.isDirectory())
            return false;

        if (!destFile.exists()) {
            createFile(destFile.getAbsolutePath());
        }
        FileOutputStream fos = new FileOutputStream(destFile);
        int readLen = 0;
        byte[] buf = new byte[1024];
        while ((readLen = inputStream.read(buf)) != -1) {
            fos.write(buf, 0, readLen);
        }
        fos.flush();
        fos.close();
        inputStream.close();
        return true;
    }

    public static File createFile(String path) throws IOException {
        if (notEmptyOrNull(path)) {
            File file = new File(path);
            if (!file.exists()) {
                int lastIndex = path.lastIndexOf(File.separator);
                String dir = path.substring(0, lastIndex);
                if (createFolder(dir) != null) {
                    file.createNewFile();
                    return file;
                }
            } else {
                file.createNewFile();
                return file;
            }
        }
        return null;
    }

    public static File createFolder(String path) {
        if (notEmptyOrNull(path)) {
            File dir = new File(path);
            if (dir.exists()) {
                if (dir.isDirectory()) {
                    return dir;
                }
            }
            dir.mkdirs();
            return dir;
        } else {
            return null;
        }
    }


    public static Integer getVersionCode(Activity ctx) {
        PackageManager packageManager = ctx.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Integer versionCode = packInfo.versionCode;
        return versionCode;
    }

    public static String getVersionName(Activity ctx) {
        PackageManager packageManager = ctx.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

    public static void donateDlg(final Activity ctx) {
        String user_names = "*瞳瞳 *晓婕 ";
        new MaterialDialog.Builder(ctx)
                .title("捐助")
                .content(
                        "如果你觉得「mxtg」对你有价值,欢迎对coder进行小额赞助!\n" +
                                "支付宝账号: 1130724659@qq.com,*水娟.\n" +
                                "感谢捐助者:\n" +
                                user_names + "\n"
                )
                .theme(Theme.LIGHT)  // the default is light, so you don't need this line
                .positiveText("复制支付宝号")
                .neutralText("残忍拒绝!!!")
                .callback(new MaterialDialog.FullCallback() {
                    @Override
                    public void onNeutral(MaterialDialog materialDialog) {

                    }

                    @Override
                    public void onNegative(MaterialDialog materialDialog) {
                    }

                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        copyText(ctx, "1130724659@qq.com");
                    }
                })
                .show();
    }

    public static void rateUs(Activity ctx) {
        try {
            Uri uri = Uri.parse("market://details?id=com.zncm.mxtg");
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            ctx.startActivity(it);
        } catch (Exception e) {
            XUtil.tShort("很抱歉没能找着匹配的Android市场!");
        }
    }


    public static void sendTo(Context ctx, String sendWhat) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, sendWhat);
        ctx.startActivity(shareIntent);
    }

    public static void aboutUsDlg(Context ctx) {
        new MaterialDialog.Builder(ctx)
                .title("关于我们")
                .content("1.使用中遇到任何问题和意见反馈可加入产品交流群" + Constant.AUTHOR_QQ_GROUP + "\n2.注意:请勿使用系统->应用程序->清除数据,那样将会丢失本软件的一切数据,后果自担")
                .theme(Theme.LIGHT)  // the default is light, so you don't need this line
                .positiveText("知道了")
                .show();

    }


    public static void function(Context ctx) {
        Uri uri = Uri.parse(Constant.FUNCTION_INTRODUCTION_URL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ctx.startActivity(intent);
    }


    public static long getTimeLong(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

    public static long getTimeLongTenHours(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static void changeLogFirst(Context ctx) {
        ChangeLog cl = new ChangeLog(ctx);
        if (cl.isFirstRun()) {
            cl.getLogDialog().show();
        }
    }

    public static void changeLog(Context ctx) {
        ChangeLog cl = new ChangeLog(ctx);
        cl.getFullLogDialog().show();
    }


    public static void copyText(Activity ctx, String text) {
        ClipboardManager cbm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        cbm.setText(text);
        tShort("已复制");
    }

    public static void copyText(Activity ctx, String text, String toast) {
        ClipboardManager cbm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        cbm.setText(text);
        tShort(toast);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = MyApplication.getInstance().ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = MyApplication.getInstance().ctx.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String getDisplayDateTime(Long time) {
        String ret;
        if (time < getYearStart()) {
            ret = getTimeYMDHM(new Date(time));
        } else {
            ret = getTimeMDHM(new Date(time));
        }
        return ret;

    }

    public static String getDisplayDate(Long time) {
        String ret;
        if (time < getYearStart() || time > (getYearStart() + 365 * Constant.DAY)) {
            ret = getDateYMD(new Date(time));
        } else {
            ret = getDateMD(new Date(time));
        }
        return ret;

    }

    public static long getDayStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }


    public static String getTimeMDHM(Date inputDate) {
        return new SimpleDateFormat("E MM-dd HH:mm").format(inputDate);
    }

    public static String getTimeYMDHM(Date inputDate) {
        return new SimpleDateFormat("yyyy-MM-dd E HH:mm").format(inputDate);
    }

    public static String getTimeMDHM(Long inputDate) {
        return new SimpleDateFormat("MM-dd HH:mm").format(new Date(inputDate));
    }

    public static String getTimeYMDHM(Long inputDate) {
        if (inputDate == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(inputDate));
    }

    public static String getTimeYMDE(Long inputDate) {
        return new SimpleDateFormat("yyyy-MM-dd E").format(new Date(inputDate));
    }

    public static String getDateYMD(Date inputDate) {
        return new SimpleDateFormat("yyyy-MM-dd").format(inputDate);
    }

    public static String getDateY_M_D() {
        return new SimpleDateFormat("yyyy_MM_dd").format(new Date());
    }

    public static String getDateMD(Date inputDate) {
        return new SimpleDateFormat("MM-dd").format(inputDate);
    }

    public static String getDateMD(Long inputDate) {
        return new SimpleDateFormat("MM-dd").format(new Date(inputDate));
    }
    public static String getDateMDE(Long inputDate) {
        return new SimpleDateFormat("MM-dd E").format(new Date(inputDate));
    }

    public static String getDateMDEHM(Long inputDate) {
        return new SimpleDateFormat("MM-dd E HH:mm").format(new Date(inputDate));
    }

    public static String getDateEHM(Long inputDate) {
        return new SimpleDateFormat("E HH:mm").format(new Date(inputDate));
    }

    public static String getDateDHM(Long inputDate) {
        return new SimpleDateFormat("dd日 HH:mm").format(new Date(inputDate));
    }

    public static String getTimeHM(Date inputDate) {
        return new SimpleDateFormat("HH:mm").format(inputDate);
    }


    public static String getTimeHM(Long inputDate) {
        if (inputDate == null) {
            return null;
        }
        return new SimpleDateFormat("HH:mm").format(new Date(inputDate));
    }


    public static int diffDays(Long dayTime) {
        long diff = dayTime - getDayStart();
        int day = (int) (Math.abs(diff) * 1.0f / Constant.DAY);
        return day;
    }


    public static int diffNowDays(Long day2) {
        long diff = dateStrToLong(getDateYMD(new Date(day2))) - dateStrToLong(getDateYMD(new Date()));
        int day = (int) (diff * 1.0f / Constant.DAY);
        return day;
    }

    public static int randomRGB() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();
        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        return Color.parseColor("#" + (r + g + b));
    }

    public static String getRandomRGB() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();
        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        return "#" + (r + g + b);
    }

    public static String diffTime(Long time) {
        long diff = time - System.currentTimeMillis();
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数
        long day = diff / nd;//计算差多少天
        long hour = diff % nd / nh;//计算差多少小时
        long min = diff % nd % nh / nm;//计算差多少分钟
        long sec = diff % nd % nh % nm / ns;//计算差多少秒//输出结果
        StringBuffer sbTime = new StringBuffer();
        sbTime.append(hour).append("时 ");
        sbTime.append(min).append("分 ");
        sbTime.append(sec).append("秒");
        return sbTime.toString();
    }

    public static String hms(Long time) {
        long diff = time;
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数
        long day = diff / nd;//计算差多少天
        long hour = diff % nd / nh;//计算差多少小时
        long min = diff % nd % nh / nm;//计算差多少分钟
        long sec = diff % nd % nh % nm / ns;//计算差多少秒//输出结果
        StringBuffer sbTime = new StringBuffer();
        if (hour > 0) {
            sbTime.append(hour).append("°");
        }
        if (min > 0) {
            sbTime.append(min).append("′");
        }
        if (sec > 0) {
            sbTime.append(sec).append("″");
        }
        return sbTime.toString();
    }


    public static Long dateStrToLong(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date addTime = null;
        try {
            addTime = dateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return addTime.getTime();
    }


    //当年第一天
    public static long getYearStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static String getDateShow(long time) {
        String ret;
        if (time < getYearStart()) {
            ret = getDateYMD(new Date(time));
        } else {
            ret = getDateMD(new Date(time));
        }
        return ret;
    }


    public static Drawable initIconWhite(Iconify.IconValue iconValue) {
        return new IconDrawable(MyApplication.getInstance().ctx, iconValue).colorRes(R.color.light_gray).sizeDp(24);
    }

    public static Drawable initIconDark(Iconify.IconValue iconValue) {
        return new IconDrawable(MyApplication.getInstance().ctx, iconValue).colorRes(R.color.icon_dark).sizeDp(24);
    }

    public static Drawable initIconThemeColor(Iconify.IconValue iconValue) {
        return new IconDrawable(MyApplication.getInstance().ctx, iconValue).color(MySp.getTheme()).sizeDp(24);
    }


    public static boolean notEmptyOrNull(String string) {
        if (string != null && !string.equalsIgnoreCase("null") && string.trim().length() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public static <T> boolean listNotNull(List<T> t) {
        if (t != null && t.size() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public static void debug(Object string) {
        if (BuildConfig.DEBUG) {
            try {
                Log.i("[GTD]", String.valueOf(string));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void tShort(String string) {
        SuperToast.Animations animations = getAnimations();
        SuperToast.create(MyApplication.getInstance().ctx, string, SuperToast.Duration.VERY_SHORT,
                Style.getStyle(Style.GRAY, SuperToast.Animations.POPUP)).show();
    }

    public static void tLong(String string) {
        SuperToast.Animations animations = getAnimations();
        SuperToast.create(MyApplication.getInstance().ctx, string, SuperToast.Duration.MEDIUM,
                Style.getStyle(Style.GRAY, SuperToast.Animations.POPUP)).show();
    }

    public static SuperToast.Animations getAnimations() {
        SuperToast.Animations animations = null;
        int type = 0;
        type = new Random().nextInt(4);
        switch (type) {
            case 0:
                animations = SuperToast.Animations.FADE;
                break;
            case 1:
                animations = SuperToast.Animations.FLYIN;
                break;
            case 2:
                animations = SuperToast.Animations.SCALE;
                break;
            case 3:
                animations = SuperToast.Animations.POPUP;
                break;
            default:
                animations = SuperToast.Animations.FADE;
                break;
        }
        return animations;
    }


    public static Long getLongTime() {
        return System.currentTimeMillis();
    }

    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager manager =
                (ConnectivityManager) ctx.getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
    }


    public static void deleteFile(File oldPath) {
        if (oldPath.isDirectory()) {
            File[] files = oldPath.listFiles();
            for (File file : files) {
                deleteFile(file);
            }
        }
        oldPath.delete();
    }
}
