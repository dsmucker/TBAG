<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>Game Play</title>
		<style type="text/css">
			.error {
				color: red;
			}
			#bg {
				position: fixed;
				top: -50%;
				left: -50%;
				width: 200%;
				height: 200%;
			}
			#bg img {
				position: absolute;
				top: 0;
				left: 0;
				right: 0;
				bottom: 0;
				margin: auto;
				min-width: 50%;
				min-height: 50%;
			}
			.container p.gamename {
				color: #f1f1f1;
				width: 100%;
				text-align: center;
				line-height: 0.1;
				font-size: 80px;
				text-shadow: 3px 2px green;
			}
			body {
				background-color: black;
			}
			* {
				box-sizing: border-box;
			}
			.container {
				position: relative;
				margin: 0 auto;
			}
			.container .content {
				position: fixed;
				bottom: 0;
				background: rgb(0, 0, 0);
				background: rgba(0, 0, 0, 0.5);
				color: #f1f1f1;
				width: 100%;
				padding: 60px;
			}
			span.text {
				display: block;
				padding: 10px;
				text-align: left;
			}
			p {
				text-indent: 50px;
			}
		</style>
	</head>
	<body>
		<%
			if(session.getAttribute("username") == null){
				response.sendRedirect("login.jsp");
			}
		%>
		<div id="bg">
			<img src="labyrinth.jpg" alt="">
		</div>
		<div class="container">
			<p class="gamename">The Escape of the Minotaur</p>
			<div class="content">
				<c:if test="${! empty errorMessage}">
					<div class="error">${errorMessage}</div>
				</c:if>
				<form action="${pageContext.servletContext.contextPath}/gameplay" method="post">
					<table>
						<c:forEach items="${output}" var="output">
		        			<tr>
		           				<td><span class='text'>${gameplay.output}</span></td>			            
		       				</tr>
	   					</c:forEach>
						<tr>
							<td><span class='text'>Input:<input type="text" name="input" size="12" value="${gameplay.input}" /></span></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</body>
</html>