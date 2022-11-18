package service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import domain.OpenChatMessage;
import domain.OpenChatUser;

/**
 * WebSocketのサーバー
 */
@ServerEndpoint("/service/openchat")
public class OpenChatEndPoint {
	
	// JSON対応
	ObjectMapper jsonMapper = new ObjectMapper();
	
	// 日付用
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

	// 接続中のユーザーリスト
	public static Set<OpenChatUser> users = new HashSet<>();

	// クライアントとの接続開始時
	@OnOpen
	public void handleOpen(Session userSession) throws IOException {
		// ユーザーの追加
		users.add(new OpenChatUser(userSession));
		
		// メッセージの作成
		OpenChatMessage message = new OpenChatMessage("welcome", "system", String.valueOf(users.size()), sdf.format(new Date()));
		
		// 全ユーザーにメッセージを送信
		sendMessageToAll(message);
	}

	// ブラウザからメッセージを受信した際の処理
	@OnMessage
	public void handleMessage(String message, Session userSession) throws IOException {
		System.out.println("message by: " + userSession);
		String messangerUserId = findUserByUserSession(userSession).getId();
		for (OpenChatUser user : users) {
			user.getSession().getBasicRemote().sendText("[" + messangerUserId + "] " + message);
		}
	}

	// クライアントとの接続終了時
	@OnClose
	public void handleClose(Session userSession) throws IOException {
		System.out.println("left: " + userSession);
		users.remove(findUserByUserSession(userSession));
		for (OpenChatUser user : users) {
			user.getSession().getBasicRemote().sendText("Bye! Current Users: " + users.size());
		}
	}

	// エラー発生時
	@OnError
	public void handleError(Throwable t) {
		t.printStackTrace();
	}
	
	// セッションからユーザーを検索
	private OpenChatUser findUserByUserSession(Session userSession) {
		Optional<OpenChatUser> user = users.stream().filter(u-> u.getSession().equals(userSession)).findFirst();
		return user.orElse(null);
	}
	
	// 全ユーザーにメッセージを送信
	private void sendMessageToAll(OpenChatMessage message) throws IOException {
		String jsonMessage = jsonMapper.writeValueAsString(message);
		for (OpenChatUser user : users) {
			user.getSession().getBasicRemote().sendText(jsonMessage);
		}
	}

}