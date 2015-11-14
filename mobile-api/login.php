<?php
// 关闭错误报告
error_reporting(0);

$workNumber = $_POST["login-user"];
$password = $_POST["login-password"];
$identity = $_POST["ident"];

session_start();
$_SESSION["temp"][0]=$workNumber;

$con = mysql_connect("localhost","root","");
if (!$con) {
  	die('Could not connect: ' . mysql_error());
} else {
		
  	mysql_select_db("teacher_class_system", $con);

	$tableName = "";
	switch($identity) {
		// 教师类型身份验证
		case "teacher":
			$tableName = "user_teacher";
			break;
		// 系负责人类型身份验证
		case "departmentHead":
			$tableName = "user_department_head";
			break;
		// 教学办类型身份验证
		case "teachingOffice":
			$tableName = "user_teaching_office";
			break;
			
		default :
			break;
	}
	
	$result = mysql_query("SELECT * FROM $tableName where workNumber = $workNumber and password = $password");
	if($result != null){
		echo "true";
	} else {
		echo "false";
	}
}

?>
