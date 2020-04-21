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
				<h2>
					<fmt:message key="header_jspf.nav.cart" bundle="${bundle}" />
				</h2>
			</div>
			<div class="cart">
				<div>
					<table class="table">
						<thead class="thead-ligth">
							<tr>
								<th scope="col" class="table_Image"><fmt:message
										key="catalog_jsp.table.image" bundle="${bundle}" /></th>
								<th scope="col"><fmt:message key="cart_jsp.table.name"
										bundle="${bundle}" /></th>
								<th scope="col"><fmt:message key="cart_jsp.table.size"
										bundle="${bundle}" /></th>
								<th scope="col"><fmt:message key="cart_jsp.table.price"
										bundle="${bundle}" /></th>
								<th scope="col"><fmt:message key="catalog_jsp.insert.quantity"
										bundle="${bundle}" /></th>
								<th scope="col"><fmt:message key="cart_jsp.table.delete"
										bundle="${bundle}" /></th>
							</tr>
						</thead>
						<tbody>
							<c:set var="total" value="0" />
							<c:forEach items="${cart.cart}" var="inCart">
								<tr>
									<td class="table_Image"><img src="data:image/jpg;base64,${inCart.key.image}"
										width="300" height="300"></td>
									<td class="table_Image"><c:out value="${inCart.key.name}" /></td>
									<td><c:out value="${inCart.key.size}" /></td>
									<td><c:out value="${inCart.key.price}" /></td>
									<td width=150px>
										<div>
											<c:if test="${inCart.value > 1 }">
												<a
													href="controller?command=changeQuantity&id=${inCart.key.id }&change=subtract"
													type="button" class="btn btn-outline-dark btn-qt">-</a>
											</c:if>
											<c:out value="${inCart.value}" />
											<c:if test="${inCart.value < inCart.key.quantity }">
												<a
													href="controller?command=changeQuantity&id=${inCart.key.id }&change=add"
													type="button" class="btn btn-outline-dark btn-qt">+</a>
											</c:if>
										</div>
									</td>
									<td><a
										href="controller?command=deleteFromCart&id=${inCart.key.id }"
										class="btn btn-danger"><fmt:message
												key="cart_jsp.table.delete" bundle="${bundle}" /></a></td>
									<c:set var="total"
										value="${total + (inCart.key.price * inCart.value )}" />
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="jumbotron">
					<h3 class="display-5">
						<fmt:message key="cart_jsp.table.total" bundle="${bundle}" />
						:
						<c:out value="${total}" />
					</h3>
					<p class="lead">
						<a class="btn btn-primary btn-lg"
							href="controller?command=checkout" role="button"><fmt:message
								key="cart_jsp.table.checkout" bundle="${bundle}" /></a>
					</p>
					<c:if test="${isEmptyCart }">
						<div class="alert alert-warning" role="alert">
							<fmt:message key="cart_jsp.table.emptyCart" bundle="${bundle}" />
						</div>
					</c:if>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/jspf/footer.jspf"%>
</body>
</html>