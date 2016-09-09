<?php 

error_reporting(0);

require_once './classes/class_update_info_teacherDeadline.php';

$jsonData = $_POST["jsonData"];

session_start();
$ident = $_SESSION['id'];

$update = new UpdateTaskInfo();
$update->updateTeacherDeadline($ident, $jsonData);

?>
