/*
 * The MIT License
 *
 * Copyright 2020 Mark A. Hunter (ACT Health).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.fhirfactory.pegacorn.deployment.topology.map.standalone.fhirbreak;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import net.fhirfactory.pegacorn.deployment.topology.map.archetypes.FHIRPlacePegacornSubsystem;
import net.fhirfactory.pegacorn.deployment.topology.map.archetypes.common.EdgeMessagingPortsCreator;
import net.fhirfactory.pegacorn.petasos.model.resilience.mode.ConcurrencyModeEnum;
import net.fhirfactory.pegacorn.petasos.model.resilience.mode.ResilienceModeEnum;
import net.fhirfactory.pegacorn.petasos.model.topology.EndpointElementTypeEnum;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElementTypeEnum;
import net.fhirfactory.pegacorn.deployment.topology.map.model.DeploymentMapEndpointElement;
import net.fhirfactory.pegacorn.deployment.topology.map.model.DeploymentMapNodeElement;

import javax.inject.Inject;

/**
 *
 * @author Mark A. Hunter
 */
public class LDAPServices extends FHIRPlacePegacornSubsystem {
	
	// TODO Fix the port number allocations for LDAP Services FHIRBreak Module

	@Inject
	EdgeMessagingPortsCreator edgeMessagingPorts;

	@Override
	public void buildSubsystemNode(DeploymentMapNodeElement solutionNode) {
		DeploymentMapNodeElement fhirbreakLDAPNode = new DeploymentMapNodeElement();
		fhirbreakLDAPNode.setConcurrencyMode(ConcurrencyModeEnum.CONCURRENCY_MODE_STANDALONE);
		fhirbreakLDAPNode.setElementVersion("0.0.1");
		fhirbreakLDAPNode.setInstanceName("FHIRBreak-LDAP");
		fhirbreakLDAPNode.setFunctionName("FHIRBreak-LDAP");
		fhirbreakLDAPNode.setResilienceMode(ResilienceModeEnum.RESILIENCE_MODE_STANDALONE);
		fhirbreakLDAPNode.setTopologyElementType(NodeElementTypeEnum.EXTERNALISED_SERVICE);
		solutionNode.getContainedElements().add(fhirbreakLDAPNode);
		buildExternalisedServiceNode(fhirbreakLDAPNode);
	}
	public void buildExternalisedServiceNode(DeploymentMapNodeElement subsystem) {
		DeploymentMapNodeElement fhirbreakExternalService = new DeploymentMapNodeElement();
		fhirbreakExternalService.setConcurrencyMode(ConcurrencyModeEnum.CONCURRENCY_MODE_STANDALONE);
		fhirbreakExternalService.setElementVersion(subsystem.getElementVersion());
		fhirbreakExternalService.setInstanceName("FHIRBreak-LDAP");
		fhirbreakExternalService.setFunctionName("FHIRBreak-LDAP");
		fhirbreakExternalService.setResilienceMode(ResilienceModeEnum.RESILIENCE_MODE_STANDALONE);
		fhirbreakExternalService.setTopologyElementType(NodeElementTypeEnum.EXTERNALISED_SERVICE);
		fhirbreakExternalService.setContainedElements(new ArrayList<DeploymentMapNodeElement>());
		subsystem.getContainedElements().add(fhirbreakExternalService);

		DeploymentMapEndpointElement endpointFHIRBreakLDAPGateway = new DeploymentMapEndpointElement();
		endpointFHIRBreakLDAPGateway.setEndpointInstanceID("LDAPGateway");
		endpointFHIRBreakLDAPGateway.setEndpointFunctionID("LDAPGateway");
		endpointFHIRBreakLDAPGateway.setEndpointType(EndpointElementTypeEnum.API);
		endpointFHIRBreakLDAPGateway.setExternalDNSEntry("gen0-fhirbreak");
		endpointFHIRBreakLDAPGateway.setExternalPortNumber("32410");
		endpointFHIRBreakLDAPGateway.setIsServer(false);
		endpointFHIRBreakLDAPGateway.setRequiresEncryption(false);
		fhirbreakExternalService.getEndpoints().add(endpointFHIRBreakLDAPGateway);
	}

