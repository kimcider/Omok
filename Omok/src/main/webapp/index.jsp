<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<script>
		function chatWinOpen() {
			var id = document.getElementById("chatId");
			if (id.value == "") {
				alert("아이디를 입력 후 채팅창을 열어주세요.");
				id.focus();
				return;
			}
			window.open("ChatWindow.jsp?chatId=" + id.value, "",
					"width=320,height=400");
			id.value = "";
		}
	</script>
	<h2>웹소켓 채팅 - 아이디 적용해서 채팅창 띄워주기</h2>
	<form method="get" action="src/baduk.jsp">
		아이디: <input type="text" name="chatId" required/>
		<input type='submit' value='채팅 참여'>
	</form>
</body>
</html>
