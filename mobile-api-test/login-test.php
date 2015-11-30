<?php 
require_once dirname(__FILE__).'/../mobile-api/classes/class_login.php';

class LoginTest extends PHPUnit_Framework_TestCase
{
	public function testLoginSuccess()
	{
		$expected = '{"workNumber":"00001","password":"00001","name":"张栋","sex":"男","birthday":"18990125","department":"计算机","telephone":"18110119120","email":"18110119120@163.com"}';
		
		$workNumber = '00001';
		$password = '00001';
		$tableName = 'user_teacher';
		$lg = new Login();
		$actual = $lg->login($workNumber,$password,$tableName);
		
		$this->assertEquals($expected,$actual);
	}
	
	function testLoginFail()
	{
		$expected = 'false';
		
		$workNumber = '11111';
		$password = '11111';
		$tableName = 'user_teacher';
		
		$lg = new Login();
		$actual = $lg->login($workNumber,$password,$tableName);
		$this->assertEquals($expected,$actual);
	}
}

?>