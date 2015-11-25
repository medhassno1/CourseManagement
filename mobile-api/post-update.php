<?php


$jsonData = $_POST["jsonData"];
$tableName = $_POST["tableName"];
$con = mysqli_connect("localhost", "root", "", "teacher_class_system");
if (!$con) {
    die('Could not connect: ' . mysql_error());
} else {
    mysqli_query($con, "SET NAMES utf8");
    $jsonArry = json_decode($jsonData, true);

    echo "<br>";
    foreach ($jsonArry as $row) {
        $search = mysqli_query($con, "select * from $tableName WHERE courseName = '$row[courseName]' ");
        $result = mysqli_fetch_assoc($search);

        $sql = "UPDATE $tableName SET timePeriod='$row[timePeriod];',teacherName ='$row[teacherName];',remark='$row[remark];' WHERE courseName = '$row[courseName]'";
        mysqli_query($con, $sql);
        print_r($sql);
        echo "<br>";
    }

}