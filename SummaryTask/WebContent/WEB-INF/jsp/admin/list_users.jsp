<!DOCTYPE html>
<html>
<%@include file='/WEB-INF/jspf/directive/taglib.jspf' %>
<c:set var="title" value="Users" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>
</head>
<body>
	<%@ include file="/WEB-INF/jspf/header.jspf"%>
	<div class="cells-users">
		<div class="cart-h2">
			<h2>
				<fmt:message key="list_users_jsp.anchor.users" bundle="${bundle}" />
			</h2>
		</div>
		<div class="cart-users">
			<table class="table">
				<thead class="thead-ligth">
					<tr>
						<th scope="col">ID</th>
						<th scope="col"><fmt:message key="list_users_jsp.table.name"
								bundle="${bundle}" /></th>
						<th scope="col"><fmt:message key="list_users_jsp.table.email"
								bundle="${bundle}" /></th>
						<th scope="col"><fmt:message key="list_users_jsp.table.city"
								bundle="${bundle}" /></th>
						<th scope="col"></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${whiteList}" var="users">
						<tr>
							<td scope="row">${users.id }</td>
							<td scope="row">${users.name }</td>
							<td scope="row">${users.email }</td>
							<td scope="row">${users.city }</td>
							<td><c:if test="${users.id != user.id }">
									<a class="btn btn-danger"
										href="controller?command=changeRole&id=${users.id}&block=true">
										<fmt:message key="list_users_jsp.table.block"
											bundle="${bundle}" />
									</a>
								</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="cart-h2">
			<h3>
				<fmt:message key="list_users_jsp.table.black" bundle="${bundle}" />
			</h3>
		</div>
		<div class="cart-users">
			<table class="table">
				<tbody>
					<c:forEach items="${blackList}" var="user">
						<tr>
							<td scope="row">${user.name }</td>
							<td scope="row">${user.email }</td>
							<td scope="row">${user.city }</td>
							<td><a class="btn btn-success"
								href="controller?command=changeRole&id=${user.id }&block=false">
									<fmt:message key="list_users_jsp.table.unlock"
										bundle="${bundle}" />
							</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<%@ include file="/WEB-INF/jspf/footer.jspf"%>
</body>
</html>