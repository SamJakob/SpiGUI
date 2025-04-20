package com.samjakob.spigui;

import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.item.ItemBuilderBase;
import com.samjakob.spigui.item.ItemColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mockito.quality.Strictness;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MockItemBuilder implements ItemBuilderBase {

    @Nonnull
    private Material material;
    private String name;
    private int amount;
    private List<String> lore;
    private ItemColor color;
    private int durability;
    private int maxDurability;
    private final Map<Enchantment, Integer> enchantments = new HashMap<>();
    private final Set<ItemFlag> flags = new HashSet<>();
    private String skullOwner;

    public MockItemBuilder(@Nonnull Material material) {
        this.material = material;
        this.amount = 1;
    }

    @Nonnull
    @Override
    public ItemBuilderBase type(@Nonnull Material material) {
        this.material = Objects.requireNonNull(material);
        return this;
    }

    @Nonnull
    @Override
    public Material getType() {
        return material;
    }

    @Nonnull
    @Override
    public ItemBuilderBase name(@Nullable String name) {
        this.name = name;
        return this;
    }

    @Nullable
    @Override
    public String getName() {
        return name;
    }

    @Nonnull
    @Override
    public ItemBuilderBase amount(int amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Nonnull
    @Override
    public ItemBuilderBase lore(@Nullable String... lore) {
        this.lore = lore != null ? Arrays.asList(lore) : null;
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilderBase lore(@Nullable List<String> lore) {
        this.lore = lore;
        return this;
    }

    @Nullable
    @Override
    public List<String> getLore() {
        return new ArrayList<>(this.lore);
    }

    @Nonnull
    @Override
    public ItemBuilderBase color(@Nonnull ItemColor color) {
        this.color = color;
        return this;
    }

    @Nullable
    @Override
    public ItemColor getColor() {
        return color;
    }

    @Nonnull
    @Override
    public ItemBuilderBase data(short data) {
        this.durability = data;
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilderBase durability(int durability) {
        this.durability = durability;
        return this;
    }

    @Override
    public int getDurability() {
        return durability;
    }

    @Nonnull
    @Override
    public ItemBuilderBase maxDurability(int maxDurability) {
        this.maxDurability = maxDurability;
        return this;
    }

    @Override
    public int getMaxDurability() {
        return maxDurability;
    }

    @Nonnull
    @Override
    public ItemBuilderBase enchant(@Nonnull Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilderBase unenchant(@Nonnull Enchantment enchantment) {
        enchantments.remove(enchantment);
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilderBase flag(@Nonnull ItemFlag... flag) {
        flags.addAll(Arrays.asList(flag));
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilderBase deflag(@Nonnull ItemFlag... flag) {
        Arrays.asList(flag).forEach(flags::remove);
        return this;
    }

    @Nonnull
    @Override
    @Deprecated
    public ItemBuilderBase skullOwner(@Nullable String name) {
        this.skullOwner = name;
        return this;
    }

    @Nonnull
    @Override
    public ItemBuilderBase skullOwner(@Nullable UUID uuid) {
        this.skullOwner = String.valueOf(uuid);
        return this;
    }

    /**
     * Builds a mock {@link ItemStack} and {@link ItemMeta} (that is returned for {@link ItemStack#getItemMeta()}).
     *
     * <p>The behavior of the mocks is to simply return whatever was passed in (e.g., setting the item stack amount with
     * {@link #amount(int)} will cause the mocked {@link #getAmount()} to return the previously set amount).
     *
     * <p>As such, some implied behaviors will be unavailable - for instance, the implicit link between {@link Material}
     * and {@link #getColor()} in later versions of the game and similarly between {@link #data(short)} and
     * {@link #durability(int)} in older versions of the game. This will need to be addressed with additional
     * scaffolding in the tests if that behavior is relevant.
     *
     * <p>Both mocks are configured with {@link Strictness#LENIENT} so that all supported aspects of the item stacks can
     * be mocked (naturally, without forcing the consumer to utilize all of the stubs).
     *
     * @return a mock {@link ItemStack} with mock {@link ItemMeta}.
     */
    @Nonnull
    @Override
    public ItemStack build() {
        final ItemStack result = mock(ItemStack.class, withSettings().strictness(Strictness.LENIENT));

        when(result.getAmount()).thenReturn(amount);
        when(result.getDurability()).thenReturn((short) durability);
        when(result.getType()).thenReturn(material);
        when(result.getEnchantmentLevel(any(Enchantment.class))).thenAnswer(invocation -> enchantments.getOrDefault(invocation.getArgument(0, Enchantment.class), 0));
        when(result.getEnchantments()).thenReturn(enchantments);

        final ItemMeta resultMeta = mock(ItemMeta.class, withSettings().strictness(Strictness.LENIENT));
        when(resultMeta.getLore()).thenReturn(lore);
        when(resultMeta.getDisplayName()).thenReturn(name);
        when(resultMeta.getItemFlags()).thenReturn(flags);
        when(resultMeta.getEnchantLevel(any(Enchantment.class))).thenAnswer(invocation -> enchantments.getOrDefault(invocation.getArgument(0, Enchantment.class), 0));
        when(resultMeta.getEnchants()).thenReturn(enchantments);
        when(result.getItemMeta()).thenReturn(resultMeta);

        return result;
    }

    /**
     * Wraps the {@link MockItemBuilder} with an {@link ItemBuilder} so it can be used in those APIs.
     *
     * @return the wrapped {@link MockItemBuilder}.
     */
    public ItemBuilder asItemBuilder() {
        Constructor<ItemBuilder> constructor;

        try {
            constructor = ItemBuilder.class.getDeclaredConstructor(ItemBuilderBase.class);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }

        // Suppress the Java (private variable) access checks.
        constructor.setAccessible(true);

        try {
            // Construct the instance.
            return constructor.newInstance(this);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } finally {
            // Re-enable the Java access checks.
            constructor.setAccessible(false);
        }
    }

}
