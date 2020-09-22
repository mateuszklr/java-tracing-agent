package dakaraphi.devtools.tracing.config;

import com.typesafe.config.Optional;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Tracer {
    @Optional @Setter @Getter
    public int id = 0;

    @Optional @Setter @Getter
    public boolean enabled = true;
    @Optional @Setter @Getter
    public String name;
    @Optional @Getter
    public String classRegex;
    @Optional @Getter
    public String methodRegex;
    @Optional @Setter @Getter
    public Integer line;
    @Optional @Setter @Getter
    public List<String> variables;
    @Optional @Setter @Getter
    public LogWhen logWhen;
    @Optional @Setter @Getter
    public LogStackFrames logStackFrames;

    @Getter
    private Pattern classRegexPattern;
    @Getter
    private Pattern methodRegexPattern;

    public void setClassRegex(String classRegex) {
        this.classRegex = classRegex;
        if (classRegex != null) {
            this.classRegexPattern = Pattern.compile(classRegex);
        }
    }

    public void setMethodRegex(String methodRegex) {
        this.methodRegex = methodRegex;
        if (methodRegex != null) {
            this.methodRegexPattern = Pattern.compile(methodRegex);
        }
    }

    public static class LogWhen {
        @Optional @Setter @Getter
        public List<VariableCondition> variableValues = new ArrayList<>();
        @Optional @Getter
        private String stackFramesRegex;
        @Optional @Getter
        private String threadNameRegex;

        @Getter
        private Pattern threadNameRegexPattern;
        @Getter
        private Pattern stackFramesRegexPattern;

        public void setStackFramesRegex(String stackFramesRegex) {
            this.stackFramesRegex = stackFramesRegex;
            if (stackFramesRegex != null) {
                this.stackFramesRegexPattern = Pattern.compile(stackFramesRegex);
            }
        }

        public void setThreadNameRegex(String threadNameRegex) {
            this.threadNameRegex = threadNameRegex;
            if (threadNameRegex != null) {
                this.threadNameRegexPattern = Pattern.compile(threadNameRegex);
            }
        }
    }

    public static class VariableCondition {
        @Optional @Setter @Getter
        public int index;
        @Optional @Getter
        private String valueRegex;

        @Getter
        private Pattern valueRegexPattern;

        public void setValueRegex(String valueRegex) {
            this.valueRegex = valueRegex;
            if (valueRegex != null) {
                this.valueRegexPattern = Pattern.compile(valueRegex);
            }
        }
    }

    public static class LogStackFrames {
        @Optional @Setter @Getter
        public int limit;
        @Optional @Getter
        private String includeRegex;
        @Optional @Getter
        private String excludeRegex;

        @Getter
        private Pattern includeRegexPattern;
        @Getter
        private Pattern excludeRegexPattern;

        public void setIncludeRegex(String includeRegex) {
            this.includeRegex = includeRegex;
            if (includeRegex != null) {
                this.includeRegexPattern = Pattern.compile(includeRegex);
            }
        }

        public void setExcludeRegex(String excludeRegex) {
            this.excludeRegex = excludeRegex;
            if (excludeRegex != null) {
                this.excludeRegexPattern = Pattern.compile(excludeRegex);
            }
        }
    }
}
