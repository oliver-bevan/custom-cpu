import javax.swing.*;

public class Display {
    JFrame mainFrame;
    JTextArea displayOutput;

    public Display() {
        mainFrame = new JFrame();
        displayOutput = new JTextArea();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(300,300);

        mainFrame.add(displayOutput);

        mainFrame.setVisible(true);
    }
    public void update(Memory memory) {
        String output = "";
        for(int i = 32256; i < 32767; i = i + 8) {
            for(int j = 0; j < 8; j++) {
                output = output + (char) memory.getData((short) (i + j));
            }
            output = output + "\n";
        }
        displayOutput.setText(output);
        //mainFrame.repaint();
    }

}
