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
package org.openmrs.module.metadatasharing;

import org.openmrs.module.metadatasharing.converter.BaseConverter;
import org.openmrs.module.metadatasharing.converter.ConceptMap19Converter;
import org.openmrs.module.metadatasharing.handler.MetadataHandler;
import org.openmrs.module.metadatasharing.handler.impl.ConceptMap19Handler;
import org.openmrs.module.metadatasharing.handler.impl.ConceptReferenceTerm19Handler;
import org.openmrs.module.metadatasharing.reflection.ClassUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetadataSharing19Configuration {
	
	public static boolean supportsConceptReferenceTerm() {
		return ClassUtil.loadClass("org.openmrs.ConceptReferenceTerm") != null;
	}
	
	@Bean(name = "metadatasharing.ConceptReferenceTerm19Handler")
	public MetadataHandler<?> getConceptReferenceTerm19Handler() {
		if (supportsConceptReferenceTerm()) {
			//1.9 and later
			return new ConceptReferenceTerm19Handler();
		}
		return null;
	}
	
	@Bean(name = "metadatasharing.ConceptMap19Handler")
	public MetadataHandler<?> getConceptMap19Handler() {
		if (supportsConceptReferenceTerm()) {
			return new ConceptMap19Handler();
		}
		return null;
	}
	
	@Bean(name = "metadatashaing.ConceptMap19Converter")
	public BaseConverter getConceptMap19Converter() {
		if (supportsConceptReferenceTerm()) {
			return new ConceptMap19Converter();
		}
		return null;
	}
}
