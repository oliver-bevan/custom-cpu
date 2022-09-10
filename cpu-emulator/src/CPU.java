import java.nio.ByteBuffer;

public class CPU {
    private Register[] registers;
    private Memory memory;

    public CPU() {
        // Instantiate with default settings
        registers = new Register[10];

        memory = new Memory();
    }

    public void loadInstructionsIntoMemory(int[] instructions) {
        for(int i = 0; i < instructions.length; i++) {
            byte[] instructionBytes = ByteBuffer.allocate(4).putInt(instructions[i]).array();
            for (int j = 0; j < 4; j++) {
                short memAddress = (short) (i*4 + j);
                System.out.println(String.format("%8s", Integer.toBinaryString(instructionBytes[j] & 0xFF)).replace(' ', '0'));
                this.memory.setData(instructionBytes[j], memAddress);
            }
        }
    }
}
