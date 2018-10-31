<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="robots" content="noindex, nofollow" />
<link rel="stylesheet" href="./css/i1810a.css">
<title>決済確認画面</title>

</head>
<body>
	<jsp:include page="header.jsp" />
	<div class="main-contents">
		<div id="page-name">
			<h1>決済確認画面</h1>
		</div>
		<div class="info"></div>
		<s:form id="form" action="SettlementCompleteAction">
		<s:token/>
			<s:iterator value="#session.destinationInfoDtoList" status="st">
				<h3>
				<s:if test="#st.index == 0">
					<input type="radio" name="id" checked="checked"
						value="<s:property value='id'/>" />
				</s:if>
				<s:else>
					<input type="radio" name="id" value="<s:property value='id'/>" />
				</s:else>


					<s:label value="お届け先住所" />
				</h3>

				<div class="destination-box">
					<h4>
						<s:label value="名前" />
						:
						<s:property value="familyName" />
						<span></span>
						<s:property value="firstName" />
					</h4>

					<h4>
						<s:label value="ふりがな" />
						:
						<s:property value="familyNameKana" />
						<span> </span>
						<s:property value="firstNameKana" />
					</h4>

					<h4>
						<s:label value="住所" />
						:
						<s:property value="userAddress" />
					</h4>

					<h4>
						<s:label value="電話番号" />
						:
						<s:property value="telNumber" />
					</h4>

					<h4>
						<s:label value="メールアドレス" />
						:
						<s:property value="email" />
					</h4>
				</div>
			</s:iterator>

			<br>

			<div class="submit-btn-box-all">
				<s:if test="#session.destinationInfoDtoList!=null">

					<div id="contents-btn-set">
						<s:submit value="決済" class="submit-btn-login" />
					</div>
				</s:if>

			</div>
		</s:form>
		<br>
		<div class="submit-btn-box-all">
			<div id="contents-btn-set">
				<s:form action="CreateDestinationAction">
					<s:submit value="新規登録" class="submit-btn-login" />
				</s:form>

				<s:hidden name="logined" value="%{logined}" />
				<s:hidden name="logined" value="%{#session.logi]
				ned}" />
				<s:hidden name="userId" value="%{userId}" />
				<s:hidden name="#session.userId" value="%{#session.userId}" />
			</div>
		</div>
	</div>
	<br>
	<s:include value="footer.jsp" />
</body>
</html>