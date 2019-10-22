package kpfu.itis.group11_801.kilin.workingClass.servlets.changeUserInfoServletPackage;

import kpfu.itis.group11_801.kilin.workingClass.Helpers;
import kpfu.itis.group11_801.kilin.workingClass.database.Image;
import kpfu.itis.group11_801.kilin.workingClass.database.User;
import kpfu.itis.group11_801.kilin.workingClass.database.services.UserService;
import kpfu.itis.group11_801.kilin.workingClass.servlets.userServletPackage.UserServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ChangeUserInfoServlet")
@MultipartConfig
public class ChangeUserInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User)request.getSession().getAttribute("user");
        String pathName = getServletContext().getRealPath("") + File.separator + "files" + File.separator + "users" + File.separator;
        String smallPath = "files" + File.separator + "users"+ File.separator;
        File dir = new File(pathName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String firstName = request.getParameter("first_name");
        String secondName = request.getParameter("second_name");
        Part photoPart = request.getPart("photo");
        BufferedInputStream fileContent = new BufferedInputStream(photoPart.getInputStream());
        if (fileContent.available() != 0) {
            String fileName = System.currentTimeMillis() + ".png";
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(pathName + fileName));
            while (fileContent.available() != 0) {
                outputStream.write(fileContent.read());
            }
            outputStream.close();
            fileContent.close();
            user.setImage(new Image(smallPath + fileName));
        }

        user.setFirstName(firstName);
        user.setSecondName(secondName);

        new UserService().update(user);
        response.sendRedirect("/WorkingClass_war_exploded/user");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect("/WorkingClass_war_exploded/main");
        } else {
            Map<String, Object> root = new HashMap<>();
            root.put("user", request.getSession().getAttribute("user"));
            Helpers.render(request, response, "set.ftl", root);
        }
    }
}
