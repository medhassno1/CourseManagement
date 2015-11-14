<?php
// 关闭错误报告
error_reporting(0);

$workNumber = $_POST["login-user"];
$password = $_POST["login-password"];
$tableName = $_POST["ident"];

$con = mysql_connect("localhost","root","");
if (!$con) {
  	die('Could not connect: ' . mysql_error());
} else {
		
  	mysql_select_db("teacher_class_system", $con);
	
	$result = mysql_query("SELECT * FROM $tableName where workNumber = $workNumber and password = $password");
	if(mysql_num_rows($result) < 1){
		echo "false";
	} else {
		echo "true";
	}
}

?>
