package edu.uga.cs.pcf.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import models.pcf.pcl.BinaryExpression;
import models.pcf.pcl.Func;
import models.pcf.pcl.PCLCondition;
import models.pcf.pcl.PCLConstraint;
import models.pcf.pcl.PCLConstraintConstantAttribute;
import models.pcf.pcl.PCLConstraintOperation;
import models.pcf.pcl.PCLContext;
import models.pcf.pcl.PCLExceptionHandler;
import models.pcf.pcl.PCLExpression;
import models.pcf.pcl.PCLPreCondition;
import models.pcf.pcl.PCLProcessElement;
import models.pcf.pcl.UnaryExpression;

import org.apache.log4j.Logger;
import org.switchyard.component.camel.model.v1.V1CamelImplementationModel;
import org.switchyard.config.model.composite.ComponentImplementationModel;
import org.switchyard.config.model.composite.ComponentModel;
import org.switchyard.config.model.composite.ComponentServiceModel;
import org.switchyard.config.model.composite.CompositeReferenceModel;
import org.switchyard.config.model.composite.CompositeServiceModel;
import org.switchyard.config.model.composite.InterfaceModel;
import org.switchyard.config.model.composite.v1.V1ComponentModel;
import org.switchyard.config.model.composite.v1.V1ComponentReferenceModel;
import org.switchyard.config.model.composite.v1.V1InterfaceModel;

import pcf.pcl.PCLParseResult;
import pcf.pcl.PCLParser;
import scala.Option;
import scala.collection.Iterator;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

import edu.uga.cs.pcf.base.ServiceRegistry.ServiceRegistryInformation;

/**
 * Takes pcl and switchyard.xml as input, generates the backbone of constraint
 * operations and service integration template that are compatible with SCA
 * specifications.
 */
public class PcfLinker {

    private static final String SWITCHYARD_XML = "switchyard.xml";
    private static final String NEPOTISM_PCL = "nepotism.pcl";
    private static final Logger logger = Logger.getLogger(PcfLinker.class);
    private InputStream pcl;
    private InputStream switchyard;
    private PCLParseResult pclResult;
    private SwitchyardConfigParser switchyardParser;

    /**
     * Parses pcl file and switchyard configuration
     */
    public void initialize() {
        // Parses pcl
        PCLParser parser = new PCLParser();
        String content = null;
        try {
            content = CharStreams.toString(new InputStreamReader(pcl, Charsets.UTF_8));
            this.pclResult = parser.pclParse(content);
            logger.info("------------------ parsed pcl --------------------");
            logger.info(this.pclResult);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Closeables.closeQuietly(pcl);
        }

        // Parses switchyard configuration
        this.switchyard = getDefaultSwitchyard();
        try {
            this.switchyardParser = new SwitchyardConfigParser(this.switchyard);
            logger.info("------------------ parsed switchyard configuration --------------------");
            logger.info(this.switchyardParser);
        } catch (Exception ex) {
            logger.error("failed to parse switchard configuration xml.", ex);
        } finally {
            Closeables.closeQuietly(this.switchyard);
        }
    }

