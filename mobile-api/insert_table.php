<?php
/**
 * ����tableName���������ݵ�tableName
 */
error_reporting(0);
$jsonData = $_POST["jsonData"];

$tableName = $_POST["tableName"];

$con = mysqli_connect("localhost", "root", "", "teacher_class_system");
if (!$con) {
    die('Could not connect: ' . mysqli_error());
} else {
    mysqli_query($con, "SET NAMES utf8");
    $jsonArry = json_decode($jsonData, true);
        // ��ȡ���ݿ����ֶ���
        $keys = array_keys($jsonArry[0]);
        $tableList = '(' . implode($keys, ',') . ')';
        foreach ($jsonArry as $row) {
            $statement = "INSERT INTO $tableName $tableList VALUES " . ' ("' . implode($row, '","') . '")';
            echo($statement);
            $sql = mysqli_query($con, $statement);
        }
    

}
?>