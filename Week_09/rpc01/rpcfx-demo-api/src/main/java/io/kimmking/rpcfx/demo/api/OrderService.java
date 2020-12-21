package io.kimmking.rpcfx.demo.api;

import io.kimmking.rpcfx.demo.api.annotation.ServiceScanner;


public interface OrderService {

    Order findOrderById(int id);

}
