<!DOCTYPE html>
<html>
<%@include file='/WEB-INF/jspf/directive/taglib.jspf' %>
<c:set var="title" value="Checkout" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>
</head>
<body>
	<%@ include file="/WEB-INF/jspf/header.jspf"%>
	<div class="content">
		<div class="cells-cart">
			<div class="back-cart">
				<a href="controller?command=cart"><fmt:message key="checkout_jsp.anchor.back" bundle="${bundle}"/></a>
			</div>
			<div class="cart-h2">
				<h2><fmt:message key="checkout_jsp.anchor.order" bundle="${bundle}"/></h2>
			</div>
			<div>
				<table class="table">
					<thead class="thead-ligth">
						<tr>
							<th scope="col"><fmt:message key="checkout_jsp.table.name" bundle="${bundle}"/></th>
							<th scope="col"><fmt:message key="checkout_jsp.table.size" bundle="${bundle}"/></th>
							<th scope="col"><fmt:message key="checkout_jsp.table.price" bundle="${bundle}"/></th>
							<th scope="col"><fmt:message key="checkout_jsp.table.type" bundle="${bundle}"/></th>
							<th scope="col"><fmt:message key="checkout_jsp.table.gender" bundle="${bundle}"/></th>
						</tr>
					</thead>
					<tbody>
						<c:set var="total" value="0" />
						<c:set var="count" value="0" />
						<c:forEach items="${cart.cart }" var="item">
							<tr>
								<th scope="row">${item.key.name}</th>
								<td>${item.key.size}</td>
								<td>${item.key.price}</td>
								<td>${item.key.type}</td>
								<td>${item.key.gender}</td>
								<c:set var="total" value="${total + (item.key.price*item.value) }" />
								<c:set var="count" value="${count + 1 }" />
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div class="jumbotron">
					<h3 class="display-5"><fmt:message key="checkout_jsp.anchor.total" bundle="${bundle}"/></h3>
					<p>
						<c:out value="${count}" />
						<fmt:message key="checkout_jsp.anchor.amount" bundle="${bundle}"/> ${total}
					</p>
					<form method="post" action="controller">
						<p>
							<input type="text" placeholder="<fmt:message key="checkout_jsp.form.city" bundle="${bundle}"/>" name="city" required value="${city }">
							<input type="hidden" name="command" value="confirm"> <input
								type="hidden" name="total" value="${total}">
						</p>
						<c:if test="${isEmptyCity }">
							<div class="alert alert-warning" role="alert"><fmt:message key="checkout_jsp.form.empty" bundle="${bundle}"/></div>
						</c:if>
						<p class="lead">
							<button class="btn btn-success btn-lg" type="submit"><fmt:message key="checkout_jsp.form.confirm" bundle="${bundle}"/></button>
						</p>
					</form>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/jspf/footer.jspf"%>
</body>
</html>