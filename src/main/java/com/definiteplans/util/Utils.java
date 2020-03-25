package com.definiteplans.util;

import java.net.URI;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.definiteplans.dom.EnumValue;


public class Utils {

    public static String getVariable(String name) {
        String result = System.getProperty(name);
        if (StringUtils.isBlank(result)) {
            result = System.getenv(name);
        }
        return result;
    }


    public static List<Integer> getAgeValues() {
        List<Integer> arr = new ArrayList<>();
        for (int i = 18; i <= 100; i++)
            arr.add(Integer.valueOf(i));
        return arr;
    }

    public static List<EnumValue> getHeightValues() {
        List<EnumValue> arr = new ArrayList<>();

        for (int f = 1; f <= 8; f++) {
            for (int i = 1; i <= 11; i++) {
                int numInches = f * 12 + i;
                arr.add(new EnumValue(numInches, f + "' " + i + "\""));
            }
        }

        return arr;
    }

//    public static Integer getFilterIntValue(IRequestParameters postData, String key) {
//        return (postData.getParameterValue(key) != null && isInt(postData.getParameterValue(key))) ? toInt(postData.getParameterValue(key).toString()) : null;
//    }
//
//    public static List<Integer> getFilterValues(List<StringValue> arr) {
//        List<Integer> arrParam = new ArrayList<>();
//        if (arr != null && arr.size() > 0) {
//            for (StringValue sv : arr) {
//                if (!isInt(sv))
//                    continue;
//                arrParam.add(toInt(sv.toString()));
//            }
//        }
//        return arrParam;
//    }

    public static List<Integer> getFilterValues2(List<EnumValue> arr) {
        List<Integer> arrParam = new ArrayList<>();
        if (arr != null && arr.size() > 0)
            for (EnumValue sv : arr) arrParam.add(Integer.valueOf(sv.getId()));

        return arrParam;
    }

    public static String mapToString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        if (map == null || map.size() == 0) return sb.toString();

        for (String key : map.keySet()) {
            if (sb.length() > 0) sb.append("&");

            String value = map.get(key);
            if (value == null) value = "";
            sb.append(key).append("=").append(value);
        }

        return sb.toString();
    }

    public static Map<String, String> stringToMap(String input) {
        Map<String, String> map = new HashMap<>();
        if (input == null || input.length() == 0) return map;

        String[] nameValuePairs = input.split("&");
        for (String nameValuePair : nameValuePairs) {
            String[] nameValue = nameValuePair.split("=");
            map.put(nameValue[0], (nameValue.length > 1) ? nameValue[1] : "");
        }

        return map;
    }

//    public static String getString(JSONObject obj, String key) {
//        try {
//            return obj.getString(key);
//        } catch (JSONException e) {
//            return "";
//        }
//    }
//
//    public static String getCleanStringValue(StringValue sv) {
//        if (sv == null || sv.isNull() || sv.isEmpty()) return null;
//        return sv.toString().replace("\r", "").replace("\n", "");
//    }
//
//    public static boolean isInt(StringValue sv) {
//        try {
//            sv.toInt();
//            return true;
//        } catch (StringValueConversionException e) {
//            return false;
//        }
//    }

    public static boolean isInt(String sv) {
        try {
            Integer.parseInt(sv);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Long toLong(String s) {
        try {
            return Long.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer toInt(String s) {
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String formatNumber(int n) {
        try {
            return NumberFormat.getIntegerInstance().format(n);
        } catch (Exception e) {
            return String.valueOf(n);
        }
    }

    public static String formatCurrency(double n) {
        try {
            return NumberFormat.getCurrencyInstance().format(n);
        } catch (Exception e) {
            return String.valueOf(n);
        }
    }

    public static boolean isValidUrl(String url) {
        if (url == null || url.length() == 0) return false;

        try {
            URI.create(url);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
