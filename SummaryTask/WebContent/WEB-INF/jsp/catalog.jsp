<!DOCTYPE html>
<html>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<c:set var="title" value="Catalog" />
</head>
<body>
	<%@ include file="/WEB-INF/jspf/header.jspf"%>
	<div class="content">
		<%@ include file="/WEB-INF/jspf/seacher.jspf"%>
		<div aria-label="breadcrumb">
			<ol class="breadcrumb">
				<li class="breadcrumb-item"><a
					href="controller?command=choice&break=gender"><fmt:message
							key="catalog_jsp.anchor.gender" bundle="${bundle}" /></a></li>
				<li class="breadcrumb-item"><a
					href="controller?command=choice&break=type"><fmt:message
							key="catalog_jsp.anchor.type" bundle="${bundle}" /></a></li>
			</ol>
		</div>
		<form class="sorter" method="post" action="controller">
			<select class="custom-select" name="select">
				<option value="" selected disabled hidden=""><fmt:message
						key="catalog_jsp.select.sortby" bundle="${bundle}" /></option>
				<option value="CompareByName"><fmt:message
						key="catalog_jsp.select.byNameAZ" bundle="${bundle}" /></option>
				<option value="CompareByNameReverse"><fmt:message
						key="catalog_jsp.select.byNameZA" bundle="${bundle}" /></option>
				<option value="CompareByPrice"><fmt:message
						key="catalog_jsp.select.priceLowHign" bundle="${bundle}" /></option>
				<option value="CompareByPriceReverse"><fmt:message
						key="catalog_jsp.select.priceHignLow" bundle="${bundle}" /></option>
				<option value="CompareByIdReverse"><fmt:message
						key="catalog_jsp.select.byNovelty" bundle="${bundle}" /></option>
				<option value="CompareBySizeReverse"><fmt:message
						key="catalog_jsp.select.largerSmaller" bundle="${bundle}" /></option>
				<option value="CompareBySize"><fmt:message
						key="catalog_jsp.select.smallerLarger" bundle="${bundle}" /></option>
			</select> <input type="hidden" name="command" value="sorter">
			<div class="interval">
				<p>
					<fmt:message key="catalog_jsp.price.price" bundle="${bundle}" />
				</p>
				<input type="number" name="from" min="1"
					placeholder="<fmt:message key="catalog_jsp.price.from" bundle="${bundle}"/>">
				<span>--</span> <input type="number" name="to" min="2"
					placeholder="<fmt:message key="catalog_jsp.price.to" bundle="${bundle}"/>">
			</div>
			<input type="submit"
				value="<fmt:message key="catalog_jsp.price.show" bundle="${bundle}"/>"
				class="btn btn-success">
		</form>
		<c:if test="${userRole.name == 'admin' }">
			<div id="Insert"
				title="<fmt:message key="catalog_jsp.insert.title" bundle="${bundle}"/>">
				<form class="form-insert" method="post" action="controller"
					enctype="multipart/form-data">
					<div class="form-group">
						<label for="name"><fmt:message
								key="catalog_jsp.insert.nameOfProduct" bundle="${bundle}" /></label> <input
							type="text" class="form-control" id="name" name="name"
							placeholder="<fmt:message key="catalog_jsp.insert.enterName" bundle="${bundle}"/>"
							required>
					</div>
					<div class="form-group">
						<label for="size"><fmt:message
								key="catalog_jsp.insert.size" bundle="${bundle}" /></label> <input
							class="form-control" id="size" type="number" min="30" max="50"
							name="size"
							placeholder="<fmt:message key="catalog_jsp.insert.size" bundle="${bundle}"/>"
							required>
					</div>
					<div class="form-group">
						<label for="price"><fmt:message
								key="catalog_jsp.insert.price" bundle="${bundle}" /></label> <input
							class="form-control" id="price" type="number" min="1"
							name="price"
							placeholder="<fmt:message key="catalog_jsp.insert.price" bundle="${bundle}"/>"
							required>
					</div>
					<div class="form-group">
						<label for="quantity"><fmt:message
								key="catalog_jsp.insert.quantity" bundle="${bundle}" /></label> <input
							class="form-control" id="quantity" type="number" min="1"
							name="quantity"
							placeholder="<fmt:message key="catalog_jsp.insert.quantity" bundle="${bundle}"/>"
							required>
					</div>
					<div class="form-group">
						<label for="image"><fmt:message
								key="catalog_jsp.insert.image" bundle="${bundle}" /></label> <input
							class="form-control" id="image" type="file" name="image"
							placeholder="<fmt:message key="catalog_jsp.insert.image" bundle="${bundle}"/>"
							required>
					</div>
					<input type="hidden" name="command" value="insertProduct">
					<button type="submit" class="btn btn-primary">
						<fmt:message key="catalog_jsp.insert.submit" bundle="${bundle}" />
					</button>
				</form>
			</div>
			<div class="insert-button">
				<input class="btn btn-primary btn-lg btn-block" type='button'
					value='<fmt:message key="catalog_jsp.insert.insert" bundle="${bundle}"/>'>
				<c:if test="${notAdd }">
					<div class="alert alert-danger" role="alert">
						<fmt:message key="catalog_jsp.insert.notAdded" bundle="${bundle}" />
					</div>
				</c:if>
			</div>
			<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
			<script src="http://code.jquery.com/ui/1.11.0/jquery-ui.js"></script>
			<link rel="stylesheet"
				href="http://code.jquery.com/ui/1.11.0/themes/smoothness/jquery-ui.css">
			<script>
				var Insert = $("#Insert").dialog({
					autoOpen : false
				});
				$("input[type=button]").click(function() {
					Insert.dialog("open");
				});
			</script>
		</c:if>
		<div class="cells">
			<table class="table">
				<thead class="thead-ligth">
					<tr>
						<c:if test="${userRole.name == 'admin' }">
							<th scope="col"><fmt:message key="catalog_jsp.table.id"
									bundle="${bundle}" /></th>
						</c:if>
						<th scope="col" class="table_Image"><fmt:message
								key="catalog_jsp.table.image" bundle="${bundle}" /></th>
						<th scope="col" class="table_Name"><fmt:message
								key="catalog_jsp.table.name" bundle="${bundle}" /></th>
						<th scope="col"><fmt:message key="catalog_jsp.table.size"
								bundle="${bundle}" /></th>
						<th scope="col"><fmt:message key="catalog_jsp.table.price"
								bundle="${bundle}" /></th>
						<c:if test="${userRole.name == 'admin' }">
							<th scope="col"><fmt:message key="catalog_jsp.table.type"
									bundle="${bundle}" /></th>
							<th scope="col"><fmt:message key="catalog_jsp.table.gender"
									bundle="${bundle}" /></th>
							<th scope="col"><fmt:message
									key="catalog_jsp.table.quantity" bundle="${bundle}" /></th>
						</c:if>
						<th scope="col"><fmt:message key="catalog_jsp.table.order"
								bundle="${bundle}" /></th>
						<th scope="col"></th>
						<c:if test="${userRole.name == 'admin' }">
							<th scope="col"></th>
						</c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${products}" var="product">
						<tr>
							<c:if test="${userRole.name == 'admin' }">
								<td><fmt:formatNumber pattern="000" value="${product.id }" /></td>
							</c:if>
							<td><img alt=""
								src="controller?command=getImage&id=${product.id }" width="300"
								height="300"></td>
							<td class="nameProduct"><c:out value="${product.name}" /></td>
							<td><c:out value="${product.size}" /></td>
							<td><c:out value="${product.price}" /></td>
							<c:if test="${userRole.name == 'admin' }">
								<td><c:out value="${product.type}" /></td>
								<td><c:out value="${product.gender}" /></td>
								<td><c:out value="${product.quantity}" /></td>
							</c:if>
							<td width=50px><c:set var="check" value="${true }" /> <c:forEach
									items="${cart.cart }" var="inCart">
									<c:if test="${inCart.key.id == product.id && check}">
										<p class="text-success" id="inCart">
											<fmt:message key="catalog_jsp.table.added" bundle="${bundle}" />
										</p>
										<c:set var="check" value="${false }" />
									</c:if>
								</c:forEach> <c:if test="${product.quantity == 0 }">
									<p class="text-muted" id="inCart">
										<fmt:message key="catalog_jsp.table.notAvailable"
											bundle="${bundle}" />
									</p>
								</c:if></td>
							<td><c:if test="${product.quantity <= 0 }">
									<button class="btn btn-success" disabled>
										<fmt:message key="catalog_jsp.table.order" bundle="${bundle}" />
									</button>
								</c:if> <c:if test="${product.quantity > 0 }">
									<button class="btn btn-success"
										onclick="window.location.href = 'controller?command=orderCart&id=${product.id}';">
										<fmt:message key="catalog_jsp.table.order" bundle="${bundle}" />
									</button>
								</c:if></td>
							<c:if test="${userRole.name == 'admin' }">
								<td>
									<div class="update-button">
										<button class="btn btn-success"
											onclick="window.location.href = 'controller?command=updateProduct&id=${product.id}';">
											<fmt:message key="catalog_jsp.table.change"
												bundle="${bundle}" />
										</button>
									</div>
								</td>
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