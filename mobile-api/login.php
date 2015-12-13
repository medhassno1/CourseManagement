<?php
// 关闭错误报告
error_reporting(0);

require_once './classes/class_login.php';

$workNumber = $_POST["login-user"];
$password = $_POST["login-password"];
$tableName = $_POST["ident"];

$log = new Login;
$response = $log->login($workNumber,$password,$tableName);
if(response!="false"){
    session_start();
    $_SESSION['id']=$tableName;
}

echo $response;

?>
