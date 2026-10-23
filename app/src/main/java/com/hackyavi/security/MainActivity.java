package com.hackyavi.security;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private final int bg = Color.rgb(8, 14, 24);
    private final int panel = Color.rgb(17, 29, 43);
    private final int green = Color.rgb(51, 220, 155);
    private final int blue = Color.rgb(67, 164, 255);
    private LinearLayout content;
    private TextView status;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showHome();
    }

    private GradientDrawable rounded(int color) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(16));
        d.setStroke(dp(1), Color.rgb(35, 58, 76));
        return d;
    }

    private int dp(float v) { return (int)(v * getResources().getDisplayMetrics().density + 0.5f); }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setLineSpacing(dp(3), 1.0f);
        return t;
    }

    private LinearLayout column() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        return l;
    }

    private void base(String title, String subtitle) {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        LinearLayout page = column();
        page.setPadding(dp(20), dp(22), dp(20), dp(28));
        page.setBackgroundColor(bg);
        scroll.setBackgroundColor(bg);

        TextView brand = text("HACKY-AVI  /  OPEN SECURITY", 12, green, true);
        brand.setLetterSpacing(0.08f);
        page.addView(brand);
        TextView heading = text(title, 28, Color.WHITE, true);
        LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(-1, -2);
        hp.topMargin = dp(10);
        page.addView(heading, hp);
        TextView sub = text(subtitle, 14, Color.rgb(171, 191, 208), false);
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(-1, -2);
        sp.topMargin = dp(6);
        sp.bottomMargin = dp(18);
        page.addView(sub, sp);
        content = page;
        scroll.addView(page);
        setContentView(scroll);
    }

    private void card(String title, String description, Runnable action) {
        LinearLayout box = column();
        box.setPadding(dp(16), dp(15), dp(16), dp(15));
        box.setBackground(rounded(panel));
        TextView t = text(title, 17, green, true);
        TextView d = text(description, 13, Color.rgb(195, 211, 224), false);
        box.addView(t);
        LinearLayout.LayoutParams dp = new LinearLayout.LayoutParams(-1, -2);
        dp.topMargin = dp(6);
        box.addView(d, dp);
        if (action != null) {
            Button b = new Button(this);
            b.setText("OPEN TOOL");
            b.setTextColor(Color.rgb(6, 22, 28));
            b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(green));
            LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, -2);
            bp.topMargin = dp(10);
            box.addView(b, bp);
            b.setOnClickListener(v -> action.run());
            box.setOnClickListener(v -> action.run());
        }
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.bottomMargin = dp(12);
        content.addView(box, p);
    }

    private void backButton() {
        Button b = new Button(this);
        b.setText("←  BACK TO HOME");
        b.setTextColor(blue);
        b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(panel));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.topMargin = dp(10);
        content.addView(b, p);
        b.setOnClickListener(v -> showHome());
    }

    private void showHome() {
        base("Phone security,\nmade understandable.", "A privacy-first toolkit. Checks run locally on your device.");
        status = text(networkSummary(), 13, Color.WHITE, false);
        status.setPadding(dp(14), dp(14), dp(14), dp(14));
        status.setBackground(rounded(Color.rgb(13, 39, 42)));
        LinearLayout.LayoutParams st = new LinearLayout.LayoutParams(-1, -2);
        st.bottomMargin = dp(16);
        content.addView(status, st);

        card("01  Network diagnostics", "Check connectivity and available network transport.", this::showNetwork);
        card("02  App privacy checker", "Open Android app settings to inspect permissions and special access.", this::showPrivacy);
        card("03  DNS & connection info", "View DNS addresses reported by Android for the active network.", this::showDns);
        card("04  Cybersecurity learning lab", "Short defensive lessons and safe practice rules.", this::showLab);
        card("05  Password strength checker", "Estimate password strength on-device. Input is not saved or sent.", this::showPassword);

        TextView foot = text("Version 0.1.0  •  MIT License  •  Community project", 11, Color.rgb(125, 151, 170), false);
        foot.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams fp = new LinearLayout.LayoutParams(-1, -2);
        fp.topMargin = dp(6);
        content.addView(foot, fp);
    }

    private String networkSummary() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return "Network status unavailable";
        Network n = cm.getActiveNetwork();
        if (n == null) return "●  No active network detected";
        NetworkCapabilities c = cm.getNetworkCapabilities(n);
        if (c == null) return "●  Network connected; details unavailable";
        String type = "Other network";
        if (c.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) type = "Wi-Fi";
        else if (c.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) type = "Mobile data";
        else if (c.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) type = "Ethernet";
        return "●  Active network: " + type + "\nInternet capability: " +
                (c.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ? "available" : "not reported");
    }

    private void showNetwork() {
        base("Network diagnostics", "Basic, read-only checks using Android's network APIs.");
        addParagraph(networkSummary());
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null && cm.getActiveNetwork() != null) {
            NetworkCapabilities c = cm.getNetworkCapabilities(cm.getActiveNetwork());
            if (c != null) {
                addParagraph("Validated internet: " + yesNo(c.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)));
                addParagraph("Metered network: " + yesNo(!c.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)));
            }
        }
        addParagraph("Note: Android's connectivity status is an indicator, not proof that every website or service is reachable.");
        backButton();
    }

    private String yesNo(boolean value) { return value ? "Yes" : "No"; }

    private void showPrivacy() {
        base("App privacy checker", "Android controls access to other apps. This tool opens system settings rather than bypassing those protections.");
        card("Open app settings", "Review installed apps, permissions, battery use, and storage access in Android Settings.", () -> {
            try { startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS)); }
            catch (Exception e) { startActivity(new Intent(Settings.ACTION_SETTINGS)); }
        });
        card("Special app access", "Review options such as notification access, display over other apps, and usage access when available.", () -> {
            try { startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)); }
            catch (Exception e) { startActivity(new Intent(Settings.ACTION_SETTINGS)); }
        });
        addParagraph("This app does not read private data from other apps or change their permissions automatically.");
        backButton();
    }

    private void showDns() {
        base("DNS & connection info", "Shows DNS server addresses exposed by Android for the current active network.");
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        Network n = cm == null ? null : cm.getActiveNetwork();
        LinkProperties lp = (cm == null || n == null) ? null : cm.getLinkProperties(n);
        if (lp == null) {
            addParagraph("No active network properties are available right now. Connect to Wi-Fi or mobile data and try again.");
        } else {
            addParagraph("Interface: " + safe(lp.getInterfaceName()));
            List<InetAddress> dns = lp.getDnsServers();
            if (dns == null || dns.isEmpty()) {
                addParagraph("DNS server addresses are not exposed for this network.");
            } else {
                StringBuilder sb = new StringBuilder("DNS servers reported by Android:\n");
                for (InetAddress address : dns) sb.append("• ").append(address.getHostAddress()).append("\n");
                addParagraph(sb.toString());
            }
            addParagraph("Private DNS mode and VPN routing can affect what these values mean. This is not packet capture and does not show every app's connections.");
        }
        backButton();
    }

    private String safe(String s) { return (s == null || s.isEmpty()) ? "Not reported" : s; }

    private void showLab() {
        base("Cybersecurity learning lab", "Defensive fundamentals. Practise only on devices and networks you own or are authorized to assess.");
        card("1. DNS basics", "DNS translates names such as example.com into network addresses. A DNS resolver can see the domains it is asked to resolve, subject to encryption and caching.", null);
        card("2. Network permissions", "Android permissions and app sandboxing reduce what one app can access. Review permissions regularly and remove access an app does not need.", null);
        card("3. Safer Wi-Fi", "Use WPA2-AES or WPA3 when available, update router firmware, change default admin credentials, and disable WPS if you do not need it.", null);
        card("4. Authorized testing", "Define the scope first, get written permission, use test accounts, avoid disrupting services, and document findings responsibly.", null);
        card("5. Phishing awareness", "Check the actual domain, be cautious with unexpected attachments, and never share one-time codes or recovery keys.", null);
        backButton();
    }

    private void showPassword() {
        base("Password strength checker", "Runs locally. Avoid entering a password you currently use for an important account; test with an example instead.");
        EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setHint("Enter a sample password");
        input.setTextColor(Color.WHITE);
        input.setHintTextColor(Color.GRAY);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        input.setPadding(dp(12), dp(10), dp(12), dp(10));
        input.setBackground(rounded(panel));
        content.addView(input, new LinearLayout.LayoutParams(-1, -2));

        TextView result = text("Your result will appear here.", 15, Color.WHITE, false);
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2);
        rp.topMargin = dp(14);
        content.addView(result, rp);

        Button check = new Button(this);
        check.setText("CHECK SAMPLE");
        check.setTextColor(Color.rgb(6, 22, 28));
        check.setBackgroundTintList(android.content.res.ColorStateList.valueOf(green));
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2);
        cp.topMargin = dp(12);
        content.addView(check, cp);
        check.setOnClickListener(v -> result.setText(passwordFeedback(input.getText().toString())));
        addParagraph("This is a simple educational estimate, not a guarantee against guessing or data breaches. Do not reuse passwords; use a password manager and unique passwords.");
        backButton();
    }

    private String passwordFeedback(String p) {
        if (p.isEmpty()) return "Enter a sample password to check.";
        int score = 0;
        if (p.length() >= 12) score++;
        if (p.length() >= 16) score++;
        boolean lower = p.matches("(?s).*\\p{Lower}.*");
        boolean upper = p.matches("(?s).*\\p{Upper}.*");
        boolean digit = p.matches("(?s).*\\d.*");
        boolean symbol = p.matches("(?s).*[^\\p{Alnum}].*");
        if (lower) score++;
        if (upper) score++;
        if (digit) score++;
        if (symbol) score++;
        String rating = score <= 2 ? "Weak" : score <= 4 ? "Fair" : score <= 5 ? "Good" : "Stronger by these basic checks";
        String advice = p.length() < 12 ? "\nTry a longer passphrase." : "\nLength is a useful start.";
        if (p.toLowerCase().matches(".*(password|qwerty|123456|letmein).*")) advice += "\nAvoid common words and sequences.";
        return "Estimate: " + rating + "\nLength: " + p.length() + " characters" + advice + "\n\nThis simple score does not check known breached-password lists.";
    }

    private void addParagraph(String s) {
        TextView t = text(s, 14, Color.rgb(204, 219, 231), false);
        t.setPadding(dp(14), dp(14), dp(14), dp(14));
        t.setBackground(rounded(panel));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.bottomMargin = dp(12);
        content.addView(t, p);
    }
}
