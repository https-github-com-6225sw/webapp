package com.csye6225.cloudwebapp.errorhandler;

import com.csye6225.cloudwebapp.dao.AssignmentDao;
import com.csye6225.cloudwebapp.dao.UserDao;
import com.csye6225.cloudwebapp.entity.Assignment;
import com.csye6225.cloudwebapp.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;
import java.util.Map;

public class UserRoleInterceptor  implements HandlerInterceptor {

    @Autowired
    private UserDao userDao;
    @Autowired
    private AssignmentDao assignmentDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Map<String, String> pathVariables = (Map<String, String>) request
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        String email = request.getRemoteUser();
        System.out.println("Email from header - " + email);
        List<User> user = userDao.findByEmail(email);
        String ass_id = pathVariables.get("assignmentId");
        System.out.println("Assignment want to delete - " + ass_id);

        //get creater id according to pathVariable
        Assignment assignment = assignmentDao.findById(Integer.valueOf(ass_id));
        int creater_id = assignment.getUser();
        System.out.println("creater Id according to pathVariable - " + creater_id);
        if (!user.isEmpty()){
            String user_id = String.valueOf(user.get(0).getId());
            System.out.println("creater Id according to header - " + user_id);
            if(creater_id == (Integer.valueOf(user_id))){
                return true;
            }else{
                response.setStatus(204);
                return false;
            }
        }
        response.setStatus(204);
        return false;
    }

}