    /**
     * Connects pcl and switchyard and links the two by generating the service
     * integration template
     */
    public void link() {
        logger.info("--------------begin linking pcl and switchyard----------------");
        // Gets all the constraints
        Option<PCLConstraint> constraint = this.pclResult.constraint();
        PCLConstraint pclConstraint = null;
        if (!constraint.isEmpty()) {
            pclConstraint = constraint.get();
        }
        PCLContext context = pclConstraint.context();
        scala.collection.immutable.List<PCLProcessElement> processElements = context.elements();
        Iterator<PCLProcessElement> iter = processElements.toIterator();
        logger.info("--------------process elements----------------");
        List<PCLProcessElement> activities = Lists.newArrayList();
        while (iter.hasNext()) {
            PCLProcessElement processElement = iter.next();
            logger.info(processElement);
            activities.add(processElement);
        }

        // constraint operations
        logger.info("--------------constraint operations----------------");
        scala.collection.immutable.List<PCLCondition> conditions = pclConstraint.conditions();
        Iterator<PCLCondition> condIter = conditions.toIterator();
        List<PCLCondition> pclConditions = Lists.newArrayList();
        while (condIter.hasNext()) {
            PCLCondition cond = condIter.next();
            logger.info(cond);
            pclConditions.add(cond);
        }

        // pcl exceptions
        logger.info("--------------pcl exceptions----------------");
        Option<PCLExceptionHandler> exceptionHandlerOption = pclConstraint.exceptionHandler();
        PCLExceptionHandler exceptionHandler = null;
        if (exceptionHandlerOption.isDefined()) {
            exceptionHandler = exceptionHandlerOption.get();
        }
        logger.info(exceptionHandler);

        // Gets all the switchyard configuration
        java.util.List<CompositeServiceModel> services = this.switchyardParser.getServices();
        java.util.List<CompositeReferenceModel> references = this.switchyardParser.getReferences();
        java.util.List<ComponentModel> components = this.switchyardParser.getComponents();
        for (CompositeServiceModel service : services) {
             logger.info("-----------------------------------------------------------------------");
             logger.info(String.format("service name %s", service.getName()));
        }
        for (ComponentModel component : components) {
            logger.info("-----------------------------------------------------------------------");
            logger.info(String.format("component name %s\tservice name %s", component.getName(), getServiceName(component)));
        }
        for (CompositeReferenceModel reference : references) {
            logger.info("-----------------------------------------------------------------------");
            logger.info(String.format("reference name %s", reference.getName()));
        }

        /*
         * Links pcl and switchyard configuration and follows service integration
         * template
         * Step 1: match the services with an sca component associated to an sca service
         */
        List<PclSwitchyardMatcher> matches = findMatched(activities, pclConditions, exceptionHandler, services, components);
        logger.info("-----------------------------------------------------------------------");
        logger.info("found matched activity and sca component");
        logger.info(Joiner.on("\n").join(matches));

        /*
         * Step 2: for these matched services, creates a new camel route following
         * service integration template.
         * The new component should have the exact implementation with the old one.
         */
        for (PclSwitchyardMatcher match : matches) {
            ComponentModel newComponent = buildCamelRouteComponent(match);
            match.setNewComponent(newComponent);
        }
    }

