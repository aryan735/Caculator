import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.regex.Pattern;

public class Calculator extends JFrame {
    private JTextField display;
    private double val = 0;
    private char operator = ' ';
    private boolean go = true;
    private boolean addWrite = true;

    public Calculator() {
        setTitle("Calculator");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        display = new JTextField("0");
        display.setBounds(20, 30, 350, 70);
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.PLAIN, 36));
        display.setBackground(Color.WHITE);
        add(display);

        // Create number buttons (0-9)
        String[] btnLabels = {"7", "8", "9", "/", "4", "5", "6", "*", "1", "2", "3", "-", ".", "0", "=", "+"};
        JButton[] btns = new JButton[btnLabels.length];
        int[] x = {20, 110, 200, 290}; // Button X positions
        int[] y = {150, 230, 310, 390, 470}; // Button Y positions

        // Add buttons to the frame
        int index = 0;
        for (int i = 0; i < y.length - 1; i++) {
            for (int j = 0; j < x.length; j++) {
                btns[index] = createButton(btnLabels[index], x[j], y[i]);
                add(btns[index]);
                index++;
            }
        }

        // Create special buttons
        JButton clear = createButton("C", x[0], y[4]);
        JButton sqrt = createButton("√", x[1], y[4]);
        JButton power = createButton("^", x[2], y[4]);
        JButton log = createButton("log", x[3], y[4]);

        // Add special buttons to the frame
        add(clear);
        add(sqrt);
        add(power);
        add(log);

        // Button Action Listeners
        clear.addActionListener(e -> clearDisplay());
        sqrt.addActionListener(e -> applySingleOp(Math::sqrt));
        power.addActionListener(e -> applyOp('^'));
        log.addActionListener(e -> applySingleOp(Math::log));

        btns[3].addActionListener(e -> applyOp('/')); // Division
        btns[7].addActionListener(e -> applyOp('*')); // Multiplication
        btns[11].addActionListener(e -> applyOp('-')); // Subtraction
        btns[15].addActionListener(e -> applyOp('+')); // Addition
        btns[14].addActionListener(e -> evaluate()); // Equals
        btns[12].addActionListener(e -> appendPoint()); // Decimal point

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            btns[i % 10].addActionListener(e -> appendDigit(finalI));
        }

        setVisible(true);
    }

    private JButton createButton(String text, int x, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 80, 70);
        btn.setFont(new Font("Arial", Font.PLAIN, 24));
        return btn;
    }

    private void clearDisplay() {
        display.setText("0");
        operator = ' ';
        val = 0;
    }

    private void applyOp(char op) {
        if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", display.getText())) {
            if (go) {
                val = calculate(val, display.getText(), operator);
                if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
                    display.setText(String.valueOf((int) val));
                } else {
                    display.setText(String.valueOf(val));
                }
                operator = op;
                go = false;
                addWrite = false;
            } else {
                operator = op;
            }
        }
    }

    private void applySingleOp(java.util.function.DoubleUnaryOperator op) {
        double currentVal = Double.parseDouble(display.getText());
        display.setText(String.valueOf(op.applyAsDouble(currentVal)));
    }

    private void appendDigit(int digit) {
        if (addWrite) {
            if (Pattern.matches("[0]*", display.getText())) {
                display.setText(String.valueOf(digit));
            } else {
                display.setText(display.getText() + digit);
            }
        } else {
            display.setText(String.valueOf(digit));
            addWrite = true;
        }
        go = true;
    }

    private void appendPoint() {
        if (addWrite) {
            if (!display.getText().contains(".")) {
                display.setText(display.getText() + ".");
            }
        } else {
            display.setText("0.");
            addWrite = true;
        }
        go = true;
    }

    private void evaluate() {
        if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", display.getText())) {
            if (go) {
                val = calculate(val, display.getText(), operator);
                if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
                    display.setText(String.valueOf((int) val));
                } else {
                    display.setText(String.valueOf(val));
                }
                operator = '=';
                addWrite = false;
            }
        }
    }

    private double calculate(double x, String input, char operator) {
        double y = Double.parseDouble(input);
        switch (operator) {
            case '+': return x + y;
            case '-': return x - y;
            case '*': return x * y;
            case '/': return x / y;
            case '^': return Math.pow(x, y);
            default: return y;
        }
    }

    public static void main(String[] args) {
        new Calculator();
    }
}
