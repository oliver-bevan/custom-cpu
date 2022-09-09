import java.io.*;

public class Main {
    CPU cpu;

    public static void main(String[] args) {
        System.out.println("Instantiating CPU");
        cpu = new CPU();

    }
    public int[] loadInstructionsFromFile(String file) {
        try {
            InputStream inputStream = new FileInputStream(file);

            long fileSize = new File(file).length();
            byte[] allBytes = new byte[(int) fileSize];
            int bytesRead = inputStream.read(allBytes);

            int[] instructions = new int[bytesRead / 4]

            for (int i = 0; i < allBytes.length - 4; i = i + 4) {
                
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Error reading file");
            System.exit(0);
        }
    }
}