    /**
     * Creates a camel route component and creates the skeleton camel route in Java.
     * @param match
     * @return
     */
    private ComponentModel buildCamelRouteComponent(PclSwitchyardMatcher match) {
        Preconditions.checkArgument(match != null);

        // sca component
        ComponentModel old = match.getOldComponent();
        logger.info("------------old component--------------");
        logger.info(old);

        // sca service
        CompositeServiceModel service = match.getService();
        logger.info("------------sca service--------------");
        logger.info(service);

        // Constraint pcl

        // This target name space is kept the same
        String namespace = old.getTargetNamespace();

        ComponentModel result = new V1ComponentModel();

        // Set up service name
        result.setName(old.getName());

        // Sets up the service and uses the same old ones
        List<ComponentServiceModel> services = old.getServices();
        for (ComponentServiceModel s : services) {
            result.addService(s);
        }

        /*
         * sample sca references and services:
         * <sca:service name="JobInterview">
         *   <sca:interface.wsdl interface="JobInterview.wsdl#wsdl.porttype(JobInterviewPortType)"/>
         * </sca:service>
         * <sca:reference name="JobInterviewService">
         *   <sca:interface.wsdl interface="JobInterview.wsdl#wsdl.porttype(JobInterviewPortType)"/>
         * </sca:reference>
         * <sca:reference name="NepotismCheckerInvoker">
         *   <sca:interface.java interface="edu.uga.cs.pcf.services.jobinterview.NepotismCheckerInvoker"/>
         * </sca:reference>
         * <sca:reference name="FailedConstraintOperationLoggerInvoker">
         *   <sca:interface.java interface="edu.uga.cs.pcf.process.emergencyreaction.FailedConstraintOperationLoggerInvoker"/>
         * </sca:reference>
         * <sca:reference name="ContextMonitorInvoker">
         *   <sca:interface.java interface="edu.uga.cs.pcf.process.monitor.ContextMonitorInvoker"/>
         * </sca:reference>
         */

        // Adds a references to the existing service, because the original service
        // is now integrated within and referenced by the new camel route.
        // Sets up name and interface properly.
        logger.info("-------------------add original service reference-------------------");
        V1ComponentReferenceModel serviceRef = new V1ComponentReferenceModel(namespace);
        serviceRef.setName(service.getName());
        serviceRef.setInterface(service.getInterface());
        result.addReference(serviceRef);

        // Continues to add the following references that are referenced by the camel route:
        // 1. monitor
        // 2. constraint operation
        // 3. exception handler if an exception handler is specified in pcl

        // Adds context monitor
        logger.info("-------------------add context monitor reference-------------------");
        ServiceRegistryInformation monitorServiceRegistry = ServiceRegistry.getRegistry(ServiceRegistry.MONITOR_OPERATION);
        V1ComponentReferenceModel monitorRef = new V1ComponentReferenceModel(namespace);
        monitorRef.setName(monitorServiceRegistry.getServiceName());
        V1InterfaceModel monitorJavaInterface = new V1InterfaceModel(InterfaceModel.JAVA, namespace);
        monitorJavaInterface.setInterface(monitorServiceRegistry.getJavaImplementation());
        monitorRef.setInterface(monitorJavaInterface);
        result.addReference(monitorRef);

        // Adds constraint operations
        List<PCLCondition> conditions = match.getConditions();
        for (PCLCondition condition : conditions) {
            ConstraintOperationAttributeEntry constraintOperationAttributeEntry = getConstraintOperation(condition);
            if (constraintOperationAttributeEntry != null) {
                logger.info("-------------------add constraint operation reference-------------------");
                String operationName = constraintOperationAttributeEntry.constraintOperation.operation();
                logger.info(String.format("operation name = %s", operationName));
                ServiceRegistryInformation constraintOperationServiceRegistry = ServiceRegistry.getRegistry(operationName);
                if (constraintOperationServiceRegistry != null) {
                    V1ComponentReferenceModel constraintOperationRef = new V1ComponentReferenceModel(namespace);
                    constraintOperationRef.setName(constraintOperationServiceRegistry.getServiceName());
                    V1InterfaceModel constraintOperationJavaInterface = new V1InterfaceModel(InterfaceModel.JAVA, namespace);
                    constraintOperationJavaInterface.setInterface(constraintOperationServiceRegistry.getJavaImplementation());
                    constraintOperationRef.setInterface(constraintOperationJavaInterface);
                    result.addReference(constraintOperationRef);
                }
            }
        }

        // Adds exception reference if necessary
        if (match.getExceptionHandler() != null) {
            PCLExceptionHandler exceptionHandler = match.getExceptionHandler();
            logger.info("------------add exception handler--------------");
            String operationName = exceptionHandler.handler();
            logger.info(String.format("operation name = %s", operationName));
            ServiceRegistryInformation exceptionHandlerServiceRegistry = ServiceRegistry.getRegistry(operationName);
            if (exceptionHandlerServiceRegistry != null) {
                V1ComponentReferenceModel exceptionHandlerRef = new V1ComponentReferenceModel(namespace);
                exceptionHandlerRef.setName(exceptionHandlerServiceRegistry.getServiceName());
                V1InterfaceModel exceptionHandlerJavaInterface = new V1InterfaceModel(InterfaceModel.JAVA, namespace);
                exceptionHandlerJavaInterface.setInterface(exceptionHandlerServiceRegistry.getJavaImplementation());
                exceptionHandlerRef.setInterface(exceptionHandlerJavaInterface);
                result.addReference(exceptionHandlerRef);
            }
        }

        // After all the references have been created, generates a Camel route skeleton Java class

        // Replaces the existing implementation with a Camel route implementation
        ComponentImplementationModel existingImplementation = old.getImplementation();
        logger.info(String.format("Existing implementation is\n%s", existingImplementation));
        logger.info("This implementation will be replaced with a Camel route implementation.");

        String javaFileName = String.format("edu.uga.cs.pcf.camel.%sCamelRoute.java", result.getName());
        generateCamelRouteSkeleton(javaFileName, match);
        V1CamelImplementationModel camelRoute = new V1CamelImplementationModel(namespace);
        camelRoute.setJavaClass(javaFileName);
        // Replaces the existing implementation with the Camel route
        result.setImplementation(camelRoute);

        // Prints out the new component
        logger.info("------------new sca component--------------");
        logger.info(String.format("\n%s", result));

        return result;
    }

    private static class ConstraintOperationAttributeEntry {
        PCLConstraintOperation constraintOperation;
        PCLConstraintConstantAttribute attribute;
    }

