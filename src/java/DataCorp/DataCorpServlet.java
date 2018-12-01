/*
 * Andrew Matrai
    CNT 4714 fall 2018 project 4
Assignment title: A three tiered distributed web based application
Date: Nov 11, 2018 (LAST DAY OF UNIVERSITY FOR ME YAY)
 */
package DataCorp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author abmat
 */
public class DataCorpServlet extends HttpServlet {

    private Connection connection;
    private Statement statement;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //attempt database connection
        String dbproblem = "There is no problem";
        boolean incrementStatus = false;
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project4", "root", "root");
            statement = connection.createStatement();
        }
        catch (ClassNotFoundException | SQLException exception)
        {
            throw new UnavailableException(exception.getMessage());
        }
        String sqlIn = request.getParameter("sqlIn");
        String tableText = "SQL Results </br>";
        
        tableText += "<body>";
        boolean status = false;
        int quantChange = 0;
        ArrayList<Integer> quant1 = new ArrayList<Integer>();
        ArrayList<Integer> quant2 = new ArrayList<Integer>();
        ResultSet rs;
        try
        {
            status = statement.execute("SELECT quantity FROM shipments");
            rs = statement.getResultSet();
            while(rs.next())
            {
                quant1.add(rs.getInt(1));
            }
        }
        catch ( SQLException sqlException ) 
        {
          
        } // end cat
        
        try
        {
            status = statement.execute(sqlIn);
            if (status)
            {
                rs = statement.getResultSet();
                ResultSetMetaData rsMetaData = rs.getMetaData();
                int col = rsMetaData.getColumnCount();
                tableText+=("<title>Results</title>");
                tableText+=("</head>");
                tableText+=("<body><table align=\"center\" border = \"2\">");
                tableText+="<tr>";
                for (int j=1;j<=col;j++)
                {
                    tableText+=("<td>" + (rsMetaData.getColumnName(j)) + "</td>");
                }
                tableText+="</tr>";
                while (rs.next())
                {
                  tableText+=("<tr>");
                  for (int i = 1;i<=col;i++)
                  {
                      tableText+=("<td>" + (rs.getObject(i)).toString() + "</td>");
                  }
                  tableText+=("</tr>");
                }
                tableText+=("</table>");
            }
            else
            {
                try
                {
                    status = statement.execute("SELECT quantity FROM shipments");
                    rs = statement.getResultSet();
                    while(rs.next())
                    {
                        quant2.add(rs.getInt(1));
                    }
                }
                catch ( SQLException sqlException ) 
                {

                } // end cat
                if (compareQuant(quant1, quant2))
                {
                    String increment = "UPDATE suppliers t1 JOIN shipments t2 ON t1.snum = t2.snum AND t2.quantity > 100 SET t1.status = status + 5";
                    status = statement.execute(increment);
                    tableText+="<body><p>Statement Executed Successfully.</p>";
                    tableText+="<p> Busisness Logic Detected. Updating Supplier Status</p>";
                }
                else
                {
                    tableText+="<body><p>Statement Executed Successfully.</p>";
                    tableText+="<p> No Busisness Logic Detected.</p>";
                }
                
                
            }
        }
        catch ( SQLException sqlException ) 
        {
           sqlException.printStackTrace();
           tableText+=( "<p>Database error occurred. " );
           tableText+=("Error executing SQL statement. Uknown statement:" + sqlIn + "</p>");
        } // end catch
        tableText+=("</body></html>");
        
        HttpSession session = request.getSession();
        session.setAttribute("tableText", tableText);
        session.setAttribute("sqlIn", sqlIn);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }
    boolean compareQuant(ArrayList<Integer> q1, ArrayList<Integer> q2)
    {
        if (q1.size() <= q2.size())
        for (int i=0;i<q2.size();i++)
        {
            if (i<q1.size())
            {
                if (Math.abs(q2.get(i) - q1.get(i)) >= 100)
                {
                    return true;
                }
            }
            else
            {
                if (q2.get(i)>= 100)
                {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    public void destroy()
   {
      // attempt to close statements and database connection
      try 
      {
         statement.close();
         connection.close();
      } // end try
      // handle database exceptions by returning error to client
      catch( SQLException sqlException ) 
      {
      } // end catch
   } // end method destroy
}
