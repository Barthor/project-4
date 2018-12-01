<%-- 
     * Andrew Matrai
    CNT 4714 fall 2018 project 4
Assignment title: A three tiered distributed web based application
Date: Nov 11, 2018 (LAST DAY OF UNIVERSITY FOR ME YAY)
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"  import="java.io.PrintWriter" language="java"%>
<!DOCTYPE html>
<%String tableText = (String)session.getAttribute("tableText");
String sqlIn = (String)session.getAttribute("sqlIn");
if (sqlIn==null) sqlIn = " ";
if (tableText==null) tableText = " " ;%>
<html lang="en">
    <head>
        <meta http-equiv = "refresh" content = "60" />
        <title>DataCorp Database Management Tool</title>
        <style type = "text/css">
            <!--
                body{background-color: black;}
                #servlet {color:purple;}
                #jsp {color:cyan;}
             -->
        </style>
    </head>
    <body>
        
        <h1 style="text-align: center; color: white;">
            DataCorp Database Systems
        </h1>
        <div id="middle" style="text-align: center; color: white;">
            <p> Please enter a valid SQL query <br> </p>
            
            <form action = "/project-4/DataCorpServlet" method = "get">
                <input type = "text" name="sqlIn"> </input>
			<br/>
			<input type="submit" value="Execute Command" name="execute">
			<input type="reset" value="Clear Form" name="clear">
            </form>
        </div>
        
        <hr style="color: white;">
        
        <div id="footer" style="text-align: center; color:white;">
            <h3> <%=tableText%> </h3>
            
        </div>
    </body>
</html>
