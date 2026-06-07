package winterwolfsv.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfigClient;

public class ModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {

        // Return the screen here with the one you created from Cloth Config Builder
        return parent -> AutoConfigClient.getConfigScreen(Config.class, parent).get();

    }
}