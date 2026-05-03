package com.daqem.coldcase.item.detective;

import com.daqem.coldcase.item.ColdCaseItem;

public class AutopsyKit extends ColdCaseItem {

    public AutopsyKit(Properties props) {
        super(props);
    }

    @Override
    public String getLoreKeyFormat() {
        return "item.coldcase.autopsy_kit.lore.%d";
    }
}
