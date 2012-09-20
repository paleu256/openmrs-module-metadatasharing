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
package org.openmrs.module.metadatasharing.task;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.openmrs.module.metadatasharing.MetadataSharing;
import org.openmrs.module.metadatasharing.wrapper.PackageExporter;
import org.openmrs.serialization.SerializationException;

/**
 * Helps troubleshooting.
 */
public class TaskReport implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final String ENCODING = "UTF-8";
	
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n";
	
	private final List<TaskLogEntry> logs;
	
	private InputStream metadataPackage;
	
	private String metadataBeforeMerging;
	
	private String metadataAfterMerging;
	
	public TaskReport(List<TaskLogEntry> logs) {
		this.logs = logs;
	
		metadataBeforeMerging = XML_HEADER;
		metadataAfterMerging = XML_HEADER;
	}
	
	public List<TaskLogEntry> getLogs() {
		return logs;
	}
	
	public void setMetadataPackage(InputStream metadataPackage) {
		this.metadataPackage = metadataPackage;
	}
	
	public void addMetadataBeforeMerging(List<Object> items) throws SerializationException {
		metadataBeforeMerging += MetadataSharing.getInstance().getMetadataSerializer().serialize(items);
	}
	
	public void addMetadataAfterMerging(List<Object> items) throws SerializationException {
		metadataAfterMerging += MetadataSharing.getInstance().getMetadataSerializer().serialize(items);
	}
	
	public InputStream getSerializedTaskReport() throws IOException, SerializationException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		ZipOutputStream zip = new ZipOutputStream(output);
		
		String serializedLogs = XML_HEADER;
		serializedLogs += MetadataSharing.getInstance().getMetadataSerializer().serialize(logs);
		serializedLogs += "\n</root>";
		zip.putNextEntry(new ZipEntry("logs.xml"));
		zip.write(serializedLogs.getBytes(ENCODING));
		zip.closeEntry();
		
		if (metadataPackage != null) {
			zip.putNextEntry(new ZipEntry("metadataPackage.zip"));
			IOUtils.copy(metadataPackage, zip);
			zip.closeEntry();
		}
		
		String serverDetails = XML_HEADER;
		try {
			PackageExporter exporter = MetadataSharing.getInstance().newPackageExporter();
			exporter.getPackage().setName("tmp");
			exporter.getPackage().setDescription("tmp");
			serverDetails += exporter.exportPackage().getSerializedPackage().getHeader();
		}
		catch (Exception e) {
			serverDetails += "Failed to determine server details\n" + ExceptionUtils.getFullStackTrace(e);
		}
		zip.putNextEntry(new ZipEntry("serverDetails.xml"));
		zip.write((serverDetails + "\n</root>").getBytes(ENCODING));
		zip.closeEntry();
		
		zip.putNextEntry(new ZipEntry("metadataBeforeMerging.xml"));
		zip.write((metadataBeforeMerging + "\n/<root>").getBytes(ENCODING));
		zip.closeEntry();
		
		zip.putNextEntry(new ZipEntry("metadataAfterMerging.xml"));
		zip.write((metadataAfterMerging + "\n</root>").getBytes(ENCODING));
		zip.closeEntry();
		
		zip.close();
		
		ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		return input;
	}
	
}
