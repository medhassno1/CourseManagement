<?php 
/**
 * �����������֤Ϊuser_teaching_office���ã�
 * ����tableName��workNumerɾ���û���Ϣ
 * �����ɾ��ϵ�����ˣ���ɾ��department_head_majors�ж�Ӧ����Ϣ
 */
error_reporting(0);
require_once './classes/class_delete_user.php';
$tableName = $_POST["tableName"];
$workNumber = $_POST["workNumber"];

session_start();
$ident = $_SESSION['id'];

$deleteUser = new DeleteUser();
$deleteUser->deleteU($tableName, $ident, $workNumber);

?>
