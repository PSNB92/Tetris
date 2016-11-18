package org.psnbtech;

class ArrayUtils {
    private static boolean[][] cloneArray(boolean[][] src) {
        int length = src.length;
        boolean[][] target = new boolean[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }

    static boolean[][] rotateArray(boolean[][] inputArray) {
        int side = inputArray.length;

        boolean[][] newArray = ArrayUtils.cloneArray(inputArray);

        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                newArray[j][side - i-1] = inputArray[i][j];
            }
        }

        return newArray;
    }
}
