<?php
/**
 * 插入多行表的同时，更新单行表
 */
error_reporting(0);
require_once './classes/class_insert_tc_update_bc.php';
$jsonData = $_POST["jsonData"];

$tableName = $_POST["tableName"];
$insertTcUpdateBc = new InsertTcUpdateBc();
$insertTcUpdateBc->insertTc($tableName, $jsonData);

?>