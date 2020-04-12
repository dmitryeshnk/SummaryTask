<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<form action="controller" method="post">
	<div class="search">
		<div class="input-group mb-3">


			<input type="text" class="form-control" name="name"
				placeholder="<fmt:message key="search.anchor.search" bundle="${bundle}" />...">
			<input type="hidden" name="command" value="search">
			<div class="input-group-append">
				<button class="btn btn-outline-primary" type="submit">
					<fmt:message key="search.anchor.search" bundle="${bundle}" />
				</button>
			</div>

		</div>
	</div>
</form>