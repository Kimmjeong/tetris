package com.tetris.vo;

public class TetrisVo {
	private String nickName;
	private Long score;
	private String regDate;
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Long getScore() {
		return score;
	}
	public void setScore(Long score) {
		this.score = score;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	@Override
	public String toString() {
		return "TetrisVo [nickName=" + nickName + ", score=" + score + ", regDate=" + regDate + "]";
	}
	
	
}
