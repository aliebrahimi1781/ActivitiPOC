/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hekmatof.activiti.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Tijs Rademakers
 */
@RestController
@RequestMapping("/service")
public class ModelSaveRestResource implements ModelDataJsonConstants {

	protected static final Logger logger = LoggerFactory
			.getLogger(ModelSaveRestResource.class);

	@Autowired
	private ProcessEngine engine;

	private RepositoryService repositoryService;

	@PostConstruct
	private void init() {
		this.repositoryService = engine.getRepositoryService();
	}

	@Autowired
	private ObjectMapper objectMapper;

	@RequestMapping(value = "/model/{modelId}/save", method = RequestMethod.PUT)
	public void saveModel(@PathVariable String modelId,
			@RequestBody MultiValueMap<String, String> values) {
		try {

			Model model = repositoryService.getModel(modelId);

			ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model
					.getMetaInfo());

			modelJson.put(MODEL_NAME, values.getFirst("name"));
			modelJson.put(MODEL_DESCRIPTION, values.getFirst("description"));
			model.setMetaInfo(modelJson.toString());
			model.setName(values.getFirst("name"));

			repositoryService.saveModel(model);

			repositoryService.addModelEditorSource(model.getId(), values
					.getFirst("json_xml").getBytes("utf-8"));

			InputStream svgStream = new ByteArrayInputStream(values.getFirst(
					"svg_xml").getBytes("utf-8"));
			TranscoderInput input = new TranscoderInput(svgStream);

			PNGTranscoder transcoder = new PNGTranscoder();
			// Setup output
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			TranscoderOutput output = new TranscoderOutput(outStream);

			// Do the transformation
			transcoder.transcode(input, output);
			final byte[] result = outStream.toByteArray();
			repositoryService.addModelEditorSourceExtra(model.getId(), result);
			outStream.close();

		} catch (Exception e) {
			logger.error("Error saving model", e);
			throw new ActivitiException("Error saving model", e);
		}
	}

	@RequestMapping(value = "/model/{modelId}/deploy")
	public @ResponseBody ResponseEntity<String> deploy(
			@PathVariable String modelId) {
		logger.info("in deploy module");
		Model model = engine.getRepositoryService().getModel(modelId);
		logger.debug("model id is " + model.getId());
		try {
			repositoryService
					.createDeployment()
					.addInputStream(
							model.getName() + ".bpmn",
							new ByteArrayInputStream(
									exportModel((getBpmnModel(model)))))
					.deploy();
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (IOException e) {
			logger.error("failed to export model to BPMN XML", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/model/{modelId}/export", produces = "application/xml")
	public @ResponseBody byte[] export(@PathVariable String modelId) {
		logger.info("in export module");
		Model model = engine.getRepositoryService().getModel(modelId);
		logger.info("model id is " + model.getId());
		try {
			return exportModel(getBpmnModel(model));
		} catch (IOException e) {
			logger.error("failed to export model to BPMN XML", e);
			return null;
		}
	}

	protected BpmnModel getBpmnModel(Model modelData)
			throws JsonProcessingException, IOException {
		JsonNode editorNode = new ObjectMapper().readTree(repositoryService
				.getModelEditorSource(modelData.getId()));
		BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
		return jsonConverter.convertToBpmnModel(editorNode);

	}

	protected byte[] exportModel(BpmnModel model) {
		return new BpmnXMLConverter().convertToXML(model);
		/*
		 * ds = new DownloadStream(in, "application/xml", filename); // Need a
		 * file download POPUP ds.setParameter("Content-Disposition",
		 * "attachment; filename=" + filename);
		 */
	}
}
