package com.gamebench.gbaclientlib;

public class StartSessionOptions {
    private boolean screenshots = false;
    private boolean autoSync = false;

    public void setAutoSync(boolean autoSync) {
        this.autoSync = autoSync;
    }

    public void setScreenshots(boolean screenshots) {
        this.screenshots = screenshots;
    }

    public boolean isScreenshots() {
        return screenshots;
    }

    public boolean isAutoSync() {
        return autoSync;
    }
}
