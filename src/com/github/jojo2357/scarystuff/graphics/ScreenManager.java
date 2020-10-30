package src.com.github.jojo2357.scarystuff.graphics;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import src.com.github.jojo2357.scarystuff.events.EventManager;
import src.com.github.jojo2357.scarystuff.events.GameTimes;
import src.com.github.jojo2357.scarystuff.events.events.MouseInputEvent;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class ScreenManager {
    public static long window;

    private static final Dimensions windowSize = new Dimensions(600, 600);

    private static final Point lastPosition = new Point(0, 0);
    private static MouseInputEvent lastMouseEvent = new MouseInputEvent(new Point(0, 0), (byte) 0);

    private static final double[] x = new double[1];
    private static final double[] y = new double[1];

    private static double rot = 0;
    private static float zoom = 1;

    public static Point screenOffset = new Point();

    public static void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        window = glfwCreateWindow(windowSize.getWidth(), windowSize.getHeight(), "Hello World!", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*
            glfwGetWindowSize(window, pWidth, pHeight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
        } // the stack frame is popped automatically

        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public static boolean tick() {
        glfwPollEvents();
        glfwGetCursorPos(window, x, y);
        Point currentMouse = new Point(x[0], y[0]);

        byte mouseButtonActions = (byte) 0;// release is index 0, press 1. left mouse button is actions & 1, right is & 2, middle is & 4

        for (int mouseChecker = 0; mouseChecker <= 2; mouseChecker++) {
            mouseButtonActions |= glfwGetMouseButton(window, mouseChecker) == 1 ? (byte) Math.pow(2, mouseChecker) : (byte) 0;
        }

        MouseInputEvent event = new MouseInputEvent(currentMouse, mouseButtonActions);
        if (!event.equals(lastMouseEvent)) {
            EventManager.addEvent(event);
        }
        lastMouseEvent = event;

        glClear(GL_COLOR_BUFFER_BIT);// clear the framebuffer// swap the color buffers
        return glfwWindowShouldClose(window);
    }

    public static void renderTexture(Texture text, Point point) {
        renderTexture(text, point, 1);
    }

    public static void renderTexture(Texture text, Point point, float sizeFactor) {
        renderTexture(text, point, sizeFactor, 0, new Dimensions(text.getWidth(), text.getHeight()));
    }

    public static void renderTexture(Texture text, Point point, float sizeFactor, Dimensions dimensions) {
        renderTexture(text, point, sizeFactor, 0, dimensions);
    }

    public static void renderTexture(Texture text, Point point, float sizeFactor, double rotation, Dimensions specialDimensions) {
        if (EventManager.currentPhase != GameTimes.FIRST_RENDER && EventManager.currentPhase != GameTimes.SECOND_RENDER && EventManager.currentPhase != GameTimes.THIRD_RENDER)
            throw new IllegalStateException("attempted to render outside of render phase!");

        text.bind();

        double offset = Math.toDegrees(Math.atan(specialDimensions.getHeight() / (double) specialDimensions.getWidth())) - 45;
        glEnable(GL_TEXTURE_2D);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f((float) zoom * convertToScreenCoord(myRounder(sizeFactor * specialDimensions.getDiagonal() * Math.sin((Math.PI / 180.0) * (-45 + rotation + offset)) + point.getX()), windowSize.getWidth()), zoom * -(float) convertToScreenCoord(myRounder(sizeFactor * specialDimensions.getDiagonal() * Math.cos((Math.PI / 180.0) * (-45 + rotation + offset)) + point.getY()), windowSize.getHeight()));
        glTexCoord2f(1f, 0);
        glVertex2f((float) zoom * convertToScreenCoord(myRounder(sizeFactor * specialDimensions.getDiagonal() * Math.sin((Math.PI / 180.0) * (45 + rotation - offset)) + point.getX()), windowSize.getWidth()), zoom * -(float) convertToScreenCoord(myRounder(sizeFactor * specialDimensions.getDiagonal() * Math.cos((Math.PI / 180.0) * (45 + rotation - offset)) + point.getY()), windowSize.getHeight()));
        glTexCoord2f(1f, -1f);
        glVertex2f((float) zoom * convertToScreenCoord(myRounder(sizeFactor * specialDimensions.getDiagonal() * Math.sin((Math.PI / 180.0) * (135 + rotation + offset)) + point.getX()), windowSize.getWidth()), zoom * -(float) convertToScreenCoord(myRounder(sizeFactor * specialDimensions.getDiagonal() * Math.cos((Math.PI / 180.0) * (135 + rotation + offset)) + point.getY()), windowSize.getHeight()));
        glTexCoord2f(0, -1f);
        glVertex2f((float) zoom * convertToScreenCoord(myRounder(sizeFactor * specialDimensions.getDiagonal() * Math.sin((Math.PI / 180.0) * (225 + rotation - offset)) + point.getX()), windowSize.getWidth()), zoom * -(float) convertToScreenCoord(myRounder(sizeFactor * specialDimensions.getDiagonal() * Math.cos((Math.PI / 180.0) * (225 + rotation - offset)) + point.getY()), windowSize.getHeight()));
        glEnd();
        glDisable(GL_TEXTURE_2D);
        /*Point a = new Point((float) myRounder(sizeFactor * specialDimensions.getDiagonal() * Math.sin((Math.PI / 180.0) * (-45 + rotation + offset)) + point.getX() - ScreenManager.windowSize.getWidth()) / ScreenManager.windowSize.getWidth(), -(float) myRounder(sizeFactor * specialDimensions.getDiagonal() * Math.cos((Math.PI / 180.0) * (-45 + rotation + offset)) + point.getY()  - ScreenManager.windowSize.getHeight()) / ScreenManager.windowSize.getHeight());
        Point b = new Point((float) myRounder(sizeFactor * specialDimensions.getDiagonal() * Math.sin((Math.PI / 180.0) * (45 + rotation - offset)) + point.getX()   - ScreenManager.windowSize.getWidth()) / ScreenManager.windowSize.getWidth(), -(float) myRounder(sizeFactor * specialDimensions.getDiagonal() * Math.cos((Math.PI / 180.0) * (45 + rotation - offset)) + point.getY() - ScreenManager.windowSize.getHeight()) / ScreenManager.windowSize.getHeight());
        Point c = new Point((float) myRounder(sizeFactor * specialDimensions.getDiagonal() * Math.sin((Math.PI / 180.0) * (135 + rotation + offset)) + point.getX()  - ScreenManager.windowSize.getWidth()) / ScreenManager.windowSize.getWidth(), -(float) myRounder(sizeFactor * specialDimensions.getDiagonal() * Math.cos((Math.PI / 180.0) * (135 + rotation + offset)) + point.getY()- ScreenManager.windowSize.getHeight()) / ScreenManager.windowSize.getHeight());
        Point d = new Point((float) myRounder(sizeFactor * specialDimensions.getDiagonal() * Math.sin((Math.PI / 180.0) * (225 + rotation - offset)) + point.getX()  - ScreenManager.windowSize.getWidth()) / ScreenManager.windowSize.getWidth(), -(float) myRounder(sizeFactor * specialDimensions.getDiagonal() * Math.cos((Math.PI / 180.0) * (225 + rotation - offset)) + point.getY() - ScreenManager.windowSize.getHeight()) / ScreenManager.windowSize.getHeight());*/
    }

    public static void drawBox(Point topLeft, Point bottomRight, int r, int g, int b) {
        GL11.glColor4f(r, g, b, 255);
        Point[] points = {new Point(convertToScreenCoord(topLeft.getX(), ScreenManager.windowSize.getWidth()), -convertToScreenCoord(topLeft.getY(), ScreenManager.windowSize.getHeight())),
                new Point(convertToScreenCoord(topLeft.getX(), ScreenManager.windowSize.getWidth()), -convertToScreenCoord(bottomRight.getY(), ScreenManager.windowSize.getHeight())),
                new Point(convertToScreenCoord(bottomRight.getX(), ScreenManager.windowSize.getWidth()), -convertToScreenCoord(bottomRight.getY(), ScreenManager.windowSize.getHeight())),
                new Point(convertToScreenCoord(bottomRight.getX(), ScreenManager.windowSize.getWidth()), -convertToScreenCoord(topLeft.getY(), ScreenManager.windowSize.getHeight())),
                new Point(convertToScreenCoord(topLeft.getX(), ScreenManager.windowSize.getWidth()), -convertToScreenCoord(topLeft.getY(), ScreenManager.windowSize.getHeight()))};

        glBegin(GL_LINES);
        for (int i = 0; i < points.length - 1; i++){
            glVertex2f(points[i].getX(), points[i].getY());
            glVertex2f(points[i + 1].getX(), points[i + 1].getY());
        }
        glEnd();

        /*glBegin(GL_LINE);
        glVertex2f(convertToScreenCoord(topLeft.getX(), ScreenManager.windowSize.getWidth()), -convertToScreenCoord(topLeft.getY(), ScreenManager.windowSize.getHeight()));
        glVertex2f(convertToScreenCoord(topLeft.getX(), ScreenManager.windowSize.getWidth()), -convertToScreenCoord(bottomRight.getY(), ScreenManager.windowSize.getHeight()));
        glVertex2f(convertToScreenCoord(bottomRight.getX(), ScreenManager.windowSize.getWidth()), -convertToScreenCoord(bottomRight.getY(), ScreenManager.windowSize.getHeight()));
        glVertex2f(convertToScreenCoord(bottomRight.getX(), ScreenManager.windowSize.getWidth()), -convertToScreenCoord(topLeft.getY(), ScreenManager.windowSize.getHeight()));
        glEnd();*/
        //GL11.glColor4f(r, g, b, 255);
    }

    private static float myRounder(double in) {
        if (Math.abs(in) % 1 < 0.01)
            return (float) Math.floor(in);
        if (Math.abs(in) % 1 > 0.99)
            return (float) Math.ceil(in);
        return (float) in;
    }

    private static float convertToScreenCoord(float pointIn, float dimension) {
        return (pointIn - dimension) / dimension;
    }

    public static void finishRender() {
        glfwSwapBuffers(window);
    }
}
