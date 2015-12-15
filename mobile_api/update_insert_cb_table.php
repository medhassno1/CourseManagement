<?php
/**
 * 传入jsonData和tableName，由系负责人和教学办更新单行表
 */
 error_reporting(0);
require_once './classes/class';
$jsonData = $_POST["jsonData"];
$tableName = $_POST["tableName"];
session_start();
$iden = $_SESSION['id'];

$update = new UpdateInsertCbTable();
$update->updateInsert($jsonData, $tableName, $iden);
?>
