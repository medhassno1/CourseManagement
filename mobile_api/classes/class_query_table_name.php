<?php 
error_reporting(0);
require_once 'trans.php';
class QueryTableName {
    public function queryT($tableName = "", $workNumber = "") {
        $con = mysql_connect("localhost", "root", "");
        mysql_select_db('teacher_class_system', $con);
        if (!$con) {
            die('Could not connect: '.mysql_error());
        } else {
            $find_state = mysql_query("SELECT taskState FROM task_info where relativeTable = '$tableName'");
            $state_result = mysql_fetch_assoc($find_state);
            if ($state_result['taskState'] == 2) {
            mysql_query("SET NAMES utf8");
            $result = mysql_query("SELECT * FROM $tableName");
            while ($row = mysql_fetch_assoc($result)) {
                $output[] = $row;
            }
            echo json_encode_ex($output, "");
            }else{
                $find_department = mysql_query("SELECT department FROM user_department_head where workNumber = '$workNumber'");
                $department_result=mysql_fetch_assoc($find_department);
                if($department_result['department']!= 'tc_math_nor'&&$department_result['department']!= 'tc_infocom_ope'&&$tableName!='tc_math_nor'&&$tableName!='tc_math_nor'){
                    $result = mysql_query("SELECT * FROM $tableName");
                    while ($row = mysql_fetch_assoc($result)) {
                        $output[] = $row;
                    }
                    echo json_encode_ex($output, "");
                }else if($department_result['department']== 'tc_math_nor'&&$department_result== 'tc_infocom_ope'&&$tableName=='tc_math_nor'&&$tableName=='tc_math_nor'){
                    $result = mysql_query("SELECT * FROM $tableName");
                    while ($row = mysql_fetch_assoc($result)) {
                        $output[] = $row;
                    }
                    echo json_encode_ex($output, "");
            }
            mysql_close();
        }

    }

}}
?>
