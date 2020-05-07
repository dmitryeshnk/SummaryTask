<!DOCTYPE html>
<html>
<%@include file='/WEB-INF/jspf/directive/taglib.jspf' %>
<c:set var="title" value="Login" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
	<%@ include file="/WEB-INF/jspf/header.jspf"%>

	<div class="reg">
		<div class="container">
			<div class="row">
				<div class="col-md-6 col-md-offset-3 well">

					<%
						Object errorMessage = request.getAttribute("errorMessage");
					if (errorMessage != null) {
						out.println("<div class=\"alert alert-danger\" role=\"alert\">");
						out.println(errorMessage);
						out.println("</div>");
					}
					%>

					<h3 class="text-center">
						<fmt:message key="login_jsp.anchor.login" bundle="${bundle}" />
					</h3>
					<form class="form" action="controller" method="post">
						<div class="col-xs-12">
							<div class="form-group">
								<input name="email" type="email" class="form-control"
									placeholder="<fmt:message key="login_jsp.anchor.email" bundle="${bundle}" />" required />
							</div>
						</div>
						<div class="col-xs-12">
							<div class="form-group">
								<input name="password" type="password" class="form-control"
									placeholder="<fmt:message key="login_jsp.anchor.password" bundle="${bundle}" />" required />
							</div>
						</div>
						<input type="hidden" name="command" value="login">
						<div class="under">
							<button type="submit" class="btn btn-primary"><fmt:message key="login_jsp.anchor.signIn" bundle="${bundle}" /></button>
							<div class="alert alert-ligth" role="alert">
								<fmt:message key="login_jsp.anchor.noAccount" bundle="${bundle}" />
								<a href="reg.jsp" class="alert-link"><fmt:message
										key="login_jsp.anchor.registration" bundle="${bundle}" /></a>.
							</div>
						</div>
						<c:if test="${isIncorrectUser}">
							<div class="alert alert-danger" role="alert">
								<fmt:message key="login_jsp.anchor.findUser" bundle="${bundle}" />
							</div>
							<c:remove var="isIncorrectUser" scope="session" />
						</c:if>
						<c:if test="${isInvalidEmail}">
							<div class="alert alert-danger" role="alert">
								<fmt:message key="login_jsp.anchor.invalidEmail" bundle="${bundle}" />
							</div>
							<c:remove var="isInvalidEmail" scope="session" />
						</c:if>
						<c:if test="${isEmptyLogin}">
							<div class="alert alert-danger" role="alert">
								<fmt:message key="login_jsp.anchor.emptyFields" bundle="${bundle}" />
							</div>
							<c:remove var="isEmptyLogin" scope="session" />
						</c:if>
					</form>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/jspf/footer.jspf"%>
</body>
</html>