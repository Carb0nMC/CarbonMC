package io.github.carbon.carbonmc;

public class PluginServiceProvider {
    private static CarbonMC carbonMC;

    public PluginServiceProvider(CarbonMC carbonMC) {
        PluginServiceProvider.carbonMC = carbonMC;
    }

    public static CarbonMC getCarbonMC() {
        return carbonMC;
    }
}
