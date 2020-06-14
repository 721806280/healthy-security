package com.healthy.security.core.social.view;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.healthy.security.core.properties.SecurityConstants;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 访问/connect后的跳转视图
 * 查看第三方账号绑定信息视图
 */
@Component("connect/status")
public class HealthyConnectionStatusView extends AbstractView {
    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        // org.springframework.social.connect.web.ConnectController.connectionStatus()
        Map<String, List<Connection<?>>> connections = (Map<String, List<Connection<?>>>) map.get("connectionMap");

        Map<String, Boolean> result = new HashMap<>();
        for (String key : connections.keySet()) {
            result.put(key, CollUtil.isNotEmpty(connections.get(key)));
        }

        httpServletResponse.setContentType(SecurityConstants.APPLICATION_JSON_UTF8_VALUE);
        httpServletResponse.getWriter().write(JSONUtil.toJsonStr(result));
    }
}
