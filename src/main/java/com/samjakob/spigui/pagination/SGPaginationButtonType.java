package com.samjakob.spigui.pagination;

public enum SGPaginationButtonType {

    PREV_BUTTON(3),
    CURRENT_BUTTON(4),
    NEXT_BUTTON(5),
    UNASSIGNED(0);

    private final int slot;

    SGPaginationButtonType(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public static SGPaginationButtonType forSlot(int slot) {
        for (SGPaginationButtonType buttonType : SGPaginationButtonType.values()) {
            if (buttonType.slot == slot) return buttonType;
        }

        return SGPaginationButtonType.UNASSIGNED;
    }

}
