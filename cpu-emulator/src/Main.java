import java.io.*;
import java.math.BigInteger;

public class Main {
    CPU cpu;

    public static void main(String[] args) {
        System.out.println("Instantiating CPU");
        //cpu = new CPU();
        int[] instructions = loadInstructionsFromFile("testbin.bin");

        CPU cpu = new CPU();

        cpu.loadInstructionsIntoMemory(instructions, (short) 0);

        cpu.run();
    }
    public static int[] loadInstructionsFromFile(String file) {
        try {
            InputStream inputStream = new FileInputStream(file);
            byte[] buffer = new byte[4];

            long fileSize = new File(file).length();
            int numInstructions = (int) fileSize / 4;

            int[] instructions = new int[numInstructions];
            int instructionIndex = 0;

            for(int i = 0; i < fileSize; i = i + 4) {
                if (inputStream.read(buffer) != buffer.length) {
                    throw new IOException("Invalid number of bytes in file");
                }
                int instruction = new BigInteger(buffer).intValue();
                instructions[instructionIndex] = instruction;
                instructionIndex++;
            }
            return instructions;
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Error reading file");
            System.exit(0);
        }
        return null;
    }
}