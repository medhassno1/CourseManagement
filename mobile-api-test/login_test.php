<?php 
require_once dirname(__FILE__).'/../mobile_api/classes/class_login.php';

class LoginTest extends PHPUnit_Framework_TestCase
{
	public function testLoginSuccess()
	{
		$expected = '{"workNumber":"00001","password":"00001","name":"西瓜","sex":"男","birthday":"20151201","department":"计算机","telephone":"110","email":"git@github.com"}';
		
		$workNumber = '00001';
		$password = '00001';
		$tableName = 'user_teacher';
		$lg = new Login;
		$actual = $lg->login($workNumber,$password,$tableName);
		
		$this->assertEquals($expected,$actual);
	}
	
	function testLoginFail()
	{
		$expected = 'false';
		
		$workNumber = '11111';
		$password = '11111';
		$tableName = 'user_teacher';
		
		$lg = new Login;
		$actual = $lg->login($workNumber,$password,$tableName);
		$this->assertEquals($expected,$actual);
	}
}

?>