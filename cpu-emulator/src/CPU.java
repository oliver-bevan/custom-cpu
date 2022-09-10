import java.nio.ByteBuffer;
import java.util.Arrays;

public class CPU {
    private Register[] registers;
    private Memory memory;

    private int programOffset;
    private int pc;

    private boolean haltFlag;


    public CPU() {
        // Instantiate with default settings
        registers = new Register[10];

        for (int i = 0; i < registers.length; i++) {
            registers[i] = new Register();
        }

        memory = new Memory();
    }

    public void run() {
        pc = programOffset;
        haltFlag = false;

        while (haltFlag != true) {
            byte opcode = memory.getData((short) pc);
            byte[] context = new byte[3];

            for(int i = 0; i < 3; i++) {
                context[i] = memory.getData((short) (i + pc + 1));
            }
            Operation operation = Operation.getFromOpcode(opcode);
            this.execute(operation, context);

            pc = pc + 4;
            System.out.println(registers[1].getIntValue());
        }

    }

    public void execute(Operation operation, byte[] context) {
        byte[] value = Arrays.copyOfRange(context, 1, 3);
        short numericalValue = ByteBuffer.allocate(2).put(value).getShort(0);

        Byte registerAddress = context[0];
        short numericalRegisterAddress = registerAddress.shortValue();

        switch (operation) {
            case LDV:
                registers[numericalRegisterAddress].setValue(numericalValue);
                break;
            case LDR:
                break;
            case LDM:
                break;
            case STR:
                break;
            case STM:
                break;
            case CMPR:
                break;
            case CMPV:
                break;
            case BEQ:
                break;
            case BGT:
                break;
            case BLT:
                break;
            case BRA:
                break;
            case ADDV:
                int currentValue = registers[numericalRegisterAddress].getIntValue();

                registers[numericalRegisterAddress].setValue((short) (currentValue + numericalValue));
                break;
            case ADDR:
                break;
            case SUBV:
                break;
            case SUBR:
                break;
            case HALT:
                haltFlag = true;
                break;
            case NOOP:
                break;
        }
    }


    public void loadInstructionsIntoMemory(int[] instructions, short offset) {
        programOffset = offset;
        for(int i = 0; i < instructions.length; i++) {
            byte[] instructionBytes = ByteBuffer.allocate(4).putInt(instructions[i]).array();
            for (int j = 0; j < 4; j++) {
                short memAddress = (short) (i*4 + j + offset);
                this.memory.setData(instructionBytes[j], memAddress);
            }
        }
    }
}