	public void createFHIRBreakSites(DeploymentMapNodeElement nodeFHIRBreakExternalService) {
		DeploymentMapNodeElement nodeSiteA = new DeploymentMapNodeElement();
		nodeSiteA.setConcurrencyMode(ConcurrencyModeEnum.CONCURRENCY_MODE_STANDALONE);
		nodeSiteA.setElementVersion(nodeFHIRBreakExternalService.getElementVersion());
		nodeSiteA.setInstanceName("___");
		nodeSiteA.setResilienceMode(ResilienceModeEnum.RESILIENCE_MODE_STANDALONE);
		nodeSiteA.setTopologyElementType(NodeElementTypeEnum.SITE);
		nodeFHIRBreakExternalService.getContainedElements().add(nodeSiteA);
		createFHIRBreakLDAPService(nodeSiteA);
	}

	public void createFHIRBreakLDAPService(DeploymentMapNodeElement nodeFHIRBreakSite) {
		DeploymentMapNodeElement nodeFHIRBreakService = new DeploymentMapNodeElement();
		nodeFHIRBreakService.setConcurrencyMode(ConcurrencyModeEnum.CONCURRENCY_MODE_STANDALONE);
		nodeFHIRBreakService.setElementVersion(nodeFHIRBreakSite.getElementVersion());
		nodeFHIRBreakService.setInstanceName("FHIRBreak-LDAP");
		nodeFHIRBreakService.setResilienceMode(ResilienceModeEnum.RESILIENCE_MODE_STANDALONE);
		nodeFHIRBreakService.setTopologyElementType(NodeElementTypeEnum.SERVICE);
		nodeFHIRBreakSite.getContainedElements().add(nodeFHIRBreakService);
		createFHIRBreakLDAPPlatforms(nodeFHIRBreakService);
		createFHIRBreakLDAPEndpoints(nodeFHIRBreakService);
	}

