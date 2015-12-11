<?php
/**
 * 更新单行表
 */
error_reporting(0);
$jsonData = $_POST["jsonData"];

$tableName = $_POST["tableName"];

$con = mysqli_connect("localhost", "root", "", "teacher_class_system");
if (!$con) {
    die('Could not connect: ' . mysqli_error());
} else {
    foreach ($jsonArry as $row) {
        $search = mysqli_query($con, "select * from $tableName WHERE courseName = '$row[courseName]' ");
        $result = mysqli_fetch_array($search);
        //更新老师
        $updateTeacher = $result['teacherName'] . $row['teacherName'] . ';';
        $updateTimePeriod = $result['timePeriod'] . $row['timePeriod'] . ';';
        $updateRemark = $result['remark'] . $row['remark'] . ';';
        $sql = "UPDATE $tableName SET timePeriod='$updateTimePeriod',teacherName ='$updateTeacher',remark='$updateRemark' WHERE courseName = '$row[courseName]'";
        mysqli_query($con, $sql);
    }
}
?>