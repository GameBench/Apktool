/*
 *  Copyright (C) 2010 Ryszard Wiśniewski <brut.alll@gmail.com>
 *  Copyright (C) 2010 Connor Tumbleson <connor.tumbleson@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package brut.androlib;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ApktoolProperties {
    private static final Logger LOGGER = Logger.getLogger(ApktoolProperties.class.getName());

    private static Properties sProps;

    public static String get(String key) {
        return get().getProperty(key);
    }

    public static Properties get() {
        if (sProps == null) {
            loadProps();
        }
        return sProps;
    }

    public static String getVersion() {
        return get("application.version");
    }

    private static void loadProps() {
        sProps = new Properties();
        try {
            InputStream in = ApktoolProperties.class.getResourceAsStream("/properties/apktool.properties");
            sProps.load(in);
            in.close();
        } catch (NullPointerException | IOException ex) {
            LOGGER.warning("Can't load properties.");
        }

        InputStream templateStream = null;
        try {
            templateStream = com.android.tools.smali.baksmali.Main.class.getClassLoader().getResourceAsStream("baksmali.properties");
        } catch(NoClassDefFoundError ex) {
            LOGGER.warning("Can't load baksmali properties.");
        }
        Properties properties = new Properties();
        String version = "(unknown)";

        if (templateStream != null) {
            try {
                properties.load(templateStream);
                version = properties.getProperty("application.version");
                templateStream.close();
            } catch (IOException ignored) { }
        }
        sProps.put("baksmaliVersion", version);

        templateStream = null;
        try {
            templateStream = com.android.tools.smali.smali.Main.class.getClassLoader().getResourceAsStream("smali.properties");
        } catch(NoClassDefFoundError ex) {
            LOGGER.warning("Can't load smali properties.");
        }
        properties = new Properties();
        version = "(unknown)";

        if (templateStream != null) {
            try {
                properties.load(templateStream);
                version = properties.getProperty("application.version");
                templateStream.close();
            } catch (IOException ignored) { }
        }
        sProps.put("smaliVersion", version);
    }
}
