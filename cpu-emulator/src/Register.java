public class Register {
    private short data;

    public Register() {
        data = 0;
    }

    public short getValue() {
        return data;
    }

    public void setValue(short data) {
        this.data = data;
    }

    public int getIntValue() {
        return Short.toUnsignedInt(data);
    }
}
