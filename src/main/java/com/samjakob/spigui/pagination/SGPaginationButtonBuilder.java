package com.samjakob.spigui.pagination;

import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.buttons.SGButton;

public interface SGPaginationButtonBuilder {

    SGButton buildPaginationButton(SGPaginationButtonType type, SGMenu inventory);

}
