<?php
require "init.php";
if($_SERVER['REQUEST_METHOD']=='POST'){

$email = $_POST["email"];
$password = $_POST["password"];

$sql = "SELECT * FROM account WHERE email='$email' AND password='$password'";

  //executing query
$r = mysqli_query($con,$sql);

  //fetching result
$check = mysqli_fetch_array($r);

  //if we got some result
  if(isset($check)){
	//
	// $res = mysqli_fetch_array($r);
		$result = array();
		array_push($result,array(
			"email"=>$check['email'],
			"password"=>$check['password'],
			"userid"=>$check['userid'],
			)
		);
	// echo json_encode($result);
/////////
		$userid = $result[0]["userid"];
		// echo json_encode($userid);
			$token = bin2hex(random_bytes(64));
			$sql_query = "UPDATE account SET token = '$token' WHERE userid= '$userid';";
			 if (mysqli_query($con,$sql_query)==true) {
				if (!isset($myObj)){
							$myObj = new stdClass();
				}
				$myObj->token = $token;
				$myObj->userid = $userid ;
				reponseModel($myObj,"User Found",true,200);
			}else {
				reponseModel(null,"Someting went wrong",false,400);
			}
///////////////////


  }else{
	$token = bin2hex(random_bytes(64));
	$sql_query = "INSERT INTO account (email,password,token) VALUES('$email','$password','$token')";

	if (mysqli_query($con,$sql_query)) {

		$user_meta_id = $con->insert_id;

		if (!isset($myObj)){
					$myObj = new stdClass();
		}
		$myObj->token = $token;
		$myObj->userid = $user_meta_id;
		reponseModel($myObj,"New User Created",true,200);
		// code...
	}else {
				reponseModel(null,"Someting went wrong",false,400);
	}
  //displaying failure
 // echo "failure";

  }
}
function reponseModel($data,$message,$status,$code) {

	if (!isset($filnalObject))
			$filnalObject = new stdClass();
			$filnalObject->message = $message;
			$filnalObject->code = $code;
	$filnalObject->status = $status;
	$filnalObject->data = $data;

	$filnalObject = json_encode($filnalObject);

 echo $filnalObject;
}

mysqli_close($con);
?>
