package com.guesslive.admin.common.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MD5Utils
{
    private static String byteArrayToHex(byte[] paramArrayOfByte)
    {
        char[] arrayOfChar1 = new char[16];
        char[] tmp8_6 = arrayOfChar1;
        tmp8_6[0] = 48;
        char[] tmp13_8 = tmp8_6;
        tmp13_8[1] = 49;
        char[] tmp18_13 = tmp13_8;
        tmp18_13[2] = 50;
        char[] tmp23_18 = tmp18_13;
        tmp23_18[3] = 51;
        char[] tmp28_23 = tmp23_18;
        tmp28_23[4] = 52;
        char[] tmp33_28 = tmp28_23;
        tmp33_28[5] = 53;
        char[] tmp38_33 = tmp33_28;
        tmp38_33[6] = 54;
        char[] tmp44_38 = tmp38_33;
        tmp44_38[7] = 55;
        char[] tmp50_44 = tmp44_38;
        tmp50_44[8] = 56;
        char[] tmp56_50 = tmp50_44;
        tmp56_50[9] = 57;
        char[] tmp62_56 = tmp56_50;
        tmp62_56[10] = 65;
        char[] tmp68_62 = tmp62_56;
        tmp68_62[11] = 66;
        char[] tmp74_68 = tmp68_62;
        tmp74_68[12] = 67;
        char[] tmp80_74 = tmp74_68;
        tmp80_74[13] = 68;
        char[] tmp86_80 = tmp80_74;
        tmp86_80[14] = 69;
        char[] tmp92_86 = tmp86_80;
        tmp92_86[15] = 70;
        char[] arrayOfChar2 = new char[paramArrayOfByte.length * 2];
        int k = paramArrayOfByte.length;
        int i = 0;
        int j = 0;
        while (i < k)
        {
            int m = paramArrayOfByte[i];
            int n = j + 1;
            arrayOfChar2[j] = arrayOfChar1[(m >>> 4 & 0xF)];
            j = n + 1;
            arrayOfChar2[n] = arrayOfChar1[(m & 0xF)];
            i += 1;
        }
        return new String(arrayOfChar2);
    }

    public static final String stringToMD5(String paramString)
    {
        try
        {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramString.getBytes());
            paramString = byteArrayToHex(localMessageDigest.digest());
            return paramString;
        }
        catch (Exception paramString1)
        {
            paramString1.printStackTrace();
        }
        return null;
    }
    


    public static String getSahreMd5(){
        String t = stringToMD5(new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis())) + "d5s02e3f42w3d7lu9ls03lds").toLowerCase();
        return t;
    }
}
