package com.acgist.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.acgist.core.config.RabbitConfig;
import com.acgist.core.gateway.GatewaySession;
import com.acgist.core.gateway.request.GatewayRequest;
import com.acgist.core.gateway.response.GatewayResponse;
import com.acgist.data.pojo.entity.GatewayEntity;
import com.acgist.data.pojo.entity.GatewayEntity.Status;
import com.acgist.data.pojo.entity.PermissionEntity;

/**
 * <p>拦截器 - 更新网关信息</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Component
public class GatewaySaveInteceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		final GatewaySession gatewaySession = GatewaySession.getInstance(this.context);
		final PermissionEntity permission = gatewaySession.getPermission();
		if(gatewaySession.done() && permission.getSave()) {
			final GatewayRequest gatewayRequest = gatewaySession.getRequest();
			final GatewayResponse gatewayResponse = gatewaySession.getResponse();
			final Status status = permission.getNotify() ? Status.ANSWER : Status.FINISH;
			final GatewayEntity entity = new GatewayEntity();
			entity.setCode(gatewayResponse.getCode());
			entity.setStatus(status);
			entity.setRequest(gatewayRequest.toString());
			entity.setQueryId(gatewaySession.getQueryId());
			entity.setMessage(gatewayResponse.getMessage());
			entity.setResponse(gatewayResponse.toString());
			entity.setUsername(gatewayRequest.getUsername());
			this.rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_GATEWAY, RabbitConfig.QUEUE_GATEWAY_SAVE, entity);
		}
	}
	
}
