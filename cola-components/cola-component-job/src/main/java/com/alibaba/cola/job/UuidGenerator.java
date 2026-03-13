package com.alibaba.cola.job;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class UuidGenerator {
    private static final char[] CHILD_ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    private static final char[] PARENT_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss-");

    private static int CUSTOM_LENGTH = 8;

    /**
     * job id, 时间+随机数，样例：20250317T141750-dgkyfjlm
     * @return
     */
    public static String nextJobId() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String formattedDateTime = currentDateTime.format(FORMATTER);
        return formattedDateTime + NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, CHILD_ALPHABET,
                CUSTOM_LENGTH);
    }

    /**
     * job id, 时间+随机数+jobName，样例：20250317T141750-dgkyfjlm-myJobName
     * @return
     */
    public static String nextJobId(String jobName){
        return nextJobId()+"-"+jobName;
    }

    /**
     * batch job id, 时间+大写之母，样例： 20250317T141750-ADBAMLAU
     * @return
     */
    public static String nextBatchJobId() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String formattedDateTime = currentDateTime.format(FORMATTER);
        return formattedDateTime + NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, PARENT_ALPHABET,
                CUSTOM_LENGTH);
    }

    /**
     * 生成随机的NanoId工具内部类
     */
    public final class NanoIdUtils {
        public static final SecureRandom DEFAULT_NUMBER_GENERATOR = new SecureRandom();
        public static final char[] DEFAULT_ALPHABET = "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        public static final int DEFAULT_SIZE = 21;

        private NanoIdUtils() {
        }

        public static String randomNanoId() {
            return randomNanoId(DEFAULT_NUMBER_GENERATOR, DEFAULT_ALPHABET, 21);
        }

        public static String randomNanoId(Random random, char[] alphabet, int size) {
            if (random == null) {
                throw new IllegalArgumentException("random cannot be null.");
            } else if (alphabet == null) {
                throw new IllegalArgumentException("alphabet cannot be null.");
            } else if (alphabet.length != 0 && alphabet.length < 256) {
                if (size <= 0) {
                    throw new IllegalArgumentException("size must be greater than zero.");
                } else {
                    int mask = (2 << (int)Math.floor(Math.log((double)(alphabet.length - 1)) / Math.log(2.0D))) - 1;
                    int step = (int)Math.ceil(1.6D * (double)mask * (double)size / (double)alphabet.length);
                    StringBuilder idBuilder = new StringBuilder();

                    while(true) {
                        byte[] bytes = new byte[step];
                        random.nextBytes(bytes);

                        for(int i = 0; i < step; ++i) {
                            int alphabetIndex = bytes[i] & mask;
                            if (alphabetIndex < alphabet.length) {
                                idBuilder.append(alphabet[alphabetIndex]);
                                if (idBuilder.length() == size) {
                                    return idBuilder.toString();
                                }
                            }
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("alphabet must contain between 1 and 255 symbols.");
            }
        }
    }
}

