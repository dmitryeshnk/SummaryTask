<!DOCTYPE html>
<html>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<c:set var="title" value="Registration" />
<body>
	<%@ include file="/WEB-INF/jspf/header.jspf"%>
	<div class="reg">
		<div class="container">
			<div class="row">
				<div class="col-md-6 col-md-offset-3 well">
					<h3 class="text-center"><fmt:message key="reg_jsp.anchor.registration" bundle="${bundle}" /></h3>
					<form class="form" action="controller" method="post">
						<div class="col-xs-12">
							<div class="form-group">
								<input type="text" name="name" class="form-control"
									placeholder="<fmt:message key="reg_jsp.anchor.name" bundle="${bundle}"/>" required />
							</div>
						</div>
						<div class="col-xs-12">
							<div class="form-group">
								<input type="email" name="email" class="form-control"
									placeholder="<fmt:message key="reg_jsp.anchor.email" bundle="${bundle}"/>" required />
							</div>
						</div>
						<div class="col-xs-12">
							<div class="form-group">
								<input type="password" name="password" class="form-control"
									placeholder="<fmt:message key="reg_jsp.anchor.password" bundle="${bundle}"/>" required />
							</div>
						</div>
						<input type="hidden" name="command" value="register">
						<div class="under">
							<input type="submit" class="btn btn-primary" value="<fmt:message key="reg_jsp.anchor.register" bundle="${bundle}"/>">
							<div class="alert alert-ligth" role="alert">
								<fmt:message key="reg_jsp.anchor.haveAccount" /> <a href="login.jsp" class="alert-link"><fmt:message key="reg_jsp.anchor.login" bundle="${bundle}"/></a>.
							</div>
						</div>
						<c:if test="${isWrongEmail}">
							<div class="alert alert-danger" role="alert"><fmt:message key="reg_jsp.anchor.invalidEmail" bundle="${bundle}"/></div>
							<c:remove var="isWrongEmail" scope="session" />
						</c:if>
						<c:if test="${isUserAlreadyExist}">
							<div class="alert alert-danger" role="alert"><fmt:message key="reg_jsp.anchor.user" bundle="${bundle}"/></div>
							<c:remove var="isUserAlreadyExist" scope="session" />
						</c:if>
						<c:if test="${isEmptyRegistration}">
							<div class="alert alert-danger" role="alert"><fmt:message key="reg_jsp.anchor.empty" bundle="${bundle}"/></div>
							<c:remove var="isEmptyRegistration" scope="session" />
						</c:if>
					</form>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/jspf/footer.jspf"%>
</body>
</html>