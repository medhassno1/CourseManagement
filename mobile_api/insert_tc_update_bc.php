<?php
/**
 * ������б��ͬʱ�����µ��б�
 */
error_reporting(0);
require_once './classes/class_insert_tc_update_bc.php';
$jsonData = $_POST["jsonData"];

$tableName = $_POST["tableName"];
$insertTcUpdateBc = new InsertTcUpdateBc();
$insertTcUpdateBc->insertTc($tableName, $jsonData);

?>