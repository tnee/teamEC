<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="robots" content="noindex, nofollow" />
<link rel="stylesheet" href="./css/i1810a.css">
<title>ログイン画面</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	<div class="main-contents">
		<div id="page-name">
			<h1>ログイン画面</h1>
		</div>
		<s:form action="LoginAction" id="login-form">

			<div class="error-message-box">
				<div class="error">
					<div class="error-message">
						<div class="red">
							<p>
								<s:iterator value="#session.loginErrorMessageList"><br>
									<s:property />
								</s:iterator>
							</p>
						</div>
					</div>
				</div>

				<!-- エラーメッセージ -->

				<s:if test="!#session.loginIdMessage.isEmpty()">
					<div class="error">
						<div class="error-message">
							<div class="red">
								<p>
									<s:iterator value="#session.loginIdMessage"><br>
										<s:property />
									</s:iterator>
								</p>
							</div>
						</div>
					</div>
				</s:if>

				<s:if test="!#session.loginPassMessage.isEmpty()">
					<div class="error">
						<div class="error-message">
							<div class="red">
								<p>
									<s:iterator value="#session.loginPassMessage"><br>
										<s:property />
									</s:iterator>
								</p>
							</div>
						</div>
					</div>
				</s:if>
			</div>
			<!-- statusが1のとき（ID記憶のチェックがついていた場合） -->
			<div class="contents">
				<div class="vertical-list-table">
					<s:if test="#session.status == 1">
						<h3>ログインID</h3>
						<s:textfield name="loginUserId" class="txt"
							value="%{#session.loginUserId}"></s:textfield>
						<br>
					</s:if>

					<s:else>
						<h3 style="display:inline;">ログインID</h3>
						<s:textfield name="loginUserId" class="txt" value="%{loginUserId}" ></s:textfield>
						<br><br>
					</s:else>
					<h3 style="display:inline;">パスワード</h3>
					<div class="pass-set">
						<a onclick="goResetPasswordAction()" href="#" tabindex="-1" >パスワードの変更</a>
					</div>
					<br>
					<s:password name="loginPassword" class="txt" autocomplete="new-password"></s:password>
					<br>
					<s:checkbox name="status" value="#session.status"></s:checkbox>
					<label>ログインID保存</label> <br> <br>
					<s:submit value="LOGIN" class="submit-btn-login" />
					<br>
				</div>
			</div>
			<br>
			<div class="new-person">
				<h3>新しいお客様ですか？</h3>
			</div>
			<br>
			<div class="submit-btn-box-all">
				<s:submit onclick="goCreateUserAction()" value="新規ユーザー登録"
					class="submit-btn2" />
			</div>
			<br>

		</s:form>
		<!-- javascript -->
		<script>
			function goResetPasswordAction() {
				location.href = "ResetPasswordAction";
			}
			function goCreateUserAction() {
				document.getElementById("login-form").action = "CreateUserAction";
			}
		</script>
	</div>
	<s:include value="footer.jsp" />
</body>
</html>