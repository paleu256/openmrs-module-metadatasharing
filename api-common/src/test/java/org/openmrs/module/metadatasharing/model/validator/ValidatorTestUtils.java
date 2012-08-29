/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.metadatasharing.model.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

/**
 * The set of utils useful during testing validators
 * @see {@link Validator}
 */
public class ValidatorTestUtils {
	
	/**
	 * Determines whether errors contain an ObjectError with the specified code
	 * 
	 * @param code - error code
	 * @return true if errors contain an ObjectError with the specified code
	 */
	public static boolean errorsContain(Errors errors, String code) {
		for (Object obj : errors.getAllErrors()) {
			if (((ObjectError) obj).getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}
	
	private ValidatorTestUtils() {
	}
}
