/**
 * 
 */
package org.hekmatof.activiti.editor;

import java.io.UnsupportedEncodingException;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author esa
 *
 */
@Controller
@RequestMapping("/service")
public class NewModel implements ModelDataJsonConstants {
	private static final Logger logger = LoggerFactory
			.getLogger(NewModel.class);

	@Autowired
	private ProcessEngine engine;

	@Autowired
	private ObjectMapper objectMapper;

	@RequestMapping(value = "/newModel/{name}", method = RequestMethod.GET, produces = "application/json")
	public String newModel(@PathVariable String name) {

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode editorNode = objectMapper.createObjectNode();
		editorNode.put("id", "canvas");
		editorNode.put("resourceId", "canvas");
		ObjectNode stencilSetNode = objectMapper.createObjectNode();
		stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
		editorNode.put("stencilset", stencilSetNode);
		Model modelData = engine.getRepositoryService().newModel();

		ObjectNode modelObjectNode = objectMapper.createObjectNode();
		modelObjectNode.put(MODEL_NAME, name);
		modelObjectNode.put(MODEL_REVISION, 1);
		String description = "";
		modelObjectNode.put(MODEL_DESCRIPTION, description);
		modelData.setMetaInfo(modelObjectNode.toString());
		modelData.setName(name);

		engine.getRepositoryService().saveModel(modelData);
		try {
			engine.getRepositoryService().addModelEditorSource(
					modelData.getId(), editorNode.toString().getBytes("utf-8"));
			logger.info("new model created with id " + modelData.getId()
					+ " and name " + modelData.getName());
		} catch (UnsupportedEncodingException e) {
			logger.error("unsupported Encoding utf-8", e);
		}
		return "redirect:/service/editor?id=" + modelData.getId();
	}
}
