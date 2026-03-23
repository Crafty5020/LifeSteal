package me.modernadventurer.lifesteal.polymer;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import me.modernadventurer.lifesteal.Loader;

public class LifestealResourcePackBuilder {
    public static void Build() {
        boolean valid_id = PolymerResourcePackUtils.addModAssets(Loader.MOD_ID);
        if (!valid_id) {
            throw new RuntimeException("Invalid assets MOD ID!");
        }
        Loader.LOGGER.info("Valid assets MOD ID Building Resource Pack");
        boolean buildMainSuccess = PolymerResourcePackUtils.buildMain();
        if (!buildMainSuccess) {
            throw new RuntimeException("Unable to build resource pack!");
        }
        PolymerResourcePackUtils.markAsRequired();
    }

}
