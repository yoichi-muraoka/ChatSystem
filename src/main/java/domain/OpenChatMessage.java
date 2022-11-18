package domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OpenChatMessage {
	
	private String type;
	private String userId;
	private String message;
	private String created;
	
	
	
}
