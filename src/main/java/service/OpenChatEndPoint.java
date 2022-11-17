package service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * WebSocketのサーバー
 */
@ServerEndpoint("/service/openchat")
public class OpenChatEndPoint {

	// 接続中のユーザーリスト
	public static Set<Session> userSessions = new HashSet<>();

	// クライアントとの接続開始時
	@OnOpen
	public void handleOpen(Session userSession) {
		System.out.println("joined: " + userSession);
		userSessions.add(userSession);
	}

	// ブラウザからメッセージを受信した際の処理
	@OnMessage
	public void handleMessage(String message, Session userSession) throws IOException {
		System.out.println("message by: " + userSession);
		for (Session us : userSessions) {
			us.getBasicRemote().sendText(message);
		}
	}

	// クライアントとの接続終了時
	@OnClose
	public void handleClose(Session userSession) {
		System.out.println("left: " + userSession);
		userSessions.remove(userSession);
	}

	// エラー発生時
	@OnError
	public void handleError(Throwable t) {
		t.printStackTrace();
	}

}