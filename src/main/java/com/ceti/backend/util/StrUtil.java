package com.ceti.backend.util;

import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil {
    public static final String DOT = ".";
    public static final String SLASH = "/";
    public static final String EMPTY = "";

    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String getGeneralField(String getOrSetMethodName) {
        if (getOrSetMethodName.startsWith("get") || getOrSetMethodName.startsWith("set")) {
            return cutPreAndLowerFirst(getOrSetMethodName, 3);
        }
        return null;
    }

    public static String genSetter(String fieldName) {
        return upperFirstAndAddPre(fieldName, "set");
    }

    public static String genGetter(String fieldName) {
        return upperFirstAndAddPre(fieldName, "get");
    }

    public static String cutPreAndLowerFirst(String str, int preLength) {
        if (str == null) {
            return null;
        }
        if (str.length() > preLength) {
            char first = Character.toLowerCase(str.charAt(preLength));
            if (str.length() > preLength + 1) {
                return first + str.substring(preLength + 1);
            }
            return String.valueOf(first);
        }
        return null;
    }

    public static String upperFirstAndAddPre(String str, String preString) {
        if (str == null || preString == null) {
            return null;
        }
        return preString + upperFirst(str);
    }

    public static String upperFirst(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static String lowerFirst(String str) {
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    public static String removePrefix(String str, String prefix) {
        if (str != null && str.startsWith(prefix)) {
            return str.substring(prefix.length());
        }
        return str;
    }

    public static String removePrefixIgnoreCase(String str, String prefix) {
        if (str != null && str.toLowerCase().startsWith(prefix.toLowerCase())) {
            return str.substring(prefix.length());
        }
        return str;
    }

    public static String removeSuffix(String str, String suffix) {
        if (str != null && str.endsWith(suffix)) {
            return str.substring(0, str.length() - suffix.length());
        }
        return str;
    }

    public static String removeSuffixIgnoreCase(String str, String suffix) {
        if (str != null && str.toLowerCase().endsWith(suffix.toLowerCase())) {
            return str.substring(0, str.length() - suffix.length());
        }
        return str;
    }

    public static List<String> split(String str, char separator) {
        return split(str, separator, 0);
    }

    public static List<String> split(String str, char separator, int limit) {
        if (str == null) {
            return null;
        }
        List<String> list = new ArrayList<String>(limit == 0 ? 16 : limit);
        if (limit == 1) {
            list.add(str);
            return list;
        }

        boolean isNotEnd = true;    //未结束切分的标志
        int strLen = str.length();
        StringBuilder sb = new StringBuilder(strLen);
        for (int i = 0; i < strLen; i++) {
            char c = str.charAt(i);
            if (isNotEnd && c == separator) {
                list.add(sb.toString());
                //清空StringBuilder
                sb.delete(0, sb.length());

                //当达到切分上限-1的量时，将所剩字符全部作为最后一个串
                if (limit != 0 && list.size() == limit - 1) {
                    isNotEnd = false;
                }
            } else {
                sb.append(c);
            }
        }
        list.add(sb.toString());
        return list;
    }

    public static String[] split(String str, String delimiter) {
        if (str == null) {
            return null;
        }
        if (str.trim().length() == 0) {
            return new String[]{str};
        }

        int dellen = delimiter.length();    //del length
        int maxparts = (str.length() / dellen) + 2;        // one more for the last
        int[] positions = new int[maxparts];

        int i, j = 0;
        int count = 0;
        positions[0] = -dellen;
        while ((i = str.indexOf(delimiter, j)) != -1) {
            count++;
            positions[count] = i;
            j = i + dellen;
        }
        count++;
        positions[count] = str.length();

        String[] result = new String[count];

        for (i = 0; i < count; i++) {
            result[i] = str.substring(positions[i] + dellen, positions[i + 1]);
        }
        return result;
    }

    public static String repeat(char c, int count) {
        char[] result = new char[count];
        for (int i = 0; i < count; i++) {
            result[i] = c;
        }
        return new String(result);
    }

    public static String convertCharset(String str, String sourceCharset, String destCharset) {
        if (isBlank(str) || isBlank(sourceCharset) || isBlank(destCharset)) {
            return str;
        }
        try {
            return new String(str.getBytes(sourceCharset), destCharset);
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

    public static boolean equalsNotEmpty(String str1, String str2) {
        if (isEmpty(str1)) {
            return false;
        }
        return str1.equals(str2);
    }

    public static String format(String template, Object... values) {
        return String.format(template.replace("{}", "%s"), values);
    }

    public static String getDMY(String date) {
        Pattern pattern1 = Pattern.compile("^\\d{4}[/|-]+\\d{2}[/|-]+\\d{2}");
        Pattern pattern2 = Pattern.compile("^\\d{1,2}[/|-]+\\d{1,2}[/|-]+\\d{2,4}");
        Matcher matcher2 = pattern2.matcher(date);
        Matcher matcher1 = pattern1.matcher(date);
        String format;
        if (matcher2.find()) {
            format = "dmy";
        } else if (matcher1.find()) {
            format = "ymd";
        } else {
            return null;
        }

        String[] result = date.split("[^\\d]");
        if (result.length < 3) {
            return null;
        }
        if (format.toLowerCase().equals("dmy")) {

            if (result[0].length() < 2) {
                result[0] = "0".concat(result[0]);
            }
            if (result[1].length() < 2) {
                result[1] = "0".concat(result[1]);
            }
            if (result[2].length() == 2) {
                result[2] = "20".concat(result[2]);
            }
            date = result[0] + result[1] + result[2];
        } else if (format.toLowerCase().equals("ymd")) {
            if (result[0].length() == 2) {
                result[0] = "20".concat(result[0]);
            }
            if (result[1].length() < 2) {
                result[1] = "0".concat(result[1]);
            }
            if (result[2].length() < 2) {
                result[2] = "0".concat(result[2]);
            }
            date = result[2] + result[1] + result[0];
        } else {
        }
        return date;
    }

    public static boolean compareText(String key, String line, double scoreRate) {
        key = key.trim().toLowerCase();
        line = line.trim().toLowerCase();

        int levenshteinDistance = levenshteinDistance(key, line);
        int length = Math.max(key.length(), line.length());
        double score = 1.0 - (double) levenshteinDistance / length;
        return score > scoreRate;
    }

    public static int levenshteinDistance(String x, String y) {
        int[][] dp = new int[x.length() + 1][y.length() + 1];

        for (int i = 0; i <= x.length(); ++i) {
            for (int j = 0; j <= y.length(); ++j) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = min(dp[i - 1][j - 1] + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)), dp[i - 1][j] + 1, dp[i][j - 1] + 1);
                }
            }
        }

        return dp[x.length()][y.length()];
    }

    public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    public static int min(int... numbers) {
        return Arrays.stream(numbers).min().orElse(2147483647);
    }

    public static String getCollectionTypeCode(String refNo) {
        //ma refNo 014CLLA1831200061 -> cat CLLA
        if (isBlank(refNo)) {
            return null;
        }
        if (refNo.length() < 7) {
            return null;
        }
        return refNo.substring(3, 7);
    }

    public static boolean containText(String left, String right) {
        if (left == null || right == null) {
            return true;
        }
        left = left.toLowerCase();
        right = right.toLowerCase();
        return left.contains(right) || right.contains(left);
    }

    public static String foldingAscii(String value) {
        if (isBlank(value)) {
            return value;
        }
        String nfdNormalizedString = Normalizer.normalize(value.toLowerCase(), Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("").replace('đ', 'd').replaceAll(" +", " ");
    }

}
