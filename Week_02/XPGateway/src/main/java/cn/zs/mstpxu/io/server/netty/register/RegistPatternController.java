package cn.zs.mstpxu.io.server.netty.register;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @aothor Master_PXu 
 * @date 时间 2020年12月9日下午9:26:01
 * @project_name 项目名 XPGateway
 * @type_name 类名 RegistPatternController
 * @function 功能 TODO
 */
@Controller
@RequestMapping("regist")
public class RegistPatternController {
	@PostMapping("/add")
	void add() {}
	@PostMapping("/delete")
	void delete() {}
	@PostMapping("/update")
	void update() {}
}
