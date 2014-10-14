# PCF project list
## pcf
include pcl parser and web ui.
Use maven command to build and create war file, and then copies it to the jboss's deployment folder, e.g., jboss/jboss-eap-6.1-test/standalone/deployments.
```
# build a war
mvn clean test package
# start jboss enterprise application platform v6.1 with switchyard runtime first
# deploy to jboss, this command applies to all the jboss maven project
mvn clean jboss-as:deploy
# after application is deployed, use redeploy if any change is made.
mvn clean jboss-as:redeploy
# run switchyard parser
mvn exec:java -Dexec.mainClass="edu.uga.cs.pcf.base.PcfLinker"
```
## pcf-operations
+ constraint operations and exception handling operations
+ Used patterns: dynamic router and wiretap
+ [add camel-http to switchyard](https://community.jboss.org/wiki/SwitchYardAS7CamelIntegration). It is difficult to configure and leaves it for a future version.
## pcf-processes
include all the business processes and scientific workflows
Uses web service tester or soapui to test:
  + [job interview process in wsdl](http://localhost:8080/JobInterviewProcess/JobInterviewProcess?wsdl)
  + [emergency reaction workflow in wsdl](http://localhost:8080/EmergencyReactionWorkflow/EmergencyReactionWorkflowService?wsdl)

Uses any web browser to test and send a http GET request to this link
  + [glycomics workflow in http binding](http://localhost:8080/processes/glycomics)
  + [emergency reaction workflow](http://localhost:8080/EmergencyReactionWorkflow/EmergencyReactionWorkflowService?wsdl)
  + [job interview process](http://localhost:8080/JobInterviewProcess/JobInterviewProcess?wsdl)
## pcf-services
include all the services that are used to compose the processes
## Switchyard-helloworld
include a simple switchyard project

# PCF framework introduction

## End-to-end process constraint framework (PCF)
* Process engine for BPEL and BPMN
* PCL
  + PCL parser
* ProContO
  + Semantic ontology tooling (OWL Api)
* Runtime monitor
* Constraint operations

## Service oriented architecture(SOA) and Service Component Architecture(SCA)

[SOA vs SCA](http://stackoverflow.com/questions/5719564/advantages-of-sca-over-spring)

[SCA introduction](http://www.servicetechmag.com/i68/1112-2)

Service oriented architecture (SOA) provides a standard approach to
register/discover/invoke Web services, which abstracts business logic by making
it less dependent on the actual implementation language. Nowadays Java, C++, C#,
PHP, or Python can all be the implementation programming language behind our Web
services. Although SOA developers can consume Web services without
concerning about their implementation details, they still need to face various
endpoints, binding and protocols when composing various Web services:

* interface: WSDL port type, Java interface
* service/component implementation: Java class, BPEL process, Python, Spring
* binding: JMS, Web Service, RMI/IIOP, SOAP, REST
* protocols: HTTP, HTTPS, GET, POST

This is where SCA comes into play. Built upon existing SOA standards and
technologies, Service Component Architecture(SCA) aims at promoting SOA to the
next level. It abstracts the endpoints of the services and hides protocol implementation from SOA developers. When an SCA service is needed, the developer just
looks it up via the SCA API's and then invokes the service.

The building blocks in SCA are listed below [add citation](http://www.servicetechmag.com/i68/1112-2):

* Composite - A composite is used to assemble SCA elements in logical groups and contains a set of components, services, references and the wires that interconnect them, plus a set of properties.
* Component - A Component is the basic elements of business function in an SCA assembly, which are combined into complete business solutions by SCA composites.
* Properties - Properties allow for the configuration of an implementation with externally set values. It is worth mentioning some highly configurable applications usually offer large number of properties that can be changed by business users to fit into the needs of enterprise users.
* Service - A Service represents an addressable interface of the implementation.
* Implementation - An implementation is a technological realization of a business function. Implementation types include Java EE, Spring Framework, BPEL and C++.
* Binding - Binding specifies the technology implementation of a Service or Reference.
* Reference - A reference represents a requirement that the implementation has on a service provided by another component.
* Promote - Promote is used to connect different components across composites.
* Wire - Wire is used to connect different components within a composite.

## Process runtime engines in SwitchYard

SwitchYard is developed by Jboss and adopts the SCA approach. It supports Camel routing, BPMN 2, business process management and service
orchestration, and Drools. In PCF framework, it is
used as the underlying process engine for both BPEL and BPMN and controls the
deployment and execution of workflow. Furthermore, PCF utilizes SwitchYard's SCA
technology to enable the interaction and integration between workflow and its associated
constraint operations. Not only the workflows and processes (represented in BPEL and BPMN) are
treated as SCA components, but also the constraint operations are promoted from
a plain Web service to an SCA component.

## BPEL process as SCA components
SwitchYard supports BPEL development and deployment by utilizing
[Jboss's Riftsaw project](http://riftsaw.jboss.org/). Riftsaw  is based on
[Apache ODE project](http://ode.apache.org/) and supports the following features:
* [WS-BPEL 2.0 OASIS standard](http://docs.oasis-open.org/wsbpel/2.0/OS/wsbpel-v2.0-OS.pdf)
* JBossWS Native and CXF Web Service stack support.
* UDDI registration of BPEL endpoints, and Runtime UDDI Endpoint lookup as preview feature.

## BPMN process as SCA components
SwitchYard supports BPMN2 via the integration of [JBoss BPM Suite](http://www.jboss.org/products/bpmsuite/overview/). The BPM component is a pluggable container and has a jBPM 5 integration.

## Camel routing through Enterprise Integration Pattern
Camel is a library to define routing and mediation rules and works with a variety of binding protocols. Another powerful features for Camel library is its native support and implementation of the EIPs. Enterprise Integration Patterns (EIPs) are a set of patterns and best practices in the area of enterprise integration.
Camel is not a process runtime in the real sense. However, due to its support of some common process runtime services (e.g., data transformation, data mediation, service monitoring, and orchestration, etc.), it can be used to build simple workflow manager and utilizes the EIPs to compose the services into a complete workflow.

## Constraint operations as SCA components
All the constraint operations (and exception handler operations) are developed
as a plain Web service and then promoted to an SCA component.

# Implementation for PCF processes
Demonstrate that PCF's support of bpel2.0 and bpmn2

## Development environment setup
* Install Java, Ant, Maven and configure them properly.
  + JDK 7
  + Ant 1.9.3
  + Maven 3.2.1
  + Scala 2.10.1 (no need to download or install it, and it will be handled by maven and must choose the correct version for Scala IDE)
* Install SopaUI to test Web services.
* Eclipse IDE
  + Eclispe IDE Kepler v4.3
* JBoss Tools for Kepler
  + Should not choose Jboss Tools for Luna or Juno.
  + Help -> Eclipse MarketPlace
  + Searches for "jboss tools" and picks the latest version for kepler
  + Selects all the plugsin and clicks install
* Install SwitchYard tooling
  + After jboss tools is installed, goes to "Jboss Central" --> Software/Update
  + Chooses the following:
    - JBoss Business Process and Rules Development
    - JBoss Integration and SOA Development
    - Jboss BPEL Editor
    - Switchyard project support
  + Installs all the features/plugins
  + Restarts Eclipse IDE to make the changes
  + (Optional) Install from Switchyard repository
    - Try this method if the first one does not work and install at least the following:
    - Refers to the [switchyard tooling installation guide](https://docs.jboss.org/author/display/SWITCHYARD/Installing+Eclipse+Tooling)
    - Add [an update site](http://download.jboss.org/jbosstools/updates/stable/kepler/integration-stack/aggregate/4.1.4.Final/) to eclipse repository
    - Install all the plugins listed from this update site
* Install Jboss EAP 6.1
  + download jboss-eap-6.1 (NOT jboss-as-6.1).
  + Note: JBoss EAP is Enterprise Application Platform, while JBoss AS is community application server. Jboss EAP 6.1 uses Jboss AS 7.2 internally.
  + extract to a directory
  + (optional) create a admin user and password with add_user.sh
* Install Switchyard 1.1 runtime to Jboss EAP
  + download switchyard-installer-1.1.0.Final.zip
  + install switchyard 1.1 runtime following [the guide](https://docs.jboss.org/author/display/SWITCHYARD/Installation+Guide)
```
cd switchyard-installer-1.1
ant
set home folder of JBOSS_EAP directory
```
  + (optional) install bpel console
```
cd switchyard-installer-1.1
ant install-bpel-console
set home folder of JBOSS_EAP directory
```
  + in Eclipse-JbossTools, add a new server runtime (Windows  -> Preference --> Server).
  + Selects JBoss EAP server runtime 6.1+ and points the folder to the existing EAP installation directory where SwitchYard is just installed.
  + install Scala IDE plugins for Eclipse
    - Choose the update site for Eclipse Kepler and Scala 2.10
    - Add [update site](http://download.scala-ide.org/sdk/helium/e38/scala210/stable/site)
    - Install "Eclipse IDE for Eclipse" plugin
+ The inal step is to import the following projects into workspace:
    - pcf/pcf
    - pcf/pcf-operations
    - pcf/pcf-processes
    - pcf/pcf-services

## Jboss SwitchYard project and the underlying concept SCA (Service Component Architecture)
* SwitchYard is based on SCA
  + [Tutorial Reference](https://docs.jboss.org/author/display/SWITCHYARD/Tutorial)
  + [SwitchYard tutorial in vimeo](http://vimeo.com/album/2797329)

### camel routing integration
Camel is particular strong as a light-weight and agile routing and mediation framework. After being integrated with SCA runtime environment (e.g, SwitchYard), it brings powerful features to dynamic routing on the service level.


## workflow based on sca services
general approaches:
+ apply pipeline pattern from [enterprise integration patterns (EIP)](http://www.enterpriseintegrationpatterns.com/PipesAndFilters.html)
+ create a new workflow based on sca services only

### Example: glycomics workflow
+ use
  [content-based router in enterprise integration patterns](http://www.eaipatterns.com/ContentBasedRouter.html)
  and route message based on the result from constraint operation
+ [switchyard's remote invoker](https://docs.jboss.org/author/display/SWITCHYARD/Remote+Invoker)
+ develop constraint operation as a remote service, add it as a reference in workflow's switchyard configuration

constraint operation:
+ input: file size and networkLocation in the ontology
+ output: true or false

## Design/deploy bpel 2.0 and bpmn2 using switchyard suite and maven
* Create switchyard project
  + create switchyard project (File --> New --> SwitchYard --> SwitchyYard Project)
  + choose target runtime: Switchyard: AS7 Extension 1.1.0.Final (JBoss EAP 6.1 + Runtime). Note: this option will only show up after defining the JBoss EAP with switchyard runtime in Eclipse.
  + choose capabilities of bean service, bpel and bpm
* Start jboss EAP
```
cd path/to/jboss-eap-6.1/bin
./standalone.sh
```
* Deploy switchyard to jboss EAP
```
mvn clean install
mvn jboss-as:deploy
```
* Check jboss eap console output

## bpel + sca
* [switchyard's bpel reference](https://docs.jboss.org/author/display/SWITCHYARD/BPEL)
* [Bpel process tutorial about flow activity](http://wso2.com/library/tutorials/2010/07/eclipse-bpel-designer-wso2bps-tutorial/)
* log for successfully deployed bpel process
```
13:25:16,027 INFO  [org.switchyard.common.camel.SwitchYardCamelContext] (MSC service thread 1-4) Route: direct:{urn:switchyard-quickstart:bpel-say-hello:0.1.0}SayHelloService started and consuming from: Endpoint[direct://%7Burn:switchyard-quickstart:bpel-say-hello:0.1.0%7DSayHelloService]
13:25:16,031 INFO  [org.switchyard] (MSC service thread 1-4) Addressing [enabled = false, required = false]
13:25:16,031 INFO  [org.switchyard] (MSC service thread 1-4) MTOM [enabled = false, threshold = 0]
13:25:16,033 INFO  [org.jboss.ws.cxf.metadata] (MSC service thread 1-4) JBWS024061: Adding service endpoint metadata: id=SayHelloService
 address=http://localhost:8080/SayHelloService/SayHelloService
 implementor=org.switchyard.component.soap.endpoint.BaseWebService
 serviceName={http://www.jboss.org/bpel/examples}SayHelloService
 portName={http://www.jboss.org/bpel/examples}SayHelloPort
 annotationWsdlLocation=null
 wsdlLocationOverride=vfs:/content/switchyard-quickstart-bpel-service-say-hello.jar/SayHelloArtifacts.wsdl
 mtomEnabled=false
 handlers=[org.switchyard.component.soap.InboundResponseHandler]
```
* swithyard.xml
  + defines a sca:service
* deploy.xml mapping
  + (correct!!) process name maps to the definition in bpel's targetNamespace:processName
  + partnerLink maps to bpel's partnerLink name attribute with a myRole
  + service name in deploy.xml maps to the <sca:service> name under <sca:component> for the bpel's artifacts, e.g., should map to SayHelloServicesdfd. Because bpel is a SCA component and should only be related to the component level.
  + deploy.xml is the link between bpel and switchyard.xml

```
<deploy xmlns="http://www.apache.org/ode/schemas/dd/2007/03"
	xmlns:examples="http://www.jboss.org/bpel/examples"
    xmlns:domain="urn:switchyard-quickstart:bpel-say-hello:0.1.0">

<!-- say_hello example -->
<process name="examples:SayHello">
    <active>true</active>
    <retired>false</retired>
    <process-events generate="all"/>
    <provide partnerLink="client">
        <service name="domain:SayHelloServicesdfd" port="SayHelloPort"/>
        </provide>
    </process>
</deploy>
```

```
    <bpel:component name="SayHelloProcess">
      <bpel:implementation.bpel process="sh:SayHelloProcess"/>
      <bpel:service name="SayHelloServicesdfd">
        <bpel:interface.wsdl interface="SayHelloArtifacts.wsdl#wsdl.porttype(SayHello)"/>
      </bpel:service>
    </bpel:component>
```

* SayHello.bpel and SayHelloArtifacts.wsdl
  + At first, create a bpel project after bpel designer plugins are installed.
  + Creates a bpel process definition from a template and specifies its process name and namespace
  + In the next dialog, choose synchronous bpel process and populates the template properties for artifacts.wsdl: service name, port name, service address (not important) and chooses SOAP as binding protocol.
  + After all the files are generated in the bpel designer, copies them to the switchyard workspace. Manually adds a new SCA component to the switchyard.xml.
  + Adjusts the deploy.xml if necessary.

## bpmn + sca
* Invoking other SwitchYard Services from inside a BPM process itself.
  + To do this, you can use the SwitchYardServiceTaskHandler, which is provided out-of-the-box, and auto-registered with the runtime.  To make authoring BPMN2 processes easier, SwitchYard provides a new widget for the Eclipse BPMN2 Modeler visual editor palette. Here is a screenshot from the "Help Desk" demo quickstart

* Does not use the choice of "wrapped message" when generating web services, so that the SOAP message is created in this way:
```
<urn:interview>
   <interviewee>?</interviewee>
   <interviewer>?</interviewer>
</urn:interview>
```
* Uses jaxb transformer and refers to transform-jaxb project in quick-start
* Adds variable in bpmn process and uses them as temporary variables between the input-output parameter mapping.
* Default input name is "Parameter" and default output name is "Result"
* Specifies the input and output for each switchyard service task and other bpmn tasks.

# Note
* Fix maven dependency of switchyard api
```xml
<repositories>
  <repository>
    <id>central</id>
    <url>http://repo1.maven.org/maven2/</url>
  </repository>
</repositories>
```

* Things to do after import quickstart project in SwitchYard
  + Remove checkstyle plugin
    Help --> About Eclipse --> Installation Details --> Installed Software,
  + remove checkstyle plugins
  + Choose "Do nothing" (no option) if Eclipse m2e tries to resolve "check style" plugin
    during importing maven project
  + Disable XML schema validation, preference --> XML --> Validation, un-check
    "Honor all XML schema locations"
* jboss add_user.sh to create admin user so that we can access jboss management console (http://localhost:9990/console/)
  + username: admin, password: Pewgapeg7!
* switchyard remote (http://localhost:8080/switchyard-remote)
  SCA Bindings: An SCA binding can be added to composite-level services to make that service available to other applications and remote clients
  through a SwitchYard internal communication protocol. There is only one configuration option available for SCA bindings: clustered : when enabled, the service
  will be published in the distributed SY runtime registry so that other cluster instances can discover and consume the service. Regardless of the clustering setting, all services with an SCA binding are invokable through the SwitchYard remote invoker endpoint. The default URL for this endpoint is http://localhost:8080/switchyard-remote. The hostname and port for this endpoint are based on the default HTTP listener defined in AS 7.
* switchyard bpel console (http://localhost:8080/bpel-console) default username is admin with password admin.
