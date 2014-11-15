/**
 * 
 */
package org.hekmatof.activiti.editor;

import java.io.InputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.Model;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author esa
 *
 */
@RestController
@RequestMapping("/service")
public class EditorResource implements ModelDataJsonConstants {

	private static Logger logger = LoggerFactory
			.getLogger(EditorResource.class);

	@Autowired
	private ProcessEngine engine;

	@Autowired
	private ObjectMapper objectMapper;

	@RequestMapping(value = "/editor", method = RequestMethod.GET, produces = "application/xhtml+xml")
	public @ResponseBody String getEditorPage() {
		InputStream editorStream = this.getClass().getClassLoader()
				.getResourceAsStream("editor.html");
		try {
			return IOUtils.toString(editorStream);
		} catch (Exception e) {
			logger.error("Error while loading editor page", e);
			return null;
		}
	}

	@RequestMapping(value = "/editor/plugins", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody String getPlugins() {
		InputStream pluginStream = this.getClass().getClassLoader()
				.getResourceAsStream("plugins.xml");
		try {
			return IOUtils.toString(pluginStream);
		} catch (Exception e) {
			logger.error("Error while loading plugins", e);
			return null;
		}
	}

	@RequestMapping(value = "/editor/stencilset", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getStencilset() {
		InputStream stencilsetStream = this.getClass().getClassLoader()
				.getResourceAsStream("stencilset.json");
		try {
			return IOUtils.toString(stencilsetStream);
		} catch (Exception e) {
			logger.error("Error while loading stencil set", e);
			return null;
		}
	}

	@RequestMapping(value = "/model/{modelId}/json", method = RequestMethod.GET, produces = "application/json")
	public ObjectNode getEditorJson(@PathVariable String modelId) {
		ObjectNode modelNode = null;

		Model model = engine.getRepositoryService().getModel(modelId);

		if (model != null) {
			try {
				if (StringUtils.isNotEmpty(model.getMetaInfo())) {
					modelNode = (ObjectNode) objectMapper.readTree(model
							.getMetaInfo());
				} else {
					modelNode = objectMapper.createObjectNode();
					modelNode.put(MODEL_NAME, model.getName());
				}
				modelNode.put(MODEL_ID, model.getId());
				ObjectNode editorJsonNode = (ObjectNode) objectMapper
						.readTree(new String(engine.getRepositoryService()
								.getModelEditorSource(model.getId()), "utf-8"));
				modelNode.put("model", editorJsonNode);

			} catch (Exception e) {
				logger.error("Error creating model JSON", e);
				logger.error("Error creating model JSON", e);
			}
		}
		return modelNode;
	}
}
