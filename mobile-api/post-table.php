<?php
// �رմ��󱨸�
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

// ���º�һ���α�
function updateCbTable() {
	
    foreach ($jsonData as $row) {
        $search = mysqli_query($con, "select * from $cbTable WHERE courseName = '$row[courseName]' ");
        $result = mysqli_fetch_array($search);
        //������ʦ
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

// ���½�ʦ��
function updateUserTable() {
	
	// ��ȡ����ֶ���
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

// ���������
function insertTable() {
	
	// ��ȡ���ݿ����ֶ���
    $keys = array_keys($jsonData[0]);
    $tableList= '(' . implode($keys, ',') . ')';

    $statement = "INSERT INTO $tableName $tableList VALUES ";
    foreach ($jsonData as $row) {
        $statement .=' ("' . implode($row, '","') . '"),';
    }
	// ȥ�����ġ�,��������ӡ�;��
	$statement = rtrim($statement, ",");
	$statement .= ";";
    $sql = mysqli_query($con,$statement);
}


?>