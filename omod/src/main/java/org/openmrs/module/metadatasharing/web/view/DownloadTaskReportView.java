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
package org.openmrs.module.metadatasharing.web.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.openmrs.module.metadatasharing.MetadataSharingConsts;
import org.openmrs.module.metadatasharing.task.Task;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;


/**
 *
 */
@Component(MetadataSharingConsts.MODULE_ID + ".DownloadTaskReportView")
public class DownloadTaskReportView extends AbstractView {

	/**
     * @see org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void renderMergedOutputModel(@SuppressWarnings("rawtypes") Map model, HttpServletRequest request, HttpServletResponse response)
        throws Exception {
    	
    	Task task = (Task) model.get("task");
		response.setContentType("application/zip");
		String filename = task.getPackage().getName().replace(" ", "_") + "-" + task.getPackage().getVersion() + "-report";
		response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".zip");
		response.setHeader("Pragma", "No-cache");
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		
		InputStream in = null;
		OutputStream out = null;
		try {
			in = task.getTaskReport().getSerializedTaskReport();
			out = response.getOutputStream();
			IOUtils.copy(in, out);
			in.close();
			out.close();
		}
		finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
    }
	
}
