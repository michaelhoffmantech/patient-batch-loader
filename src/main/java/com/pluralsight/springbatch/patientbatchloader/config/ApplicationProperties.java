package com.pluralsight.springbatch.patientbatchloader.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Patient Batch Loader.
 * <p>
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

	private final Batch batch = new Batch(); 
	
	public Batch getBatch() {
		return batch;
	}
	
	public static class Batch {
		private String inputPath = "c:/input";
		
		public String getInputPath() {
			return this.inputPath;
		}
		
		public void setInputPath(String inputPath) {
			this.inputPath = inputPath; 
		}
	}
	
    private final Metrics metrics = new Metrics();

    public Metrics getMetrics() {
        return metrics;
    }

    public static class Metrics {

        private final Jmx jmx = new Jmx();

        private final Logs logs = new Logs();

        public Jmx getJmx() {
            return jmx;
        }

        public Logs getLogs() {
            return logs;
        }

        public static class Jmx {

            private boolean enabled = true;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }
        }

        public static class Logs {

            private boolean enabled = false;

            private long reportFrequency = 60;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public long getReportFrequency() {
                return reportFrequency;
            }

            public void setReportFrequency(long reportFrequency) {
                this.reportFrequency = reportFrequency;
            }
        }
    }
}
