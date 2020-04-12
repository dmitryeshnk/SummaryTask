<!DOCTYPE html>
<html>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<c:set var="title" value="Cart" />
</head>
<body>
	<%@ include file="/WEB-INF/jspf/header.jspf"%>
	<%@ page import="java.util.List"%>
	<div class="content">
		<div class="cells-cart">
			<div class="cart-h2">
				<h2><fmt:message key="header_jspf.nav.cart" bundle="${bundle}"/></h2>
			</div>
			<div class="cart">
				<div>
					<table class="table">
						<thead class="thead-ligth">
							<tr>
								<th scope="col"><fmt:message key="cart_jsp.table.name"  bundle="${bundle}"/></th>
								<th scope="col"><fmt:message key="cart_jsp.table.size"  bundle="${bundle}"/></th>
								<th scope="col"><fmt:message key="cart_jsp.table.price"  bundle="${bundle}"/></th>
								<th scope="col"><fmt:message key="cart_jsp.table.delete"  bundle="${bundle}"/></th>
							</tr>
						</thead>
						<tbody>
							<c:set var="total" value="0" />
							<c:forEach items="${cart.cart}" var="item">
								<tr>
									<td><c:out value="${item.name}" /></td>
									<td><c:out value="${item.size}" /></td>
									<td><c:out value="${item.price}" /></td>
									<td><a
										href="controller?command=deleteFromCart&id=${item.id }"
										class="btn btn-danger"><fmt:message key="cart_jsp.table.delete"  bundle="${bundle}"/></a></td>
									<c:set var="total" value="${total + item.price }" />
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="jumbotron">
					<h3 class="display-5">
						<fmt:message key="cart_jsp.table.total"  bundle="${bundle}"/>:
						<c:out value="${total}" />
					</h3>
					<p class="lead">
						<a class="btn btn-primary btn-lg"
							href="controller?command=checkout" role="button"><fmt:message key="cart_jsp.table.checkout" bundle="${bundle}" /></a>
					</p>
					<c:if test="${isEmptyCart }">
						<div class="alert alert-warning" role="alert"><fmt:message key="cart_jsp.table.emptyCart" bundle="${bundle}" /></div>
					</c:if>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/jspf/footer.jspf"%>
</body>
</html>