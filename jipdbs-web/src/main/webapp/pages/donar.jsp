<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" session="true"%>

<h1>Colabor&aacute; con IPDB</h1>
<p>Tu colaboraci&oacute;n nos permite cubrir los costos de mantenimiento del sistema.</p>
<p>Es f&aacute;cil. No necesit&aacute;s una cuenta. Pod&eacute;s hacerlo con tarjeta de cr&eacute;dito, transferencia bancaria, pago f&aacute;cil y otros.</p>
<br/>
<div style="width: 300px; margin: auto;">
<form action='https://argentina.dineromail.com/Shop/Shop_Ingreso.asp' method='post'>
<input type='hidden' name='NombreItem' value='IPDB'>
<input type='hidden' name='TipoMoneda' value='1'>
<label for='PrecioItem' style='font-size: 12px;'>Ingres&aacute; el monto a colaborar:</label>
<input style="width: 45px; text-align: right;" type='text' id='PrecioItem' name='PrecioItem' value='10.00'>
<input type='hidden' name='E_Comercio' value='1860801'>
<input type='hidden' name='NroItem' value='IPDB'>
<input type='hidden' name='image_url' value='http://'>
<input type='hidden' name='DireccionExito' value='http://www.ipdburt.com.ar/thanks.jsp'>
<input type='hidden' name='DireccionFracaso' value='http://www.ipdburt.com.ar/nok.jsp'>
<input type='hidden' name='DireccionEnvio' value='0'>
<input type='hidden' name='Mensaje' value='0'>
<input type='hidden' name='MediosPago' value='4,2,7,13'><br/>
<input type='image'width="250" height="70" src='https://argentina.dineromail.com/imagenes/botones/donar-medios_c.gif' border='0' name='submit' alt='Donar con DineroMail'>
</form>
</div>
<br/>
