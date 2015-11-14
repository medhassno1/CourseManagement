<?php
// 关闭错误报告
error_reporting(0);

$json = $_POST["jsonData"];
$con = mysqli_connect("localhost","root","","teacher_class_system");

if (!$con){
    die('Could not connect: ' . mysql_error());
} else {
    mysqli_query($con, "SET NAMES utf8");
    $json = json_decode($json, true);
	// 获取数据库表的字段名
    $keys = array_keys($json[0]);
    $tableList= '(' . implode($keys, ',') . ')';
	
    foreach ($json as $row) {
        $key = $row['workNumber'];
        $statement1 = "DELETE FROM user_teacher WHERE workNumber='$key';";
        mysqli_query($con,$statement1);
        $statement2 = "INSERT INTO user_teacher $tableList VALUES " . ' ("' . implode($row, '","') . '");';
        mysqli_query($con,$statement2);
    }
}