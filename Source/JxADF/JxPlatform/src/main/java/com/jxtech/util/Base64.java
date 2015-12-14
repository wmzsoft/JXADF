package com.jxtech.util;

public final class Base64 {
    private static final int BASELENGTH = 255;
    private static final int LOOKUPLENGTH = 64;
    private static final int TWENTYFOURBITGROUP = 24;
    private static final int EIGHTBIT = 8;
    private static final int SIXTEENBIT = 16;
    private static final int SIXBIT = 6;
    private static final int FOURBYTE = 4;
    private static final int SIGN = -128;
    private static final char PAD = 61;
    private static final boolean fDebug = false;
    private static final byte[] base64Alphabet = new byte[BASELENGTH];
    private static final char[] lookUpBase64Alphabet = new char[LOOKUPLENGTH];

    protected static boolean isWhiteSpace(char paramChar) {
        return ((paramChar == ' ') || (paramChar == '\r') || (paramChar == '\n') || (paramChar == '\t'));
    }

    protected static boolean isPad(char paramChar) {
        return (paramChar == '=');
    }

    protected static boolean isData(int paramChar) {
        return (base64Alphabet[paramChar] != -1);
    }

    protected static boolean isBase64(char paramChar) {
        return ((isWhiteSpace(paramChar)) || (isPad(paramChar)) || (isData(paramChar)));
    }

