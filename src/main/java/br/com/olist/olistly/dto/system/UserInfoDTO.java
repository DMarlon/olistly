package br.com.olist.olistly.dto.system;

import br.com.olist.olistly.model.system.User;

public class UserInfoDTO {
	private Long id;
	private String login;
	private String name;

	public static UserInfoDTO valueOf(User user) {
		UserInfoDTO userInfo = new UserInfoDTO();
		if (user != null) {
			userInfo.setId(user.getId());
			userInfo.setLogin(user.getLogin());
			userInfo.setName(user.getName());
		}

		return userInfo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
