package com.zeml.rotp_zwa.action.stand;

import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.action.stand.StandEntityLightAttack;

public class WhiteAlbumPunch extends StandEntityLightAttack {
    public WhiteAlbumPunch(StandEntityLightAttack.Builder builder){
        super(builder);
    }


    @Override
    public boolean cancelsVanillaClick() {
        return true;
    }
}
