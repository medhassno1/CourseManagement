<?php
/**
 * ����tableName��jsonData���������ݵ����б������ڵ��б��ڽ��и���
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
    // ��ȡ���ݿ������ֶ���
    $keys = array_keys($jsonArry[0]);
    $tableList = '(' . implode($keys, ',') . ')';
    foreach ($jsonArry as $row) {
        $statement = "INSERT INTO $tableName $tableList VALUES " . ' ("' . implode($row, '","') . '")';
        $sql = mysqli_query($con, $statement);
    }
    foreach ($jsonArry as $row) {
        $cbTableName = 'cb_' . $tableName;
        $search = mysqli_query($con, "select * from $cbTableName WHERE courseName = '$row[courseName]' ");
        $result = mysqli_fetch_array($search);
        //������ʦ
        $updateTeacher = $result['teacherName'] . $row['teacherName'] . '；';
        $updateTimePeriod = $result['timePeriod'] . $row['timePeriod'] . '；';
        $updateRemark = $result['remark'] . $row['remark'] . '；';
        $sql = "UPDATE $cbTableName SET timePeriod='$updateTimePeriod',teacherName ='$updateTeacher',remark='$updateRemark' WHERE courseName = '$row[courseName]'";
        mysqli_query($con, $sql);
        print_r($sql);
    }
}
?>