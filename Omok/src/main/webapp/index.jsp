<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<style>
        @import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap');

        * {
            font-family: 'Noto Sans KR', sans-serif;
        }

        body {
            background-color: #EEEFF1;
        }

        div {
            margin: auto;
            width: 300px;
            background-color: white;
            border-radius: 5px;
            text-align: center;
            padding: 20px;
        }

        input {
            width: 90%;
            padding: 10px;
            box-sizing: border-box;
            border-radius: 5px;
            border: 1px solid lightgray;
        }

        #btn {
            margin: 10px;
            background: linear-gradient(to left, rgb(255, 77, 46), rgb(255, 155, 47));
            color: white;
            border: none;
        }

    </style>
</head>
<body>
	<h2 align="center">Omok Game</h2>
    <div class="text">
    <p class="title" style="color:rgb(255, 155, 47); font-size: 17px; font-weight: bold;" >HOW TO PLAY?</p>
    	<p>검은표창과 흰색표창 중에</p>
        <p><b>먼저 5개</b>를 <b>연속</b>으로</p>
        <p>바둑판위에 두는 플레이어가 승리합니다.</p>
		<form method="get" action="src/baduk.jsp">
			<input type="text" name="chatId" id="idplace" placeholder="아이디 입력" required/>
			<input type='submit' id='btn' value='Game Start'>
		</form>
	</div>
</body>
</html>