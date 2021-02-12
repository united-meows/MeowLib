package pisi.unitedmeows.meowlib;

import pisi.unitedmeows.meowlib.etc.IAction;
import pisi.unitedmeows.meowlib.etc.MLibSetting;
import pisi.unitedmeows.meowlib.etc.MLibSettings;

import java.util.HashMap;

public class MeowLib {

    /* change this to meowlib map */
    private static HashMap<MLibSettings, MLibSetting<?>> SETTINGS;
    static {
        SETTINGS = new HashMap<>();
        setup();
    }

    private static void setup() {
        for (MLibSettings setting : MLibSettings.values()) {
            SETTINGS.put(setting, new MLibSetting<>(setting, setting.getValue()));
        }
    }

    public HashMap<MLibSettings, MLibSetting<?>> settings() {
        return SETTINGS;
    }

    public static Exception run(IAction action) {
        try {
            action.run();
            return null;
        } catch (Exception ex) {
            return ex;
        }
    }
}
