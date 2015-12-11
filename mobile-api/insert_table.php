<?php
/**
 * 传入tableName，插入数据到tableName
 */
error_reporting(0);
$jsonData = $_POST["jsonData"];

$tableName = $_POST["tableName"];

$con = mysqli_connect("localhost", "root", "", "teacher_class_system");
if (!$con) {
    die('Could not connect: ' . mysqli_error());
} else {
    // 获取数据库表的字段名
    $keys = array_keys($jsonArry[0]);
    $tableList = '(' . implode($keys, ',') . ')';
    foreach ($jsonArry as $row) {
        $statement = "INSERT INTO $tableName $tableList VALUES " . ' ("' . implode($row, '","') . '")';
        $sql = mysqli_query($con, $statement);
    }
}
?>