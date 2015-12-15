<?php

class Login{
    // 测试工具PHPUnit要求一定要在这里给变量默认值，于是默认为空。
    public function login($workNumber = "",$password = "",$tableName = ""){
        $con = mysqli_connect("localhost", "root", "", "teacher_class_system");
        if (!$con) {
            die('Could not connect: ' . mysqli_error());
        } else {
            mysqli_query($con, "SET NAMES utf8");
        
            $result = mysqli_query($con, "SELECT * FROM $tableName where workNumber = $workNumber and password = $password");
            if (!$result || mysqli_num_rows($result) == 0) {
                return "false";
            } else {
                $result_arr = mysqli_fetch_assoc($result);
                return json_encode($result_arr, JSON_UNESCAPED_UNICODE);
            }
        }
    }
}

?>