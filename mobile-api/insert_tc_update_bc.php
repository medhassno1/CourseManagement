<?php
/**
 * ����tableName��jsonData���������ݵ����б������ڵ��б��ڽ��и���
 */
error_reporting(0);
require_once './classes/class_insert_tc_update_bc.php';
$jsonData = $_POST["jsonData"];

$tableName = $_POST["tableName"];
$insertTcUpdateBc = new InsertTcUpdateBc();
$insertTcUpdateBc->insertTcUpdateBc($tableName, $jsonData);

?>