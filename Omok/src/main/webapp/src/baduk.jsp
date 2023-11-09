<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<title>Document</title>
	
	<link rel="stylesheet" href="style2.css">
	<script>
		//var webSocket = new WebSocket("ws://192.168.0.122:8000/Omok/ChatingServer");
		var webSocket = new WebSocket("ws://localhost:8000/Omok/ChatingServer");
		var chatWindow, chatMessage, chatId;
		
		window.onload = function() {
			
		};
		// 웹소켓 서버에 연결되었을 때 실행
		webSocket.onopen = function(event) {
			webSocket.send("id ${param.chatId}");
		};
		
		// 메시지 전송
		function sendMessage(row, col) {
			webSocket.send(row + ' ' + col); // 서버로 전송
		}
		
		// 메시지를 받았을 때 실행
		webSocket.onmessage = function(event) {
			var message = event.data.split("|"); // 대화명과 메시지 분리
			var turn = message[0];
			var row = message[1];
			var col = message[2];
			
			var imgSrc = []
			imgSrc[0] = '../img/board_black.jpg';
			imgSrc[1] = '../img/board_white.jpg';
			
			document.getElementById("board_" + row + "_" + col).src = imgSrc[turn];
		};
	</script>
</head>
<body>
	<div class="board">
		<c:forEach var="row" begin="0" end="18" step="1">
			<div class="board-row">
				<c:forEach var="col" begin="0" end="18" step="1">
					<button type="button" class="trans_button" data-row="${row}"
						data-col="${col }">
						<img src="../img/board_empty.jpg" alt="placeholder" id="board_${row}_${col}">
					</button>
				</c:forEach>
			</div>
		</c:forEach>
	</div>

	<script>
		var turn = 0; // 0이 검은돌
		$('.trans_button').on(
				'click',
				function() {
					var row = $(this).data('row');
					var col = $(this).data('col');
					var temp = $(this);
					
					sendMessage(row, col);
				});

	</script>
</body>
</html>