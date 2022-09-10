import java.nio.ByteBuffer;
import java.util.Arrays;

public class CPU {
    private Display display;
    private Register[] registers;
    private Memory memory;

    private int programOffset;
    private int pc;

    private boolean haltFlag;
    private boolean eqFlag;
    private boolean ltFlag;
    private boolean gtFlag;


    public CPU() {
        // Instantiate with default settings
        registers = new Register[10];

        for (int i = 0; i < registers.length; i++) {
            registers[i] = new Register();
        }

        memory = new Memory();

        display = new Display();
    }

    public void run() {
        pc = programOffset;
        haltFlag = false;

        while (!haltFlag) {
            byte opcode = memory.getData((short) pc);
            byte[] context = new byte[3];

            for(int i = 0; i < 3; i++) {
                context[i] = memory.getData((short) (i + pc + 1));
            }
            Operation operation = Operation.getFromOpcode(opcode);
            this.execute(operation, context);

            pc = pc + 4;
            display.update(memory);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }

    }

    public void execute(Operation operation, byte[] context) {
        if (operation == Operation.HALT) {
            haltFlag = true;
            return;
        } else if(operation == Operation.NOOP) {
            return;
        }

        byte[] data = Arrays.copyOfRange(context, 1, 3);
        short numericalValue = ByteBuffer.allocate(2).put(data).getShort(0);

        Byte lastDataByte = context[2];
        short lastDataByteValue = lastDataByte.shortValue();

        Byte registerAddress = context[0];
        short numericalRegisterAddress = registerAddress.shortValue();

        short dataFromRegister = registers[numericalRegisterAddress].getValue();
        byte byteDataFromRegister = (byte) (dataFromRegister & 0xFF);

        switch(operation) {
            case LDV:
                registers[numericalRegisterAddress].setValue(numericalValue);
                break;
            case LDR:
                registers[numericalRegisterAddress].setValue(registers[lastDataByteValue].getValue());
                break;
            case LDM:
                registers[numericalRegisterAddress].setValue(memory.getData(numericalValue));
                break;
            case STM:
                memory.setData(byteDataFromRegister, numericalValue);
                break;
            case STR:
                memory.setData(byteDataFromRegister, registers[lastDataByteValue].getValue());
                break;
            case CMPR:
                if (registers[numericalRegisterAddress].getValue() == registers[lastDataByteValue].getValue()) {
                    eqFlag = true;
                    gtFlag = false;
                    ltFlag = false;
                } else if(registers[numericalRegisterAddress].getValue() > registers[lastDataByteValue].getValue()) {
                    eqFlag = false;
                    gtFlag = true;
                    ltFlag = false;
                } else if(registers[numericalRegisterAddress].getValue() < registers[lastDataByteValue].getValue()) {
                    eqFlag = false;
                    gtFlag = false;
                    ltFlag = true;
                }
                break;
            case CMPV:
                if (registers[numericalRegisterAddress].getValue() == numericalValue) {
                    eqFlag = true;
                    gtFlag = false;
                    ltFlag = false;
                } else if(registers[numericalRegisterAddress].getValue() > numericalValue) {
                    eqFlag = false;
                    gtFlag = true;
                    ltFlag = false;
                } else if(registers[numericalRegisterAddress].getValue() < numericalValue) {
                    eqFlag = false;
                    gtFlag = false;
                    ltFlag = true;
                }
                break;
            case BEQ:
                if (eqFlag) {
                    pc = numericalValue;
                }
                break;
            case BGT:
                if (gtFlag) {
                    pc = numericalValue;
                }
                break;
            case BLT:
                if (ltFlag) {
                    pc = numericalValue;
                }
                break;
            case BRA:
                pc = numericalValue;
                break;
            case ADDV:
                registers[numericalRegisterAddress].setValue((short) (dataFromRegister + numericalValue));
                break;
            case ADDR:
                registers[numericalRegisterAddress].setValue((short) (dataFromRegister + registers[lastDataByteValue].getValue()));
                break;
            case SUBV:
                registers[numericalRegisterAddress].setValue((short) (dataFromRegister - numericalValue));
                break;
            case SUBR:
                registers[numericalRegisterAddress].setValue((short) (dataFromRegister - registers[lastDataByteValue].getValue()));
                break;
            default:
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
