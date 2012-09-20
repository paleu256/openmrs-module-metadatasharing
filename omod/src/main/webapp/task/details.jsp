<%@ include file="../template/localInclude.jsp"%>
<openmrs:require privilege="Share Metadata" otherwise="/login.htm"
	redirect="/module/metadatasharing/task/manageTasks.form" />
<%@ include file="../template/localHeader.jsp"%>

<c:if test="${!task.completed}">
<script type="text/javascript">
window.setTimeout(function() {
	location.reload();
}, 5000);
</script>
</c:if>

<h3>
	<spring:message code="metadatasharing.task.manageTasks" />
</h3>

<p>
	<spring:message code="metadatasharing.task.taskDetails" />
</p>
<p>
	<c:choose>
		<c:when test="${!task.completed}">
			<img src="${pageContext.request.contextPath}/moduleResources/metadatasharing/images/loading.gif" />
			<spring:message code="metadatasharing.task.running" />
		</c:when>
		<c:when test="${empty task.errors}">
			<b></b><spring:message code="metadatasharing.task.completed" /><br />
			Download task report <a href="download.form?uuid=${task.uuid}">here</a></b>
		</c:when>
		<c:otherwise>
			<b><span class="errors"><spring:message
					code="metadatasharing.task.failed" /></span><br />
			Download task report <a href="download.form?uuid=${task.uuid}">here</a></b>
		</c:otherwise>
	</c:choose>
	<c:if test="${!empty task.errors}">
	(errors: ${fn:length(task.errors)})
</c:if>
</p>
<p>
	<c:forEach var="log" items="${task.logs}">
		<em>${log.date} - ${log.message}<br /></em>
		<c:if test="${!empty log.exception}">
			<pre>${log.exception.fullStackTrace}</pre><br/>
		</c:if>
	</c:forEach>
</p>

<%@ include file="/WEB-INF/template/footer.jsp"%>