    /**
     * As of now, only handles pre condition and the expression
     * 1. isRelative(a,b) == false
     * 2. not isRelative(a,b)
     * @param condition
     * @return
     */
    private ConstraintOperationAttributeEntry getConstraintOperation(PCLCondition condition) {
        if (condition instanceof PCLPreCondition) {
            PCLPreCondition pre = (PCLPreCondition)condition;
            PCLExpression expression = pre.expression();
            if (expression instanceof BinaryExpression) {
                BinaryExpression binary = (BinaryExpression)expression;
                if (binary.left() instanceof Func) {
                    Func function = (Func)binary.left();
                    logger.info(binary.right());
                    models.pcf.pcl.Constant constant = (models.pcf.pcl.Constant)binary.right();
                    ConstraintOperationAttributeEntry result = new ConstraintOperationAttributeEntry();
                    result.constraintOperation = function.operation();
                    result.attribute = constant.constant();
                    return result;
                }
            } else if (expression instanceof UnaryExpression) {
                // UnaryExpression(not,Func(isRelative(List(Var(PCLConstraintComplexAttribute(List(t1, ., interviewer))), Var(PCLConstraintComplexAttribute(List(t1, ., interviewee)))))))
                UnaryExpression unary = (UnaryExpression)expression;
                System.out.print(unary);
                String operator = unary.operator();
                Func function = (Func)unary.arg();
                logger.info(function);
                ConstraintOperationAttributeEntry result = new ConstraintOperationAttributeEntry();
                result.constraintOperation = function.operation();
                if ("not".equals(operator)) {
                    result.attribute = new PCLConstraintConstantAttribute<>("false");
                }

                return result;
            }
        }
        return null;
    }

    /**
     * Generates a camel route in Java.
     * @param javaFileName
     * @param match
     */
    private void generateCamelRouteSkeleton(String javaFileName,
            PclSwitchyardMatcher match) {
        logger.info(String.format("creates a java file %s.", javaFileName));
        String template =
                "package edu.uga.cs.pcf.process.glycomics;\n" + 
                "\n" + 
                "import org.apache.camel.builder.RouteBuilder;\n" + 
                "\n" + 
                "public class %sCamelServiceRoute extends RouteBuilder {\n" + 
                "    /**\n" + 
                "     * The Camel route is configured via this method. \n" + 
                "     * The from endpoint is required to be a SwitchYard service.\n" + 
                "     */\n" + 
                "    public void configure() {\n" + 
                "        from(\"switchyard://%s\")\n" + 
                "        .log(\"Received message for '%s' : ${body}\")\n" + 
                "        // Send context information to context monitor using wiretap pattern\n" + 
                "        .log(\"wiretap to context monitor\")\n" + 
                "        .wireTap(\"switchyard://%s\")\n" + 
                "        // Apply content based router pattern and invoke constraint operation\n" + 
                "        .log(\"Invoke constraint operation...\")\n" + 
                "        .to(\"switchyard://%s\")\n" + 
                "        .choice()\n" + 
                "        .when(body().contains(\"%s\"))\n" + 
                "            .log(\"go to original service.....\")\n" + 
                "            .to(\"switchyard://%s\")\n" + 
                "        .otherwise()\n" + 
                "            .log(\"go to exception handler.....\")\n" + 
                "            .to(\"switchyard://%s\");\n" + 
                "    }\n" + 
                "}";

        // sca component
        ComponentModel old = match.getOldComponent();
        // sca service
        CompositeServiceModel service = match.getService();

        String originalServiceName = old.getName();
        ServiceRegistryInformation monitorServiceRegistry = ServiceRegistry.getRegistry(ServiceRegistry.MONITOR_OPERATION);

        List<PCLCondition> conditions = match.getConditions();
        String constraintOperation = "";
        String attribute = "";
        for (PCLCondition condition : conditions) {
            ConstraintOperationAttributeEntry constraintOperationAttributeEntry = getConstraintOperation(condition);
            if (constraintOperationAttributeEntry != null) {
                String operationName = constraintOperationAttributeEntry.constraintOperation.operation();
                attribute = constraintOperationAttributeEntry.attribute.value().toString();

                ServiceRegistryInformation constraintOperationServiceRegistry = ServiceRegistry.getRegistry(operationName);
                if (constraintOperationServiceRegistry != null) {
                    constraintOperation = constraintOperationServiceRegistry.getServiceName();
                    
                }
            }
        }

        // Exception handler
        String exceptionHandlerService = "";
        if (match.getExceptionHandler() != null) {
            PCLExceptionHandler exceptionHandler = match.getExceptionHandler();
            logger.info("------------add exception handler--------------");
            String operationName = exceptionHandler.handler();
            logger.info(String.format("operation name = %s", operationName));
            ServiceRegistryInformation exceptionHandlerServiceRegistry = ServiceRegistry.getRegistry(operationName);
            if (exceptionHandlerServiceRegistry != null) {
                exceptionHandlerService = exceptionHandlerServiceRegistry.getServiceName();
            }
        }

        String content = String.format(template,
                service.getName(),
                service.getName(),
                service.getName(),
                monitorServiceRegistry.getServiceName(),
                constraintOperation,
                attribute,
                originalServiceName,
                exceptionHandlerService);
        logger.info(String.format("generated camel route following service integration template.\n%s", content));
    }

