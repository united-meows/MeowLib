package pisi.unitedmeows.meowlib;

import pisi.unitedmeows.meowlib.etc.IAction;
import pisi.unitedmeows.meowlib.etc.MLibSetting;
import pisi.unitedmeows.meowlib.etc.MLibSettings;

import java.io.Serializable;
import java.util.HashMap;

public class MeowLib {

    /* change this to meowlib map */
    private static HashMap<MLibSettings, MLibSetting<Serializable>> SETTINGS;

    static {
        SETTINGS = new HashMap<>();
        setup();
    }

    private static void setup() {
        for (MLibSettings setting : MLibSettings.values()) {
            SETTINGS.put(setting, new MLibSetting<Serializable>(setting, (Serializable)setting.getValue()));

            System.out.println(setting.getName() + " " + setting.getValue());
        }
    }

    public static HashMap<MLibSettings, MLibSetting<Serializable>> settings() {
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
