<%--
  Created by IntelliJ IDEA.
  User: Franco
  Date: 28/12/2016
  Time: 11:05 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Inicio</title>
</head>

<body style="background-color:#abbf78">
<h1 align="center" style="font-family: sans-serif">Períodos de Clima de Planetas Vulcano, Ferengis, Betasoides</h1>
<br/>
<br/>
<br/>
<br/>
<table align="center" border="1" style="font-size: larger; text-align: center; font-family: Courier">
    <tr>
        <td width="30%"><strong>Período de Sequía</strong></td>
        <td width="30%"><strong>Período de Lluvias</strong></td>
        <td width="30%"><strong>Período de Condiciones Optimas</strong></td>
    </tr>
    <tr>
        <td width="30%">${periodo.periodoSequia}</td>
        <td width="30%">${periodo.periodoLluvia}</td>
        <td width="30%">${periodo.periodoOptimo}</td>
    </tr>
</table>
<br/>
<table align="left" style="font-size: larger; text-align: center; font-family: sans-serif">
    <tr>
        <td><strong>Día de pico máximo de lluvia:</strong></td>
        <td>${periodo.picoLluvia}</td>
    </tr>
</table>
</body>
</html>