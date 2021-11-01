package com.project.contap.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

public class AddLog {
    public static void addExLog(
            HttpServletRequest request
            ) throws IOException {
        Logger log = LogManager.getLogger("AddExeption");
        StringBuilder sb = new StringBuilder();
        sb.append("[Method : ");
        sb.append(request.getMethod());
        sb.append("] [URI : ");
        sb.append(request.getRequestURI());
        sb.append("] [User : ");
        if(request.getUserPrincipal() != null)
        {
            sb.append(request.getUserPrincipal().getName() + "]  ");
        }
        else
        {
            sb.append("Null]  ");
        }

        Enumeration params = request.getParameterNames();
        while(params.hasMoreElements()) {
            String name = (String) params.nextElement();
            sb.append(name + " : " + request.getParameter(name) + "     ");
        }
        log.error(sb);
    }

    public static void addAPIUseTime(
            HttpServletRequest request,
            Long runTime) throws IOException
    {
        Logger log = LogManager.getLogger("AddApiTime");
        StringBuilder sb = new StringBuilder();
        sb.append("[Method : ");
        sb.append(request.getMethod());
        sb.append("]  [URI : ");
        sb.append(request.getRequestURI());
        sb.append("]  [Runtime : ");
        sb.append(runTime.toString());
        sb.append("ms]");
        log.error(sb);
    }
}
