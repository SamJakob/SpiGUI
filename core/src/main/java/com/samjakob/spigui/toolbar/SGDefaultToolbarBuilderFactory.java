package com.samjakob.spigui.toolbar;

import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.samjakob.spigui.SpiGUI;

public final class SGDefaultToolbarBuilderFactory {

    static {
        // See ItemBuilderFactory.
        FACTORY = new SGDefaultToolbarBuilderFactory();
        SpiGUI.ensureFactoriesInitialized();
    }

    /** The static singleton {@link SGDefaultToolbarBuilderFactory} instance. */
    @Nonnull
    private static final SGDefaultToolbarBuilderFactory FACTORY;

    @Nullable
    private Supplier<SGToolbarBuilder> supplier;

    /** Internal default constructor for the {@link SGDefaultToolbarBuilderFactory}. */
    private SGDefaultToolbarBuilderFactory() {}

    /**
     * Get the globally registered instance of the {@link SGDefaultToolbarBuilderFactory}.
     *
     * @return the {@link SGDefaultToolbarBuilderFactory}.
     */
    public static SGDefaultToolbarBuilderFactory get() {
        return Objects.requireNonNull(FACTORY);
    }

    /**
     * Checks whether the supplier has been registered elsewhere with {@link #setSupplier(Supplier)}.
     *
     * @return true if the supplier has already been registered (implying new ones will be ignored).
     */
    public boolean hasSupplier() {
        return this.supplier != null;
    }

    /**
     * Set the supplier for the {@link SGDefaultToolbarBuilderFactory}.
     *
     * @param supplier to use when creating an {@link SGToolbarBuilder}.
     */
    public void setSupplier(@Nonnull Supplier<SGToolbarBuilder> supplier) {
        if (hasSupplier()) {
            return;
        }

        this.supplier = Objects.requireNonNull(supplier);
    }

    /**
     * Instantiate a new {@link SGToolbarBuilder} using the supplier passed to {@link #setSupplier(Supplier)}.
     *
     * @return supplier to use for creating {@link SGToolbarBuilder}s.
     */
    @Nonnull
    public SGToolbarBuilder newToolbarBuilder() {
        final Supplier<SGToolbarBuilder> supplier = Objects.requireNonNull(
                this.supplier, "The SGDefaultToolbarBuilderFactory has not been configured with #setSupplier yet.");
        return Objects.requireNonNull(
                supplier.get(),
                "The supplier returned a null SGToolbarBuilder which is not permitted. This means whatever has called #setSupplier first has provided an invalid supplier.");
    }
}
