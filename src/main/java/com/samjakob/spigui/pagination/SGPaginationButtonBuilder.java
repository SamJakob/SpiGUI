package com.samjakob.spigui.pagination;

import com.samjakob.spigui.SGInventory;
import com.samjakob.spigui.buttons.SGButton;

public interface SGPaginationButtonBuilder {

    SGButton buildPaginationButton(SGPaginationButtonType type, SGInventory inventory);

}
