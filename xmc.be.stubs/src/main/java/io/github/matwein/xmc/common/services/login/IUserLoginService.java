package io.github.matwein.xmc.common.services.login;

import io.github.matwein.xmc.common.stubs.login.DtoBootstrapFile;

public interface IUserLoginService {
	String login(DtoBootstrapFile dtoBootstrapFile);
}
