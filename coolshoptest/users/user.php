<?php
require "init.php";


if($_SERVER['REQUEST_METHOD']=='GET'){

    $authrizationToken =getBearerToken();


    $sql = "SELECT * FROM account WHERE token='$authrizationToken'";

      //executing query
    $r = mysqli_query($con,$sql);

      //fetching result
    $check = mysqli_fetch_array($r);

      //if we got some result
      if(isset($check)){

        $userid  = $_GET['userid'];


        $sql = "SELECT * FROM account WHERE userid='$userid'";

          //executing query
        $r = mysqli_query($con,$sql);

          //fetching result
        $check = mysqli_fetch_array($r);

        if(isset($check)){
        //
        // $res = mysqli_fetch_array($r);
          $result = array();
          array_push($result,array(
            "email"=>$check['email'],
            "password"=>$check['password'],
            "userid"=>$check['userid'],
            "avatarUrl"=>$check['avatarUrl'],
            )
          );
        // echo json_encode($result);
      /////////
          $userid = $result[0]["userid"];
          $avatar = $result[0]["avatarUrl"];
          $email = $result[0]["email"];
          // echo json_encode($userid);

              if (!isset($myObj)){
                    $myObj = new stdClass();
              }

              $myObj->userid = $userid ;
                $myObj->avatar_url = $avatar;
                $myObj->email = $email;
              reponseModel($myObj,"User Found",true,200);
            }
      }else {
          reponseModel(null,"Authorization Failed",false,400);
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
