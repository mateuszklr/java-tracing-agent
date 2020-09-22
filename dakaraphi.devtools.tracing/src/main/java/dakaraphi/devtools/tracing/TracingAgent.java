package dakaraphi.devtools.tracing;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.util.Scanner;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import dakaraphi.devtools.tracing.config.ConfigurationSerializer;
import dakaraphi.devtools.tracing.config.TracingConfig;
import dakaraphi.devtools.tracing.filewatcher.FileWatcher;
import dakaraphi.devtools.tracing.logger.TraceLogger;

/**
 * TracingAgent can only be launched within a jar file.
 * You must specify the following JVM property at launch to indicate location of the agent jar file
 * -javaagent: dakaraphi.devtools.tracing.jar
 *
 * If running inside an OSGI application.  You must make this class and its dependencies global to all OSGI modules
 * org.osgi.framework.bootdelegation = dakaraphi.devtools.*
 *
 * These classes must also be loaded by the JVM boot class loader so that they can be invoked before any other classes are loaded.
 * This is done by setting the JAR's manifest to have this entry.
 * Boot-Class-Path: dakaraphi.devtools.tracing-all.jar
 *
 * @author chadmeadows
 *
 */
public class TracingAgent {
	public static final String DEFAULT_CONFIG_FILENAME = "tracing-agent.conf";
	public static TracingConfig tracingConfig = null;

    public static void premain(String agentArgs, Instrumentation instrumentation) throws Exception {
    	agentmain(agentArgs, instrumentation);
	}

	public static void agentmain(String agentArgs, Instrumentation instrumentation) throws Exception {
		TraceLogger.log("Starting v0.5.1");
		TraceLogger.log("classloader: " + TracingAgent.class.getClassLoader());
		TraceLogger.log("arguments: " + agentArgs);

		final File configFile;
		if (agentArgs != null && !agentArgs.trim().equals("")) {
			configFile = new File(agentArgs);
		} else {
			configFile = new File(System.getProperty(ConfigurationSerializer.FILE_PROPERTY_KEY));
		}
		ClassMethodSelector selector = loadConfig(configFile);

		final TracingTransformer transformer = new TracingTransformer(instrumentation, selector, new MethodRewriter());

		FileWatcher.createFileWatcher().addListener(() -> {
			ClassMethodSelector selector1 = loadConfig(configFile);
			transformer.setClassMethodSelector(selector1);
			transformer.retransform();
		}, configFile).start();
	}

	public static void main(String[] args) throws Exception {
    	System.out.println("Running VMs...\n");
		for (VirtualMachineDescriptor vm : VirtualMachine.list()) {
			System.out.println(vm.id() + " - " + vm.displayName());
		}
		System.out.println("\nWhich VM to attach to?");
		int pid = new Scanner(System.in).nextInt();
		var vm = VirtualMachine.attach("" + pid);
		File jarLocation = getJarLocation();
		String defaultConfig = createDefaultConfig(jarLocation);
		vm.loadAgent(jarLocation.getAbsolutePath(), defaultConfig);
	}

	private static File getJarLocation() {
		return new File(TracingAgent.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	}

	private static String createDefaultConfig(File jarLocation) throws IOException {
		File configPath = new File(jarLocation.getParentFile(), DEFAULT_CONFIG_FILENAME);
		if (!configPath.exists()) {
			configPath.createNewFile();
			byte[] sampleConfig = TracingAgent.class
					.getResource("/sample-config.conf")
					.openStream().readAllBytes();
			Files.write(configPath.toPath(), sampleConfig);
		}
		return configPath.getAbsolutePath();
	}

	private static ClassMethodSelector loadConfig(File configFile) {
		ConfigurationSerializer serializer = new ConfigurationSerializer(configFile);
		tracingConfig = serializer.readConfig();
		return ClassMethodSelector.makeSelector(tracingConfig);
	}
}
