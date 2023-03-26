package io.github.carbon.carbonmc.utils;

public enum ServerRuntime {
    WATERFALL(true),
    PAPER(false);

    private boolean bungee;

    ServerRuntime(boolean bungee) {
        this.bungee = bungee;
    }

    public boolean isBungee() {
        return bungee;
    }
}
