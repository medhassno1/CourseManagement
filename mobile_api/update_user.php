<?php 
/**
 * ����jsonData��tableName�����������֤����ϵ�������û���
 * ���Ϊuser_teaching_office����Ȩ���޸�department���������޸�
 */
error_reporting(0);
require_once './classes/class_update_user.php';
$jsonData = $_POST["jsonData"];
$tableName = $_POST["tableName"];
$manageMajor = $_POST["manageMajor"];
session_start();
$ident = $_SESSION['id'];

$updateUserTeacher = new UpdateUser();
$updateUserTeacher->updateUserData($jsonData, $manageMajor, $tableName, $ident);
?>
