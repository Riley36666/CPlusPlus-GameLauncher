package Java;

import java.io.*;
import java.util.Properties;

public class GameSettings {
    private static final String SETTINGS_FILE = "settings.properties";
    private static final Properties props = new Properties();

    // Load or create defaults on startup
    static {
        load();
    }

    public static void load() {
        try (FileInputStream in = new FileInputStream(SETTINGS_FILE)) {
            props.load(in);
        } catch (IOException e) {
            System.out.println("Settings file not found. Creating default settings...");
            setDefaults();
            save();
        }
    }

    public static void save() {
        try (FileOutputStream out = new FileOutputStream(SETTINGS_FILE)) {
            props.store(out, "Game Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setDefaults() {
        props.setProperty("player.color", "RED");
        props.setProperty("floor.color", "PINK");
        props.setProperty("fullscreen", "false");
        props.setProperty("FPS", "60");
        props.setProperty("cactus.color", "GREEN");
        props.setProperty("floors.unlocked", "1");
    }

    // --- Player color ---
    public static String getPlayerColor() {
        return props.getProperty("player.color", "RED");
    }

    public static void setPlayerColor(String color) {
        props.setProperty("player.color", color);
    }

    // --- Floor color ---
    public static String getFloorColor() {
        return props.getProperty("floor.color", "GREEN");
    }

    public static void setFloorColor(String color) {
        props.setProperty("floor.color", color);
    }
    // --- Fullscreen ---
    public static boolean isFullscreen() {
        return Boolean.parseBoolean(props.getProperty("fullscreen", "false"));
    }

    public static void setFullscreen(boolean fullscreen) {
        props.setProperty("fullscreen", String.valueOf(fullscreen));
    }
    // --- FPS ---
    public static String getFPS() {
        return props.getProperty("FPS", "60");
    }
    public static int getFPSValue() {return  Integer.parseInt(props.getProperty("FPS", "60"));}
    public static void setFPS(String fps) {
        props.setProperty("FPS", fps);
    }
    // --- Cactus color ---
    public static String getCactusColor() {return props.getProperty("cactus.color", "GREEN");}
    public static void setCactusColor(String color) { props.setProperty("cactus.color", color); }
    // --- Unlocked floors ---
    public static int getUnlockedFloors() {return  Integer.parseInt(props.getProperty("floors.unlocked", "1"));}
    public static void setUnlockedFloors(int floors) {props.setProperty("floors.unlocked", String.valueOf(floors));}
}
