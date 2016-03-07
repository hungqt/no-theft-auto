<?php
$servername = "mysql.stud.ntnu.no";
$username = "glennchr_nta";
$password = "nta123";
$dbname = "glennchr_nta";

$json = file_get_contents('php://input');
$request = json_decode($json,true);

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
if($request != null){
	
	$stmt = $conn->prepare("INSERT INTO user(username,email,password) VALUES (?, ?, ?)");
	$stmt->bind_param("sss",$username,$email,$password);
	
	$username = $request['username'];
	$email = $request['email'];
	$password = $request['password'];
	$stmt->execute();
	
	echo "Success!";
	
	$stmt->close();
}
$conn->close();
?>