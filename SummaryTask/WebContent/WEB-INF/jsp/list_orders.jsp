<!DOCTYPE html>
<html>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<c:set var="title" value="All Orders" />
</head>
<body>
	<%@ include file="/WEB-INF/jspf/header.jspf"%>
	<div class="content">
		<div class="cells">
			<table class="table">
				<thead class="thead-ligth">
					<tr>
						<th scope="col">ID</th>
						<th scope="col"><fmt:message key="list_orders_jsp.table.cost" bundle="${bundle}"/></th>
						<c:if test="${userRole.name == 'admin' }">
							<th scope="col"><fmt:message key="list_orders_jsp.table.userId" bundle="${bundle}"/></th>
						</c:if>
						<th scope="col"><fmt:message key="list_orders_jsp.table.status" bundle="${bundle}"/></th>
						<th scope="col"><fmt:message key="list_orders_jsp.table.products" bundle="${bundle}"/></th>
						<c:if test="${userRole.name == 'admin' }">
							<th scope="col"></th>
							<th scope="col"></th>
						</c:if>
					</tr>
				</thead>
				<tbody>
					<c:if test="${isOrdersEmpty }">
						<div class="alert alert-warning" role="alert"><fmt:message key="list_orders_jsp.table.empty" bundle="${bundle}"/></div>
					</c:if>
					<c:forEach items="${orders}" var="order">
						<tr>
							<td><fmt:formatNumber pattern="000" value="${order.id }"/></td>
							<td>${order.cost }</td>
							<c:if test="${userRole.name == 'admin' }">
								<td>${order.user_id }</td>
							</c:if>
							<td>${order.status.name }</td>
							<td>${order.productsId }</td>
							<c:if test="${userRole.name == 'admin' }">
								<td><a href="controller?command=orderStatus&status=paid&id=${order.id }" type="button" class="btn btn-success"><fmt:message key="list_orders_jsp.table.paid" bundle="${bundle}"/></a></td>
								<td><a href="controller?command=orderStatus&status=cancel&id=${order.id }" type="button" class="btn btn-danger"><fmt:message key="list_orders_jsp.table.cancel" bundle="${bundle}"/></a></td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<%@ include file="/WEB-INF/jspf/footer.jspf"%>
</body>
</html>