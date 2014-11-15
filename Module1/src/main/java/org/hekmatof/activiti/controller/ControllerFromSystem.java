/**
 * 
 */
package org.hekmatof.activiti.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author esa
 *
 */
@RestController
@RequestMapping("/base")
public class ControllerFromSystem {

	@RequestMapping("/someBaseData")
	public @ResponseBody List<String> getSomeBaseData() {
		List<String> result = new ArrayList<>();
		result.add("base1");
		result.add("base2");
		return result;
	}
}
