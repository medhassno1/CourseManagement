<?php
// 关闭错误报告
error_reporting(0);

$jsonData = $_POST["jsonDataData"];
$tableName=$_POST["tableName"];
$action = $_POST["action"];

$con = mysqli_connect("localhost","root","","teachersystem");
if (!$con){
    die('Could not connect: ' . mysql_error());
} else {
	mysqli_query($con, "SET NAMES utf8");
	$jsonData = json_decode($jsonData, true);
	
	switch($action){
		case "updateCbTable":
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
			insertTable();
			break;
		case "insertUserTable":
			insertTable();
			break;
	}
}

// 更新合一报课表
function updateCbTable() {
	
    foreach ($jsonData as $row) {
        $search = mysqli_query($con, "select * from $cbTable WHERE courseName = '$row[courseName]' ");
        $result = mysqli_fetch_array($search);
        //更新老师
        $updateTeacher = $result['teacherName'] . $row['teacherName'] . ';';
        $updateTimePeriod = $result['timePeriod'] . $row['timePeriod'] . ';';
        $updateRemark = $result['remark'] . $row['remark'] . ';';
        $sql = "UPDATE $cbTable SET timePeriod='$updateTimePeriod' WHERE courseName = '$row[courseName]'";
        mysqli_query($con, $sql);

        $sql = "UPDATE $cbTable SET teacherName ='$updateTeacher' WHERE courseName = '$row[courseName]'";
        mysqli_query($con, $sql);

        $sql = "UPDATE $cbTable SET remark='$updateRemark' WHERE courseName = '$row[courseName]'";
        mysqli_query($con, $sql);
    }
}

// 更新教师表
function updateUserTable() {
	
	// 获取表的字段名
	$keys = array_keys($jsonData[0]);
    $tableList= '(' . implode($keys, ',') . ')';
	
    foreach ($jsonData as $row) {
        $key = $row['workNumber'];
        $statement1 = "DELETE FROM $tableName WHERE workNumber='$key';";
        mysqli_query($con,$statement1);
        $statement2 = "INSERT INTO $tableName $tableList VALUES " . ' ("' . implode($row, '","') . '");';
        mysqli_query($con,$statement2);
    }
}

function updateTable() {
	
}

// 插入任意表
function insertTable() {
	
	// 获取数据库表的字段名
    $keys = array_keys($jsonData[0]);
    $tableList= '(' . implode($keys, ',') . ')';

    $statement = "INSERT INTO $tableName $tableList VALUES ";
    foreach ($jsonData as $row) {
        $statement .=' ("' . implode($row, '","') . '"),';
    }
	// 去掉最后的“,”，并添加“;”
	$statement = rtrim($statement, ",");
	$statement .= ";";
    $sql = mysqli_query($con,$statement);
}


?>