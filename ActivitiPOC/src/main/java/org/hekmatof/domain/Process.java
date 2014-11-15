/**
 * 
 */
package org.hekmatof.domain;

import org.activiti.engine.repository.ProcessDefinition;

/**
 * @author esa
 *
 */
public class Process {

	public Process() {
	}

	public Process(ProcessDefinition def) {
		this.id = def.getId();
		this.key = def.getKey();
		this.version = def.getVersion();
		this.category = def.getCategory();
		this.deploymentId = def.getDeploymentId();
		this.resourceName = def.getResourceName();
		this.tenantId = def.getTenantId();
		this.hasStartFormKey = def.hasStartFormKey();
	}

	String id;
	String key;
	int version;
	String category;
	String deploymentId;
	String resourceName;
	String tenantId;
	// Map<String, TaskDefinition> taskDefinitions;
	boolean hasStartFormKey;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public boolean isHasStartFormKey() {
		return hasStartFormKey;
	}

	public void setHasStartFormKey(boolean hasStartFormKey) {
		this.hasStartFormKey = hasStartFormKey;
	}

}
