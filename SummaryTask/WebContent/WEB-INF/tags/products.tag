<%@include file='/WEB-INF/jspf/directive/taglib.jspf'%>
<%@ attribute name="product" required="true" rtexprvalue="true"
	type="ua.nure.yeshenko.SummaryTask.db.entity.Product"%>

<img alt="" src="controller?command=getImage&id=${product.id }"
	width="100" height="100"> ${product.name }<br>
