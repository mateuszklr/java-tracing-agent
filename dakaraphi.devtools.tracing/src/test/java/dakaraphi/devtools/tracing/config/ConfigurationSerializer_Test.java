package dakaraphi.devtools.tracing.config;

import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;

public class ConfigurationSerializer_Test {

    @Test
    public void canReadConfigFile() throws URISyntaxException {
        var resource = this.getClass().getResource("/testconfig.conf");
        var file = new File(resource.toURI());
        var tracingConfig = new ConfigurationSerializer(file).readConfig();

        assert tracingConfig.tracers.get(0).logStackFrames.getIncludeRegexPattern().pattern().equals("package.*");
    }
}
