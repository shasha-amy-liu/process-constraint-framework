package edu.uga.cs.pcf.base;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class ServiceRegistry {

    public static final String MONITOR_OPERATION = "monitor";
    public static final String IS_RELATIVE_OPERATION = "isRelative";
    public static final String EXCEPTION_HANDLER_OPERATION = "FailedConstraintOperationLogger";

    private static final ServiceRegistryInformation nepotismChecker = new ServiceRegistryInformation(
            IS_RELATIVE_OPERATION, "NepotismCheckerInvoker",
            "edu.uga.cs.pcf.services.jobinterview.NepotismCheckerInvoker");
    private static final ServiceRegistryInformation monitorInvoker = new ServiceRegistryInformation(
            MONITOR_OPERATION, "ContextMonitorInvoker",
            "edu.uga.cs.pcf.process.monitor.ContextMonitorInvoker");
    private static final ServiceRegistryInformation exceptionHandlerInvoker = new ServiceRegistryInformation(
            EXCEPTION_HANDLER_OPERATION, "FailedConstraintOperationLoggerInvoker",
            "edu.uga.cs.pcf.process.emergencyreaction.FailedConstraintOperationLoggerInvoker");
    private static final Map<String, ServiceRegistryInformation> serviceRegistry =
            ImmutableMap.of(
                    IS_RELATIVE_OPERATION, nepotismChecker,
                    MONITOR_OPERATION, monitorInvoker,
                    EXCEPTION_HANDLER_OPERATION, exceptionHandlerInvoker);

    public static ServiceRegistryInformation getRegistry(String operationName) {
        return serviceRegistry.get(operationName);
    }

    public static class ServiceRegistryInformation {
        private String operationName;
        private String serviceName;
        private String javaImplementation;

        public ServiceRegistryInformation(String operationName,
                String serviceName, String javaImplementation) {
            super();
            this.operationName = operationName;
            this.serviceName = serviceName;
            this.javaImplementation = javaImplementation;
        }

        public String getOperationName() {
            return operationName;
        }

        public void setOperationName(String operationName) {
            this.operationName = operationName;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getJavaImplementation() {
            return javaImplementation;
        }

        public void setJavaImplementation(String javaImplementation) {
            this.javaImplementation = javaImplementation;
        }
    }
}
