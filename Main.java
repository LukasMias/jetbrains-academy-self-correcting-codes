package correcter;

import java.io.*;
import java.util.Scanner;

import static correcter.Encode.flipOneBit;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        File sendFile = new File("send.txt");
        File encodedFile = new File("encoded.txt");
        File receivedFile = new File("received.txt");
        File decodedFile = new File("decoded.txt");

        System.out.print("Write a mode: ");
        String mode = scanner.next();
        switch(mode) {
            case "encode":
                encodeFile(sendFile, encodedFile);
                break;
            case "send":
                sendFile(encodedFile,receivedFile);
                break;
            case "decode":
                decodeFile(receivedFile,decodedFile);
                break;
            default:
                System.out.println("Wrong command!");
        }
    }

    private static void encodeFile(File sendFile, File encodedFile) {
        try (FileInputStream inputStream = new FileInputStream(sendFile)) {
            try (FileOutputStream outputStream = new FileOutputStream(encodedFile)) {
                Encode.encode(inputStream, outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFile(File encodedFile, File receivedFile) {
        try (FileInputStream inputStream = new FileInputStream(encodedFile)) {
            try (FileOutputStream outputStream = new FileOutputStream(receivedFile)) {
                while(true) {
                    byte inputByte = (byte) inputStream.read();
                    if(inputByte == -1) {
                        break;
                    }
                    outputStream.write(flipOneBit(inputByte));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void decodeFile(File receivedFile, File decodedFile) {
        try (FileInputStream inputStream = new FileInputStream(receivedFile)) {
            try (FileOutputStream outputStream = new FileOutputStream(decodedFile)) {
                Encode.correctAndDecode(inputStream, outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
