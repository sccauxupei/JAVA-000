package io.kimmking.rpcfx.demo.api;

import io.kimmking.rpcfx.demo.api.annotation.ServiceScanner;

public interface UserService {
	
    User findById(int id);

}
