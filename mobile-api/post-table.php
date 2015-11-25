<?php
// 关闭错误报告
// error_reporting(0);

$jsonData = $_POST["jsonData"];
// echo $jsonData;
$tableName=$_POST["tableName"];
$action = $_POST["action"];

$con = mysqli_connect("localhost","root","","teacher_class_system");
if (!$con){
    die('Could not connect: ' . mysql_error());
} else {
	mysqli_query($con, "SET NAMES utf8");
	$jsonArry = json_decode($jsonData, true);
	
	switch($action){
		case "updateCbTable":
            updateCbTable($con,$tableName,$jsonArry);
			break;
		case "updateTaskTable":
			break;
		case "updateUserTable":
			break;
		case "insertCbTable":
			break;
		case "insertTcTable":
			break;
		case "insertTaskTable":
			insertTable($con,$tableName,$jsonArry);
			break;
		case "insertUserTable":
			insertTable($con,$tableName,$jsonArry);
			break;
		case "insertTable":
			insertTable($con,$tableName,$jsonArry);
			break;
		default:
			break;
	}
}

// 更新合一报课表
function updateCbTable($con,$tableName,$jsonArry) {
	
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

// 更新教师表
function updateUserTable($con,$tableName,$jsonArry) {
	
	// 获取表的字段名
	$keys = array_keys($jsonArry[0]);
    $tableList= '(' . implode($keys, ',') . ')';
	
    foreach ($jsonArry as $row) {
        $key = $row['workNumber'];
        $statement1 = "DELETE FROM $tableName WHERE workNumber='$key';";
        mysqli_query($con,$statement1);
        $statement2 = "INSERT INTO $tableName $tableList VALUES " . ' ("' . implode($row, '","') . '");';
        mysqli_query($con,$statement2);
    }
}

// 插入任意表
function insertTable($con,$tableName,$jsonArry) {
	
	// 获取数据库表的字段名
    $keys = array_keys($jsonArry[0]);
    $tableList= '(' . implode($keys, ',') . ')';


    foreach ($jsonArry as $row) {
        $statement ="INSERT INTO $tableName $tableList VALUES ".' ("' . implode($row, '","') . '")';
        $sql = mysqli_query($con,$statement);

    }



}


?>