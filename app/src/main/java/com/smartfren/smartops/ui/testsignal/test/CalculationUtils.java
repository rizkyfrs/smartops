package com.smartfren.smartops.ui.testsignal.test;

import android.telephony.TelephonyManager;

/**
 * A utils class for calculations used throughout the app, as well as some validation logic.
 *
 * @since 0.0.5
 */
public final class CalculationUtils {
    /**
     * Checks to make sure the provide LTE Cell ID is with in the valid range as defined by the 3GPP specification (0-268435455).
     *
     * @param cellId The LTE Cell ID to validate.
     * @return True if the LTE Cell ID is valid, false otherwise.
     */
    public static boolean isLteCellIdValid(int cellId) {
        return cellId >= 0 && cellId <= 268_435_455;
    }

    /**
     * Pulls the Macro eNodeB ID from an LTE Cell Id.  The Macro eNB ID is the first 20 bits of the Cell Identity.
     *
     * @param cellId The LTE Cell ID to pull the Macro eNodeB ID from.
     * @return The Macro eNodeB ID (first 20 bits).
     */
    public static int getEnodebIdFromCellId(int cellId) {
        return cellId >> 8;
    }

    /**
     * Pulls the sector ID from an LTE Cell Id.  The sectorID is the last 8 bits of the Cell Identity.
     *
     * @param cellId The LTE Cell ID to pull the sector ID from.
     * @return The sector ID (last 8 bits).
     */
    public static int getSectorIdFromCellId(int cellId) {
        return cellId & 0xFF;
    }

    /**
     * Pulls the Primary Sync Sequence (PSS) from an LTE Physical Cell ID (PCI).
     *
     * @param pci The LTE Physical Cell ID to pull the PSS from.
     * @return The PSS.
     */
    public static int getPrimarySyncSequence(int pci) {
        return pci % 3;
    }

    /**
     * Pulls the Secondary Sync Sequence (SSS) from an LTE Physical Cell ID (PCI).
     *
     * @param pci The LTE Physical Cell ID to pull the SSS from.
     * @return The PSS.
     */
    public static int getSecondarySyncSequence(int pci) {
        return pci / 3;
    }

    /**
     * ¬
     * Given the Android int value for a network type, return the String value that it represents.
     *
     * @param networkType The network type in integer form as reported by Android.
     * @return The human friendly String representation of the network type int value.
     * @see TelephonyManager#getNetworkType()
     */
    public static String getNetworkType(int networkType) {
        switch (networkType) {
            case 1:
                return NetworkSurveyConstants.GPRS;

            case 2:
                return NetworkSurveyConstants.EDGE;

            case 3:
                return NetworkSurveyConstants.UMTS;

            case 4:
                return NetworkSurveyConstants.CDMA;

            case 5:
                return NetworkSurveyConstants.EVDO_0;

            case 6:
                return NetworkSurveyConstants.EVDO_A;

            case 7:
                return NetworkSurveyConstants.RTT1x;

            case 8:
                return NetworkSurveyConstants.HSDPA;

            case 9:
                return NetworkSurveyConstants.HSUPA;

            case 10:
                return NetworkSurveyConstants.HSPA;

            case 11:
                return NetworkSurveyConstants.IDEN;

            case 12:
                return NetworkSurveyConstants.EVDO_B;

            case 13:
                return NetworkSurveyConstants.LTE;

            case 14:
                return NetworkSurveyConstants.EHRPD;

            case 15:
                return NetworkSurveyConstants.HSPAP;

            case 16:
                return NetworkSurveyConstants.GSM;

            case 17:
                return NetworkSurveyConstants.TD_SCDMA;

            case 18:
                return NetworkSurveyConstants.IWLAN;

            case 19:
                return NetworkSurveyConstants.LTE_CA;

            case 20:
                return NetworkSurveyConstants.NR;

            case 0:
            default:
                return "Unknown";
        }
    }
}
