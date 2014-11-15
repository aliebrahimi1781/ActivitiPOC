/**
 * 
 */
package org.hekmatof.activiti.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author esa
 *
 */
@Component
public class APIRef1 {

	private static Logger logger = LoggerFactory.getLogger(APIRef1.class);

	public void method1() {
		logger.info("Method1 from APIRef1 executed");
	}

	public void method2() {
		logger.info("Method2 from APIRef1 executed");
	}
}