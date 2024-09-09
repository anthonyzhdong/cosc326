import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

//donan928 2169260
public class Quilts extends JComponent {

    private static double totalRelativeSize = 0.0;
    private static List<Shape> shapes = new ArrayList<>();
    private int windowSize = 800;
    private int currentDepth = 0;
    static boolean flip = false;

    public Quilts() {}

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        currentDepth = 0;

        while (currentDepth < shapes.size()) {
            drawShapeRecursive(g, centerX, centerY, 0);
            currentDepth++;
        }
    }

    private void drawShapeRecursive(Graphics g, int x, int y, int i) {
        Shape shape = shapes.get(i);
        int size = (int) Math.round(shape.getRelativeSize() / totalRelativeSize * windowSize);

        if (currentDepth == shape.getDepth()) {
            shape.draw(g, x, y, size);
        }

        if (i + 1 < shapes.size()) {
            int newSize = size / 2;
            // For triangles, draw only at the center and corners
            if (shape instanceof Triangle) {
                int[] newX = { x, x - newSize, x + newSize };
                int[] newY = new int[] { y - newSize, y + newSize, y + newSize };
                for (int j = 0; j < newX.length; j++) {
                    drawShapeRecursive(g, newX[j], newY[j], i + 1);
                }
            } else if (flip) { // flipped triangle
                int[] newX = { x, x - newSize, x + newSize };
                int[] newY = new int[] { y + newSize, y - newSize, y - newSize };
                for (int j = 0; j < newX.length; j++) {
                    drawShapeRecursive(g, newX[j], newY[j], i + 1);
                }
            } else {
                // For other shapes, draw at all four corners
                for (int offsetX = -1; offsetX <= 1; offsetX += 2) {
                    for (int offsetY = -1; offsetY <= 1; offsetY += 2) {
                        drawShapeRecursive(g, x + offsetX * newSize, y + offsetY * newSize, i + 1);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {

        String shapeType = args.length == 1 ? args[0].toLowerCase() : "s"; // Default to "s" (square) if not provided

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            int depth = 0;

            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] tokens = line.split("\\s+");

                if (tokens.length < 4) {
                    System.out.println("Invalid input format, skipping line: " + line);
                    continue;
                }

                double relSize = Double.parseDouble(tokens[0]);
                int r = Integer.parseInt(tokens[1]);
                int g = Integer.parseInt(tokens[2]);
                int b = Integer.parseInt(tokens[3]);
                totalRelativeSize += relSize;

                switch (shapeType) {
                    case "s":
                        shapes.add(new Square(new Color(r, g, b), relSize, depth));
                        break;
                    case "t":
                        shapes.add(new Triangle(new Color(r, g, b), relSize, depth));
                        break;
                    case "f":
                        flip = true;
                        shapes.add(new FlippedTriangle(new Color(r, g, b), relSize, depth));
                        
                        break;
                    default:
                        System.out.println("Unknown shape type: " + shapeType);
                        return;
                }

                depth++;
            }

            JFrame frame = new JFrame("Quilts");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new Quilts());
            frame.setSize(860, 860);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }
}

interface Shape {
    void draw(Graphics g, int x, int y, int size);

    double getRelativeSize();

    int getDepth();
}

class Square implements Shape {

    private Color rgb;
    private double relativeSize;
    private int depth;

    public Square(Color rgb, double relativeSize, int depth) {
        this.rgb = rgb;
        this.relativeSize = relativeSize;
        this.depth = depth;
    }

    @Override
    public double getRelativeSize() {
        return relativeSize;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public void draw(Graphics g, int x, int y, int size) {
        g.setColor(rgb);
        g.fillRect(x - size / 2, y - size / 2, size, size);
    }
}

class Triangle implements Shape {

    private Color rgb;
    private double relativeSize;
    private int depth;

    public Triangle(Color rgb, double relativeSize, int depth) {
        this.rgb = rgb;
        this.relativeSize = relativeSize;
        this.depth = depth;
    }

    @Override
    public double getRelativeSize() {
        return relativeSize;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public void draw(Graphics g, int x, int y, int size) {
        g.setColor(rgb);
        int[] xPoints = { x, x + size / 2, x - size / 2 };
        int[] yPoints = { y - size / 2, y + size / 2, y + size / 2 };
        g.fillPolygon(xPoints, yPoints, 3);
    }
}

class FlippedTriangle implements Shape {

    private Color rgb;
    private double relativeSize;
    private int depth;

    public FlippedTriangle(Color rgb, double relativeSize, int depth) {
        this.rgb = rgb;
        this.relativeSize = relativeSize;
        this.depth = depth;
    }

    @Override
    public double getRelativeSize() {
        return relativeSize;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public void draw(Graphics g, int x, int y, int size) {
        g.setColor(rgb);
        int[] xPoints = { x, x - size / 2, x + size / 2 };
        int[] yPoints = { y + size / 2, y - size / 2, y - size / 2 };
        g.fillPolygon(xPoints, yPoints, 3);
    }
}
