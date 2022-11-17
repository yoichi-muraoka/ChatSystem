<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Open Chat</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
	<div class="wrap">
		<h1 class="logo">Open Chat!</h1>
		<ul id="messages"></ul>
		<div class="footer">
			<div class="footer-inner">
				<form id="form" method="post">
					<input id="message" class="col-9" type="text">
					<input id="send" class="btn btn-primary" type="submit" value="POST" />
				</form>
			</div>
		</div>
	</div>
	
	<div id="webSocketEndpoint" style="display: none;">
		ws://localhost:8080<%= request.getContextPath() %>/service/openchat
	</div>
	
	<script>
		// Web Socketの生成
		const webSocket = new WebSocket(document.getElementById('webSocketEndpoint').innerText);

		//接続成功時
		webSocket.onopen = () => {
			console.log('connected!');
			//サーバーからのメッセージを受信時した時の処理
			webSocket.onmessage = (event) => {
				const messageElement = document.createElement('li');
				messageElement.innerText = event.data;
				document.getElementById('messages').appendChild(messageElement);
			}
		};

		//接続エラー時
		webSocket.onerror = () => {
			console.log('connection error!');
		};

		//投稿ボタン押下時サーバーにメッセージを送信
		document.getElementById('form').addEventListener('submit', (event) => {
			event.preventDefault();
			const messageInput = document.getElementById('message');
			webSocket.send(messageInput.value);
			messageInput.value = '';
		});
	</script>
</body>
</html>