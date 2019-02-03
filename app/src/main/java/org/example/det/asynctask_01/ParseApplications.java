package org.example.det.asynctask_01;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by D.Alfier on 31.01.2019
 * ---------------------------------
 */

public class ParseApplications {
    private static final String TAG = "ParseApplications";

    private ArrayList<FeedEntry> applications;

    ParseApplications() {
        this.applications = new ArrayList<>();  //wir initialisieren die Arrayliste selbst.
    }

    public ArrayList<FeedEntry> getApplications() {
        return applications;
    }

    boolean parse(String xmlData) {
        boolean status = true;
        FeedEntry currentRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "parse: Starting tag for " + tagName);
                        if ("entry".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentRecord = new FeedEntry();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();      //Wir merken uns den Text in einem Tag
                        break;
                    case XmlPullParser.END_TAG:         //..und wenn wir auf ein EndTag stoßen
                        //schauen wir weiter, was wir damit anfangen
                        Log.d(TAG, "parse: Ending tag for " + tagName);
                        if (inEntry) {
                            if ("entry".equalsIgnoreCase(tagName)) {
                                //nicht anders 'rum: tagName.equalsIgnoreCase("entry") - weil tagName
                                // kann "null" sein. dann gibt's 'ne Exception!
                                applications.add(currentRecord);
                                inEntry = false;
                            } else if ("name".equalsIgnoreCase(tagName)) {
                                //Wenn das EndTag </name> war, dann war das davor "textvalue"
                                //der Name ...
                                currentRecord.setName(textValue);
                            } else if ("artist".equalsIgnoreCase(tagName)) {
                                currentRecord.setArtist(textValue);
                            } else if ("releaseDate".equalsIgnoreCase(tagName)) {
                                currentRecord.setReleaseDate(textValue);
                            } else if ("summary".equalsIgnoreCase(tagName)) {
                                currentRecord.setSummary(textValue);
                            } else if ("image".equalsIgnoreCase(tagName)) {
                                currentRecord.setImageURL(textValue);
                            }
                        }
                        break;
                    default:
                        //Nothing to do
                }
                eventType = xpp.next();         //Parser soll mit dem nächsten Event weiter machen
            }
            for (FeedEntry app : applications) {
                Log.d(TAG, "********************");
                Log.d(TAG, app.toString());
            }

        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }
}
