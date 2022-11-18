package service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import domain.OpenChatUser;

/**
 * WebSocketのサーバー
 */
@ServerEndpoint("/service/openchat")
public class OpenChatEndPoint {

	// 接続中のユーザーリスト
	public static Set<OpenChatUser> users = new HashSet<>();

	// クライアントとの接続開始時
	@OnOpen
	public void handleOpen(Session userSession) throws IOException {
		System.out.println("joined: " + userSession);
		users.add(new OpenChatUser(userSession));
		for (OpenChatUser user : users) {
			user.getSession().getBasicRemote().sendText("Welcome! Current Users: " + users.size());
		}
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

}