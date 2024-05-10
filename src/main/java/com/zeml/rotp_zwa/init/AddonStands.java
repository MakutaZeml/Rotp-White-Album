package com.zeml.rotp_zwa.init;

import com.zeml.rotp_zwa.entity.stand.stands.WhiteAlbumEntity;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.init.power.stand.EntityStandRegistryObject.EntityStandSupplier;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;

public class AddonStands {

    public static final EntityStandSupplier<EntityStandType<StandStats>, StandEntityType<WhiteAlbumEntity>>
            WHITE_ALBUM = new EntityStandSupplier<>(InitStands.STAND_WHITE_ALBUM);
}