package io.lerk.lrkFM.util;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import io.lerk.lrkFM.Handler;

public class VersionCheckTask extends AsyncTask<Void, String, String> {

    public static final int NEW_VERSION_NOTIF = 42;

    private final Handler<String> handler;

    public VersionCheckTask(Handler<String> handler) {
        this.handler = handler;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String newVersion = null;

        try {
            Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=io.lerk.lrkFM&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("https://www.google.com")
                    .get();
            if (document != null) {
                Elements element = document.getElementsContainingOwnText("Current Version");
                for (Element ele : element) {
                    if (ele.siblingElements() != null) {
                        Elements sibElemets = ele.siblingElements();
                        for (Element sibElemet : sibElemets) {
                            newVersion = sibElemet.text();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newVersion;
    }

    @Override
    protected void onPostExecute(String onlineVersion) {
        handler.handle(onlineVersion);
    }
}