    public static String encode(byte[] paramArrayOfByte) {
        if (paramArrayOfByte == null)
            return null;
        int i = paramArrayOfByte.length * EIGHTBIT;
        if (i == 0)
            return "";
        int j = i % TWENTYFOURBITGROUP;
        int k = i / TWENTYFOURBITGROUP;
        int l = (j != 0) ? k + 1 : k;
        int i1 = (l - 1) / 19 + 1;
        char[] arrayOfChar = null;
        arrayOfChar = new char[l * 4 + i1];
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        int i11;
        int i12;
        int i13;
        for (int i10 = 0; i10 < i1 - 1; ++i10) {
            for (i11 = 0; i11 < 19; ++i11) {
                i4 = paramArrayOfByte[(i8++)];
                i5 = paramArrayOfByte[(i8++)];
                i6 = paramArrayOfByte[(i8++)];
                i3 = (byte) (i5 & 0xF);
                i2 = (byte) (i4 & 0x3);
                i12 = ((i4 & 0xFFFFFF80) == 0) ? (byte) (i4 >> 2) : (byte) (i4 >> 2 ^ 0xC0);
                i13 = ((i5 & 0xFFFFFF80) == 0) ? (byte) (i5 >> 4) : (byte) (i5 >> 4 ^ 0xF0);
                int i14 = ((i6 & 0xFFFFFF80) == 0) ? (byte) (i6 >> 6) : (byte) (i6 >> 6 ^ 0xFC);
                arrayOfChar[(i7++)] = lookUpBase64Alphabet[i12];
                arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i13 | i2 << 4)];
                arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i3 << 2 | i14)];
                arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i6 & 0x3F)];
                ++i9;
            }
            arrayOfChar[(i7++)] = '\n';
        }
        while (i9 < k) {
            i4 = paramArrayOfByte[(i8++)];
            i5 = paramArrayOfByte[(i8++)];
            i6 = paramArrayOfByte[(i8++)];
            i3 = (byte) (i5 & 0xF);
            i2 = (byte) (i4 & 0x3);
            i11 = ((i4 & 0xFFFFFF80) == 0) ? (byte) (i4 >> 2) : (byte) (i4 >> 2 ^ 0xC0);
            i12 = ((i5 & 0xFFFFFF80) == 0) ? (byte) (i5 >> 4) : (byte) (i5 >> 4 ^ 0xF0);
            i13 = ((i6 & 0xFFFFFF80) == 0) ? (byte) (i6 >> 6) : (byte) (i6 >> 6 ^ 0xFC);
            arrayOfChar[(i7++)] = lookUpBase64Alphabet[i11];
            arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i12 | i2 << 4)];
            arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i3 << 2 | i13)];
            arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i6 & 0x3F)];
            ++i9;
        }
        if (j == EIGHTBIT) {
            i4 = paramArrayOfByte[i8];
            i2 = (byte) (i4 & 0x3);
            i11 = ((i4 & 0xFFFFFF80) == 0) ? (byte) (i4 >> 2) : (byte) (i4 >> 2 ^ 0xC0);
            arrayOfChar[(i7++)] = lookUpBase64Alphabet[i11];
            arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i2 << 4)];
            arrayOfChar[(i7++)] = '=';
            arrayOfChar[(i7++)] = '=';
        } else if (j == SIXTEENBIT) {
            i4 = paramArrayOfByte[i8];
            i5 = paramArrayOfByte[(i8 + 1)];
            i3 = (byte) (i5 & 0xF);
            i2 = (byte) (i4 & 0x3);
            i11 = ((i4 & 0xFFFFFF80) == 0) ? (byte) (i4 >> 2) : (byte) (i4 >> 2 ^ 0xC0);
            i12 = ((i5 & 0xFFFFFF80) == 0) ? (byte) (i5 >> 4) : (byte) (i5 >> 4 ^ 0xF0);
            arrayOfChar[(i7++)] = lookUpBase64Alphabet[i11];
            arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i12 | i2 << 4)];
            arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i3 << 2)];
            arrayOfChar[(i7++)] = '=';
        }
        arrayOfChar[i7] = '\n';
        return new String(arrayOfChar);
    }

    public static byte[] decode(String paramString) {
        if (paramString == null)
            return null;
        char[] arrayOfChar = paramString.toCharArray();
        int i = removeWhiteSpace(arrayOfChar);
        if (i % 4 != 0)
            return null;
        int j = i / 4;
        if (j == 0)
            return new byte[0];
        byte[] arrayOfByte1 = null;
        int k = 0;
        int l = 0;
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        char c1 = '\0';
        char c2 = '\0';
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        arrayOfByte1 = new byte[j * 3];
        while (i7 < j - 1) {
            if ((!(isData(i5 = arrayOfChar[(i9++)]))) || (!(isData(i6 = arrayOfChar[(i9++)]))) || (!(isData(c1 = arrayOfChar[(i9++)]))) || (!(isData(c2 = arrayOfChar[(i9++)]))))
                return null;
            k = base64Alphabet[i5];
            l = base64Alphabet[i6];
            i1 = base64Alphabet[c1];
            i2 = base64Alphabet[c2];
            arrayOfByte1[(i8++)] = (byte) (k << 2 | l >> 4);
            arrayOfByte1[(i8++)] = (byte) ((l & 0xF) << 4 | i1 >> 2 & 0xF);
            arrayOfByte1[(i8++)] = (byte) (i1 << 6 | i2);
            ++i7;
        }
        if ((!(isData(i5 = arrayOfChar[(i9++)]))) || (!(isData(i6 = arrayOfChar[(i9++)]))))
            return null;
        k = base64Alphabet[i5];
        l = base64Alphabet[i6];
        c1 = arrayOfChar[(i9++)];
        c2 = arrayOfChar[(i9++)];
        if ((!(isData(c1))) || (!(isData(c2)))) {
            byte[] arrayOfByte2;
            if ((isPad(c1)) && (isPad(c2))) {
                if ((l & 0xF) != 0)
                    return null;
                arrayOfByte2 = new byte[i7 * 3 + 1];
                System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, i7 * 3);
                arrayOfByte2[i8] = (byte) (k << 2 | l >> 4);
                return arrayOfByte2;
            }
            if ((!(isPad(c1))) && (isPad(c2))) {
                i1 = base64Alphabet[c1];
                if ((i1 & 0x3) != 0)
                    return null;
                arrayOfByte2 = new byte[i7 * 3 + 2];
                System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, i7 * 3);
                arrayOfByte2[(i8++)] = (byte) (k << 2 | l >> 4);
                arrayOfByte2[i8] = (byte) ((l & 0xF) << 4 | i1 >> 2 & 0xF);
                return arrayOfByte2;
            }
            return null;
        }
        i1 = base64Alphabet[c1];
        i2 = base64Alphabet[c2];
        arrayOfByte1[(i8++)] = (byte) (k << 2 | l >> 4);
        arrayOfByte1[(i8++)] = (byte) ((l & 0xF) << 4 | i1 >> 2 & 0xF);
        arrayOfByte1[(i8++)] = (byte) (i1 << 6 | i2);
        return arrayOfByte1;
    }

    protected static int removeWhiteSpace(char[] paramArrayOfChar) {
        if (paramArrayOfChar == null)
            return 0;
        int i = 0;
        int j = paramArrayOfChar.length;
        for (int k = 0; k < j; ++k) {
            if (isWhiteSpace(paramArrayOfChar[k]))
                continue;
            paramArrayOfChar[(i++)] = paramArrayOfChar[k];
        }
        return i;
    }

    static {
        for (int i = 0; i < BASELENGTH; ++i)
            base64Alphabet[i] = -1;
        for (int j = 90; j >= 65; --j)
            base64Alphabet[j] = (byte) (j - 65);
        for (int k = 122; k >= 97; --k)
            base64Alphabet[k] = (byte) (k - 97 + 26);
        for (int l = 57; l >= 48; --l)
            base64Alphabet[l] = (byte) (l - 48 + 52);
        base64Alphabet[43] = 62;
        base64Alphabet[47] = 63;
        for (int i1 = 0; i1 <= 25; ++i1)
            lookUpBase64Alphabet[i1] = (char) (65 + i1);
        int i2 = 26;
        for (int i3 = 0; i2 <= 51; ++i3) {
            lookUpBase64Alphabet[i2] = (char) (97 + i3);
            ++i2;
        }
        int i4 = 52;
        for (int i5 = 0; i4 <= PAD; ++i5) {
            lookUpBase64Alphabet[i4] = (char) (48 + i5);
            ++i4;
        }
        lookUpBase64Alphabet[62] = '+';
        lookUpBase64Alphabet[63] = '/';
    }
}
