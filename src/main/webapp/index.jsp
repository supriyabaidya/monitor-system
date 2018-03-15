<%--
    Document   : index
    Created on : 7 Mar, 2018, 11:28:02 PM
    Author     : TomHardy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import = "java.io.*,java.util.*,java.net.*,org.json.simple.JSONObject" %>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Monitor</title>

        <style >
            table {
                border:5px solid green;
                padding: 1px;
                border-spacing: 1px
            }
            td,th {
                text-align : left ;
                border:1px solid black;
                padding: 5px;
            }
        </style>

        <script src="jquery-1.10.2.js" ></script>
        <script type="text/javascript">

            var interval = 5000;

            function getSelectedCheckboxesArray() {
                var ch_list = Array();
                $("input:checkbox[type=checkbox]:checked").each(function () {
                    ch_list.push($(this).val());
                });
                return ch_list;
            }

            function displayUsers() {
                var check_list = Array();
                check_list = getSelectedCheckboxesArray();
                var noOfCheckedItems = check_list.length;

                if (noOfCheckedItems === 0) {
                    $.ajax({
                        type: "GET",
                        url: "DisplayUsers",
                        datatype: "html",
                        async: true,
                        success: function (data) {
                            $("#output").html(data);
                        }
                    });
                }
            }

            displayUsers();
            setInterval("displayUsers()", interval);

        </script>
    </head>
    <body>
        <h1>Monitor</h1>

        <h2>A warm Welcome from Supriyo Baidya</h2>
        <h3>netbeans maven java web project 'Monitor' (including web service client) and deployed from netbeans [netbeans->GIT->Openshift] .</h3>


        <%
            if (session.getAttribute("message") != null) {
                out.println(session.getAttribute("message"));
            }
            session.removeAttribute("message");
        %>

    <c:out value="${sessionScope.message}" />
    <c:remove var="message" scope="session" />

    <h4 id="output"></h4>
</body>
</html>