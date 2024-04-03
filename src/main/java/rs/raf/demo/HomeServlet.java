package rs.raf.demo;


import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "home", value = "/home")
public class HomeServlet extends HttpServlet {

    private List<String> dishes = new ArrayList<>();
    public static String password;
    public static Map<String, Map<String,Integer>> orders = new LinkedHashMap<>();
    public static Map<String, List<String>> ordersForUser = new LinkedHashMap<>();
    public HomeServlet() {



    }

    public void init() {
        System.out.println("init method");
        readd("C:\\Users\\User\\Desktop\\Domaci4\\src\\main\\resources\\ponedeljak.txt");
        readd("C:\\Users\\User\\Desktop\\Domaci4\\src\\main\\resources\\utorak.txt");
        readd("C:\\Users\\User\\Desktop\\Domaci4\\src\\main\\resources\\sreda.txt");
        readd("C:\\Users\\User\\Desktop\\Domaci4\\src\\main\\resources\\cetvrtak.txt");
        readd("C:\\Users\\User\\Desktop\\Domaci4\\src\\main\\resources\\petak.txt");
        readd("C:\\Users\\User\\Desktop\\Domaci4\\src\\main\\resources\\password.txt");
        orders.put("monday", new LinkedHashMap<>());
        orders.put("tuesday", new LinkedHashMap<>());
        orders.put("wednesday", new LinkedHashMap<>());
        orders.put("thursday", new LinkedHashMap<>());
        orders.put("friday", new LinkedHashMap<>());
        int i = 0;
        for(String dish : dishes) {
            if(i/3 == 0)
                orders.get("monday").put(dish, 0);
            else if(i/3 == 1)
                orders.get("tuesday").put(dish, 0);
            else if(i/3 == 2)
                orders.get("wednesday").put(dish, 0);
            else if(i/3 == 3)
                orders.get("thursday").put(dish, 0);
            else
                orders.get("friday").put(dish, 0);
            i++;
        }
    }

    private void readd(String filename) {

        try(BufferedReader reader = new BufferedReader(new FileReader(filename)))
        {
            if(filename.equals("C:\\Users\\User\\Desktop\\Domaci4\\src\\main\\resources\\password.txt"))
                password = reader.readLine();
            else {
                String line;
                while ((line = reader.readLine()) != null) {
                    dishes.add(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        StringBuilder s = new StringBuilder();
        PrintWriter out = response.getWriter();
        if(!ordersForUser.containsKey(request.getSession().getId())) {

            out.println("<html> <body> <h1>Choose your lunch</h1> <form method=\"POST\" " +
                    "action=\"/home\">"
            );

            for (int i = 0; i < dishes.size(); i++) {
                if (i == 0)
                    s.append("<label for=\"monday\">Monday:</label><br><br><select name = \"monday\" id = \"monday\">");
                else if (i == 3)
                    s.append("<label for=\"tuesday\"><br> Tuesday:</label><br><br><select name = \"tuesday\" id = \"tuesday\">");
                else if (i == 6)
                    s.append("<label for=\"wednesday\"><br>Wednesday:</label><br><br><select name = \"wednesday\" id = \"wednesday\">");
                else if (i == 9)
                    s.append("<label for=\"thursday\"><br>Thursday:</label><br><br><select name = \"thursday\" id = \"thursday\">");
                else if (i == 12)
                    s.append("<label for=\"friday\"><br>Friday:</label><br><br><select name = \"friday\" id = \"friday\">");

                s.append("<option value=\"").append(dishes.get(i)).append("\">").append(dishes.get(i)).append("</option>");
                if (i % 3 == 2)
                    s.append("</select><br>");
             }

                out.println(s);
                out.println("<br> <input type=\"submit\" name=\"submit\" value=\"Potvrda\"/> </form> </body> </html> ");
            }
        else {
            StringBuilder sb = new StringBuilder();
            for(String d : ordersForUser.get(request.getSession().getId())) {
                sb.append(d).append("<br>");
            }
            out.println("<html><body><h3>Can't choose twice<br><br>" + sb +
                    "</h3></body></html>");
        }

    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(!ordersForUser.containsKey(request.getSession().getId())) {
            ordersForUser.put(request.getSession().getId(), new ArrayList<>());
            ordersForUser.get(request.getSession().getId()).add(request.getParameter("monday"));
            ordersForUser.get(request.getSession().getId()).add(request.getParameter("tuesday"));
            ordersForUser.get(request.getSession().getId()).add(request.getParameter("wednesday"));
            ordersForUser.get(request.getSession().getId()).add(request.getParameter("thursday"));
            ordersForUser.get(request.getSession().getId()).add(request.getParameter("friday"));

            List<String> pickedDishes = ordersForUser.get(request.getSession().getId());
            synchronized (this) {
                orders.get("monday").put(pickedDishes.get(0), orders.get("monday").get(pickedDishes.get(0)) + 1);
                orders.get("tuesday").put(pickedDishes.get(1), orders.get("tuesday").get(pickedDishes.get(1)) + 1);
                orders.get("wednesday").put(pickedDishes.get(2), orders.get("wednesday").get(pickedDishes.get(2)) + 1);
                orders.get("thursday").put(pickedDishes.get(3), orders.get("thursday").get(pickedDishes.get(3)) + 1);
                orders.get("friday").put(pickedDishes.get(4), orders.get("friday").get(pickedDishes.get(4)) + 1);
            }

        }
        response.sendRedirect("/home");
    }

    public void destroy() {
        System.out.println("destroy method");
    }
}
