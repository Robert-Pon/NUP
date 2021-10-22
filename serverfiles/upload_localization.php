<?php
$login = $_POST['login'];
$x1 = $_POST['x'];
$y1 = $_POST['y'];
$id1 = $_POST['id'];
$type = $_POST['type'];
$password = $_POST['password'];
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
if($i==1 ){
    $sql1 = "SELECT id FROM passwords WHERE id = '$userID' AND password = '$password'";
    $resp1 = mysqli_query($conn, $sql1);
    if($resp1->fetch_object()->id==$userID){
        $sql = "INSERT INTO localizations (id, x, y, type) VALUES ('$id1', '".doubleval($x1)."', '".doubleval($y1)."', '$type')";
                $response = mysqli_query($conn, $sql);   
        $responseObject = array("type" => "101", "id" => $userID);
    }else{
        $responseObject = array("type" => "104", "id" => strval($i));
    }
}else{
    $responseObject = array("type" => "103", "id" => strval($i));
}

echo json_encode(array($responseObject));
?>
