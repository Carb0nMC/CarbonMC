package io.github.carbon.carbonmc.plugin;

import io.github.carbon.bungeecord.BungeeCarbon;
import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.utils.ServerRuntime;
import io.github.carbon.paper.PaperCarbon;

public class PluginService {
    private static CarbonMC carbonMC;
    private static ServerRuntime sruntime;

    public static void register(ServerRuntime runtime){
        sruntime = runtime;
        carbonMC = runtime.isBungee() ? new BungeeCarbon() : new PaperCarbon();
        carbonMC.enable();
    }

    public static ServerRuntime getSruntime() {
        return sruntime;
    }

    public static CarbonMC getCarbonMC(){
        return carbonMC;
    }
}
