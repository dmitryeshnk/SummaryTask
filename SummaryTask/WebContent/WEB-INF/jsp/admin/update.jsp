<!DOCTYPE html>
<html>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<c:set var="title" value="Catalog" />
</head>
<body>
	<%@ include file="/WEB-INF/jspf/header.jspf"%>
	<div class="content">
		<form class="update-form" method="post" action="controller"
			enctype="multipart/form-data">
			<div class="alert alert-primary" role="alert">
				<fmt:message key="update_jsp.form.mutableId" bundle="${bundle}" />
				<fmt:formatNumber pattern="000" value="${mutable.id }" />
			</div>
			<div class="form-group">
				<label for="inputName"><fmt:message
						key="update_jsp.form.name" bundle="${bundle}" /></label> <input
					type="text" class="form-control" id="inputName"
					value="${mutable.name}" name="name">
			</div>
			<div class="form-group">
				<label for="inputSize"><fmt:message
						key="update_jsp.form.size" bundle="${bundle}" /></label> <input
					type="number" min="30" max="50" class="form-control" id="inputSize"
					value="${mutable.size}" name="size">
			</div>
			<div class="form-group">
				<label for="inputPrice"><fmt:message
						key="update_jsp.form.price" bundle="${bundle}" /></label> <input
					type="number" min="1" class="form-control" id="inputPrice"
					value="${mutable.price}" name="price">
			</div>
			<div class="form-row">
				<div class="form-group col-md-6">
					<label for="inputType"><fmt:message
							key="update_jsp.form.type" bundle="${bundle}" /></label> <select
						id="inputType" class="form-control" name="type">
						<option value="0"><fmt:message
								key="update_jsp.form.winter" bundle="${bundle}" /></option>
						<option value="1"><fmt:message key="update_jsp.form.demi"
								bundle="${bundle}" /></option>
						<option value="2"><fmt:message
								key="update_jsp.form.summer" bundle="${bundle}" /></option>
					</select>
				</div>
				<div class="form-group col-md-6">
					<label for="inputGender"><fmt:message
							key="update_jsp.form.gender" bundle="${bundle}" /></label> <select
						id="inputGender" class="form-control" name="gender">
						<option value="0"><fmt:message key="update_jsp.form.male"
								bundle="${bundle}" /></option>
						<option value="1"><fmt:message
								key="update_jsp.form.female" bundle="${bundle}" /></option>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="inputQuantity"><fmt:message
						key="update_jsp.form.quantity" bundle="${bundle}" /></label> <input
					type="number" min="0" class="form-control" id="inputQuantity"
					value="${mutable.quantity}" name="quantity">
			</div>
			<div class="form-group">
				<label for="inputImage"><fmt:message
						key="update_jsp.form.image" bundle="${bundle}" /></label> <input
					type="file" class="form-control" id="inputImage" name="image">
			</div>
			<input type="hidden" name="command" value="updateProduct"> <input
				type="hidden" name="id" value="${mutable.id }">
			<button type="submit" class="btn btn-primary">
				<fmt:message key="update_jsp.form.change" bundle="${bundle}" />
			</button>
			<a href="controller?command=catalog"><fmt:message
					key="update_jsp.form.cancel" bundle="${bundle}" /></a>
			<a type="button" class="btn btn-danger" href="controller?command=deleteProduct&id=${mutable.id }">
				<fmt:message key="update_jsp.form.delete" bundle="${bundle}" />
			</a>
		</form>
	</div>
	<%@ include file="/WEB-INF/jspf/footer.jspf"%>
</body>
</html>