	public void createFHIRBreakLDAPEndpoints(DeploymentMapNodeElement nodeLDAPService) {
		DeploymentMapEndpointElement endpointTechoneAPIPort = new DeploymentMapEndpointElement();
		endpointTechoneAPIPort.setEndpointInstanceID("LDAPInteractEndpoint");
		endpointTechoneAPIPort.setEndpointFunctionID("LDAPInteractEndpoint");
		endpointTechoneAPIPort.setEndpointType(EndpointElementTypeEnum.API);
		endpointTechoneAPIPort.setExternalDNSEntry("___");
		endpointTechoneAPIPort.setIsServer(false);
		endpointTechoneAPIPort.setRequiresEncryption(false);
		nodeLDAPService.getEndpoints().add(endpointTechoneAPIPort);

		DeploymentMapEndpointElement endpointPetasosTopologySync = new DeploymentMapEndpointElement();
		endpointPetasosTopologySync.setEndpointInstanceID("PetasosTopologySyncSvr");
		endpointPetasosTopologySync.setEndpointFunctionID("PetasosTopologySyncSvr");
		endpointPetasosTopologySync.setEndpointType(EndpointElementTypeEnum.API);
		endpointPetasosTopologySync.setExternalDNSEntry("___");
		endpointPetasosTopologySync.setExternalPortNumber("12860");
		endpointPetasosTopologySync.setInternalPortNumber("12860");
		endpointPetasosTopologySync.setIsServer(true);
		endpointPetasosTopologySync.setRequiresEncryption(false);
		nodeLDAPService.getEndpoints().add(endpointPetasosTopologySync);

		DeploymentMapEndpointElement endpointPetasosParcelSync = new DeploymentMapEndpointElement();
		endpointPetasosParcelSync.setEndpointInstanceID("PetasosResilienceParcelSyncSvr");
		endpointPetasosParcelSync.setEndpointFunctionID("PetasosResilienceParcelSyncSvr");
		endpointPetasosParcelSync.setEndpointType(EndpointElementTypeEnum.API);
		endpointPetasosParcelSync.setExternalDNSEntry("___");
		endpointPetasosParcelSync.setExternalPortNumber("12861");
		endpointPetasosParcelSync.setInternalPortNumber("12861");
		endpointPetasosParcelSync.setIsServer(true);
		endpointPetasosParcelSync.setRequiresEncryption(false);
		nodeLDAPService.getEndpoints().add(endpointPetasosParcelSync);

		DeploymentMapEndpointElement endpointPetasosHeartbeatSync = new DeploymentMapEndpointElement();
		endpointPetasosHeartbeatSync.setEndpointInstanceID("PetasosHeartbeatSvr");
		endpointPetasosHeartbeatSync.setEndpointFunctionID("PetasosHeartbeatSvr");
		endpointPetasosHeartbeatSync.setEndpointType(EndpointElementTypeEnum.API);
		endpointPetasosHeartbeatSync.setExternalDNSEntry("___");
		endpointPetasosHeartbeatSync.setExternalPortNumber("12862");
		endpointPetasosHeartbeatSync.setInternalPortNumber("12862");
		endpointPetasosHeartbeatSync.setIsServer(true);
		endpointPetasosHeartbeatSync.setRequiresEncryption(true);
		nodeLDAPService.getEndpoints().add(endpointPetasosHeartbeatSync);

		DeploymentMapEndpointElement endpointEndReceiveCommunication = new DeploymentMapEndpointElement();
		endpointEndReceiveCommunication.setEndpointInstanceID("EdgeReceive.CommunicationEX");
		endpointEndReceiveCommunication.setEndpointFunctionID("EdgeReceive.CommunicationEX");
		endpointEndReceiveCommunication.setEndpointType(EndpointElementTypeEnum.INGRES_MESSAGING);
		endpointEndReceiveCommunication.setExternalDNSEntry("Inherited");
		endpointEndReceiveCommunication.setExternalPortNumber("12450");
		endpointEndReceiveCommunication.setInternalPortNumber("12450");
		endpointEndReceiveCommunication.setIsServer(true);
		endpointEndReceiveCommunication.setRequiresEncryption(false);
		nodeLDAPService.getEndpoints().add(endpointEndReceiveCommunication);

	}

	public void createFHIRBreakLDAPPlatforms(DeploymentMapNodeElement nodeFHIRBreakLDAPService) {
		DeploymentMapNodeElement nodePlatformA = new DeploymentMapNodeElement();
		nodePlatformA.setConcurrencyMode(ConcurrencyModeEnum.CONCURRENCY_MODE_STANDALONE);
		nodePlatformA.setElementVersion(nodeFHIRBreakLDAPService.getElementVersion());
		nodePlatformA.setInstanceName("___");
		nodePlatformA.setResilienceMode(ResilienceModeEnum.RESILIENCE_MODE_STANDALONE);
		nodePlatformA.setTopologyElementType(NodeElementTypeEnum.PLATFORM);
		nodeFHIRBreakLDAPService.getContainedElements().add(nodePlatformA);
		createFHIRBreakLDAPServiceModules(nodePlatformA);
	}
	
	private static String FHIRBREAK_LDAP_SERVICE_MODULE_VERSION = "1.0.0";

