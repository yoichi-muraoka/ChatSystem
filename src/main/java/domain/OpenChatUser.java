package domain;

import java.util.UUID;

import javax.websocket.Session;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OpenChatUser {
	
	private String id;
	private Session session;
	
	public OpenChatUser(Session userSession) {
		id = UUID.randomUUID().toString();
		session = userSession;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof OpenChatUser)) return false;
		OpenChatUser user = (OpenChatUser) obj; 
		return user.getId().equals(this.id);
	}


}
