<?php
require "init.php";
if($_SERVER['REQUEST_METHOD']=='POST'){

  $authrizationToken =getBearerToken();


  $sql = "SELECT * FROM account WHERE token='$authrizationToken'";

    //executing query
  $r = mysqli_query($con,$sql);

    //fetching result
  $check = mysqli_fetch_array($r);

    //if we got some result
    if(isset($check)){
      $avatar=$_POST["avatar"];
      $userid=$_POST["userid"];
      if(isset($avatar)){
        upload($_POST["userid"],$avatar=$_POST["avatar"],$con);
    }
    else{
        reponseModel(null,"Image not Found",false,400);

    }
  }else {
    reponseModel(null,"Authorization Failed",false,400);
  }
    }





function upload($userid,$avatar,$con){

  $now = DateTime::createFromFormat('U.u', microtime(true));
  $id = $now->format('YmdHisu');
    $upload_folder = "Uploads"; //DO NOT put url (http://example.com/upload)
    $path = "$upload_folder/$id.jpeg";
    $link="$id.jpeg";
  	$sql_query = "UPDATE account SET avatarUrl = '$link' WHERE userid= '$userid';";
       if(mysqli_query($con,$sql_query)){
         if(file_put_contents($path, base64_decode($avatar)) != false){
           if (!isset($myObj)){
                 $myObj = new stdClass();
           }
           $myObj->avatar_url = $link;
           reponseModel($myObj,"Image Uploaded",true,200);
         }
       }
       else{
      reponseModel(null,"Image not Uploaded",false,400);
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
function getAuthorizationHeader(){
        $headers = null;
        if (isset($_SERVER['Authorization'])) {
            $headers = trim($_SERVER["Authorization"]);
        }
        else if (isset($_SERVER['HTTP_AUTHORIZATION'])) { //Nginx or fast CGI
            $headers = trim($_SERVER["HTTP_AUTHORIZATION"]);
        } elseif (function_exists('apache_request_headers')) {
            $requestHeaders = apache_request_headers();
            // Server-side fix for bug in old Android versions (a nice side-effect of this fix means we don't care about capitalization for Authorization)
            $requestHeaders = array_combine(array_map('ucwords', array_keys($requestHeaders)), array_values($requestHeaders));
            //print_r($requestHeaders);
            if (isset($requestHeaders['Authorization'])) {
                $headers = trim($requestHeaders['Authorization']);
            }
        }
        return $headers;
    }

function getBearerToken() {
    $headers = getAuthorizationHeader();
    // HEADER: Get the access token from the header
    if (!empty($headers)) {
        if (preg_match('/Bearer\s(\S+)/', $headers, $matches)) {
            return $matches[1];
        }
    }
    return null;
}
mysqli_close($con);
?>
