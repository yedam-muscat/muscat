<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>eGovFrame 공통 컴포넌트</title>
<link href="<c:url value='/css/egovframework/com/cmm/main.css' />"
	rel="stylesheet" type="text/css">
<style type="text/css">
link {
	color: #666666;
	text-decoration: none;
}

link:hover {
	color: #000000;
	text-decoration: none;
}
</style>

<link rel="preconnect" href="https://fonts.gstatic.com">
<link
	href="https://fonts.googleapis.com/css2?family=Nunito:wght@300;400;600;700;800&display=swap"
	rel="stylesheet">
<link rel="stylesheet" href="assets/css/bootstrap.css">

<link rel="stylesheet" href="assets/vendors/iconly/bold.css">

<link rel="stylesheet"
	href="assets/vendors/perfect-scrollbar/perfect-scrollbar.css">
<link rel="stylesheet"
	href="assets/vendors/bootstrap-icons/bootstrap-icons.css">
<link rel="stylesheet" href="assets/css/app.css">
<link rel="shortcut icon" href="assets/images/favicon.svg"
	type="image/x-icon">
</head>
<body>
	<div id="lnb">
		<div id="sidebar" class="active">
			<div class="sidebar-wrapper active">
				<div class="sidebar-header">
					<div class="d-flex justify-content-between">
						<div class="logo">
							<a href="index.html"><img src="assets/images/logo/logo.png"
								alt="Logo" srcset=""></a>
						</div>
						<div class="toggler">
							<a href="#" class="sidebar-hide d-xl-none d-block"><i
								class="bi bi-x bi-middle"></i></a>
						</div>
					</div>
				</div>
				<div class="sidebar-menu">
					<ul class="menu">
						<li class="sidebar-title">Menu</li>

						<li class="sidebar-item active "><a href="index.html"
							class='sidebar-link'> <i class="bi bi-grid-fill"></i> <span>Dashboard</span>
						</a></li>

						<li class="sidebar-item  has-sub"><a href="#"
							class='sidebar-link'> <i class="bi bi-stack"></i> <span>Components</span>
						</a>
							<ul class="submenu ">
								<li class="submenu-item "><a href="component-alert.html">Alert</a>
								</li>
								<li class="submenu-item "><a href="component-badge.html">Badge</a>
								</li>
								<li class="submenu-item "><a
									href="component-breadcrumb.html">Breadcrumb</a></li>
								<li class="submenu-item "><a href="component-button.html">Button</a>
								</li>
								<li class="submenu-item "><a href="component-card.html">Card</a>
								</li>
								<li class="submenu-item "><a href="component-carousel.html">Carousel</a>
								</li>
								<li class="submenu-item "><a href="component-dropdown.html">Dropdown</a>
								</li>
								<li class="submenu-item "><a
									href="component-list-group.html">List Group</a></li>
								<li class="submenu-item "><a href="component-modal.html">Modal</a>
								</li>
								<li class="submenu-item "><a href="component-navs.html">Navs</a>
								</li>
								<li class="submenu-item "><a
									href="component-pagination.html">Pagination</a></li>
								<li class="submenu-item "><a href="component-progress.html">Progress</a>
								</li>
								<li class="submenu-item "><a href="component-spinner.html">Spinner</a>
								</li>
								<li class="submenu-item "><a href="component-tooltip.html">Tooltip</a>
								</li>
							</ul></li>

						<li class="sidebar-item  has-sub"><a href="#"
							class='sidebar-link'> <i class="bi bi-collection-fill"></i> <span>Extra
									Components</span>
						</a>
							<ul class="submenu ">
								<li class="submenu-item "><a
									href="extra-component-avatar.html">Avatar</a></li>
								<li class="submenu-item "><a
									href="extra-component-sweetalert.html">Sweet Alert</a></li>
								<li class="submenu-item "><a
									href="extra-component-toastify.html">Toastify</a></li>
								<li class="submenu-item "><a
									href="extra-component-rating.html">Rating</a></li>
								<li class="submenu-item "><a
									href="extra-component-divider.html">Divider</a></li>
							</ul></li>

						<li class="sidebar-item  has-sub"><a href="#"
							class='sidebar-link'> <i class="bi bi-grid-1x2-fill"></i> <span>Layouts</span>
						</a>
							<ul class="submenu ">
								<li class="submenu-item "><a href="layout-default.html">Default
										Layout</a></li>
								<li class="submenu-item "><a
									href="layout-vertical-1-column.html">1 Column</a></li>
								<li class="submenu-item "><a
									href="layout-vertical-navbar.html">Vertical with Navbar</a></li>
								<li class="submenu-item "><a href="layout-horizontal.html">Horizontal
										Menu</a></li>
							</ul></li>

						<li class="sidebar-title">Forms &amp; Tables</li>

						<li class="sidebar-item  has-sub"><a href="#"
							class='sidebar-link'> <i class="bi bi-hexagon-fill"></i> <span>Form
									Elements</span>
						</a>
							<ul class="submenu ">
								<li class="submenu-item "><a href="form-element-input.html">Input</a>
								</li>
								<li class="submenu-item "><a
									href="form-element-input-group.html">Input Group</a></li>
								<li class="submenu-item "><a
									href="form-element-select.html">Select</a></li>
								<li class="submenu-item "><a href="form-element-radio.html">Radio</a>
								</li>
								<li class="submenu-item "><a
									href="form-element-checkbox.html">Checkbox</a></li>
								<li class="submenu-item "><a
									href="form-element-textarea.html">Textarea</a></li>
							</ul></li>

						<li class="sidebar-item  "><a href="form-layout.html"
							class='sidebar-link'> <i
								class="bi bi-file-earmark-medical-fill"></i> <span>Form
									Layout</span>
						</a></li>

						<li class="sidebar-item  has-sub"><a href="#"
							class='sidebar-link'> <i class="bi bi-pen-fill"></i> <span>Form
									Editor</span>
						</a>
							<ul class="submenu ">
								<li class="submenu-item "><a href="form-editor-quill.html">Quill</a>
								</li>
								<li class="submenu-item "><a
									href="form-editor-ckeditor.html">CKEditor</a></li>
								<li class="submenu-item "><a
									href="form-editor-summernote.html">Summernote</a></li>
								<li class="submenu-item "><a
									href="form-editor-tinymce.html">TinyMCE</a></li>
							</ul></li>

						<li class="sidebar-item  "><a href="table.html"
							class='sidebar-link'> <i class="bi bi-grid-1x2-fill"></i> <span>Table</span>
						</a></li>

						<li class="sidebar-item  "><a href="table-datatable.html"
							class='sidebar-link'> <i
								class="bi bi-file-earmark-spreadsheet-fill"></i> <span>Datatable</span>
						</a></li>

						<li class="sidebar-title">Extra UI</li>

						<li class="sidebar-item  has-sub"><a href="#"
							class='sidebar-link'> <i class="bi bi-pentagon-fill"></i> <span>Widgets</span>
						</a>
							<ul class="submenu ">
								<li class="submenu-item "><a href="ui-widgets-chatbox.html">Chatbox</a>
								</li>
								<li class="submenu-item "><a href="ui-widgets-pricing.html">Pricing</a>
								</li>
								<li class="submenu-item "><a
									href="ui-widgets-todolist.html">To-do List</a></li>
							</ul></li>

						<li class="sidebar-item  has-sub"><a href="#"
							class='sidebar-link'> <i class="bi bi-egg-fill"></i> <span>Icons</span>
						</a>
							<ul class="submenu ">
								<li class="submenu-item "><a
									href="ui-icons-bootstrap-icons.html">Bootstrap Icons </a></li>
								<li class="submenu-item "><a
									href="ui-icons-fontawesome.html">Fontawesome</a></li>
								<li class="submenu-item "><a href="ui-icons-dripicons.html">Dripicons</a>
								</li>
							</ul></li>

						<li class="sidebar-item  has-sub"><a href="#"
							class='sidebar-link'> <i class="bi bi-bar-chart-fill"></i> <span>Charts</span>
						</a>
							<ul class="submenu ">
								<li class="submenu-item "><a href="ui-chart-chartjs.html">ChartJS</a>
								</li>
								<li class="submenu-item "><a
									href="ui-chart-apexcharts.html">Apexcharts</a></li>
							</ul></li>

						<li class="sidebar-item  "><a href="ui-file-uploader.html"
							class='sidebar-link'> <i class="bi bi-cloud-arrow-up-fill"></i>
								<span>File Uploader</span>
						</a></li>

						<li class="sidebar-item  has-sub"><a href="#"
							class='sidebar-link'> <i class="bi bi-map-fill"></i> <span>Maps</span>
						</a>
							<ul class="submenu ">
								<li class="submenu-item "><a href="ui-map-google-map.html">Google
										Map</a></li>
								<li class="submenu-item "><a href="ui-map-jsvectormap.html">JS
										Vector Map</a></li>
							</ul></li>

						<li class="sidebar-title">Pages</li>

						<li class="sidebar-item  "><a href="application-email.html"
							class='sidebar-link'> <i class="bi bi-envelope-fill"></i> <span>Email
									Application</span>
						</a></li>

						<li class="sidebar-item  "><a href="application-chat.html"
							class='sidebar-link'> <i class="bi bi-chat-dots-fill"></i> <span>Chat
									Application</span>
						</a></li>

						<li class="sidebar-item  "><a href="application-gallery.html"
							class='sidebar-link'> <i class="bi bi-image-fill"></i> <span>Photo
									Gallery</span>
						</a></li>

						<li class="sidebar-item  "><a
							href="application-checkout.html" class='sidebar-link'> <i
								class="bi bi-basket-fill"></i> <span>Checkout Page</span>
						</a></li>

						<li class="sidebar-item  has-sub"><a href="#"
							class='sidebar-link'> <i class="bi bi-person-badge-fill"></i>
								<span>Authentication</span>
						</a>
							<ul class="submenu ">
								<li class="submenu-item "><a href="auth-login.html">Login</a>
								</li>
								<li class="submenu-item "><a href="auth-register.html">Register</a>
								</li>
								<li class="submenu-item "><a
									href="auth-forgot-password.html">Forgot Password</a></li>
							</ul></li>

						<li class="sidebar-item  has-sub"><a href="#"
							class='sidebar-link'> <i class="bi bi-x-octagon-fill"></i> <span>Errors</span>
						</a>
							<ul class="submenu ">
								<li class="submenu-item "><a href="error-403.html">403</a>
								</li>
								<li class="submenu-item "><a href="error-404.html">404</a>
								</li>
								<li class="submenu-item "><a href="error-500.html">500</a>
								</li>
							</ul></li>

						<li class="sidebar-title">Raise Support</li>

						<li class="sidebar-item  "><a
							href="https://zuramai.github.io/mazer/docs" class='sidebar-link'>
								<i class="bi bi-life-preserver"></i> <span>Documentation</span>
						</a></li>

						<li class="sidebar-item  "><a
							href="https://github.com/zuramai/mazer/blob/main/CONTRIBUTING.md"
							class='sidebar-link'> <i class="bi bi-puzzle"></i> <span>Contribute</span>
						</a></li>

						<li class="sidebar-item  "><a
							href="https://github.com/zuramai/mazer#donate"
							class='sidebar-link'> <i class="bi bi-cash"></i> <span>Donate</span>
						</a></li>

					</ul>
				</div>
				<button class="sidebar-toggler btn x">
					<i data-feather="x"></i>
				</button>
			</div>
		</div>
		<c:set var="isMai" value="false" />
		<c:set var="isUat" value="false" />
		<c:set var="isSec" value="false" />
		<c:set var="isSts" value="false" />
		<c:set var="isCop" value="false" />
		<c:set var="isUss" value="false" />
		<c:set var="isSym" value="false" />
		<c:set var="isSsi" value="false" />
		<c:set var="isDam" value="false" />
		<c:set var="isCom" value="false" />
		<c:set var="isExt" value="false" />
		<ul class="lnb_title">
			<c:forEach var="result" items="${resultList}" varStatus="status">

				<c:if test="${isMai == 'false' && result.gid == '0'}">
					<li><strong class="left_title_strong"><strong
							class="top_title_strong"><spring:message
									code="comCmm.mai.title" /></strong></strong>
					<!-- 포털(예제) 메인화면 --></li>
					<c:set var="isMai" value="true" />
				</c:if>
				<c:if test="${isUat == 'false' && result.gid == '10'}">
					<li><strong class="left_title_strong"><strong
							class="top_title_strong"><spring:message
									code="comCmm.uat.title" /></strong></strong>
					<!-- 사용자디렉토리/통합인증 --></li>
					<c:set var="isUat" value="true" />
				</c:if>

				<c:if test="${isSec == 'false' && result.gid == '20'}">
					<li><strong class="left_title_strong"><strong
							class="top_title_strong"><spring:message
									code="comCmm.sec.title" /></strong></strong>
					<!-- 보안 --></li>
					<c:set var="isSec" value="true" />
				</c:if>
				<c:if test="${isSts == 'false' && result.gid == '30'}">
					<li><strong class="left_title_strong"><strong
							class="top_title_strong"><spring:message
									code="comCmm.sts.title" /></strong></strong>
					<!-- 통계/리포팅 --></li>
					<c:set var="isSts" value="true" />
				</c:if>
				<c:if test="${isCop == 'false' && result.gid == '40'}">
					<li><strong class="left_title_strong"><strong
							class="top_title_strong"><spring:message
									code="comCmm.cop.title" /></strong></strong>
					<!-- 협업 --></li>
					<c:set var="isCop" value="true" />
				</c:if>
				<c:if test="${isUss == 'false' && result.gid == '50'}">
					<li><strong class="left_title_strong"><strong
							class="top_title_strong"><spring:message
									code="comCmm.uss.title" /></strong></strong>
					<!-- 사용자지원 --></li>
					<c:set var="isUss" value="true" />
				</c:if>
				<c:if test="${isSym == 'false' && result.gid == '60'}">
					<li><strong class="left_title_strong"><strong
							class="top_title_strong"><spring:message
									code="comCmm.sym.title" /></strong></strong>
					<!-- 시스템관리 --></li>
					<c:set var="isSym" value="true" />
				</c:if>
				<c:if test="${isSsi == 'false' && result.gid == '70'}">
					<li><strong class="left_title_strong"><strong
							class="top_title_strong"><spring:message
									code="comCmm.ssi.title" /></strong></strong>
					<!-- 시스템/서비스연계  --></li>
					<c:set var="isSsi" value="true" />
				</c:if>
				<c:if test="${isDam == 'false' && result.gid == '80'}">
					<li><strong class="left_title_strong"><strong
							class="top_title_strong"><spring:message
									code="comCmm.dam.title" /></strong></strong>
					<!-- 디지털 자산 관리 --></li>
					<c:set var="isDam" value="true" />
				</c:if>
				<c:if test="${isCom == 'false' && result.gid == '90'}">
					<li><strong class="left_title_strong"><strong
							class="top_title_strong"><spring:message
									code="comCmm.com.title" /></strong></strong> <!-- 요소기술 --></li>
					<c:set var="isCom" value="true" />
				</c:if>
				<c:if test="${isExt == 'false' && result.gid == '100'}">
					<li><strong class="left_title_strong"><strong
							class="top_title_strong"><spring:message
									code="comCmm.ext.title" /></strong></strong>
					<!-- 외부 추가 컴포넌트 --></li>
					<c:set var="isExt" value="true" />
				</c:if>

				<c:set var="componentMsgKey">comCmm.left.${result.order}</c:set>
				<ul class="2depth">
					<li><a
						href="${pageContext.request.contextPath}<c:out value="${result.listUrl}"/>"
						target="_content" class="link"> <c:out value="${result.order}" />.
							<spring:message code="${componentMsgKey}" />
							<!-- <c:out value="${result.name}"/> --></a></li>
				</ul>
			</c:forEach>
		</ul>

		<script
			src="assets/vendors/perfect-scrollbar/perfect-scrollbar.min.js"></script>
		<script src="assets/js/bootstrap.bundle.min.js"></script>


		<script src="assets/js/main.js"></script>
</body>
</html>
