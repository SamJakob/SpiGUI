package com.samjakob.spigui.item;

/**
 * Items such as glass panes can have variable color. Historically, Minecraft (and the Bukkit/Spigot APIs) represented
 * these colors with data/durability values.
 *
 * <p>This was unclear when working with the items so SpiGUI's (and its predecessor's) item API has always had a tool to
 * manage and manipulate those colors in an identifiable way.
 *
 * <p>This version of the item color tool is designed to have an API that will allow identifying color types across
 * different game and API versions.
 *
 * @author SamJakob
 * @version 3.0.0
 */
public enum ItemColor {

    /** <img height="64" width="64" src="colors/white_wool.png" alt="White wool"> */
    WHITE,
    /** <img height="64" width="64" src="colors/orange_wool.png" alt="Orange wool"> */
    ORANGE,
    /** <img height="64" width="64" src="colors/magenta_wool.png" alt="Magenta wool"> */
    MAGENTA,
    /** <img height="64" width="64" src="colors/light_blue_wool.png" alt="Light blue wool"> */
    LIGHT_BLUE,
    /** <img height="64" width="64" src="colors/yellow_wool.png" alt="Yellow wool"> */
    YELLOW,
    /** <img height="64" width="64" src="colors/lime_wool.png" alt="Lime wool"> */
    LIME,
    /** <img height="64" width="64" src="colors/pink_wool.png" alt="Pink wool"> */
    PINK,
    /** <img height="64" width="64" src="colors/gray_wool.png" alt="Gray wool"> */
    GRAY,
    /** <img height="64" width="64" src="colors/light_gray_wool.png" alt="Light gray wool"> */
    LIGHT_GRAY,
    /** <img height="64" width="64" src="colors/cyan_wool.png" alt="Cyan wool"> */
    CYAN,
    /** <img height="64" width="64" src="colors/purple_wool.png" alt="Purple wool"> */
    PURPLE,
    /** <img height="64" width="64" src="colors/blue_wool.png" alt="Blue wool"> */
    BLUE,
    /** <img height="64" width="64" src="colors/brown_wool.png" alt="Brown wool"> */
    BROWN,
    /** <img height="64" width="64" src="colors/green_wool.png" alt="Green wool"> */
    GREEN,
    /** <img height="64" width="64" src="colors/red_wool.png" alt="Red wool"> */
    RED,
    /** <img height="64" width="64" src="colors/black_wool.png" alt="Black wool"> */
    BLACK
}
