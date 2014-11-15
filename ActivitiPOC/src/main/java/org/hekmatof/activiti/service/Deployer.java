/**
 * 
 */
package org.hekmatof.activiti.service;

import javax.annotation.PostConstruct;

import org.activiti.engine.ProcessEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author esa
 *
 */
@Service
public class Deployer {

	private static Logger logger = LoggerFactory.getLogger(Deployer.class);

	@Autowired
	private ProcessEngine engine;

	@PostConstruct
	public void deployDefaultProcess() {
		logger.debug("deploy default process");
		engine.getRepositoryService().createDeployment()
				.addClasspathResource("SampleProcess.bpmn").deploy();
	}
}
