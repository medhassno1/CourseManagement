<?php
error_reporting(0);

$tableName = $_POST["tableName"];
$con = mysqli_connect("localhost", "root", "", "teacher_class_system");
//$tableName ='tc_com_nor201401';
session_start();
$ident = $_SESSION['id'];
if (!$con) {
    die('Could not connect: ' . mysql_error());
} else {
    if ($ident = 'user_teaching_office') {
        $statement1 = "DELETE FROM task_info WHERE relativeTable='$tableName';";
//        print_r($statement1);
//        echo '<br>';
        mysqli_query($con, $statement1);
        $statement2 = "DROP TABLE IF EXISTS $tableName;";
        mysqli_query($con, $statement2);
//        print_r($statement2);
    } else {
        echo "没有权限";
    }
}