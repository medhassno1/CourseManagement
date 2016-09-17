<?php
require_once 'trans.php';

class QueryTeacherSelectCourses
{
    public function queryTeacher($tableName = "", $workNumber = "")
    {
        $con = mysql_connect("localhost", "root", "");
	    mysql_select_db('teacher_class_system',$con);
        if (!$con) {
            die('Could not connect: ' . mysql_error());
        } else {
            mysql_query("SET NAMES utf8");
            $find_state = mysql_query("SELECT taskState FROM task_info where relativeTable = '$tableName'");
            $state_result = mysql_fetch_assoc($find_state);
            if ($state_result['taskState'] == 2) {
                $tableNameCB = 'cb_' . $tableName;
                $statement = "SELECT * FROM $tableNameCB";
                $result = mysql_query($statement);
                while ($row = mysql_fetch_assoc($result)) {
                	$output[] = $row;
            	}
                echo json_encode_ex($output," ");
            } else {
                $result = mysql_query("SELECT * FROM $tableName WHERE workNumber = '$workNumber' ORDER BY insertTime ASC");
                if (mysql_num_rows($result)>0) {
                    while ($row = mysql_fetch_assoc($result)) {
                	$output[] = $row;
            	    }
                    echo json_encode_ex($output," ");
                } else {
                    $result = mysql_query("SELECT * FROM $tableName WHERE workNumber = ''");
                    while ($row = mysql_fetch_assoc($result)) {
                	$output[] = $row;
            	    }
                    echo json_encode_ex($output," ");
                }
            }
        }
    }
}
?>