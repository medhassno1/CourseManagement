<?php
error_reporting(0);


$tableName = $_POST["tableName"];
$workNumber = $_POST["workNumber"];
//$tableName = 'tc_net_pro201502';
//$workNumber = '10003';
session_start();
$_SESSION['id'] = 'user_teaching_office';
$con = mysqli_connect("localhost", "root", "", "teacher_class_system");
if (!$con) {
    die('Could not connect: ' . mysql_error());
} else {

    mysqli_query($con, "SET NAMES utf8");
    $state = mysqli_query($con, "SELECT taskState FROM task_info WHERE relativeTable='$tableName'");
    $state_result = mysqli_fetch_assoc($state);
    if ($state_result['taskState'] == 2) {
        $tableName_cb = 'cb_' . $tableName;

        $statement = "SELECT * FROM $tableName_cb";
        $result = mysqli_query($con, $statement);
        print_r($result);
        echo mysqli_num_rows($result);
        foreach ($result as $row) {
            echo json_encode($row, JSON_UNESCAPED_UNICODE);
            echo '<br>';
        }

    } else {
        $result = mysqli_query($con, "SELECT * FROM $tableName WHERE workNumber = $workNumber");
        if (mysqli_num_rows($result) < 1) {
            $sql = "SELECT * FROM $tableName WHERE workNumber = '' ORDER BY insertTime ";
            $search = mysqli_query($con, $sql);

            foreach ($search as $row) {
                echo json_encode($row, JSON_UNESCAPED_UNICODE);
                echo '<br>';
            }

        } else {
            foreach ($result as $row) {
                echo json_encode($row, JSON_UNESCAPED_UNICODE);
                echo '<br>';
            }

        }
    }


}