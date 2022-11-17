<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Open Chat</title>
</head>
<body>
	<h1>Open Chat!</h1>
	<ul id="list"></ul>
	<div>
		<input id="message" type="text">
		<input id="send" type="button" value="投稿" />
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
				document.getElementById('list').appendChild(messageElement);
			}
		};

		//接続エラー時
		webSocket.onerror = () => {
			console.log('connection error!');
		};

		//投稿ボタン押下時サーバーにメッセージを送信
		document.getElementById('send').addEventListener('click', function() {
			const messageInput = document.getElementById('message');
			webSocket.send(messageInput.value);
			messageInput.value = '';
		});
	</script>
</body>
</html>