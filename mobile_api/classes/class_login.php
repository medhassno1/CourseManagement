<?php
require_once 'trans.php';
class Login{
    public function login1($workNumber = "",$password = "",$tableName = ""){
        $con = mysql_connect("localhost", "root", "");
	mysql_select_db('teacher_class_system',$con);
        if (!$con) {
            die('Could not connect: ' . mysql_error());
        } else {
            mysql_query("SET NAMES utf8");
            $result = mysql_query( "SELECT * FROM $tableName where workNumber = '$workNumber' and password = '$password'");
	        if (mysql_num_rows($result)>0) {
                $result_arr = mysql_fetch_assoc($result);
                
		$year =date("Y");
                $date =date("Ymd");             
                $timeResult = mysql_query("SELECT * FROM task_info where year >= '$year'");
                while($row = mysql_fetch_array($timeResult)) {
                    $table_name=$row["relativeTable"];
                    if($row["teacherDeadline"] < $date && $row["taskState"]=='0') {
                        $result1 = mysql_query("UPDATE `task_info` SET `taskState` = '1' where relativeTable='$table_name'");
                    }
                    
                    if($row["departmentDeadline"] < $date) {
                        $result1 = mysql_query("UPDATE task_info SET taskState = '2' where relativeTable='$table_name'");
                    }
               }
               
		       mysql_close();
               return json_encode_ex($result_arr,"");
            } else { 
		       mysql_close();
               return "false";
            }
        }
    }
}

?>