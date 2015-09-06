<%@include file="/common/taglibs.jsp"%>
<%@page import="java.util.List,
				java.util.ArrayList,
				com.transys.model.BusinessObject,
				com.transys.model.menu.*"%>
<%
    MenuTree menuTree = (MenuTree) session.getAttribute("menuTree");
	BusinessObject currentBo = (BusinessObject)session.getAttribute("curObj"); 
	String hierarchy ="";
	if (currentBo!=null) {
		hierarchy = currentBo.getObjectHierarchy();
		String[] boData = hierarchy.split("/");
		
		//List<BusinessObject> businessObjects = MenuHelper.getMenuForParent(menuTree, Long.parseLong(boData[2]));
		BusinessObject bo = new BusinessObject();
		bo.setId(1l);
		bo.setObjectName("Customer");
		bo.setAction("customer");
		bo.setUrl("customer");
		bo.setObjectHierarchy("/1/");
		bo.setHidden(0);
		List<BusinessObject> businessObjects = new ArrayList<BusinessObject>();
		businessObjects.add(bo);

		if (businessObjects!=null) {
%>

<div style="background:#eaeaea;" >
<br/>
	<ul class="arrowlistmenu">
	<c:forEach var="item" items="<%=businessObjects%>">
		<c:set var="cssClass" value="" />
		<c:if test='<%= hierarchy.contains("/"+((BusinessObject)pageContext.getAttribute("item")).getId()+"/")%>'>
			<c:set var="cssClass" value="selected"/>
		</c:if>
		<c:if  test="${item.hidden==0}">
			<li>
				<a href="${ctx}${item.action}" class="${cssClass}">${item.objectName}
				</a>
					<%
                    	BusinessObject businessObject = (BusinessObject)pageContext.getAttribute("item");
                		//List<BusinessObject> submenus = MenuHelper.getMenuForParent(menuTree, businessObject.getId());
                		BusinessObject bos = new BusinessObject();
						bos.setId(1l);
						bos.setObjectName("Add Customer");
						bos.setAction("customer");
						bos.setUrl("customer");
						bos.setObjectHierarchy("/1/");
						bos.setHidden(0);
						List<BusinessObject> submenus = new ArrayList<BusinessObject>();
						submenus.add(bos);
						if (submenus!=null && submenus.size()>0) {
                    %>
                    <ul class="categoryitems">
                    <c:set var="subSize" value="<%=submenus.size()%>"></c:set>
					<c:forEach var="subItem" items="<%=submenus%>" varStatus="varStat">
						<c:if test="${subSize eq varStat.count}">
							<c:set var="none" value="none"></c:set>
						</c:if>
						<c:if test="${subItem.hidden==0}">
							<li style="border-bottom: ${none}"><a href="${ctx}/${subItem.action}">${subItem.objectName}
							</a>
							</li>
						</c:if>
						<c:set var="none" value=""></c:set>
					</c:forEach>
					</ul>
					<%} %>
				</li>
			</c:if>
	</c:forEach>
	</ul>
</div>
<%
}}
%>