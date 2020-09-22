package dakaraphi.devtools.tracing.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;

import java.io.File;

public class ConfigurationSerializer {
    public static final String FILE_PROPERTY_KEY = "dakaraphi.devtools.tracing.config.file";
    private final File configFile;

    public ConfigurationSerializer(File configFile) {
        this.configFile = configFile;
    }

    public TracingConfig readConfig() {
        final Config config = ConfigFactory.parseFile(configFile);
        return ConfigBeanFactory.create(config, TracingConfig.class);
    }
}
