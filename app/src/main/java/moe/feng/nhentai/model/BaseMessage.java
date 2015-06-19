package moe.feng.nhentai.model;

public class BaseMessage {

	private int code = -1;
	private Object data;

	public BaseMessage(int code, Object data) {
		this.code = code;
		this.data = data;
	}

	public BaseMessage() {

	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public <T> T getData() {
		return (T) data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
