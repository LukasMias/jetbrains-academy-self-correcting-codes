package correcter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Encode {

    //encoding and decoding
    static void encode(FileInputStream fileInputStream, FileOutputStream fileOutputStream) {
        while(true) {
            try{
                byte inputByte = (byte) fileInputStream.read();
                if(inputByte == -1) {
                    break;
                }
                fileOutputStream.write(getHammingByte(inputByte, false));
                fileOutputStream.write(getHammingByte(inputByte, true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void correctAndDecode(FileInputStream fileInputStream, FileOutputStream fileOutputStream) {
        while(true) {
            try {
                byte inputByte1 = (byte) fileInputStream.read();
                if(inputByte1 == -1) {
                    break;
                }
                byte outputByte1 = (byte) (inputByte1 ^ (1 << (8 - findHammingErrors(inputByte1))));
                byte inputByte2 = (byte) fileInputStream.read();
                byte outputByte2 = (byte) (inputByte2 ^ (1 << (8 - findHammingErrors(inputByte2))));

                fileOutputStream.write(undoHamming(outputByte1, false)
                        + undoHamming(outputByte2, true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //Hamming methods
    private static byte getHammingByte(byte inputByte, boolean isSecondByte) {
        int parityBit;
        byte outputByte = 0;
        int secondByteConstant = 0;

        if(isSecondByte) {
            secondByteConstant = 4;
        }

        outputByte += (byte) (getBit(inputByte,           secondByteConstant) << 5);
        outputByte += (byte) (getBit(inputByte, 1 + secondByteConstant) << 3);
        outputByte += (byte) (getBit(inputByte, 2 + secondByteConstant) << 2);
        outputByte += (byte) (getBit(inputByte, 3 + secondByteConstant) << 1);

        parityBit = (getBit(outputByte, 2)
                + getBit(outputByte, 4)
                + getBit(outputByte, 6) ) % 2;
        outputByte += (byte) (parityBit << 7);

        parityBit = (getBit(outputByte, 2)
                + getBit(outputByte, 5)
                + getBit(outputByte, 6) ) % 2;
        outputByte += (byte) (parityBit << 6);

        parityBit = (getBit(outputByte, 4)
                + getBit(outputByte, 5)
                + getBit(outputByte, 6) ) % 2;
        outputByte += (byte) (parityBit << 4);

        return outputByte;
    }

    private static int findHammingErrors(byte inputByte) {
        int parityBit;
        int result = 0;

        parityBit = (getBit(inputByte, 2)
                + getBit(inputByte, 4)
                + getBit(inputByte, 6) ) % 2;
        if(parityBit != getBit(inputByte, 0)) {
            result += 1;
        }
        parityBit = (getBit(inputByte, 2)
                + getBit(inputByte, 5)
                + getBit(inputByte, 6) ) % 2;
        if(parityBit != getBit(inputByte, 1)) {
            result += 2;
        }

        parityBit = (getBit(inputByte, 4)
                + getBit(inputByte, 5)
                + getBit(inputByte, 6) ) % 2;
        if(parityBit != getBit(inputByte, 3)) {
            result += 4;
        }

        return result == 0 ? 8 : result;

    }

    private static byte undoHamming(byte inputByte, boolean isSecondByte) {
        byte outputByte = 0;
        int secondByteConstant = 0;

        if(isSecondByte) {
            secondByteConstant = 4;
        }
        outputByte += (byte) (getBit(inputByte, 2) << 7 - secondByteConstant);
        outputByte += (byte) (getBit(inputByte, 4) << 6 - secondByteConstant);
        outputByte += (byte) (getBit(inputByte, 5) << 5 - secondByteConstant);
        outputByte += (byte) (getBit(inputByte, 6) << 4 - secondByteConstant);
        return outputByte;
    }


    //small bitwise methods
    static byte flipOneBit(byte input) {
        Random random = new Random();
        int randomIndex = random.nextInt(7);
        return (byte) (input ^ (1 << randomIndex));
    }

    private static int getBit(byte input, int index) {
        return (input & 1 << (7 - index)) == 0 ? 0 : 1;
    }

}
