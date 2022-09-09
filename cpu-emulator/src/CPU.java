public class CPU {
    private Register[] registers;
    private Memory memory;

    public CPU() {
        // Instantiate with default settings
        registers = new Register[10];

        memory = new Memory();
    }

    public void loadInstructionsIntoMemory(int[] instructions) {

    }
}
