<?php

class Login{
    // ���Թ���PHPUnitҪ��һ��Ҫ�����������Ĭ��ֵ������Ĭ��Ϊ�ա�
    public function login($workNumber = "",$password = "",$tableName = ""){
        $con = mysqli_connect("localhost", "root", "", "teacher_class_system");
        if (!$con) {
            die('Could not connect: ' . mysqli_error());
        } else {
            mysqli_query($con, "SET NAMES utf8");
        
            $result = mysqli_query($con, "SELECT * FROM $tableName where workNumber = $workNumber and password = $password");
            if (mysqli_num_rows($result) == 0) {
                return "false";
            } else {
                $result_arr = mysqli_fetch_assoc($result);
                return json_encode($result_arr, JSON_UNESCAPED_UNICODE);
            }
        }
    }
}

?>