    /**
     * Finds the match between pcl's process element and switchyard's sca component.
     * @param activities
     * @param components
     * @return
     */
    private List<PclSwitchyardMatcher> findMatched(
            List<PCLProcessElement> activities,
            List<PCLCondition> conditions,
            PCLExceptionHandler exceptionHandler,
            java.util.List<CompositeServiceModel> services,
            List<ComponentModel> components) {
        List<PclSwitchyardMatcher> result = Lists.newArrayList();
        for (PCLProcessElement activity : activities) {
            PclSwitchyardMatcher match = new PclSwitchyardMatcher();
            // Set the conditions
            match.setConditions(conditions);
            // Set the exception handler
            match.setExceptionHandler(exceptionHandler);

            for (ComponentModel component : components) {
                // A sca component is linked to an sca service via service attribute.
                if (activity.name().equals(getServiceName(component))) {
                    match.setOldComponent(component);
                    match.setProcessElement(activity);
                    break;
                }
            }

            for (CompositeServiceModel service : services) {
                if (activity.name().equals(service.getName())) {
                    match.setService(service);
                    break;
                }
            }

            result.add(match);
        }

        return result;
    }

    private static class PclSwitchyardMatcher {
        private PCLProcessElement processElement;
        private CompositeServiceModel service;
        private ComponentModel oldComponent;
        private ComponentModel newComponent;
        private List<PCLCondition> conditions;
        private PCLExceptionHandler exceptionHandler;

        public PclSwitchyardMatcher() {
            super();
        }

        public PCLProcessElement getProcessElement() {
            return processElement;
        }

        public void setProcessElement(PCLProcessElement processElement) {
            this.processElement = processElement;
        }

        public ComponentModel getOldComponent() {
            return oldComponent;
        }

        public void setOldComponent(ComponentModel oldComponent) {
            this.oldComponent = oldComponent;
        }

        public ComponentModel getNewComponent() {
            return newComponent;
        }

        public void setNewComponent(ComponentModel newComponent) {
            this.newComponent = newComponent;
        }

        public CompositeServiceModel getService() {
            return service;
        }

        public void setService(CompositeServiceModel service) {
            this.service = service;
        }

        public List<PCLCondition> getConditions() {
            return conditions;
        }

        public void setConditions(List<PCLCondition> conditions) {
            this.conditions = conditions;
        }

        public PCLExceptionHandler getExceptionHandler() {
            return exceptionHandler;
        }

        public void setExceptionHandler(PCLExceptionHandler exceptionHandler) {
            this.exceptionHandler = exceptionHandler;
        }

        @Override
        public String toString() {
            return String.format(
                    "PclSwitchyardMatcher [processElement=%s, conditions=%s, exceptionHandler=%s, service=%s, oldComponent=%s, newComponent=%s]",
                    processElement.name(),
                    conditions == null || conditions.isEmpty() ? "null" : "[" + Joiner.on(", ").join(conditions),
                    exceptionHandler == null ? "null" : exceptionHandler,
                    service == null ? "null" : service.getName(),
                    oldComponent == null ? "null" : oldComponent.getName(),
                    newComponent == null ? "null" : newComponent.getName());
        }
    }

    private String getServiceName(ComponentModel component) {
        return component.getServices().get(0).getName();
    }

    public PcfLinker(InputStream pcl, InputStream switchyard) {
        super();
        this.pcl = pcl;
        this.switchyard = switchyard;
    }

    public static InputStream getDefaultPcl() {
        InputStream is = PcfLinker.class.getClassLoader().getResourceAsStream(NEPOTISM_PCL);
        return is;
    }

    public static InputStream getDefaultSwitchyard() {
        InputStream is = PcfLinker.class.getClassLoader().getResourceAsStream(SWITCHYARD_XML);
        return is;
    }

    public static void main(String[] args) {
        InputStream pcl = getDefaultPcl();
        InputStream switchyard = getDefaultSwitchyard();

        PcfLinker linker = new PcfLinker(pcl, switchyard);
        linker.initialize();
        linker.link();
    }
}
