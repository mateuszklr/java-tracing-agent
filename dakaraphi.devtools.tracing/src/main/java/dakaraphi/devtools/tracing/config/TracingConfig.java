package dakaraphi.devtools.tracing.config;

import com.typesafe.config.Optional;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class TracingConfig {
	public List<Tracer> tracers;
	@Optional
	public LogConfig logConfig = new LogConfig();

	@Getter @Setter
	public static class LogConfig {
		public boolean threadName;
		public boolean threadId;
		public boolean multiLine;
	}
}
