<?php
/*
 * 更新系负责人所负责系表
 */
error_reporting(0);
require_once './classes/class_update_manager_major.php';
$jsonData = $_POST["jsonData"];

$updateManger = new UpdateManagerMajor();
$updateManger->UpdateManagerMajor($jsonData);
?>
