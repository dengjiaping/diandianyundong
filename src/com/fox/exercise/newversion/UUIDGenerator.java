package com.fox.exercise.newversion;

import java.net.InetAddress;
import java.util.Calendar;


public class UUIDGenerator {

    private static final byte ALPHABET[] = {(byte) '0', (byte) '1',
            (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6',
            (byte) '7', (byte) '8', (byte) '9', (byte) 'A', (byte) 'B',
            (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G',
            (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L',
            (byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q',
            (byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V',
            (byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z'};

    // 每天的毫�?
    protected static final int millisPerDay = 86400000;

    // 机器编号
    public static int hostId = 0;

    private static byte hostCode1 = ALPHABET[(hostId >> 12) & 0X0F];

    private static byte hostCode2 = ALPHABET[(hostId >> 8) & 0X0F];

    private static byte hostCode3 = ALPHABET[(hostId >> 4) & 0X0F];

    private static byte hostCode4 = ALPHABET[(hostId) & 0X0F];

    // private static byte hostCode1 = ALPHABET[(hostId >>> 4) & 0X0F];

    // private static byte hostCode2 = ALPHABET[hostId & 0X0F];

    // 本地的IP地址
    protected static final byte emptyIP[] = {(byte) 0, (byte) 0, (byte) 0,
            (byte) 0};

    protected static final byte IP[];

    static {
        byte addr[];
        try {
            addr = InetAddress.getLocalHost().getAddress();
        } catch (Exception e) {
            addr = emptyIP;
        }

        IP = addr;
    }

    // 序号
    private static short counter = (short) 1;

    // 日期
    private static long dateCount = 0;

    private static byte dateBuffer[] = null;

    // 生成日期�?
    private synchronized static byte[] createDateBuffer(long dd) {
        if (dateCount != dd) {
            dateBuffer = new byte[5];
            Calendar cal = Calendar.getInstance();
            int y = cal.get(Calendar.YEAR) - 2000;
            int y1 = (y & 0XF0) >>> 4;
            int y2 = y & 0X0F;
            int m = cal.get(Calendar.MONTH);
            int d = cal.get(Calendar.DAY_OF_MONTH);
            int h = cal.get(Calendar.HOUR_OF_DAY);
            dateBuffer[0] = ALPHABET[y1];
            dateBuffer[1] = ALPHABET[y2];
            dateBuffer[2] = ALPHABET[m];
            dateBuffer[3] = ALPHABET[d];
            dateBuffer[4] = ALPHABET[h];
        }

        return dateBuffer;
    }

    /**
     * 生成唯一的编�?
     *
     * @return
     */
    public static String getUUID() {
        byte[] uuid = new byte[16];
        // 当前时间，毫�?
        long tt = System.currentTimeMillis();
        long dd = tt / millisPerDay;
        int ss = (int) (tt % millisPerDay);

        // 日期：YYMD
        byte date[] = createDateBuffer(dd);
        System.arraycopy(date, 0, uuid, 0, 5);

        // 时间
        // int d1 = ss >>> 25;
        // int d2 = (ss >>> 20) & 0X1F;
        // int d3 = (ss >>> 15) & 0X1F;
        int d4 = (ss >>> 10) & 0X1F;
        int d5 = (ss >>> 5) & 0X1F;
        int d6 = ss & 0X1F;
        int i = 5;
        // uuid[i++] = ALPHABET[d1];
        // uuid[i++] = ALPHABET[d2];
        // uuid[i++] = ALPHABET[d3];
        uuid[i++] = ALPHABET[d4];
        uuid[i++] = ALPHABET[d5];
        uuid[i++] = ALPHABET[d6];

        // 机器编号
        uuid[i++] = hostCode1;
        uuid[i++] = hostCode2;
        uuid[i++] = hostCode3;
        uuid[i++] = hostCode4;

        // 序号
        short c;
        synchronized (UUIDGenerator.class) {
            if (counter < 1) {
                counter = 1;
            }

            c = counter++;
        }
        int c1 = (c >> 12) & 0X0F;
        int c2 = (c >> 8) & 0X0F;
        int c3 = (c >> 4) & 0X0F;
        int c4 = c & 0X0F;
        uuid[i++] = ALPHABET[c1];
        uuid[i++] = ALPHABET[c2];
        uuid[i++] = ALPHABET[c3];
        uuid[i++] = ALPHABET[c4];
        return new String(uuid);
    }

    public static void setInit(int uuid) {
        hostId = uuid;
        hostCode1 = ALPHABET[(uuid >> 12) & 0X0F];
        hostCode2 = ALPHABET[(uuid >> 8) & 0X0F];
        hostCode3 = ALPHABET[(uuid >> 4) & 0X0F];
        hostCode4 = ALPHABET[(uuid) & 0X0F];
    }


    public static void main(String a[]) throws Exception {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(System.currentTimeMillis());

        for (int i = 0; i < 100000; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(getUUID());
        }

    }

}
