public enum Operation {
    LDV(0x00), LDR(0x01), LDM(0x02),
    STM(0x10), STR(0x01),
    CMPR(0x20), CMPV(0x21),
    BEQ(0x30), BGT(0x31), BLT(0x32), BRA(0x33),
    ADDV(0x40), ADDR(0x41),
    SUBV(0x42), SUBR(0x43),
    HALT(0xFE), NOOP(0xFF);

    private final byte opcode;

    private Operation(int opcode) {
        this.opcode = (byte) opcode;
    }

    public static Operation getFromOpcode(byte opcode) {
        for(Operation operation : Operation.values()) {
            if (operation.opcode == opcode) {
                return operation;
            }
        }
        return null;
    }
}
