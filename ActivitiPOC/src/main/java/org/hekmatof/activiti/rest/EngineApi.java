/**
 * 
 */
package org.hekmatof.activiti.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Task;
import org.hekmatof.activiti.security.SecurityUtils;
import org.hekmatof.domain.UserTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author esa
 *
 */
@Controller
@RequestMapping("/api")
public class EngineApi {
	private static Logger logger = LoggerFactory.getLogger(EngineApi.class);

	@Autowired
	private ProcessEngine engine;

	@RequestMapping("process/list")
	public @ResponseBody List<org.hekmatof.domain.Process> processList() {
		return engine.getRepositoryService().createProcessDefinitionQuery()
				.list().stream()
				.map(process -> new org.hekmatof.domain.Process(process))
				.collect(Collectors.toList());
	}

	@RequestMapping("/startProcess/{processKey}")
	public ResponseEntity<String> startProcess(@PathVariable String processKey) {
		logger.info("start process by processKey: " + processKey);
		try {
			engine.getRuntimeService().startProcessInstanceByKey(processKey);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (ActivitiObjectNotFoundException e) {
			logger.warn("process with processKey " + processKey
					+ " does not exist");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/taskform/{taskId}")
	public String getTaskForm(@PathVariable String taskId) {
		Task task = engine.getTaskService().createTaskQuery().taskId(taskId)
				.singleResult();
		if (task != null && !task.getFormKey().isEmpty())
			return task.getFormKey();
		return "error";
	}

	@RequestMapping("/tasks")
	public @ResponseBody List<UserTask> getAssignedUserTasks() {
		logger.info("get current user assignee tasks");
		return (List<UserTask>) engine.getTaskService().createTaskQuery()
				.taskAssignee(SecurityUtils.getCurrentLogin()).list().stream()
				.map(task -> new UserTask((TaskEntity) task))
				.collect(Collectors.toList());
	}
}
