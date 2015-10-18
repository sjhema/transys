<%@include file="/common/taglibs.jsp"%>

<br />
<h4 style="margin-top: -15px; !important">Add/Edit Additional Fee</h4>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post" id="typeForm">
	<form:hidden path="id" id="id" />
	<table id="form-table" class="table">
	
		<tr>
			<td class="form-left"><transys:label code="Description" /></td>
			<td align="${left}"><form:input path="description" cssClass="flat" />
				<br>
			<form:errors path="description" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Fee" /></td>
			<td align="${left}"><form:input path="fee" cssClass="flat" />
				<br>
			<form:errors path="fee" cssClass="errorMessage" /></td>
		</tr>
			<tr>
			<td align="${left}" class="form-left"><transys:label code="Effective Date From" /></td>
			<td align="${left}">
			<form:input path="effectiveStartDate" class="flat" id="datepicker7" name="effectiveStartDate" /></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Effective Date To" /></td>
			<td align="${left}">
			<form:input path="effectiveEndDate" class="flat" id="datepicker8" name="effectiveEndDate"  /></td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10 class="danger" style="font-size: 13px;font-weight: bold;color: white;">Notes/Comments</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10>
				<form:textarea row="5" path="comments" cssClass="flat" id="additionalFeeComments" style="width:100%; height:150%;"/>
				<br><form:errors path="comments" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2"><input type="submit" id="create" onclick="return validate()" value="<transys:label code="Save"/>"
				class="flat btn btn-primary btn-sm" /> <input type="button" id="cancelBtn" value="<transys:label code="Cancel"/>"
				class="flat btn btn-primary btn-sm" onClick="location.href='main.do'" /></td>
		</tr>
	</table>
</form:form>