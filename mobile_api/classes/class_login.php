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
        
            $result = mysql_query( "SELECT * FROM $tableName where workNumber = $workNumber and password = $password");
	    if (mysql_num_rows($result)>0) {
		$result_arr = mysql_fetch_assoc($result);
		mysql_close();
                return json_encode_ex($result_arr,"");
            }else{ 
		mysql_close();
                return "false";
            }
        }
    }
}

?>