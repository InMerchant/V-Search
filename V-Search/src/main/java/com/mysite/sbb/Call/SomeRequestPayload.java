package com.mysite.sbb.Call;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SomeRequestPayload {
	@JsonProperty("title")
	private String title;

	// Getter와 Setter
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
