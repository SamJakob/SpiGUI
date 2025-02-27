package com.samjakob.spigui.menu;

import java.util.Objects;
import java.util.StringJoiner;
import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

/** Used to refer to a player's "viewing session" of a given menu. */
public class SGOpenMenu {

    /** The {@link SGMenu} that is currently open. */
    private final SGMenu menu;

    /** The player viewing the menu. */
    private final Player player;

    /**
     * Pairs an {@link SGMenu} instance with a player viewing that menu.
     *
     * @param menu The {@link SGMenu} that is open.
     * @param player The player viewing the menu.
     */
    public SGOpenMenu(@Nonnull SGMenu menu, @Nonnull Player player) {
        this.menu = Objects.requireNonNull(menu);
        this.player = Objects.requireNonNull(player);
    }

    /**
     * Get the open {@link SGMenu} instance.
     *
     * @return The menu that is open.
     */
    @Nonnull
    public SGMenu getMenu() {
        return this.menu;
    }

    /**
     * Get the player viewing the {@link SGMenu}.
     *
     * @return The player viewing the menu.
     */
    @Nonnull
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SGOpenMenu)) return false;
        SGOpenMenu that = (SGOpenMenu) o;
        return Objects.equals(menu, that.menu) && Objects.equals(getPlayer(), that.getPlayer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(menu, getPlayer());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SGOpenMenu.class.getSimpleName() + "[", "]")
                .add("menu=" + menu)
                .add(String.format("player=%s (%s)", player.getUniqueId().toString(), player.getDisplayName()))
                .toString();
    }
}
