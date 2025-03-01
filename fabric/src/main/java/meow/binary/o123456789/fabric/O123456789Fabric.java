package meow.binary.o123456789.fabric;

import meow.binary.o123456789.O123456789;
import net.fabricmc.api.ModInitializer;

public final class O123456789Fabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        O123456789.init();
    }
}
