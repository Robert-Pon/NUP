<?php
$login = $_POST['login'];
$password = $_POST['password'];
$id1 = $_POST['id'];
$id2 = $_POST['id1'];
$name = $_POST['name'];
$surname = $_POST['surname'];
$login = $_POST['login'];
$city = $_POST['city'];
$school = $_POST['school'];
$email = $_POST['email'];
$number = $_POST['number'];
$class = $_POST['class'];
$description = $_POST['description'];
$conn = mysqli_connect("server", "login", "password", "db");
$sql = "SELECT id FROM people WHERE login = '$login'";
$id = mysqli_query($conn, $sql);
$i = 0;
$userID = 0;
$responseObject = array("type" => "103", "id" => "-");
while($res = $id -> fetch_object()){
 $i++;
 $userID = $res -> id;
}
if($i==1 && $id1 = $userID){
    $sql1 = "SELECT id FROM passwords WHERE id = '$userID' AND password = '$password'";
    $resp1 = mysqli_query($conn, $sql1);
    if($resp1->fetch_object()->id==$userID){
        $set = "UPDATE people SET name = '$name', familyname = '$surname', city = '$city', school = '$school', number = '$number', class = '$class', description = '$description', email = '$email' WHERE id = '$userID'";
        mysqli_query($conn, $set);
        $responseObject = array("type" => "101", "id" => strval($i));
    }else{
        $responseObject = array("type" => "104", "id" => strval($i));
    }
}else{
    $responseObject = array("type" => "103", "id" => strval($i));
}

echo json_encode($responseObject);
?>