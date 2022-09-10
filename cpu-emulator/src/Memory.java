public class Memory {
    private byte[] mainmem;

    public Memory() {
        mainmem = new byte[65536];
    }

    public void setData(byte data, short address) {
        this.mainmem[Short.toUnsignedInt(address)] = data;
    }

    public byte getData(short address) {
        return mainmem[Short.toUnsignedInt(address)];
    }

    public void dumpMemory() {
        System.out.println("--BEGIN MEMORY DUMP--");

        for(int i = 0; i < mainmem.length; i++) {
            String output = String.format("%8s", Integer.toBinaryString(mainmem[i] & 0xFF)).replace(' ', '0');
            System.out.println(output);
        }
        System.out.println("--END MEMORY DUMP--");
    }
}
