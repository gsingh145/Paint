import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * Paint
 * <p>
 * allows user to draw and has extra features like fill and clear
 *
 * @author Gaurav Singh, 29939
 * @version November 17, 2022
 */
public class Paint extends JComponent implements Runnable {
    private Image image; // the canvas
    private Graphics2D graphics2D;  // this will enable drawing
    private int curX; // current mouse x coordinate
    private int curY; // current mouse y coordinate
    private int oldX; // previous mouse x coordinate
    private int oldY; // previous mouse y coordinate


    JButton clrButton;
    JButton fillButton;
    JButton eraseButton;
    JButton randomButton;

    Color backGround;
    Color penColor;


    JTextField hexText;
    JButton hexButton;
    JButton rgbButton;


    JTextField rText;
    JTextField gText;

    JTextField bText;


    Paint paint; // variable of the type ColorPicker

    /* action listener for buttons */
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == clrButton) {
                paint.clear();
                hexText.setText("#");
                rText.setText("");
                gText.setText("");
                bText.setText("");
            }
            if (e.getSource() == fillButton) {
                paint.fill();
                hexText.setText("#");
                rText.setText("");
                gText.setText("");
                bText.setText("");
            }
            if (e.getSource() == eraseButton) {
                paint.erase();
            }

            if (e.getSource() == randomButton) {
                String[] array;
                array = paint.random();
                hexText.setText(array[0]);
                rText.setText(array[1]);
                gText.setText(array[2]);
                bText.setText(array[3]);
                repaint();

            }

            if (e.getSource() == hexButton) {
                Color color = null;
                try {
                    color = Color.decode(hexText.getText());
                } catch (Exception exception) {
                    exception.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Not a valid Hex Value", "Error"
                            , JOptionPane.ERROR_MESSAGE);
                }
                if (color != null) {
                    paint.createHex(color);
                    rText.setText(String.valueOf(color.getRed()));
                    gText.setText(String.valueOf(color.getGreen()));
                    bText.setText(String.valueOf(color.getBlue()));
                }
            }
            if (e.getSource() == rgbButton) {
                Color color = null;
                try {
                    int red = 0;
                    int green = 0;
                    int blue = 0;
                    if (!rText.getText().equals("")) {
                        red = Integer.valueOf(rText.getText());
                    }
                    if (!gText.getText().equals("")) {
                        green = Integer.valueOf(gText.getText());
                    }
                    if (!bText.getText().equals("")) {
                        blue = Integer.valueOf(bText.getText());
                    }

                    color = new Color(red, green, blue);
                    paint.createRBG(color);
                    hexText.setText(String.format("#%02x%02x%02x", red, green, blue));
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Not a valid RGB Value", "Error"
                            , JOptionPane.ERROR_MESSAGE);
                }
                if (color != null) {

                    paint.createHex(color);
                    rText.setText(String.valueOf(color.getRed()));
                    gText.setText(String.valueOf(color.getGreen()));
                    bText.setText(String.valueOf(color.getBlue()));
                }
            }


        }
    };

    public void createRBG(Color c) {
        graphics2D.setPaint(c);
        penColor = c;
    }

    public void createHex(Color c) {
        graphics2D.setPaint(c);
        penColor = c;
    }

    public void clear() {
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);

        backGround = Color.white;
        graphics2D.setPaint(Color.black);
        penColor = Color.black;
        repaint();
    }

    public void fill() {
        backGround = graphics2D.getColor();
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(Color.black);
        penColor = Color.black;
        repaint();
    }

    public void erase() {
        graphics2D.setPaint(backGround);
        penColor = backGround;
    }

    public String[] random() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        Color randColor = new Color(r, g, b);
        graphics2D.setPaint(randColor);
        penColor = randColor;
        repaint();
        return new String[]{String.format("#%02x%02x%02x", r, g, b), String.valueOf(r),
                String.valueOf(g), String.valueOf(b)};
    }

    public Paint() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                /* set oldX and oldY coordinates to beginning mouse press*/
                oldX = e.getX();
                oldY = e.getY();
            }
        });


        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                /* set current coordinates to where mouse is being dragged*/
                curX = e.getX();
                curY = e.getY();

                /* draw the line between old coordinates and new ones */
                graphics2D.drawLine(oldX, oldY, curX, curY);

                /* refresh frame and reset old coordinates */
                repaint();
                oldX = curX;
                oldY = curY;

            }
        });


    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Paint());
    }

    public void run() {
        /* set up JFrame */
        JFrame frame = new JFrame("Paint");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        paint = new Paint();
        content.add(paint, BorderLayout.CENTER);

        clrButton = new JButton("Clear");
        clrButton.addActionListener(actionListener);
        fillButton = new JButton("Fill");
        fillButton.addActionListener(actionListener);
        eraseButton = new JButton("Erase");
        eraseButton.addActionListener(actionListener);
        randomButton = new JButton("Random");
        randomButton.addActionListener(actionListener);

        JPanel topPanel = new JPanel();
        topPanel.add(clrButton);
        topPanel.add(fillButton);
        topPanel.add(eraseButton);
        topPanel.add(randomButton);
        content.add(topPanel, BorderLayout.NORTH);


        hexText = new JTextField("#", 10);
        hexText.addActionListener(actionListener);
        hexButton = new JButton("Hex");
        hexButton.addActionListener(actionListener);

        rText = new JTextField(5);
        rText.addActionListener(actionListener);
        gText = new JTextField(5);
        gText.addActionListener(actionListener);
        bText = new JTextField(5);
        bText.addActionListener(actionListener);
        rgbButton = new JButton("RBG");
        rgbButton.addActionListener(actionListener);


        JPanel panel = new JPanel();
        panel.add(hexText);
        panel.add(hexButton);
        panel.add(rText);
        panel.add(gText);
        panel.add(bText);
        panel.add(rgbButton);
        content.add(panel, BorderLayout.SOUTH);

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


    }

    protected void paintComponent(Graphics g) {
        if (image == null) {
            image = createImage(getSize().width, getSize().height);
            /* this lets us draw on the image (ie. the canvas)*/
            graphics2D = (Graphics2D) image.getGraphics();
            /* gives us better rendering quality for the drawing lines */
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            /* set canvas to white with default paint color */
            graphics2D.setPaint(Color.white);
            graphics2D.fillRect(0, 0, getSize().width, getSize().height);
            graphics2D.setPaint(Color.black);
            repaint();
        }
        graphics2D.setStroke(new BasicStroke(5));
        g.drawImage(image, 0, 0, null);

    }
}