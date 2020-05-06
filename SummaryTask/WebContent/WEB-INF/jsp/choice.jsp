<!DOCTYPE html>
<html>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<c:set var="title" value="Catalog" />
</head>
<body>
	<%@ include file="/WEB-INF/jspf/header.jspf"%>
	<div class="content">
		<%@ include file="/WEB-INF/jspf/seacher.jspf"%>
		<c:if test="${empty gender }">
			<div class="gender">
				<div class="female">
					<a href="controller?command=choice&gender=female"> <span>
							<fmt:message key="choice_jsp.anchor.women" bundle="${bundle}" />
					</span>
					</a>
				</div>
				<div class="male">
					<a href="controller?command=choice&gender=male"> <span>
							<fmt:message key="choice_jsp.anchor.men" bundle="${bundle}" />
					</span>
					</a>
				</div>
			</div>
		</c:if>
		<c:if test="${empty type && not empty gender}">
			<div aria-label="breadcrumb">
				<ol class="breadcrumb">
					<li class="breadcrumb-item"><a
						href="controller?command=choice&break=gender"><c:out
								value="${gender.name}" /></a></li>
				</ol>
			</div>
			<div class="type">
				<div class="winter">
					<a href="controller?command=choice&type=winter"> <span>
							<fmt:message key="choice_jsp.anchor.winter" bundle="${bundle}" />
					</span>
					</a>
				</div>
				<div class="demi-season">
					<a href="controller?command=choice&type=demi_season"> <span>
							<fmt:message key="choice_jsp.anchor.demi" bundle="${bundle}" />
					</span>
					</a>
				</div>
				<div class="summer">
					<a href="controller?command=choice&type=summer"> <span>
							<fmt:message key="choice_jsp.anchor.summer" bundle="${bundle}" />
					</span>
					</a>
				</div>
			</div>
		</c:if>
	</div>
	<%@ include file="/WEB-INF/jspf/footer.jspf"%>
</body>
</html>