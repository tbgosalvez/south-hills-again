/**
 * Created by tbg on 12/2/16.
 */
package com.tbgosalvez.southhills;

import android.app.Activity;
import java.util.HashMap;

public class CONSTANTS
{
    public static final String strOrder_Details = "et_msg contents";
    public static final String strArtist_Choice = "rg_bands selected";
    public static final String strNumberOfTickets = "quantity of tickets purchased";
    public static final String strTicketPrice  = "unit price per ticket";
    public static final String strTotalPrice = "grand total ticket price";
    public static final String strSnackChoices = "snacks and/or drink";
    public static final String strBillingInfo = "user billing information";
    public static final String strBundle = "bundle: order details, user billing info";
    public static final String strPmtType = "payment type";
    static String[] arrNames = { "Metallica","Taylor Swift","Ariana Grande","Rammstein" };

    /*  strings.xml

        <string-array name="array_artist_names">
            <item>Metallica</item>
            <item>Taylor Swift</item>
            <item>Ariana Grande</item>
            <item>Rammstein</item>
        </string-array>

        strings.xml     */

    public static final HashMap<String, Float> arrPrices = new HashMap<String, Float>()
    {
        {
            put(arrNames[0], 100f);
            put(arrNames[1], 88.8f);
            put(arrNames[2], 55f);
            put(arrNames[3], 66.6f);
        }
    };

    public static final HashMap<String, Integer> arrMP3s = new HashMap<String, Integer>()
    {
        {
            put(arrNames[0],R.raw.cd);
            put(arrNames[1], R.raw.bs);
            put(arrNames[2], R.raw.dw);
            put(arrNames[3],R.raw.dh);
        }
    };

    public static final HashMap<String, Integer> arrImages = new HashMap<String, Integer>()
    {
        {
            put(arrNames[0],R.drawable.ma);
            put(arrNames[1], R.drawable.ts);
            put(arrNames[2],R.drawable.ag);
            put(arrNames[3], R.drawable.rs);
        }
    };


    public static final String strEmail = "email";
    public static final String strPasswd = "passwd";

}
