package rs.raf.demo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet(name = "picked-dishes", value = "/picked-dishes")
public class AdminServlet extends HttpServlet {

        private String password;
        private Map<String, Map<String,Integer>> orders;
        private Map<String, List<String>>ordersForUser;
        public AdminServlet() {



        }

        public void init() {
           orders = HomeServlet.orders;
           ordersForUser  = HomeServlet.ordersForUser;;
        }

        public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

            PrintWriter out = response.getWriter();
            password = request.getParameter("password");

            if(password!=null && password.equals(HomeServlet.password))
            {
                out.println("<html> <body> <h1>Choose your lunch</h1> <form method=\"POST\" " +
                        "action=\"/picked-dishes\">"
                );
                int i = 0;
                for(Map<String, Integer> dailyOrders : orders.values()) {
                    if (i == 0)
                        out.println("<h2>Orders for Monday: </h2>");
                    else if(i == 1)
                        out.println("<h2>Orders for Tuesday: </h2>");
                    else if (i == 2)
                        out.println("<h2>Orders for Wednesday: </h2>");
                    else if (i == 3)
                        out.println("<h2>Orders for Thursday: </h2>");
                    else
                        out.println("<h2>Orders for Friday: </h2>");

                    for(Map.Entry<String, Integer> entry : dailyOrders.entrySet())
                        out.println(entry.getKey() + " : " + entry.getValue() + "<br>");
                    i++;
                }
                out.println("<input type=\"submit\" name=\"submit\" value=\"Clear all\"/>\n</form> </body> </html>");
            }
            else
                response.sendRedirect("/home");
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            if(password!=null && password.equals(HomeServlet.password))
            {
                for(Map<String, Integer> dailyOrders : orders.values())
                    dailyOrders.replaceAll((k,v) -> 0);
                ordersForUser.clear();
            }
            response.sendRedirect("/picked-dishes?password=" + password);
        }

        public void destroy() {
            System.out.println("destroy method");
        }
    }

