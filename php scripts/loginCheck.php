<?php
$servername = "mysql.stud.ntnu.no";
$username = "glennchr_nta";
$password = "nta123";
$dbname = "glennchr_nta";

$json = file_get_contents('php://input');
$request = json_decode($json,true);


// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
else{
}

if($request != null){
	
	$userName = $request['username'];
	$passWord = $request['password'];
	$query = "SELECT user_id FROM user WHERE username=? AND password=?";
	$stmt = $conn->stmt_init();
	
	if(!$stmt->prepare($query)){
		echo "Failed to prepare statement";
	}
	else{
		$stmt->bind_param("ss",$userName,$passWord);
		$stmt->execute();
		$stmt->bind_result($id);
		
		if($stmt->fetch() != null){
			echo "Success";
		}
		else{
			echo "Failure";
		}
		$stmt->close();
	
	}
}
$conn->close();
?>