	public void createFHIRBreakLDAPServiceModules(DeploymentMapNodeElement nodeNode) {
		DeploymentMapNodeElement nodeLDAPServiceModule = new DeploymentMapNodeElement();
		nodeLDAPServiceModule.setConcurrencyMode(ConcurrencyModeEnum.CONCURRENCY_MODE_STANDALONE);
		nodeLDAPServiceModule.setElementVersion(FHIRBREAK_LDAP_SERVICE_MODULE_VERSION);
		nodeLDAPServiceModule.setInstanceName("FHIRBreak-LDAP-Reader");
		nodeLDAPServiceModule.setFunctionName("FHIRBreak-LDAP-Reader");
		nodeLDAPServiceModule.setResilienceMode(ResilienceModeEnum.RESILIENCE_MODE_STANDALONE);
		nodeLDAPServiceModule.setTopologyElementType(NodeElementTypeEnum.PROCESSING_PLANT);
		nodeNode.getContainedElements().add(nodeLDAPServiceModule);


		DeploymentMapEndpointElement endpointEndReceiveCommunication = new DeploymentMapEndpointElement();
		endpointEndReceiveCommunication.setEndpointInstanceID("Receive-Default");
		endpointEndReceiveCommunication.setEndpointFunctionID("Receive-Default");
		endpointEndReceiveCommunication.setEndpointType(EndpointElementTypeEnum.INGRES_MESSAGING);
		endpointEndReceiveCommunication.setExternalDNSEntry("Inherited");
		endpointEndReceiveCommunication.setExternalPortNumber("12450");
		endpointEndReceiveCommunication.setIsServer(true);
		endpointEndReceiveCommunication.setRequiresEncryption(false);
		nodeLDAPServiceModule.getEndpoints().add(endpointEndReceiveCommunication);
	}

	private static String LDAP_SYSTEM_VERSION = "1.0.0";

	public Set<DeploymentMapNodeElement> buildConnectedSystemSet() {
       // Build the Node hierarchy
       // 1st: The Solution Node
       DeploymentMapNodeElement solution = new DeploymentMapNodeElement();
       solution.setConcurrencyMode(ConcurrencyModeEnum.CONCURRENCY_MODE_STANDALONE);
       solution.setElementVersion(LDAP_SYSTEM_VERSION);
       solution.setInstanceName("ACT Health");
       solution.setFunctionName("ACT Health");
       solution.setResilienceMode(ResilienceModeEnum.RESILIENCE_MODE_STANDALONE);
       solution.setTopologyElementType(NodeElementTypeEnum.SOLUTION);
       // 2nd: The PegacornSubsystem Node
       DeploymentMapNodeElement subsystem = new DeploymentMapNodeElement();
       subsystem.setConcurrencyMode(ConcurrencyModeEnum.CONCURRENCY_MODE_STANDALONE);
       subsystem.setElementVersion(LDAP_SYSTEM_VERSION);
       subsystem.setInstanceName("IDAM-Services");
       subsystem.setFunctionName("IDAM-Services");
       subsystem.setResilienceMode(ResilienceModeEnum.RESILIENCE_MODE_STANDALONE);
       subsystem.setTopologyElementType(NodeElementTypeEnum.SUBSYSTEM);
       solution.getContainedElements().add(subsystem);
       // 3rd: The Service Node (this is as far as we go)!
       DeploymentMapNodeElement externalisedService = new DeploymentMapNodeElement();
       externalisedService.setConcurrencyMode(ConcurrencyModeEnum.CONCURRENCY_MODE_STANDALONE);
       externalisedService.setElementVersion(LDAP_SYSTEM_VERSION);
       externalisedService.setInstanceName("LDAP-Server");
       externalisedService.setFunctionName("LDAP-Server");
       externalisedService.setResilienceMode(ResilienceModeEnum.RESILIENCE_MODE_STANDALONE);
       externalisedService.setTopologyElementType(NodeElementTypeEnum.EXTERNALISED_SERVICE);
       subsystem.getContainedElements().add(externalisedService);
       // Now the EndpointElement (within the Topology Server)
       DeploymentMapEndpointElement endpoint = new DeploymentMapEndpointElement();
       endpoint.setEndpointInstanceID("LDAP-Service-API-Port");
       endpoint.setEndpointFunctionID("LDAP-Service-API-Port");
       endpoint.setEndpointType(EndpointElementTypeEnum.API);
       endpoint.setExternalDNSEntry("___");
       endpoint.setIsServer(true);
       endpoint.setRequiresEncryption(true);
       externalisedService.getEndpoints().add(endpoint);
       
       HashSet<DeploymentMapNodeElement> connectedSystems = new HashSet<DeploymentMapNodeElement>();
       connectedSystems.add(solution);
       return(connectedSystems);
	}
}
